package com.timofeev.words.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.timofeev.words.data.dto.Response
import com.timofeev.words.data.dto.WordMeaningRequest

class RetrofitNetworkClient(
    private val dictionaryApi: DictionaryApi,
    private val context: Context
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        when {
            !isConnected() -> return object : Response { override var resultCode = -1}
            dto is WordMeaningRequest -> {
                try {
                    val response = dictionaryApi.getWordMeaning(dto.word)
                    return response.apply { resultCode = 200 }
                } catch (e: Exception) {
                    println("Ошибка сети $e")
                    return object : Response { override var resultCode: Int = 500 }
                }

            }
            else -> {
                return object : Response { override var resultCode = 400 }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}