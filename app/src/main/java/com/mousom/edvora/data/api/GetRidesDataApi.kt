package com.mousom.edvora.data.api

import com.mousom.edvora.data.model.RideData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface GetRidesDataApi {

    @GET
    suspend fun getRidesData(@Url url: String): Response<RideData>
}