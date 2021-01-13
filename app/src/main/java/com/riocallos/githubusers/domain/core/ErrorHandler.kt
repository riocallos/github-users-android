package com.riocallos.githubusers.domain.core

import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

class ErrorHandler {

    companion object {

        fun handleError(error: Throwable): Error {
            return when (error) {
                is HttpException -> {
                    try {
                        val body = error.response()!!.errorBody()
                        val string = body!!.string()
                        try {
                            val errorResponse = Gson().fromJson(string, Errors::class.java)
                            val message = if (errorResponse.error[0].detail.isNotEmpty()) {
                                errorResponse.error[0].detail
                            } else {
                                "Please check your internet connection."
                            }
                            Error(message)
                        } catch (e: Exception) {
                            val errorResponse = Gson().fromJson(string, SingleError::class.java)
                            val message = if (errorResponse.error!!.detail.isNotEmpty()) {
                                errorResponse.error.detail
                            } else {
                                "Please check your internet connection."
                            }
                            Error(message)
                        }
                    } catch (e: Exception) {
                        Error("An unexpected error occurred. Please try again")
                    }
                }
                is IOException -> {
                    if (error.message == "Canceled") {
                        // Empty error message to ignore cancelled requests.
                        Error("An unexpected error occurred. Please try again")
                    } else {
                        Error("Please check your internet connection.")
                    }
                }
                else -> {
                    Error("An unexpected error occurred. Please try again")
                }
            }
        }
    }
}