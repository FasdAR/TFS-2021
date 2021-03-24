package ru.fasdev.tfs.view.ui.fragment.topicList

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.fasdev.tfs.R
import ru.fasdev.tfs.domain.topic.interactor.TopicInteractor
import ru.fasdev.tfs.domain.topic.interactor.TopicInteractorImpl
import ru.fasdev.tfs.domain.topic.repo.TestAllTopicRepoImpl
import ru.fasdev.tfs.domain.topic.repo.TestSubscribedTopicRepoImpl
import ru.fasdev.tfs.domain.topic.repo.TopicRepo
import ru.fasdev.tfs.view.feature.mapper.mapToSubTopicUi
import ru.fasdev.tfs.view.feature.mapper.mapToTopicUi
import ru.fasdev.tfs.view.ui.fragment.channels.ChannelsFragment
import ru.fasdev.tfs.view.ui.fragment.chat.ChatFragment
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.TopicHolderFactory
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.diffUtil.TopicDiffUtilCallback
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewHolder.SubTopicViewHolder
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewHolder.TopicViewHolder
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewType.TopicUi
import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseAdapter
import ru.fasdev.tfs.view.ui.global.recycler.base.ViewType

class TopicListFragment : Fragment(R.layout.fragment_topic_list),
        TopicViewHolder.OnClickTopicListener, SubTopicViewHolder.OnClickSubTopicListener
{
    companion object {
        const val ALL_MODE = 1
        const val SUBSCRIBED_MODE = 2

        private const val MODE_KEY = "mode"

        fun newInstance(mode: Int): TopicListFragment = TopicListFragment().apply {
            arguments = bundleOf(MODE_KEY to mode)
        }
    }

    lateinit var recycler: RecyclerView

    private val mode: Int get() = arguments?.getInt(MODE_KEY, ALL_MODE) ?: ALL_MODE

    private val topicRepo: TopicRepo by lazy {
        when(mode) {
            ALL_MODE -> TestAllTopicRepoImpl()
            SUBSCRIBED_MODE -> TestSubscribedTopicRepoImpl()
            else -> error("Don't support this type mode $mode")
        }
    }
    private val topicInteractor: TopicInteractor by lazy { TopicInteractorImpl(topicRepo) }

    private val holderFactory by lazy { TopicHolderFactory(this, this) }
    private val adapter by lazy { BaseAdapter(holderFactory, TopicDiffUtilCallback()) }

    private val fragmentRouter: FragmentRouter
        get() = requireActivity() as FragmentRouter

    fun search(query: String) {
        adapter.items = topicInteractor.searchTopics(query).mapToTopicUi()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = view as RecyclerView
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        adapter.items = topicInteractor.getAllTopics().mapToTopicUi()
    }

    override fun onClickTopic(idTopic: Int, opened: Boolean) {
        val uiModels = mutableListOf<ViewType>().apply { addAll(adapter.items) }
        val subTopics = topicInteractor.getSubTopicsInMainTopic(idTopic).mapToSubTopicUi()

        val topicIndex = uiModels.indexOfFirst { it.uId == idTopic && it is TopicUi}
        val topic = uiModels[topicIndex] as TopicUi

        uiModels[topicIndex] = topic.copy(isOpen = opened)

        if (opened) {
            uiModels.addAll(topicIndex+1, subTopics)
        }
        else {
            uiModels.removeAll(subTopics)
        }

        adapter.items = uiModels
    }

    override fun onClickSubTopic(idSubTopic: Int) {
        /*
        val parentTopic = topicInteractor.getMainTopicInSubTopic(idSubTopic)
        parentTopic?.let {
            fragmentRouter.navigateTo(ChatFragment.newInstance(it.id, idSubTopic), ChatFragment.TAG)
        }
        */
    }
}