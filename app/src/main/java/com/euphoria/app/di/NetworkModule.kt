package com.euphoria.app.di

import com.euphoria.app.data.api.MoviesApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {
    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://s3-eu-west-1.amazonaws.com/sequeniatesttask/")
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
            .create(MoviesApi::class.java)
    }
}