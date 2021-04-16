package ru.fasdev.tfs.screen.fragment.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.fasdev.tfs.R
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.core.ext.doOnApplyWindowsInsets
import ru.fasdev.tfs.core.ext.getColorCompat
import ru.fasdev.tfs.core.ext.getSystemInsets
import ru.fasdev.tfs.core.ext.setSystemInsetsInTop
import ru.fasdev.tfs.data.mapper.mapToUiList
import ru.fasdev.tfs.data.repo.MessageRepoImpl
import ru.fasdev.tfs.databinding.FragmentChatBinding
import ru.fasdev.tfs.di.module.ChatDomainModule
import ru.fasdev.tfs.di.provide.ProvideFragmentRouter
import ru.fasdev.tfs.domain.message.interactor.MessageInteractor
import ru.fasdev.tfs.domain.message.interactor.MessageInteractorImpl
import ru.fasdev.tfs.domain.message.model.DirectionScroll
import ru.fasdev.tfs.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.fragmentRouter.FragmentScreen
import ru.fasdev.tfs.recycler.adapter.RecyclerAdapter
import ru.fasdev.tfs.recycler.viewHolder.ViewType
import ru.fasdev.tfs.screen.bottomDialog.selectedEmoji.SelectEmojiBottomDialog
import ru.fasdev.tfs.screen.fragment.chat.recycler.ChatHolderFactory
import ru.fasdev.tfs.screen.fragment.chat.recycler.diff.ChatItemCallback
import ru.fasdev.tfs.screen.fragment.chat.recycler.viewHolder.MessageViewHolder
import ru.fasdev.tfs.screen.fragment.chat.recycler.viewType.ExternalMessageUi
import java.util.concurrent.TimeUnit

