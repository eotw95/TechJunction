package com.example.techjunction.room

/**
 * A generic class that holds a data or exception
 */
sealed class Result<out R> {
    data class Success<out T>(val data: T): Result<T>()
    data class Error(val exception: Exception): Result<Nothing>()
}