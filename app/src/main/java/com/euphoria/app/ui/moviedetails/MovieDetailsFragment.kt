package com.euphoria.app.ui.moviedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.euphoria.app.R
import com.euphoria.app.databinding.FragmentMovieDetailsBinding

class MovieDetailsFragment : Fragment() {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: MovieDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        bindMovieData()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun bindMovieData() {
        val film = args.film
        binding.filmName.text = film.originalName
        binding.tvLocalizedName.text = film.localizedName
        binding.tvGenresYear.text = if (film.genres.isNotEmpty()) {
            getString(R.string.genres_year_format, film.genres.joinToString(", "), film.year)
        } else {
            getString(R.string.year_only_format, film.year)
        }

        if (film.rating != null) {
            binding.tvRating.text = getString(R.string.rating_format, film.rating)
            binding.tvRatingName.text = getString(R.string.rating_name)
            binding.tvRating.visibility = View.VISIBLE
            binding.tvRatingName.visibility = View.VISIBLE
        } else {
            binding.tvRating.visibility = View.GONE
            binding.tvRatingName.visibility = View.GONE
        }

        binding.tvDescription.text = film.description?.ifEmpty { getString(R.string.no_description) }
            ?: getString(R.string.no_description)

        film.imageUrl?.let { url ->
            Glide.with(requireContext())
                .load(url)
                .placeholder(R.drawable.poster_image)
                .error(R.drawable.poster_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivPoster)
        } ?: run {
            binding.ivPoster.setImageResource(R.drawable.poster_image)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}