package com.euphoria.app.di

import com.euphoria.app.data.repository.MoviesRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { MoviesRepository(get()) }
}