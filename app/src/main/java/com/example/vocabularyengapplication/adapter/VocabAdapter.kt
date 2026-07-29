package com.example.vocabularyengapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabularyengapplication.databinding.ItemVocabularyBinding
import com.example.vocabularyengapplication.model.ListWordState
import com.example.vocabularyengapplication.model.WordData

class VocabAdapter(
    mList: List<WordData>,
    selectedListState: ListWordState,
    private val onRemoveItem: (Int) -> Unit,
) : RecyclerView.Adapter<VocabAdapter.VocabViewHolder>() {
    private var currentList = mList
    private var currentListState = selectedListState

    class VocabViewHolder(
        private val itemWordViewBinding: ItemVocabularyBinding,
    ) : RecyclerView.ViewHolder(itemWordViewBinding.root) {
        fun bind(
            item: WordData,
            currentListState: ListWordState,
            onRemoveItem: (Int) -> Unit,
        ) {
            itemWordViewBinding.tvNameVocab.text = item.name
            itemWordViewBinding.tvMeaning.text = item.meaning
            itemWordViewBinding.tvCategory.apply {
                text = item.category.title
                requestLayout()
            }

            itemWordViewBinding.layoutCategory.setCardBackgroundColor(
                itemWordViewBinding.root.context.getColor(item.category.color),
            )

            itemWordViewBinding.btnRemove.isVisible = currentListState == ListWordState.REMOVE
            itemWordViewBinding.btnRemove.setOnClickListener {
                onRemoveItem(item.id)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): VocabViewHolder {
        val view = ItemVocabularyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VocabViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: VocabViewHolder,
        position: Int,
    ) {
        holder.bind(currentList[position], currentListState, onRemoveItem)
    }

    override fun getItemCount(): Int = currentList.size

    internal fun setListState(selectedListState: ListWordState) {
        currentListState = selectedListState
        notifyDataSetChanged()
    }

    internal fun refreshList(list: List<WordData>) {
        currentList = list
        notifyDataSetChanged()
    }
}
