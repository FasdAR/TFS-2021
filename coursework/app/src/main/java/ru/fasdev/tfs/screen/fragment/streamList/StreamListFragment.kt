package ru.fasdev.tfs.screen.fragment.streamList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observable.fromIterable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
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
import ru.fasdev.tfs.screen.fragment.streamList.recycler.StreamHolderFactory
import ru.fasdev.tfs.screen.fragment.streamList.recycler.diuff.StreamItemCallback
import ru.fasdev.tfs.screen.fragment.streamList.recycler.viewHolder.StreamViewHolder
import ru.fasdev.tfs.screen.fragment.streamList.recycler.viewHolder.TopicViewHolder
import ru.fasdev.tfs.screen.fragment.streamList.recycler.viewType.StreamUi
import java.util.concurrent.TimeUnit

class StreamListFragment :
    Fragment(R.layout.fragment_topic_list),
    StreamViewHolder.OnClickStreamListener,
    TopicViewHolder.OnClickTopicListener {
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

    object StreamComponent {
        val streamRepo = StreamDomainModule.getStreamRepo(TfsApp.AppComponent.streamApi)
        val streamInteractor = StreamDomainModule.getStreamInteractor(streamRepo)
    }

    private var _binding: FragmentTopicListBinding? = null
    private val binding get() = _binding!!

    private val mode: Int get() = arguments?.getInt(MODE_KEY, ALL_MODE) ?: ALL_MODE

    private val streamInteractor: StreamInteractor = StreamComponent.streamInteractor

    private val holderFactory by lazy { StreamHolderFactory(this, this) }
    private val adapter by lazy { RecyclerAdapter(holderFactory, StreamItemCallback()) }

    private val fragmentRouter: FragmentRouter
        get() = (requireActivity() as ProvideFragmentRouter).getRouter()

    private val searchObservable get() = (parentFragment as ChannelsFragment).provideSearch

    private val searchSubject = PublishSubject.create<String>()
    private val compositeDisposable = CompositeDisposable()

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

        loadAllStreams()
        observerSearch()
    }

    fun searchStream(query: String) {
        searchSubject.onNext(query)
    }

    override fun onClickStream(idStream: Int, opened: Boolean) {
        loadTopics(idStream, opened)
    }

    override fun onClickTopic(nameTopic: String, streamName: String) {
        fragmentRouter.navigateTo(ChatFragment.getScreen(streamName, nameTopic))
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun onError(error: Throwable) {
        Snackbar.make(binding.root, "ERROR: ${error.message}", Snackbar.LENGTH_LONG).show()
    }

    // #region Rx chains
    private fun Single<List<Stream>>.mapToDomain(): Single<List<StreamUi>> {
        return flatMapObservable(::fromIterable)
            .map { it.toStreamUi() }
            .toList()
    }

    private fun getStreamSource(): Single<List<Stream>> {
        return if (mode == ALL_MODE) streamInteractor.getAllStreams()
        else streamInteractor.getSubStreams()
    }

    private fun loadAllStreams() {
        compositeDisposable.add(
            getStreamSource().subscribeOn(Schedulers.io())
                .mapToDomain()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        adapter.items = it
                    }
                )
        )
    }

    private fun observerSearch() {
        compositeDisposable.add(
            searchSubject
                .filter { isVisible }
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMapSingle {
                    if (it.isNotEmpty()) streamInteractor.searchStream(it, mode == SUBSCRIBED_MODE)
                    else getStreamSource()
                }
                .subscribeOn(Schedulers.io())
                .flatMapSingle {
                    Single.just(it).mapToDomain()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn { error ->
                    onError(error)
                    return@onErrorReturn listOf()
                }
                .subscribeBy(
                    onNext = { array ->
                        adapter.items = array
                    }
                )
        )
    }

    private fun loadTopics(idStream: Int, opened: Boolean) {
        compositeDisposable.add(
            streamInteractor.getAllTopics(idStream.toLong())
                .subscribeOn(Schedulers.io())
                .flatMapObservable { Observable.fromIterable(it) }
                .map {
                    val streamName = adapter.items
                        .filter { it is StreamUi }
                        .map { it as StreamUi }
                        .findLast { it.uId == idStream }?.nameTopic.toString()

                    it.toTopicUi(streamName)
                }
                .toList()
                .map { topics ->
                    val currentArray = mutableListOf<ViewType>().apply { addAll(adapter.items) }

                    val currentStreamIndex =
                        currentArray.indexOfFirst { it.uId == idStream && it is StreamUi }
                    val stream = currentArray[currentStreamIndex] as StreamUi
                    currentArray[currentStreamIndex] = stream.copy(isOpen = opened)

                    if (opened) {
                        currentArray.addAll(currentStreamIndex + 1, topics)
                    } else {
                        currentArray.removeAll(topics)
                    }

                    return@map currentArray
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    adapter.items = it
                }
        )
    }
    // #endregion
}
