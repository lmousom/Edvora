package com.mousom.edvora.data.api

import com.mousom.edvora.data.model.UserData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface GetUserDataApi {
    @GET
    suspend fun getUserData(@Url url: String): Response<UserData>
}