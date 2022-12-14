package com.example.test.utils

import com.example.test.data.model.UserLocationModel

sealed class ResponseDataBase<out T> {
    data class SuccessList<out T>(val value: List<T>) : ResponseDataBase<T>()
    data class SuccessNotList<out T>(val value: T) : ResponseDataBase<T>()
    data class Failure(
        val errorBody: Throwable,
    ) : ResponseDataBase<Nothing>()

    object Empty : ResponseDataBase<Nothing>()
    object Loading : ResponseDataBase<Nothing>()
}
sealed class ErrorApp<out T> {
    data class FailureDataBase<out T>(val value: T) : ErrorApp<T>()
    data class FailureUnknown<out T>(val value: T) : ErrorApp<T>()
    data class FailureLocation<out T>(val value: T) : ErrorApp<T>()
}

sealed class ResponsePhocus<out T> {
    data class Marker<out T>(val value: CustomMarker) : ResponsePhocus<T>()
    data class PhocusMarker<out T>(val value: CustomMarker) : ResponsePhocus<T>()
    data class Location<out T>(val value: UserLocationModel) : ResponsePhocus<T>()
    object Empty : ResponsePhocus<Nothing>()
    object Clear : ResponsePhocus<Nothing>()
}


