package ru.fasdev.tfs.view.ui.fragment.topicList

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.fasdev.tfs.R
import ru.fasdev.tfs.domain.topic.interactor.TopicInteractor
import ru.fasdev.tfs.domain.topic.interactor.TopicInteractorImpl
import ru.fasdev.tfs.domain.topic.repo.TestTopicRepoImpl
import ru.fasdev.tfs.view.feature.mapper.mapToSubTopicUi
import ru.fasdev.tfs.view.feature.mapper.mapToTopicUi
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

    private val testTopicRepo = TestTopicRepoImpl()
    private val topicInteractor: TopicInteractor = TopicInteractorImpl(testTopicRepo)

    private val holderFactory by lazy { TopicHolderFactory(this, this) }
    private val adapter by lazy { BaseAdapter(holderFactory, TopicDiffUtilCallback()) }

    private val mode: Int get() = arguments?.getInt(MODE_KEY, ALL_MODE) ?: ALL_MODE

    private val fragmentRouter: FragmentRouter
        get() = requireActivity() as FragmentRouter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = view as RecyclerView
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        when (mode) {
            ALL_MODE -> {
                adapter.items = topicInteractor.getAllTopics().mapToTopicUi()
            }
            SUBSCRIBED_MODE -> {
                adapter.items = topicInteractor.getSubscribedTopics().mapToTopicUi()
            }
        }
    }

    override fun onClickTopic(idTopic: Int, opened: Boolean) {
        val uiModels = mutableListOf<ViewType>().apply { addAll(adapter.items) }
        val subTopics = topicInteractor.getAllSubTopicInTopic(idTopic).mapToSubTopicUi()

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
        val parentTopic = topicInteractor.getTopicFromSubTopic(idSubTopic)
        parentTopic?.let {
            fragmentRouter.navigateTo(ChatFragment.newInstance(it.id, idSubTopic), ChatFragment.TAG)
        }
    }
}