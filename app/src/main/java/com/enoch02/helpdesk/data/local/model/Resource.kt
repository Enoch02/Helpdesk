package com.enoch02.helpdesk.data.local.model

sealed class Resource<out T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>()
    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}