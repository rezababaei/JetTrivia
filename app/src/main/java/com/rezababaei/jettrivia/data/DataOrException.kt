package com.rezababaei.jettrivia.data

import java.lang.Exception

//Our wrapper class
data class DataOrException<T, Boolean, E : Exception>(
    var data: T? = null,
    var loading: Boolean? = null,
    var e: E? = null
)
