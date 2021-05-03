package ru.fasdev.tfs.screen.fragment.streamList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Flowable
import io.reactivex.Flowable.fromIterable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ru.fasdev.tfs.R
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.data.mapper.toStreamUi
import ru.fasdev.tfs.data.mapper.toTopicUi
import ru.fasdev.tfs.databinding.FragmentTopicListBinding
import ru.fasdev.tfs.di.module.StreamDomainModule
import ru.fasdev.tfs.di.provide.ProvideFragmentRouter
import ru.fasdev.tfs.domain.stream.interactor.StreamInteractor
import ru.fasdev.tfs.domain.stream.model.Stream
import ru.fasdev.tfs.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.recycler.adapter.RecyclerAdapter
import ru.fasdev.tfs.recycler.viewHolder.ViewType
import ru.fasdev.tfs.screen.fragment.channels.ChannelsFragment
import ru.fasdev.tfs.screen.fragment.chat.ChatFragment
import ru.fasdev.tfs.screen.fragment.streamList.mvi.StreamListAction
import ru.fasdev.tfs.screen.fragment.streamList.mvi.StreamListState
import ru.fasdev.tfs.screen.fragment.streamList.recycler.StreamHolderFactory
import ru.fasdev.tfs.screen.fragment.streamList.recycler.diuff.StreamItemCallback
import ru.fasdev.tfs.screen.fragment.streamList.recycler.viewHolder.StreamViewHolder
import ru.fasdev.tfs.screen.fragment.streamList.recycler.viewHolder.TopicViewHolder
import ru.fasdev.tfs.screen.fragment.streamList.recycler.viewType.StreamUi
import ru.fasdev.tfs.view.MviView
import java.util.concurrent.Flow
import java.util.concurrent.TimeUnit

class StreamListFragment :
    Fragment(R.layout.fragment_topic_list),
    StreamViewHolder.OnClickStreamListener,
    TopicViewHolder.OnClickTopicListener,
    MviView<StreamListState> {
    companion object {
        const val ALL_MODE = 1
        const val SUBSCRIBED_MODE = 2

        private const val MODE_KEY = "mode"

        fun newInstance(mode: Int): StreamListFragment {
            return StreamListFragment().apply {
                arguments = bundleOf(MODE_KEY to mode)
            }
        }
    }

    private var _binding: FragmentTopicListBinding? = null
    private val binding get() = _binding!!

    private val mode: Int get() = arguments?.getInt(MODE_KEY, ALL_MODE) ?: ALL_MODE

    private val holderFactory by lazy { StreamHolderFactory(this, this) }
    private val adapter by lazy { RecyclerAdapter(holderFactory, StreamItemCallback()) }

    private val fragmentRouter: FragmentRouter
        get() = (requireActivity() as ProvideFragmentRouter).getRouter()

    private val searchObservable get() = (parentFragment as ChannelsFragment).provideSearch

    private val viewModel: StreamListViewModel by viewModels()

    private var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState).apply {
            _binding = FragmentTopicListBinding.bind(this!!)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchObservable.observe(viewLifecycleOwner) { searchStream(it) }

        binding.rvTopics.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTopics.adapter = adapter

        viewModel.attachView(this)

        viewModel.input.accept(StreamListAction.SideEffectLoadAllStreams(mode))
    }

    fun searchStream(query: String) {
        viewModel.input.accept(StreamListAction.SideEffectSearchStreams(query, mode))
    }

    override fun onClickStream(idStream: Int, opened: Boolean) {
        viewModel.input.accept(StreamListAction.SideEffectLoadTopics(idStream, opened))
    }

    override fun onClickTopic(nameTopic: String, streamName: String) {
        fragmentRouter.navigateTo(ChatFragment.getScreen(streamName, nameTopic))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    private fun onError(error: Throwable) {
        Snackbar.make(binding.root, "ERROR: ${error.message}", Snackbar.LENGTH_LONG).show()
    }

    override fun render(state: StreamListState) {
        when {
            state.error != null -> {
                onError(state.error)
            }
            state.isLoading -> {
                Log.d("LOADING", "IS_LOADING")
            }
            else -> {
                adapter.items = state.itemsList
            }
        }
    }
}
