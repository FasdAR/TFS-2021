package ru.fasdev.tfs.screen.bottomDialog.selectedEmoji

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
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import ru.fasdev.tfs.R
import ru.fasdev.tfs.data.source.network.emoji.EmojiList
import ru.fasdev.tfs.databinding.BottomDialogSelectEmojiBinding
import ru.fasdev.tfs.recycler.base.adapter.RecyclerAdapter
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType
import ru.fasdev.tfs.screen.bottomDialog.selectedEmoji.adapter.EmojiHolderFactory
import ru.fasdev.tfs.screen.bottomDialog.selectedEmoji.adapter.viewHolder.EmojiViewHolder
import ru.fasdev.tfs.screen.bottomDialog.selectedEmoji.adapter.viewType.EmojiUi

class SelectEmojiBottomDialog : BottomSheetDialogFragment(), EmojiViewHolder.OnSelectedListener {
    companion object {
        val TAG: String = SelectEmojiBottomDialog::class.java.simpleName
        const val KEY_SELECTED_EMOJI = "SELECTED_EMOJI"

        fun show(fragmentManager: FragmentManager): SelectEmojiBottomDialog {
            return SelectEmojiBottomDialog().also { it.show(fragmentManager, TAG) }
        }
    }

    private var _binding: BottomDialogSelectEmojiBinding? = null
    private val binding get() = _binding!!

    private val holderFactory by lazy { EmojiHolderFactory(this) }
    private val adapter by lazy { return@lazy RecyclerAdapter<ViewType>(holderFactory) }

    private var disposeEmojiLoad: CompositeDisposable = CompositeDisposable()

    override fun getTheme(): Int = R.style.Theme_TFS_BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomDialogSelectEmojiBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvList: RecyclerView = binding.rvEmojiList
        rvList.adapter = adapter

        loadEmoji()

        rvList.post {
            val sizeColumn = rvList.width / EmojiUi.COLUMN_WIDTH // Calculate column size
            rvList.layoutManager = GridLayoutManager(context, sizeColumn)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeEmojiLoad.dispose()
        _binding = null
    }

    override fun onSelectedEmoji(emoji: String) {
        setFragmentResult(TAG, bundleOf(KEY_SELECTED_EMOJI to emoji))
        dismiss()
    }

    // #region Rx chains
    private fun loadEmoji() {
        disposeEmojiLoad += Single.just(EmojiList.values())
            .flatMapObservable { Observable.fromIterable(it.withIndex()) }
            .map { EmojiUi(it.index, it.value.unicode, it.value.nameInZulip) }
            .toList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { array -> adapter.items = array }
    }
    // #endregion
}
