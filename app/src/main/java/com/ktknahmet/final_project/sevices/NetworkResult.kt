package com.ktknahmet.final_project.sevices


sealed class NetworkResult<ResultType> {

    /**
     * Describes success state of the UI with
     * [data] shown
     */
    data class Success<ResultType>(
        val data: ResultType
    ) : NetworkResult<ResultType>()

    /**
     * Describes loading state of the UI
     */

    /**
     *  Describes error state of the UI
     */
    data class Error<ResultType>(
        val message: String = ""
    ) : NetworkResult<ResultType>()

    data class Empty<ResultType>(
        val message: String = ""
    ) : NetworkResult<ResultType>()
    companion object {

        fun <ResultType> success(data: ResultType): NetworkResult<ResultType> = Success(data)

        fun <ResultType> error(message: String): NetworkResult<ResultType> = Error(message)

        fun <ResultType> empty(message: String): NetworkResult<ResultType> = Empty(message)
    }
}