class ChatFragment :
    Fragment(R.layout.fragment_chat),
    MessageViewHolder.OnLongClickMessageListener,
    MessageViewHolder.OnClickReactionListener,
    AsyncListDiffer.ListListener<ViewType> {

    companion object {
        val TAG: String = ChatFragment::class.java.simpleName

        private const val LIMIT_UPDATE = 5
        private const val COLOR_TOOLBAR = R.color.teal_500

        private const val KEY_SELECTED_MESSAGE = "SELECTED_MESSAGE"
        private const val KEY_STREAM_NAME = "STREAM_NAME"
        private const val KEY_TOPIC_NAME = "TOPIC_NAME"

        fun newInstance(streamName: String, topicName: String): ChatFragment {
            return ChatFragment().apply {
                arguments = bundleOf(KEY_STREAM_NAME to streamName, KEY_TOPIC_NAME to topicName)
            }
        }

        fun getScreen(streamName: String, topicName: String) =
            FragmentScreen(TAG, newInstance(streamName, topicName))
    }

    object ChatComponent {
        val messageRepo =
            ChatDomainModule.getMessageRepo(TfsApp.AppComponent.chatApi, TfsApp.AppComponent.json, TfsApp.AppComponent.messageDao, TfsApp.AppComponent.userDao, TfsApp.AppComponent.reactionDao)
        val messageInteractor = MessageInteractorImpl(messageRepo)
    }

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val fragmentRouter: FragmentRouter
        get() = (requireActivity() as ProvideFragmentRouter).getRouter()

    private val interactor: MessageInteractor = ChatComponent.messageInteractor

    private val holderFactory by lazy { ChatHolderFactory(this, this) }
    private val adapter by lazy {
        RecyclerAdapter(
            holderFactory,
            ChatItemCallback()
        ).apply { differListListener = this@ChatFragment }
    }

    private val streamName: String get() = requireArguments().getString(KEY_STREAM_NAME).toString()
    private val topicName: String get() = requireArguments().getString(KEY_TOPIC_NAME).toString()

    private var selectedMessageId: Int = 0

    private val compositeDisposable = CompositeDisposable()

    private var isDown: Boolean = true
    private var isFirstLoaded: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            selectedMessageId = it.getInt(KEY_SELECTED_MESSAGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)?.apply {
            _binding = FragmentChatBinding.bind(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarLayout.root.setSystemInsetsInTop()
        view.doOnApplyWindowsInsets { insetView, windowInsets, initialPadding ->
            val systemInsets = windowInsets.getSystemInsets()
            insetView.updatePadding(bottom = initialPadding.bottom + systemInsets.bottom)
        }

        binding.toolbarLayout.title.text =
            resources.getString(R.string.main_topic_title, streamName)
        binding.topic.text = resources.getString(R.string.sub_topic_title, topicName)

        setFragmentResultListener(SelectEmojiBottomDialog.TAG) { _, bundle ->
            val selectedEmoji = bundle.getString(SelectEmojiBottomDialog.KEY_SELECTED_EMOJI)

            selectedEmoji?.let { selectedReaction(selectedMessageId, it, true) }
        }

        with(binding.toolbarLayout) {
            toolbarRoot.setBackgroundColor(requireContext().getColorCompat(COLOR_TOOLBAR))

            btnNav.isVisible = true
            btnNav.setOnClickListener {
                fragmentRouter.back()
            }
        }

        binding.msgText.addTextChangedListener {
            if (it.isNullOrEmpty()) {
                binding.sendBtn.setIconResource(R.drawable.ic_add)
            } else {
                binding.sendBtn.setIconResource(R.drawable.ic_send)
            }
        }

        binding.sendBtn.setOnClickListener {
            val msgText = binding.msgText.text.toString().trim()
            sendMessage(msgText)
        }

        val rvList: RecyclerView = binding.rvList
        rvList.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, true)
        rvList.adapter = adapter

        updateChatItems()
        initListenerScrollRv()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SELECTED_MESSAGE, selectedMessageId)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        _binding = null
    }

    private fun showBottomEmojiDialog() {
        SelectEmojiBottomDialog.show(parentFragmentManager)
    }

    override fun onLongClickMessage(uIdMessage: Int) {
        selectedMessageId = uIdMessage
        showBottomEmojiDialog()
    }

    override fun onClickAddNewReaction(uIdMessage: Int) {
        selectedMessageId = uIdMessage
        showBottomEmojiDialog()
    }

    override fun onClickReaction(uIdMessage: Int, emoji: String, isSelected: Boolean) {
        selectedReaction(uIdMessage, emoji, isSelected)
    }

    override fun onCurrentListChanged(
        previousList: MutableList<ViewType>,
        currentList: MutableList<ViewType>
    ) {
        _binding?.let {
            if (!it.rvList.canScrollVertically(1) && isDown)
                it.rvList.scrollToPosition(0)
        }
    }

    private fun onError(error: Throwable) {
        _binding?.let {
            Snackbar.make(it.root, error.message.toString(), Snackbar.LENGTH_LONG).show()
        }
    }

    private fun initListenerScrollRv() {
        Observable.create<PagingItem> { emitter ->
            val scrollListener = object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = (binding.rvList.layoutManager as LinearLayoutManager)

                    val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                    val lastVisibleItem = (binding.rvList.layoutManager as LinearLayoutManager)
                        .findLastVisibleItemPosition()

                    val isUpScroll = dy < 0

                    val isUpdate = if (isUpScroll) {
                        (adapter.itemCount - lastVisibleItem) <= LIMIT_UPDATE
                    } else {
                        lastVisibleItem - LIMIT_UPDATE <= LIMIT_UPDATE
                    }

                    isDown = firstVisiblePosition == 0

                    if (isUpdate) {
                        val id = adapter.items[lastVisibleItem].uId.toLong()

                        if (isUpScroll) {
                            val pagingItem = PagingItem(id, DirectionScroll.UP)
                            emitter.onNext(pagingItem)
                        } else {
                            val pagingItem = PagingItem(id, DirectionScroll.DOWN)
                            emitter.onNext(pagingItem)
                        }
                    }
                }
            }

            binding.rvList.addOnScrollListener(scrollListener)
        }
        .distinctUntilChanged()
        .debounce(500, TimeUnit.MILLISECONDS)
        .subscribeBy {
            loadPaging(it)
        }
    }

    // #region Rx chains
    private fun sendMessage(messageText: String) {
        compositeDisposable.add(
            Single.just(messageText)
                .filter { it.isNotEmpty() }
                .doOnSuccess {
                    binding.msgText.text?.clear()
                }
                .observeOn(Schedulers.io())
                .flatMapCompletable { interactor.sendMessage(streamName, topicName, it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onError = ::onError
                )
        )
    }

    private fun selectedReaction(
        idMessage: Int = selectedMessageId,
        emojiName: String,
        isSelected: Boolean
    ) {
        compositeDisposable.add(
            interactor.setSelectionReaction(idMessage, emojiName, isSelected)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onError = ::onError
                )
        )
    }


    private fun loadPaging(pagingItem: PagingItem) {
        Flowable.just(pagingItem)
            .subscribeOn(Schedulers.io())
            .flatMap { item ->
                interactor.getMessagesByTopic(
                    nameStream = streamName,
                    nameTopic = topicName,
                    anchorMessage = item.lastVisibleId,
                    direction = item.direction
                )
                    .map { newList ->
                        newList.mapToUiList(MessageRepoImpl.USER_ID).reversed()
                    }
                    .map {
                        if (item.direction == DirectionScroll.UP) {
                            adapter.items + it
                        } else {
                            it + adapter.items
                        }
                    }
                    .map { it.distinct() }
                        /*
                    .map {
                        if (it.size > 50) {
                            val different = Math.abs(it.size - 50)

                            return@map if (item.direction == DirectionScroll.UP) {
                                it.drop(different)
                            } else {
                                it.dropLast(different)
                            }
                        }

                        return@map it
                    }*/
            }
            .onErrorResumeNext {
                onError(it)
                Flowable.just(listOf())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isNotEmpty()) adapter.items = it
                },
                onError = ::onError
            )
    }

    private fun updateChatItems() {
        Flowable.interval(0, 5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .filter { isDown }
            .flatMap {
                interactor.getMessagesByTopic(
                    nameStream = streamName,
                    nameTopic = topicName,
                    anchorMessage = MessageRepoImpl.NEWEST_ANCHOR,
                    isFirstLoaded = isFirstLoaded,
                    numAfter = 0,
                    numBefore = 20
                )
                .map { newList ->
                    newList.mapToUiList(MessageRepoImpl.USER_ID).reversed()
                }
                .map {
                    it + adapter.items
                }
                .map {
                    it.distinct()
                }
            }
            .onErrorResumeNext {
                onError(it)
                Flowable.just(listOf())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    isFirstLoaded = false
                    if (it.isNotEmpty()) adapter.items = it
                },
                onError = ::onError
            )
    }
    // #endregion

    data class PagingItem(val lastVisibleId: Long, val direction: DirectionScroll)
}
