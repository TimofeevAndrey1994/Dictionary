package com.timofeev.words.di

import android.content.Context
import com.timofeev.words.data.impl.SearchHistoryImpl
import com.timofeev.words.data.impl.WordMeaningRepositoryImpl
import com.timofeev.words.data.mappers.WordDetailsMapper
import com.timofeev.words.data.network.DictionaryApi
import com.timofeev.words.data.network.NetworkClient
import com.timofeev.words.data.network.RetrofitNetworkClient
import com.timofeev.words.domain.api.AddToSearchHistoryUseCase
import com.timofeev.words.domain.api.ClearSearchHistoryUseCase
import com.timofeev.words.domain.api.GetSearchHistoryUseCase
import com.timofeev.words.domain.api.GetWordMeaningUseCase
import com.timofeev.words.domain.api.SearchHistory
import com.timofeev.words.domain.api.WordMeaningRepository
import com.timofeev.words.domain.impl.AddToSearchHistoryUseCaseImpl
import com.timofeev.words.domain.impl.ClearSearchHistoryUseCaseImpl
import com.timofeev.words.domain.impl.GetSearchHistoryUseCaseImpl
import com.timofeev.words.domain.impl.GetWordMeaningUseCaseImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {
    //--- WordMeaningUseCase
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

    private fun provideSearchHistory(context: Context): SearchHistory {
        return SearchHistoryImpl(context)
    }

    //--- AddToSearchHistoryUseCase
    fun provideAddToSearchHistoryUseCase(context: Context): AddToSearchHistoryUseCase {
        return AddToSearchHistoryUseCaseImpl(provideSearchHistory(context))
    }
    //--- AddToSearchHistoryUseCase
    fun provideClearSearchHistoryUseCase(context: Context): ClearSearchHistoryUseCase {
        return ClearSearchHistoryUseCaseImpl(provideSearchHistory(context))
    }
    //--- AddToSearchHistoryUseCase
    fun provideGetSearchHistoryUseCase(context: Context): GetSearchHistoryUseCase {
        return GetSearchHistoryUseCaseImpl(provideSearchHistory(context))
    }
}