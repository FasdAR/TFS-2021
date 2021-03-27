package ru.fasdev.tfs.view.ui.fragment.topicList

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import ru.fasdev.tfs.R
import ru.fasdev.tfs.domain.topic.interactor.TopicInteractor
import ru.fasdev.tfs.domain.topic.interactor.TopicInteractorImpl
import ru.fasdev.tfs.domain.topic.repo.TestAllTopicRepoImpl
import ru.fasdev.tfs.domain.topic.repo.TestSubscribedTopicRepoImpl
import ru.fasdev.tfs.domain.topic.repo.TopicRepo
import ru.fasdev.tfs.view.di.ProvideFragmentRouter
import ru.fasdev.tfs.view.feature.mapper.mapToStreamUi
import ru.fasdev.tfs.view.feature.mapper.mapToTopicUi
import ru.fasdev.tfs.view.feature.mapper.toStreamUi
import ru.fasdev.tfs.view.feature.mapper.toTopicUi
import ru.fasdev.tfs.view.ui.fragment.channels.ChannelsFragment
import ru.fasdev.tfs.view.ui.fragment.chat.ChatFragment
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.TopicHolderFactory
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.diffUtil.TopicItemCallback
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewHolder.StreamViewHolder
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewHolder.TopicViewHolder
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewType.StreamUi
import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseAdapter
import ru.fasdev.tfs.view.ui.global.recycler.base.ViewType

class TopicListFragment :
    Fragment(R.layout.fragment_topic_list),
    StreamViewHolder.OnClickStreamListener,
    TopicViewHolder.OnClickTopicListener {
    companion object {
        const val ALL_MODE = 1
        const val SUBSCRIBED_MODE = 2

        private const val MODE_KEY = "mode"

        fun newInstance(mode: Int): TopicListFragment {
            return TopicListFragment().apply {
                arguments = bundleOf(MODE_KEY to mode)
            }
        }
    }

    lateinit var rvTopics: RecyclerView

    private val mode: Int get() = arguments?.getInt(MODE_KEY, ALL_MODE) ?: ALL_MODE

    private val topicRepo: TopicRepo by lazy {
        when (mode) {
            ALL_MODE -> TestAllTopicRepoImpl()
            SUBSCRIBED_MODE -> TestSubscribedTopicRepoImpl()
            else -> error("Don't support this type mode $mode")
        }
    }
    private val topicInteractor: TopicInteractor by lazy { TopicInteractorImpl(topicRepo) }

    private val holderFactory by lazy { TopicHolderFactory(this, this) }
    private val adapter by lazy { BaseAdapter(holderFactory, TopicItemCallback()) }

    private val fragmentRouter: FragmentRouter
        get() = (requireActivity() as ProvideFragmentRouter).getRouter()

    private val searchObservable get() = (parentFragment as ChannelsFragment).provideSearch

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchObservable.observe(viewLifecycleOwner) { searchStream(it) }

        rvTopics = view as RecyclerView
        rvTopics.layoutManager = LinearLayoutManager(requireContext())
        rvTopics.adapter = adapter

        topicInteractor.getAllStreams()
            .flatMapObservable { Observable.fromIterable(it) }
            .map { it.toStreamUi() }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    adapter.items = it
                },
                onError = ::onError
            )
    }

    override fun onClickStream(idStream: Int, opened: Boolean) {
        topicInteractor.getTopicsInStream(idStream)
            .flatMapObservable { Observable.fromIterable(it) }
            .map { it.toTopicUi() }
            .toList()
            .map { topics ->
                val currentArray = mutableListOf<ViewType>().apply { addAll(adapter.items) }

                val currentStreamIndex =
                    currentArray.indexOfFirst { it.uId == idStream && it is StreamUi }
                val stream = currentArray[currentStreamIndex] as StreamUi
                currentArray[currentStreamIndex] = stream.copy(isOpen = opened)

                if (opened) currentArray.addAll(currentStreamIndex + 1, topics)
                else currentArray.removeAll(topics)

                return@map currentArray
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    adapter.items = it
                },
                onError = ::onError
            )
    }

    override fun onClickTopic(idTopic: Int) {
        fragmentRouter.navigateTo(ChatFragment.getScreen(idTopic))
    }

    private fun searchStream(query: String) {
        topicInteractor.searchStream(query)
            .flatMapObservable { Observable.fromIterable(it) }
            .map { it.toStreamUi() }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    adapter.items = it
                },
                onError = ::onError
            )
    }

    private fun onError(error: Throwable) {
        Snackbar.make(rvTopics, error.message.toString(), Snackbar.LENGTH_LONG).show()
    }
}
