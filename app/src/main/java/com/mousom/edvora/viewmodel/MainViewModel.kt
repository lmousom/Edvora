package com.mousom.edvora.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mousom.edvora.data.model.RideData
import com.mousom.edvora.data.model.UserData
import com.mousom.edvora.data.repository.BaseRepository
import com.mousom.edvora.utils.EdvoraOperationError
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.http.Url
import java.io.IOException

class MainViewModel(
    private val repository: BaseRepository
) : ViewModel() {

    val getUserResponse: MutableLiveData<Response<UserData>> = MutableLiveData()

    val operationErrorLiveDate: MutableLiveData<EdvoraOperationError> = MutableLiveData()


    fun getUserResponse(@Url userUrl: String) {
        viewModelScope.launch {
            try {
                val response = repository.getUserData(userUrl)
                if (response.isSuccessful) {
                    getUserResponse.value = response
                } else {
                    operationErrorLiveDate.value = repository.responseError(
                        response.message(),
                        response.errorBody()?.string() ?: "",
                        response.code()
                    )
                }

            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> operationErrorLiveDate.value = repository.processingError()
                    is HttpException -> {
                        operationErrorLiveDate.value =
                            repository.connectionError(throwable.message())
                    }
                    else -> {
                        operationErrorLiveDate.value = repository.processingError()
                    }
                }

            }
        }
    }


}