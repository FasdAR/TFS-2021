package ru.fasdev.tfs.view.ui.fragment.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentChatBinding
import ru.fasdev.tfs.domain.message.interactor.MessageInteractor
import ru.fasdev.tfs.domain.message.interactor.MessageInteractorImpl
import ru.fasdev.tfs.domain.message.repo.TestMessageRepoImpl
import ru.fasdev.tfs.view.feature.customView.viewGroup.message.MessageViewGroup
import ru.fasdev.tfs.view.feature.mapper.mapToUiList
import ru.fasdev.tfs.view.feature.recycler.Adapter
import ru.fasdev.tfs.view.feature.recycler.base.ViewTyped
import ru.fasdev.tfs.view.feature.recycler.itemDecoration.VerticalSpaceItemDecoration
import ru.fasdev.tfs.view.feature.util.toDp
import ru.fasdev.tfs.view.ui.bottomDialog.emoji.SelectEmojiBottomDialog
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.ChatHolderFactory

class ChatFragment :
    Fragment(R.layout.fragment_chat),
    ChatHolderFactory.OnLongClickMessageListener,
    ChatHolderFactory.OnClickReactionListener,
    MessageViewGroup.OnClickPlusReactionListener {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val interactor: MessageInteractor = MessageInteractorImpl(TestMessageRepoImpl())
    private val adapter by lazy {
        return@lazy Adapter<ViewTyped>(
            ChatHolderFactory(this, this, this)
        )
    }

    private val currentChatId = 1
    private val currentUserId = 1

    private var selectedMessageId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.let { _binding = FragmentChatBinding.bind(it) }

        childFragmentManager.setFragmentResultListener(SelectEmojiBottomDialog.TAG, viewLifecycleOwner) { requestKey, bundle ->
            val selectedEmoji = bundle.getString(SelectEmojiBottomDialog.KEY_SELECTED_EMOJI)

            selectedEmoji?.let {
                interactor.changeSelectedReaction(currentChatId, selectedMessageId, selectedEmoji)
                updateChatItems()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvList: RecyclerView = binding.rvList
        rvList.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, true)
        rvList.adapter = adapter
        rvList.addItemDecoration(VerticalSpaceItemDecoration(19.toDp))

        updateChatItems()

        binding.msgText.addTextChangedListener {
            if (it.isNullOrEmpty()) binding.sendBtn.setIconResource(R.drawable.ic_add)
            else binding.sendBtn.setIconResource(R.drawable.ic_send)
        }

        binding.sendBtn.setOnClickListener {
            val msgText = binding.msgText.text
            if (!msgText.isNullOrEmpty()) {
                interactor.sendMessage(currentChatId, msgText.toString())
                updateChatItems()

                binding.msgText.text?.clear()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateChatItems() {
        adapter.items = interactor.getMessageByChat(currentChatId).mapToUiList(currentUserId)
    }

    private fun showBottomEmojiDialog() {
        SelectEmojiBottomDialog.show(childFragmentManager)
    }

    override fun onLongClickMessage(uIdMessage: Int) {
        selectedMessageId = uIdMessage
        showBottomEmojiDialog()
    }

    override fun onClickPlusReaction() {
        showBottomEmojiDialog()
    }

    override fun onClickReaction(uIdMessage: Int, emoji: String) {
        // TODO: FIX ANIMATION
        interactor.changeSelectedReaction(currentChatId, uIdMessage, emoji)
        updateChatItems()
    }
}