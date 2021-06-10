package com.example.jetpack_movies.di

import com.example.jetpack_movies.network.IApiService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

//@Module
//@InstallIn(ApplicationComponent::class)
object NetworkModule {

//    @Singleton
//    @Provides
    fun createApiService() : IApiService {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        return retrofit.create(IApiService::class.java)
    }
}