package ru.fasdev.tfs.view.ui.bottomDialog.emoji

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.BottomDialogSelectEmojiBinding
import ru.fasdev.tfs.view.feature.util.EmojiUtil
import ru.fasdev.tfs.view.ui.bottomDialog.emoji.adapter.EmojiHolderFactory
import ru.fasdev.tfs.view.ui.bottomDialog.emoji.adapter.viewHolder.EmojiViewHolder
import ru.fasdev.tfs.view.ui.bottomDialog.emoji.adapter.viewType.EmojiUi
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseAdapter
import ru.fasdev.tfs.view.ui.global.recycler.base.ViewType

class SelectEmojiBottomDialog : BottomSheetDialogFragment(), EmojiViewHolder.OnSelectedListener {
    companion object {
        val TAG: String = SelectEmojiBottomDialog::class.java.simpleName
        const val KEY_SELECTED_EMOJI = "SELECTED_EMOJI"

        fun newInstance() = SelectEmojiBottomDialog()
        fun show(fragmentManager: FragmentManager): SelectEmojiBottomDialog =
            newInstance().also { it.show(fragmentManager, TAG) }
    }

    private var _binding: BottomDialogSelectEmojiBinding? = null
    private val binding get() = _binding!!

    private val holderFactory by lazy { EmojiHolderFactory(this) }
    private val adapter by lazy { return@lazy BaseAdapter<ViewType>(holderFactory) }

    override fun getTheme(): Int = R.style.Theme_TFS_BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomDialogSelectEmojiBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvList: RecyclerView = binding.rvEmojiList
        rvList.adapter = adapter

        val listEmoji = EmojiUtil.getListEmoji(requireContext())
        val resultEmoji = listEmoji.map { EmojiUi(listEmoji.indexOf(it), it) }

        adapter.items = resultEmoji

        rvList.post {
            val sizeColumn = rvList.width / EmojiUi.COLUMN_WIDTH // Calculate column size
            rvList.layoutManager = GridLayoutManager(context, sizeColumn)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSelectedEmoji(emoji: String) {
        setFragmentResult(TAG, bundleOf(KEY_SELECTED_EMOJI to emoji))
        dismiss()
    }
}
