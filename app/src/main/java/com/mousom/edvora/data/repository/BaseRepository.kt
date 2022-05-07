package com.mousom.edvora.data.repository

import com.mousom.edvora.data.api.RetrofitInstance
import com.mousom.edvora.data.model.OperationErrorType
import com.mousom.edvora.data.model.RideData
import com.mousom.edvora.data.model.UserData
import com.mousom.edvora.utils.EdvoraOperationError
import retrofit2.Response
import retrofit2.http.Url

class BaseRepository {

    suspend fun getRidesData(@Url ridesUrl: String): Response<RideData> {
        return RetrofitInstance.getRidesDataApi.getRidesData(ridesUrl)
    }

    suspend fun getUserData(@Url userUrl: String): Response<UserData> {
        return RetrofitInstance.getUserDataApi.getUserData(userUrl)
    }

    fun responseError(
        errorMessage: String,
        fieldErrors: String,
        errorId: Int = 1
    ): EdvoraOperationError {

        val operationError = EdvoraOperationError
            .Builder(OperationErrorType.RESPONSE_ERROR)
            .errorId(errorId)
            .messageTitle("Response Error")
            .message(errorMessage)
            .fieldError(fieldErrors)
            .build()

        return operationError
    }

    fun connectionError(errorMessage: String, errorId: Int = 1): EdvoraOperationError {

        val operationError = EdvoraOperationError
            .Builder(OperationErrorType.CONNECTION_ERROR)
            .errorId(errorId)
            .messageTitle("Connection Error")
            .message(errorMessage)
            .build()

        return operationError
    }

    fun processingError(errorId: Int = 1): EdvoraOperationError {

        val operationError = EdvoraOperationError
            .Builder(OperationErrorType.PROCESSING_ERROR)
            .errorId(errorId)
            .messageTitle("API Processing Error")
            .message("Something Went Wrong Please Try again later.")
            .build()

        return operationError
    }
}