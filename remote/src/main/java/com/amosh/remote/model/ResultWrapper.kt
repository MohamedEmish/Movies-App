package com.amosh.remote.model

data class ResultWrapper<T>(
    val page: Int? = null,
    val results: T? = null
)