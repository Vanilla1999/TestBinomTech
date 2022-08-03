package com.example.test.utils

import java.text.SimpleDateFormat
import java.util.*

fun atStartOfDay(date: Date): Date {
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar[Calendar.HOUR_OF_DAY] = 0
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.SECOND] = 0
    calendar[Calendar.MILLISECOND] = 0
    return calendar.time
}
 fun getDateTimeDay(s: Long): String {
    try {
        val sdf = SimpleDateFormat("dd MMM yyy")
        val netDate = Date(s)
        return sdf.format(netDate)
    } catch (e: Exception) {
        return e.toString()
    }
}
fun getHours(s: Long): String {
    try {
        val sdf = SimpleDateFormat("HH:mm")
        val netDate = Date(s)
        return sdf.format(netDate)
    } catch (e: Exception) {
        return e.toString()
    }
}