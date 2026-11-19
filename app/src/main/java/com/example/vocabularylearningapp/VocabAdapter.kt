package com.example.vocabularylearningapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabularylearningapp.databinding.ItemVocabularyBinding

class VocabAdapter(
    private val onItemClick: ((Vocabulary) -> Unit)? = null
) : ListAdapter<Vocabulary, VocabAdapter.VocabViewHolder>(VocabDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VocabViewHolder {
        val binding = ItemVocabularyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VocabViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VocabViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VocabViewHolder(
        private val binding: ItemVocabularyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Vocabulary) {
            binding.tvWord.text = item.word
            binding.tvPhonetic.text = item.phonetic ?: ""
            binding.tvPhonetic.visibility = if (item.phonetic.isNullOrEmpty()) View.GONE else View.VISIBLE
            binding.tvMeaning.text = item.meaning

            if (item.example.isNullOrEmpty()) {
                binding.tvExample.visibility = View.GONE
            } else {
                binding.tvExample.visibility = View.VISIBLE
                binding.tvExample.text = binding.root.context.getString(R.string.example_format, item.example)
            }

            binding.root.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }

    private class VocabDiffCallback : DiffUtil.ItemCallback<Vocabulary>() {
        override fun areItemsTheSame(oldItem: Vocabulary, newItem: Vocabulary): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Vocabulary, newItem: Vocabulary): Boolean {
            return oldItem == newItem
        }
    }
}
