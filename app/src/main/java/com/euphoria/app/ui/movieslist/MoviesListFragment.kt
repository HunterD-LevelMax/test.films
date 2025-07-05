package com.euphoria.app.ui.movieslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.euphoria.app.R
import com.euphoria.app.databinding.FragmentMoviesListBinding
import com.euphoria.app.ui.movieslist.adapters.GenresAdapter
import com.euphoria.app.ui.movieslist.adapters.MoviesAdapter
import com.euphoria.app.viewmodels.GenresViewModel
import com.euphoria.app.viewmodels.MoviesViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesListFragment : Fragment() {
    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MoviesViewModel by viewModel()
    private val genresViewModel: GenresViewModel by viewModel()
    private lateinit var adapter: MoviesAdapter
    private lateinit var genresAdapter: GenresAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        observeViewModels()
    }

    private fun setupRecyclerViews() {
        adapter = MoviesAdapter { film ->
            if (isAdded) {
                val direction = MoviesListFragmentDirections
                    .actionMoviesListFragmentToMovieDetailsFragment(film)
                findNavController().navigate(direction)
            }
        }

        genresAdapter = GenresAdapter { genre ->
            val newSelection = if (genre == genresAdapter.selectedGenre) null else genre
            genresViewModel.filterMoviesByGenre(newSelection)
        }


        val concatAdapter = ConcatAdapter(genresAdapter)
        with(binding.rvGenre) {
            layoutManager = GridLayoutManager(requireContext(), 1)
            adapter = concatAdapter
        }

        with(binding.rvMovies) {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@MoviesListFragment.adapter
        }
    }

    private fun observeViewModels() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is MoviesViewModel.MoviesUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.nestedScroll.visibility = View.INVISIBLE
                        }

                        is MoviesViewModel.MoviesUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.nestedScroll.visibility = View.VISIBLE
                            genresViewModel.setMovies(state.movies)
                        }

                        is MoviesViewModel.MoviesUiState.Error -> {
                            binding.nestedScroll.visibility = View.INVISIBLE
                            binding.progressBar.visibility = View.VISIBLE
                            showError(getString(R.string.error_network))
                        }
                    }
                }
            }

            launch {
                genresViewModel.genresUiState.collectLatest { state ->
                    if (state is GenresViewModel.GenresUiState.Success) {
                        genresAdapter.submitList(state.genres)
                    }
                }
            }

            launch {
                genresViewModel.selectedGenre.collectLatest { selectedGenre ->
                    genresAdapter.updateSelectedGenre(selectedGenre)
                }
            }

            launch {
                genresViewModel.filteredMovies.collectLatest { movies ->
                    adapter.submitList(movies.sortedBy { it.localizedName })
                }
            }
        }
    }

    private fun showError(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_INDEFINITE
        ).setAction(R.string.retry) {
            viewModel.loadMovies()
        }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}