package com.euphoria.app.ui.movieslist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.euphoria.app.R
import com.euphoria.app.data.models.Film
import com.euphoria.app.databinding.ItemMovieBinding

class MoviesAdapter(
    private val onItemClick: (Film) -> Unit
) : ListAdapter<Film, MoviesAdapter.MovieViewHolder>(FilmDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MovieViewHolder(
        private val binding: ItemMovieBinding,
        private val onItemClick: (Film) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(film: Film) {
            with(binding) {
                tvTitle.text = film.localizedName
                tvTitle.maxLines = 2

                Glide.with(root.context)
                    .load(film.imageUrl)
                    .placeholder(R.drawable.poster_image)
                    .error(R.drawable.poster_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivPoster)

                root.setOnClickListener { onItemClick(film) }
            }
        }
    }

    private class FilmDiffCallback : DiffUtil.ItemCallback<Film>() {
        override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem == newItem
        }
    }
}
