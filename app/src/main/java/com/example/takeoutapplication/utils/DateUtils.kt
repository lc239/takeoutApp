package com.example.takeoutapplication.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateUtils {
    private val formatter1 = DateTimeFormatter.ofPattern("MM-dd/HH:mm:ss").withZone(ZoneId.systemDefault())
    private val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm:ss").withZone(ZoneId.systemDefault())

    fun instantToString1(instant: Instant): String{
        return formatter1.format(instant)
    }

    fun instantToString2(instant: Instant): String{
        return formatter2.format(instant)
    }
}