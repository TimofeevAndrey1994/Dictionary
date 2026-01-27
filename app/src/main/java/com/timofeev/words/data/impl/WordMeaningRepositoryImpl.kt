package com.timofeev.words.data.impl

import com.timofeev.words.data.dto.WordDetailsResponse
import com.timofeev.words.data.dto.WordMeaningRequest
import com.timofeev.words.data.mappers.WordDetailsMapper
import com.timofeev.words.data.network.NetworkClient
import com.timofeev.words.data.network.RetrofitNetworkClient
import com.timofeev.words.domain.api.WordMeaningRepository
import com.timofeev.words.domain.model.WordDetails
import com.timofeev.words.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.ArrayList

class WordMeaningRepositoryImpl(
    private val networkClient: NetworkClient,
    private val wordDetailsMapper: WordDetailsMapper
) : WordMeaningRepository {
    override fun getWordMeaning(word: String): Flow<Resource<List<WordDetails>>> = flow {
        val response = networkClient.doRequest(WordMeaningRequest().apply { this.word = word })
        when (response.resultCode) {
            -1 -> emit(Resource.Error(
                "Отсутствует подключение к сети.\nПроверьте подключение и повторите попытку!"))
            200 -> {
                val data = (response as WordDetailsResponse).map { wordDetailsMapper.map(it) }
                emit(Resource.Success(data = data))
            }
            else -> {
                emit(Resource.Error("Ошибка получения данных!"))
            }
        }
    }
}