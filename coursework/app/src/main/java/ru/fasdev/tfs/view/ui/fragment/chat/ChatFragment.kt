package ru.fasdev.tfs.view.ui.fragment.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentChatBinding
import ru.fasdev.tfs.domain.message.interactor.MessageInteractor
import ru.fasdev.tfs.domain.message.interactor.MessageInteractorImpl
import ru.fasdev.tfs.domain.message.repo.TestMessageRepoImpl
import ru.fasdev.tfs.domain.topic.interactor.TopicInteractor
import ru.fasdev.tfs.domain.topic.interactor.TopicInteractorImpl
import ru.fasdev.tfs.domain.topic.repo.TestAllTopicRepoImpl
import ru.fasdev.tfs.domain.topic.repo.TopicRepo
import ru.fasdev.tfs.view.feature.mapper.mapToUiList
import ru.fasdev.tfs.view.feature.util.*
import ru.fasdev.tfs.view.feature.util.setSystemInsets
import ru.fasdev.tfs.view.ui.bottomDialog.emoji.SelectEmojiBottomDialog
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.ChatHolderFactory
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.diffUtil.ChatDiffUtilCallback
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolder.MessageViewHolder
import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentScreen
import ru.fasdev.tfs.view.ui.global.fragmentRouter.ProvideFragmentRouter
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseAdapter
import ru.fasdev.tfs.view.ui.global.recycler.base.ViewType
import ru.fasdev.tfs.view.ui.global.recycler.itemDecoration.VerticalSpaceItemDecoration

class ChatFragment :
    Fragment(R.layout.fragment_chat),
    MessageViewHolder.OnLongClickMessageListener,
    MessageViewHolder.OnClickReactionListener,
    AsyncListDiffer.ListListener<ViewType> {

    companion object {
        val TAG: String = ChatFragment::class.java.simpleName

        private const val KEY_SELECTED_MESSAGE = "SELECTED_MESSAGE"

        private const val KEY_ID_SUB_TOPIC = "ID_SUB_TOPIC"

        fun newInstance(idSubTopic: Int) = ChatFragment().apply {
            arguments = bundleOf(KEY_ID_SUB_TOPIC to idSubTopic)
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
    private val adapter by lazy { BaseAdapter(holderFactory, ChatDiffUtilCallback(), this) }

    private val currentChatId = 1
    private val currentUserId = 1

    private val idSubTopic: Int
        get() = requireArguments().getInt(KEY_ID_SUB_TOPIC)

    private var selectedMessageId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            selectedMessageId = it.getInt(KEY_SELECTED_MESSAGE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.let { _binding = FragmentChatBinding.bind(it) }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarLayout.root.setSystemInsetsInTop()

        view.doOnApplyWindowsInsets { view, windowInsets, initialPadding ->
            val systemInsets = windowInsets.getSystemInsets()
            view.updatePadding(bottom = initialPadding.bottom + systemInsets.bottom)
        }

        val subTopic = topicInteractor.getSubTopic(idSubTopic)
        val mainTopic = topicInteractor.getMainTopic(subTopic?.rootIdTopic ?: 0)

        binding.toolbarLayout.apply {
            toolbarRoot.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.teal_500))

            title.text = resources.getString(R.string.main_topic_title, mainTopic?.name)

            btnNav.isVisible = true
            btnNav.setOnClickListener {
                fragmentRouter.back()
            }
        }

        binding.subTopic.text = resources.getString(R.string.sub_topic_title, subTopic?.name)

        setFragmentResultListener(SelectEmojiBottomDialog.TAG) { requestKey, bundle ->
            val selectedEmoji = bundle.getString(SelectEmojiBottomDialog.KEY_SELECTED_EMOJI)

            selectedEmoji?.let {
                interactor.changeSelectedReaction(currentChatId, selectedMessageId, selectedEmoji)
                updateChatItems()
            }
        }

        binding.msgText.addTextChangedListener {
            if (it.isNullOrEmpty()) binding.sendBtn.setIconResource(R.drawable.ic_add)
            else binding.sendBtn.setIconResource(R.drawable.ic_send)
        }

        binding.sendBtn.setOnClickListener {
            val msgText = binding.msgText.text.toString().trim()
            if (msgText.isNotEmpty()) {
                interactor.sendMessage(currentChatId, msgText)
                updateChatItems()

                binding.msgText.text?.clear()
            }
        }

        val rvList: RecyclerView = binding.rvList
        rvList.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, true)
        rvList.adapter = adapter
        rvList.addItemDecoration(VerticalSpaceItemDecoration(19.toDp))

        updateChatItems()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SELECTED_MESSAGE, selectedMessageId)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateChatItems() {
        adapter.items = interactor.getMessageByChat(currentChatId).mapToUiList(currentUserId)
        selectedMessageId = 0
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
        interactor.changeSelectedReaction(currentChatId, uIdMessage, emoji)
        updateChatItems()
    }

    override fun onCurrentListChanged(previousList: MutableList<ViewType>, currentList: MutableList<ViewType>) {
        if (!binding.rvList.canScrollVertically(1))
            binding.rvList.scrollToPosition(0)
    }
}
