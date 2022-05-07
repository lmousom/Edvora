package com.mousom.edvora.data.api

import com.mousom.edvora.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val getRidesDataApi: GetRidesDataApi by lazy {
        retrofit.create(GetRidesDataApi::class.java)
    }

    val getUserDataApi: GetUserDataApi by lazy {
        retrofit.create(GetUserDataApi::class.java)
    }


}