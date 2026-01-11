package com.timofeev.words.domain.api

import com.timofeev.words.domain.model.WordDetails
import com.timofeev.words.utils.Resource
import kotlinx.coroutines.flow.Flow

interface GetWordMeaningUseCase {
    fun getWordMeaning(word: String): Flow<Resource<List<WordDetails>>>
}