package com.timofeev.words.data.network

import com.timofeev.words.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}