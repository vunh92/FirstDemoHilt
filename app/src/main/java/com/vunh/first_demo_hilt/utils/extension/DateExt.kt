package com.vunh.first_demo_hilt.utils.extension

import android.annotation.SuppressLint
import com.vunh.first_demo_hilt.utils.ConstantsDateType
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@SuppressLint("SimpleDateFormat")
fun Date?.toStringDate(format: String? = ConstantsDateType.DATE): String? {
    return try {
        val simpleDateFormat = SimpleDateFormat(format)
        this?.let { simpleDateFormat.format(it) }
    }catch (ex: Exception) {
        null
    }
}

@SuppressLint("SimpleDateFormat")
fun Date?.parseSimpleDate(format: String? = ConstantsDateType.DATE): Date? {
    return try {
        val simpleDateFormat = SimpleDateFormat(format)
        this?.let { simpleDateFormat.format(it) }?.parseDate(format)
    }catch (ex: Exception) {
        null
    }
}

@SuppressLint("SimpleDateFormat")
fun String.parseDate(format: String? = ConstantsDateType.DATE): Date? {
    return try {
        val simpleDateFormat = SimpleDateFormat(format)
        simpleDateFormat.parse(this)
    }catch (ex: Exception) {
        null
    }
}

fun getCalendarDate(date: Date? = null): Calendar {
    val cal = Calendar.getInstance()
    if (date != null) {
        cal.time = date
    }
    return cal
}

fun getToday(): Date {
    val calendar = Calendar.getInstance()
    calendar[Calendar.HOUR_OF_DAY] = 0
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.SECOND] = 0
    calendar[Calendar.MILLISECOND] = 0
    return Date()
}

fun getDate(year: Int, month: Int, day: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, day, 0, 0, 0)
    return calendar.time
}

fun getDateLater(date: Date? = null, day: Int): Date {
    val calendar = Calendar.getInstance()
    if (date != null) {
        calendar.time = date
    }
    calendar.add(Calendar.DATE, day)
    calendar[Calendar.HOUR_OF_DAY] = 0
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.SECOND] = 0
    calendar[Calendar.MILLISECOND] = 0
    return calendar.time
}

fun calcDayBetweenDate(startDate: Date, endDate: Date): Int {
    val days = TimeUnit.MILLISECONDS.toDays(endDate.time.minus(startDate.time)).toInt()
    return days
}
