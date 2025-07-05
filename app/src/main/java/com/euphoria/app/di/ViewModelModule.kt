package com.euphoria.app.di

import com.euphoria.app.viewmodels.GenresViewModel
import com.euphoria.app.viewmodels.MoviesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MoviesViewModel(get()) }
    viewModel { GenresViewModel() }
}