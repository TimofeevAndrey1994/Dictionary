package com.timofeev.words.di

import android.content.Context
import com.timofeev.words.data.impl.WordMeaningRepositoryImpl
import com.timofeev.words.data.mappers.WordDetailsMapper
import com.timofeev.words.data.network.DictionaryApi
import com.timofeev.words.data.network.NetworkClient
import com.timofeev.words.data.network.RetrofitNetworkClient
import com.timofeev.words.domain.api.GetWordMeaningUseCase
import com.timofeev.words.domain.api.WordMeaningRepository
import com.timofeev.words.domain.impl.GetWordMeaningUseCaseImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {
    private fun provideNetworkClient(context: Context): NetworkClient {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.dictionaryapi.dev")
            .build()
        return RetrofitNetworkClient(retrofit.create(DictionaryApi::class.java), context)
    }
    private fun provideWordMeaningRepository(context: Context): WordMeaningRepository {

        return WordMeaningRepositoryImpl(provideNetworkClient(context), WordDetailsMapper())
    }
    fun provideGetWordMeaningUseCase(context: Context): GetWordMeaningUseCase {
        return GetWordMeaningUseCaseImpl(provideWordMeaningRepository(context))
    }
}