package ru.fasdev.tfs.view.ui.fragment.chat

import android.os.Bundle
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
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentChatBinding
import ru.fasdev.tfs.domain.message.interactor.MessageInteractor
import ru.fasdev.tfs.domain.message.interactor.MessageInteractorImpl
import ru.fasdev.tfs.domain.message.repo.TestMessageRepoImpl
import ru.fasdev.tfs.domain.topic.interactor.TopicInteractor
import ru.fasdev.tfs.domain.topic.interactor.TopicInteractorImpl
import ru.fasdev.tfs.domain.topic.repo.TestAllTopicRepoImpl
import ru.fasdev.tfs.domain.topic.repo.TopicRepo
import ru.fasdev.tfs.view.di.ProvideFragmentRouter
import ru.fasdev.tfs.view.feature.mapper.mapToUiList
import ru.fasdev.tfs.view.feature.util.doOnApplyWindowsInsets
import ru.fasdev.tfs.view.feature.util.getColorCompat
import ru.fasdev.tfs.view.feature.util.getSystemInsets
import ru.fasdev.tfs.view.feature.util.setSystemInsetsInTop
import ru.fasdev.tfs.view.ui.bottomDialog.emoji.SelectEmojiBottomDialog
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.ChatHolderFactory
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.diffUtil.ChatItemCallback
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolder.MessageViewHolder
import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentScreen
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseAdapter
import ru.fasdev.tfs.view.ui.global.recycler.base.ViewType

class ChatFragment :
    Fragment(R.layout.fragment_chat),
    MessageViewHolder.OnLongClickMessageListener,
    MessageViewHolder.OnClickReactionListener,
    AsyncListDiffer.ListListener<ViewType> {

    companion object {
        private val TAG: String = ChatFragment::class.java.simpleName

        private const val COLOR_TOOLBAR = R.color.teal_500

        private const val KEY_SELECTED_MESSAGE = "SELECTED_MESSAGE"
        private const val KEY_ID_TOPIC = "ID_TOPIC"

        fun newInstance(idSubTopic: Int): ChatFragment {
            return ChatFragment().apply {
                arguments = bundleOf(KEY_ID_TOPIC to idSubTopic)
            }
        }

        fun getScreen(idSubTopic: Int) = FragmentScreen(TAG, newInstance(idSubTopic))
    }

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val fragmentRouter: FragmentRouter
        get() = (requireActivity() as ProvideFragmentRouter).getRouter()

    private val testMessageRepoImpl = TestMessageRepoImpl()
    private val interactor: MessageInteractor = MessageInteractorImpl(testMessageRepoImpl)

    private val topicRepo: TopicRepo = TestAllTopicRepoImpl()
    private val topicInteractor: TopicInteractor = TopicInteractorImpl(topicRepo)

    private val holderFactory by lazy { ChatHolderFactory(this, this) }
    private val adapter by lazy { BaseAdapter(holderFactory, ChatItemCallback(), this) }

    private val currentChatId = 1
    private val currentUserId = 1

    private val idSubTopic: Int get() = requireArguments().getInt(KEY_ID_TOPIC)

    private var selectedMessageId: Int = 0

    private val compositeDisposable = CompositeDisposable()

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

        setFragmentResultListener(SelectEmojiBottomDialog.TAG) { _, bundle ->
            val selectedEmoji = bundle.getString(SelectEmojiBottomDialog.KEY_SELECTED_EMOJI)

            selectedEmoji?.let { changeSelectedReaction(emoji = it) }
        }

        getTopic(idSubTopic)

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

        updateChatItems(true)
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

    override fun onClickReaction(uIdMessage: Int, emoji: String) {
        changeSelectedReaction(uIdMessage, emoji)
    }

    override fun onCurrentListChanged(
        previousList: MutableList<ViewType>,
        currentList: MutableList<ViewType>
    ) {
        if (!binding.rvList.canScrollVertically(1))
            binding.rvList.scrollToPosition(0)
    }

    private fun loadingState() {
        binding.loadingLayout.root.isVisible = true
        binding.rvList.isVisible = false
    }

    private fun loadedState() {
        binding.loadingLayout.root.isVisible = false
        binding.rvList.isVisible = true
    }

    private fun onError(error: Throwable) {
        Snackbar.make(binding.root, error.message.toString(), Snackbar.LENGTH_LONG).show()
    }

    // #region Rx chains
    private fun sendMessage(messageText: String) {
        compositeDisposable.add(
            Single.just(messageText)
                .filter { it.isNotEmpty() }
                .doOnSuccess { binding.msgText.text?.clear() }
                .flatMapCompletable { interactor.sendMessage(currentChatId, it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onComplete = { updateChatItems() },
                    onError = ::onError
                )
        )
    }

    private fun getTopic(idSubTopic: Int) {
        compositeDisposable.add(
            topicInteractor
                .getTopic(idSubTopic)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { binding.topic.text = resources.getString(R.string.sub_topic_title, it.name) }
                .observeOn(Schedulers.io())
                .flatMap { topicInteractor.getStream(it.idStream) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        binding.toolbarLayout.title.text =
                            resources.getString(R.string.main_topic_title, it.name)
                    },
                    onError = ::onError
                )
        )
    }

    private fun changeSelectedReaction(idMessage: Int = selectedMessageId, emoji: String) {
        compositeDisposable.add(
            interactor
                .changeSelectedReaction(currentChatId, idMessage, emoji)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onComplete = { updateChatItems() },
                    onError = ::onError
                )
        )
    }

    private fun updateChatItems(isLoading: Boolean = false) {
        compositeDisposable.add(
            interactor.getMessageByChat(currentChatId)
                .doOnSubscribe {
                    if (isLoading) {
                        loadingState()
                    }
                }
                .map { it.mapToUiList(currentUserId) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { selectedMessageId = 0 }
                .subscribeBy(
                    onSuccess = {
                        adapter.items = it
                        loadedState()
                    },
                    onError = ::onError
                )
        )
    }
    // #endregion
}
