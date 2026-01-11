package com.timofeev.words.utils

sealed class Resource<T>(data: T? = null, message: String? = null) {
    class Success<T>(data: T): Resource<T>(data = data)
    class Error<T>(message: String): Resource<T>(message = message)
}