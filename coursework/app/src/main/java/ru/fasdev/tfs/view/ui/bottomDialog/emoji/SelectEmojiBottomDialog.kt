package ru.fasdev.tfs.view.ui.bottomDialog.emoji

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.fasdev.tfs.databinding.BottomDialogSelectEmojiBinding
import ru.fasdev.tfs.view.feature.recycler.Adapter
import ru.fasdev.tfs.view.feature.recycler.base.ViewTyped
import ru.fasdev.tfs.view.feature.util.EmojiUtil
import ru.fasdev.tfs.view.feature.util.toDp
import ru.fasdev.tfs.view.ui.bottomDialog.emoji.adapter.EmojiHolderFactory
import ru.fasdev.tfs.view.ui.bottomDialog.emoji.adapter.viewTypes.EmojiUi
import kotlin.concurrent.thread

class SelectEmojiBottomDialog : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "BS_SELECT_EMOJI"

        fun newInstance() = SelectEmojiBottomDialog()
        fun show(fragmentManager: FragmentManager): SelectEmojiBottomDialog =
            newInstance().also { it.show(fragmentManager, TAG) }
    }

    private var _binding: BottomDialogSelectEmojiBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { return@lazy Adapter<ViewTyped>(EmojiHolderFactory()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = BottomDialogSelectEmojiBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvList: RecyclerView = binding.rvEmojiList
        rvList.adapter = adapter

        thread {
            val listEmoji = EmojiUtil.getListEmoji(requireContext())
            val resultEmoji = listEmoji.map { EmojiUi(listEmoji.indexOf(it), it) }

            rvList.post {
                adapter.items = resultEmoji
            }
        }

        rvList.post {
            val sizeColumn = rvList.width / 55.toDp // Calculate column size
            rvList.layoutManager = GridLayoutManager(context, sizeColumn)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
