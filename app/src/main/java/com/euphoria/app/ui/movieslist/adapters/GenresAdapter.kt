package com.euphoria.app.ui.movieslist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.euphoria.app.databinding.ItemGenreBinding
import com.euphoria.app.R

class GenresAdapter(
    private val onGenreClick: (String) -> Unit
) : ListAdapter<String, RecyclerView.ViewHolder>(GenreDiffCallback()) {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
        private const val TYPE_FOOTER = 2
    }

    var selectedGenre: String? = null
        private set

    override fun getItemCount(): Int {
        return currentList.size + 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER
            itemCount - 1 -> TYPE_FOOTER
            else -> TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_genre_header, parent, false)
                HeaderViewHolder(view)
            }

            TYPE_FOOTER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_movie_footer, parent, false)
                FooterViewHolder(view)
            }

            else -> {
                val binding = ItemGenreBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                GenreViewHolder(binding, onGenreClick)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GenreViewHolder) {
            val genre = currentList[position - 1]
            holder.bind(genre, selectedGenre)
        }
    }

    fun updateSelectedGenre(genre: String?) {
        val oldSelected = selectedGenre
        selectedGenre = genre

        val oldIndex = currentList.indexOf(oldSelected)
        val newIndex = currentList.indexOf(genre)

        if (oldIndex != -1) notifyItemChanged(oldIndex + 1)
        if (newIndex != -1 && newIndex != oldIndex) notifyItemChanged(newIndex + 1)
    }


    class GenreViewHolder(
        private val binding: ItemGenreBinding,
        private val onGenreClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(genre: String, selectedGenre: String?) {
            with(binding) {
                tvGenre.text = genre.replaceFirstChar { it.uppercase() }
                root.isSelected = genre == selectedGenre
                root.setOnClickListener { onGenreClick(genre) }
            }
        }
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view)
    class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class GenreDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem
    }
}