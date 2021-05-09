package ru.fasdev.tfs.screen.fragment.streamList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxrelay2.PublishRelay
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentTopicListBinding
import ru.fasdev.tfs.di.provide.ProvideFragmentRouter
import ru.fasdev.tfs.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.mviCore.MviView
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.recycler.base.adapter.RecyclerAdapter
import ru.fasdev.tfs.screen.fragment.streamList.mvi.StreamListState
import ru.fasdev.tfs.screen.fragment.streamList.recycler.StreamHolderFactory
import ru.fasdev.tfs.screen.fragment.streamList.recycler.diff.StreamItemCallback
import ru.fasdev.tfs.recycler.item.stream.StreamViewHolder
import ru.fasdev.tfs.recycler.item.topic.TopicViewHolder
import ru.fasdev.tfs.screen.fragment.chat.ChatFragment
import ru.fasdev.tfs.screen.fragment.info.InfoPlaceholderFragment
import ru.fasdev.tfs.screen.fragment.info.emptyListState
import ru.fasdev.tfs.screen.fragment.info.handleErrorState
import ru.fasdev.tfs.screen.fragment.streamList.mvi.StreamListAction

class StreamListFragment : Fragment(R.layout.fragment_topic_list),
    StreamViewHolder.OnClickStreamListener, TopicViewHolder.OnClickTopicListener,
    MviView<Action, StreamListState>, InfoPlaceholderFragment.Listener {

    companion object {
        const val ALL_MODE = 1
        const val SUBSCRIBED_MODE = 2

        private const val MODE_KEY = "mode"

        fun newInstance(mode: Int): StreamListFragment {
            return StreamListFragment().apply { arguments = bundleOf(MODE_KEY to mode) }
        }
    }

    override val actions: PublishRelay<Action> = PublishRelay.create()
    private val viewModel: StreamListViewModel by viewModels()

    private var _binding: FragmentTopicListBinding? = null
    private val binding get() = _binding!!

    private val mode: Int get() = arguments?.getInt(MODE_KEY, ALL_MODE) ?: ALL_MODE

    private val holderFactory by lazy { StreamHolderFactory(this, this) }
    private val adapter by lazy { RecyclerAdapter(holderFactory, StreamItemCallback()) }

    private val fragmentRouter: FragmentRouter
        get() = (requireActivity() as ProvideFragmentRouter).getRouter()

    private val infoFragment get() = childFragmentManager.findFragmentById(R.id.info_placeholder) as InfoPlaceholderFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)?.apply {
            _binding = FragmentTopicListBinding.bind(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvTopics.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTopics.adapter = adapter

        viewModel.bind(this)
        sendLoadStreamsAction()
    }

    private fun sendLoadStreamsAction() {
        when (mode) {
            ALL_MODE -> {
                actions.accept(StreamListAction.Ui.LoadAllStreams)
            }
            SUBSCRIBED_MODE -> {
                actions.accept(StreamListAction.Ui.LoadSubStreams)
            }
        }
    }

    override fun onClickStream(idStream: Int, opened: Boolean) {
        if (opened) {
            actions.accept(StreamListAction.Ui.LoadTopics(idStream.toLong()))
        } else {
            actions.accept(StreamListAction.Ui.RemoveTopics(idStream.toLong()))
        }
    }

    override fun onClickTopic(nameTopic: String, idStream: Long) {
        //TODO: GET INFO STREAM
        fragmentRouter.navigateTo(ChatFragment.getScreen(idStream.toString(), nameTopic))
    }

    override fun render(state: StreamListState) {
        binding.swipeRefreshLayout.isRefreshing = state.isLoading
        adapter.items = state.items

        if (state.error != null) {
            binding.rvTopics.isGone = true
            binding.infoPlaceholder.isGone = false

            infoFragment.handleErrorState(state.error)
        }
        else {
            if (state.isLoading) {
                binding.infoPlaceholder.isGone = true
            }
            else {
                if (state.items.isNotEmpty()) {
                    binding.infoPlaceholder.isGone = true
                    binding.rvTopics.isGone = false
                }
                else {
                    binding.rvTopics.isGone = true
                    binding.infoPlaceholder.isGone = false

                    infoFragment.emptyListState(resources.getString(R.string.empty_streams_list))
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.unBind()
    }

    override fun onBtnClickInfoPlaceholder(buttonType: InfoPlaceholderFragment.ButtonType) {
        when (buttonType) {
            InfoPlaceholderFragment.ButtonType.POSITIVE -> {
                sendLoadStreamsAction()
            }
        }
    }
}
