package com.mousom.edvora.utils

import com.mousom.edvora.data.model.StationIdMaping

internal object FindNearestRides {

    fun findClosestRide(ride: List<Int>, targetStation: Int, id: Int): List<StationIdMaping> {
        val n = ride.size


        if (targetStation <= ride[0]) return listOf(StationIdMaping(id, ride[0]))
        if (targetStation >= ride[n - 1]) return listOf(StationIdMaping(id, ride[n - 1]))


        var i = 0
        var j = n
        var mid = 0
        while (i < j) {
            mid = (i + j) / 2
            if (ride[mid] == targetStation) return listOf(StationIdMaping(id, ride[mid]))

            if (targetStation < ride[mid]) {


                if (mid > 0 && targetStation > ride[mid - 1]) return listOf(
                    StationIdMaping(
                        id, getClosest(
                            ride[mid - 1],
                            ride[mid], targetStation
                        )
                    )
                )

                j = mid
            } else {
                if (mid < n - 1 && targetStation < ride[mid + 1]) return listOf(
                    StationIdMaping(
                        id, getClosest(
                            ride[mid],
                            ride[mid + 1], targetStation
                        )
                    )
                )
                i = mid + 1 // update i
            }
        }

        return listOf(StationIdMaping(id, ride[mid]))
    }

    fun getClosest(
        val1: Int, val2: Int,
        targetStation: Int
    ): Int {
        return if (targetStation - val1 >= val2 - targetStation) val2 else val1
    }
}