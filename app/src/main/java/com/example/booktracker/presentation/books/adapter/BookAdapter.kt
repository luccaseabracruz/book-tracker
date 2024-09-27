package com.example.booktracker.presentation.books.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracker.databinding.ItemBookBinding
import com.example.booktracker.domain.model.BookDomain

class BookAdapter : ListAdapter<BookDomain, BookAdapter.BookViewHolder>(DiffCallback()) {

    var click: (BookDomain) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBookBinding.inflate(inflater, parent, false)

        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BookViewHolder(
        private val binding: ItemBookBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookDomain) {
            binding.tvBookTitle.text = item.title
        }
    }

}

class DiffCallback(): DiffUtil.ItemCallback<BookDomain>() {
    override fun areItemsTheSame(oldItem: BookDomain, newItem: BookDomain): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: BookDomain, newItem: BookDomain): Boolean =
        oldItem.id == newItem.id
}
