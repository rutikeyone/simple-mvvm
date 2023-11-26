package com.ru.foundation.model

import java.lang.Exception
import java.lang.IllegalStateException

typealias Mapper<Input, Output> = (Input) -> Output

sealed class Result<T> {

    fun <R> map(mapper: Mapper<T, R>? = null): Result<R> = when(this) {
        is ErrorResult -> ErrorResult(this.exception)
        is PendingResult -> PendingResult()
        is SuccessResult -> {
            if(mapper == null) throw IllegalStateException("Mapper should be not NULL for success result")
            SuccessResult(mapper(this.data))
        }
    }

}

class PendingResult<T> : Result<T>()

data class SuccessResult<T>(
    val data: T
) : Result<T>()

data class ErrorResult<T>(
    val exception: Exception
): Result<T>()

fun <T> Result<T>.takeSuccess(): T? {
    return if(this is SuccessResult) {
        return this.data
    } else {
        null
    }
}