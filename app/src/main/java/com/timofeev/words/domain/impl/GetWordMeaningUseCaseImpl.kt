package com.timofeev.words.domain.impl

import com.timofeev.words.domain.api.GetWordMeaningUseCase
import com.timofeev.words.domain.api.WordMeaningRepository
import com.timofeev.words.domain.model.WordDetails
import com.timofeev.words.utils.Resource
import kotlinx.coroutines.flow.Flow

class GetWordMeaningUseCaseImpl(private val wordMeaningRepository: WordMeaningRepository): GetWordMeaningUseCase {
    override fun getWordMeaning(word: String): Flow<Resource<List<WordDetails>>> {
        return wordMeaningRepository.getWordMeaning(word)
    }
}