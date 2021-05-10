package ru.fasdev.tfs.screen.fragment.chat

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxrelay2.ReplayRelay
import dagger.android.support.AndroidSupportInjection
import ru.fasdev.tfs.R
import ru.fasdev.tfs.core.ext.doOnApplyWindowsInsets
import ru.fasdev.tfs.core.ext.getColorCompat
import ru.fasdev.tfs.core.ext.getSystemInsets
import ru.fasdev.tfs.core.ext.setSystemInsetsInTop
import ru.fasdev.tfs.databinding.FragmentChatBinding
import ru.fasdev.tfs.di.provide.ProvideFragmentRouter
import ru.fasdev.tfs.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.fragmentRouter.FragmentScreen
import ru.fasdev.tfs.mviCore.MviView
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.recycler.base.adapter.RecyclerAdapter
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType
import ru.fasdev.tfs.recycler.item.message.MessageViewHolder
import ru.fasdev.tfs.screen.bottomDialog.selectedEmoji.SelectEmojiBottomDialog
import ru.fasdev.tfs.screen.fragment.chat.model.DirectionScroll
import ru.fasdev.tfs.screen.fragment.chat.mvi.ChatAction
import ru.fasdev.tfs.screen.fragment.chat.mvi.ChatState
import ru.fasdev.tfs.screen.fragment.chat.mvi.ChatUiEffect
import ru.fasdev.tfs.screen.fragment.chat.recycler.ChatHolderFactory
import ru.fasdev.tfs.screen.fragment.chat.recycler.diff.ChatItemCallback
import ru.fasdev.tfs.screen.fragment.info.InfoPlaceholderFragment
import ru.fasdev.tfs.screen.fragment.info.handleErrorState
import javax.inject.Inject

