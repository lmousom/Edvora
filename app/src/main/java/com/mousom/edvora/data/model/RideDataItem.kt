package com.mousom.edvora.data.model

data class RideDataItem(
    val city: String,
    val date: String,
    val destination_station_code: Int,
    val id: Int,
    val map_url: String,
    val origin_station_code: Int,
    val state: String,
    val station_path: List<Int>,
    val closestStation: Int,
    val distance: Int
)