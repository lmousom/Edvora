package com.mousom.edvora.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object ParseDateUtil {

    @RequiresApi(Build.VERSION_CODES.O)
    fun stringToDate(date: String): LocalDate {

        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")

        return LocalDate.parse(date, formatter)
    }

}