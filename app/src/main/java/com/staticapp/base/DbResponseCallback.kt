package com.staticapp.base



sealed class DbResponseCallback<T> {
    class Failed<T>(val message: String) : DbResponseCallback<T>()
    class Loading<T> : DbResponseCallback<T>()
    class Success<T>(val data: T) : DbResponseCallback<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> failed(message: String) = Failed<T>(message)
    }
}