class ChatFragment : Fragment(R.layout.fragment_chat),
    MessageViewHolder.OnLongClickMessageListener, MessageViewHolder.OnClickReactionListener,
    AsyncListDiffer.ListListener<ViewType>, MviView<Action, ChatState>,
    InfoPlaceholderFragment.Listener {

    companion object {
        private val TAG: String = ChatFragment::class.java.simpleName
        private const val COLOR_TOOLBAR = R.color.teal_500
        private const val LIMIT_UPDATE = 5

        private const val KEY_STREAM_ID = "STREAM_ID"
        private const val KEY_TOPIC_ID = "TOPIC_ID"

        private fun newInstance(idStream: Long, idTopic: Long): ChatFragment {
            return ChatFragment().apply {
                arguments = bundleOf(KEY_STREAM_ID to idStream, KEY_TOPIC_ID to idTopic)
            }
        }

        fun getScreen(idStream: Long, idTopic: Long) = FragmentScreen(TAG, newInstance(idStream, idTopic))
    }

    override val actions: ReplayRelay<Action> = ReplayRelay.create()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: ChatViewModel by viewModels{viewModelFactory}

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val fragmentRouter: FragmentRouter get() = (requireActivity() as ProvideFragmentRouter).getRouter()

    private val infoFragment get() = childFragmentManager.findFragmentById(R.id.info_placeholder) as InfoPlaceholderFragment

    private val holderFactory by lazy { ChatHolderFactory(this, this) }
    private val adapter by lazy { RecyclerAdapter(holderFactory, ChatItemCallback()).apply { differListListener = this@ChatFragment } }

    private val idStream: Long
        get() = requireArguments().getLong(KEY_STREAM_ID)

    private val idTopic: Long
        get() = requireArguments().getLong(KEY_TOPIC_ID)

    private var listIsDown: Boolean = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
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

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        binding.toolbarLayout.root.setSystemInsetsInTop()
        view.doOnApplyWindowsInsets { insetView, windowInsets, initialPadding ->
            val systemInsets = windowInsets.getSystemInsets()
            insetView.updatePadding(bottom = initialPadding.bottom + systemInsets.bottom)
        }

        setFragmentResultListener(SelectEmojiBottomDialog.TAG) {_, bundle ->
            val emoji = bundle.getString(SelectEmojiBottomDialog.KEY_SELECTED_EMOJI)
            emoji?.let { actions.accept(ChatAction.Ui.SelectedReaction(emoji = emoji)) }
        }

        with(binding.toolbarLayout) {
            toolbarRoot.setBackgroundColor(requireContext().getColorCompat(COLOR_TOOLBAR))

            btnNav.isVisible = true
            btnNav.setOnClickListener { fragmentRouter.back() }
        }

        binding.rvList.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, true)
        binding.rvList.adapter = adapter
        initListenerScroll()

        binding.msgEdt.addTextChangedListener {
            //TODO: CHANGE PLUS TO NORMAL DRAWABLE
            if (it.isNullOrEmpty()) {
                binding.sendBtn.setIconResource(R.drawable.ic_add)
            } else {
                binding.sendBtn.setIconResource(R.drawable.ic_send)
            }
        }

        binding.sendBtn.setOnClickListener {
            val msgText = binding.msgEdt.text.toString().trim()
            binding.msgEdt.text?.clear()
            actions.accept(ChatAction.Ui.SendMessage(msgText))
        }

        viewModel.bind(this)
        viewModel.bind(::renderSideEffect)

        actions.accept(ChatAction.Ui.LoadStreamInfo(idStream, idTopic))
    }

    private fun initListenerScroll() {
        binding.rvList.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = (binding.rvList.layoutManager as LinearLayoutManager)
                    val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                    listIsDown = firstVisiblePosition == 0
                    val isUpScroll = dy < 0

                    val isUpdate = if (isUpScroll) {
                        (adapter.itemCount - lastVisibleItem) <= LIMIT_UPDATE
                    } else {
                        lastVisibleItem - LIMIT_UPDATE <= LIMIT_UPDATE
                    }

                    if (isUpdate) {
                        val id = adapter.items[lastVisibleItem].uId.toLong()
                        if (isUpScroll) {
                            actions.accept(ChatAction.Ui.LoadPageMessages(id, DirectionScroll.UP))
                        } else {
                            actions.accept(ChatAction.Ui.LoadPageMessages(id, DirectionScroll.DOWN))
                        }
                    }
                }
            }
        )
    }

    private fun showBottomEmojiDialog() {
        SelectEmojiBottomDialog.show(parentFragmentManager)
    }

    override fun onClickReaction(uIdMessage: Int, emoji: String, isSelected: Boolean) {
        if (!isSelected) {
            actions.accept(ChatAction.Ui.UnSelectedReaction(uIdMessage.toLong(), emoji))
        } else {
            actions.accept(ChatAction.Ui.SelectedReaction(uIdMessage.toLong(), emoji))
        }
    }

    override fun onClickAddNewReaction(uIdMessage: Int) {
        actions.accept(ChatAction.Ui.OpenEmojiDialog(uIdMessage.toLong()))
    }

    override fun onLongClickMessage(uIdMessage: Int) {
        actions.accept(ChatAction.Ui.OpenEmojiDialog(uIdMessage.toLong()))
    }

    override fun onCurrentListChanged(
        previousList: MutableList<ViewType>,
        currentList: MutableList<ViewType>
    ) {
        _binding?.let {
            if (!it.rvList.canScrollVertically(1) && listIsDown) {
                it.rvList.scrollToPosition(0)
            }
        }
    }

    override fun render(state: ChatState) {
        binding.toolbarLayout.title.text = resources.getString(R.string.main_topic_title, state.streamName)
        binding.topic.text = resources.getString(R.string.sub_topic_title, state.topicName)

        adapter.items = state.items

        if (state.error != null) {
            binding.infoPlaceholder.isGone = false
            binding.rvList.isGone = true

            infoFragment.handleErrorState(state.error)
        } else {
            binding.infoPlaceholder.isGone = true
            binding.rvList.isGone = false
        }
    }

    private fun renderSideEffect(uiEffect: ChatUiEffect) {
        when (uiEffect) {
            is ChatUiEffect.ErrorSnackbar -> {
                //TODO: ADD ERROR SNACKBAR
            }
            is ChatUiEffect.OpenEmojiDialog -> {
                showBottomEmojiDialog()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        _binding = null
        viewModel.unBind()
    }

    override fun onBtnClickInfoPlaceholder(buttonType: InfoPlaceholderFragment.ButtonType) {
        when (buttonType) {
            InfoPlaceholderFragment.ButtonType.POSITIVE -> {
                actions.accept(ChatAction.Ui.LoadPageMessages(null, DirectionScroll.UP))
            }
        }
    }
}