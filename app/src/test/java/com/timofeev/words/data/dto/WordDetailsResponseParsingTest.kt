package com.timofeev.words.data.dto

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class WordDetailsResponseParsingTest {

    @Test
    fun `gson parses dictionary response into list dto`() {
        val json = """
            [
              {
                "word": "hello",
                "phonetics": [{"text": "/həˈləʊ/", "audio": "https://example.com/audio.mp3"}],
                "meanings": [
                  {
                    "partOfSpeech": "noun",
                    "definitions": [{"definition": "A greeting", "example": "Hello, world!"}]
                  }
                ]
              }
            ]
        """.trimIndent()

        val parsed = Gson().fromJson(json, WordDetailsResponse::class.java)

        assertNotNull(parsed)
        assertEquals(1, parsed.size)
        assertEquals("hello", parsed.first().word)
        assertEquals("noun", parsed.first().meanings?.first()?.partOfSpeech)
    }
}
