package com.timofeev.words.data.mappers

import com.timofeev.words.data.dto.Definition
import com.timofeev.words.data.dto.Meaning
import com.timofeev.words.data.dto.Phonetic
import com.timofeev.words.data.dto.WordDetailsResponseItem
import com.timofeev.words.domain.model.WordDetails

class WordDetailsMapper {
    fun map(wordDetailsResponseItem: WordDetailsResponseItem): WordDetails {
        val meanings = wordDetailsResponseItem.meanings?.map { map(it) }
        val phonetics = wordDetailsResponseItem.phonetics?.map { map(it) }
        return WordDetails(
            meanings = meanings,
            phonetics = phonetics,
            word = wordDetailsResponseItem.word
        )
    }

    private fun map(phonetic: Phonetic): com.timofeev.words.domain.model.Phonetic {
        return com.timofeev.words.domain.model.Phonetic(
            audio = phonetic.audio,
            sourceUrl = phonetic.sourceUrl,
            text = phonetic.text
        )
    }

    private fun map(definition: Definition?): com.timofeev.words.domain.model.Definition{
        return com.timofeev.words.domain.model.Definition(
            definition = definition?.definition,
            example = definition?.example
        )
    }

    private fun map(meaning: Meaning): com.timofeev.words.domain.model.Meaning{
        val definitions = meaning.definitions?.map { map(it) }
        return com.timofeev.words.domain.model.Meaning(
            antonyms = meaning.antonyms,
            definitions = definitions,
            partOfSpeech = meaning.partOfSpeech,
            synonyms = meaning.synonyms
        )
    }
}