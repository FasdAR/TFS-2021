package ru.fasdev.tfs.view.ui.bottomDialog.emoji

import android.os.Bundle
import android.util.Log
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
import ru.fasdev.tfs.view.feature.util.toDp
import ru.fasdev.tfs.view.feature.util.toSp
import ru.fasdev.tfs.view.ui.bottomDialog.emoji.adapter.EmojiHolderFactory
import ru.fasdev.tfs.view.ui.bottomDialog.emoji.adapter.viewTypes.EmojiUi
import java.util.regex.Matcher
import java.util.regex.Pattern

class SelectEmojiBottomDialog : BottomSheetDialogFragment()
{
    companion object {
        const val TAG = "BS_SELECT_EMOJI"
        const val SUPPORTED_EMOJI = "\uD83D\uDE0E\uD83D\uDE0C\uD83D\uDE0C\uD83D\uDE14\uD83D\uDE0B" +
                "\uD83D\uDE1B\uD83D\uDE0D\uD83D\uDE01\uD83D\uDC4D\uD83D\uDE18\uD83C\uDF83\uD83D" +
                "\uDE0A\uD83D\uDE1A\uD83D\uDE43\uD83D\uDE05\uD83D\uDE02\uD83D\uDE07\uD83D\uDE09" +
                "\uD83D\uDE0C\uD83D\uDE17\uD83D\uDE1C\uD83D\uDE0E\uD83D\uDE0F\uD83D\uDE1C\uD83D" +
                "\uDE17\uD83D\uDE17\uD83D\uDE02\uD83D\uDE0E\uD83D\uDE0C\uD83D\uDE0C\uD83D\uDE14" +
                "\uD83D\uDE0B\uD83D\uDE1B\uD83D\uDE0D\uD83D\uDE01\uD83D\uDC4D\uD83D\uDE18\uD83C" +
                "\uDF83\uD83D\uDE0A\uD83D\uDE1A\uD83D\uDE43\uD83D\uDE05\uD83D\uDE02\uD83D\uDE07" +
                "\uD83D\uDE09\uD83D\uDE0C\uD83D\uDE17\uD83D\uDE1C\uD83D\uDE0E\uD83D\uDE0F\uD83D" +
                "\uDE1C\uD83D\uDE17\uD83D\uDE17\uD83D\uDE02"

        const val EMOJI_REGEX = "[\uD83C-\uDBFF\uDC00-\uDFFF]+"

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

        val result = Pattern.compile("\\P{M}\\p{M}*+").matcher(SUPPORTED_EMOJI)

        val matchList: MutableList<String> = mutableListOf()
        while (result.find()) {
            matchList.add(result.group())
        }

        val resL = matchList.map { EmojiUi(matchList.indexOf(it), it) }
        adapter.items = resL

        rvList.post {
            val sizeColumn = rvList.width / 55.toDp
            rvList.layoutManager = GridLayoutManager(context, sizeColumn)
            rvList.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}