package com.many.calendarperevernu.mycalendar

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
class MyCalendur {
    val WEEKCOUNTS = hashMapOf(
        "MONDAY" to 0,
        "TUESDAY" to 1,
        "WEDNESDAY" to 2,
        "THURSDAY" to 3,
        "FRIDAY" to 4,
        "SATURDAY" to 5,
        "SUNDAY" to 6,
    )
    init {
//        for (i in 18..24) {
//            val date = LocalDate.of(2022, 4, i).dayOfWeek
//            Log.d("MY", date.toString())
//        }
    }

    private fun getMonthDays(year: Int, month: Int): Int {
        val yearMonthObject = YearMonth.of(year, month)
        return yearMonthObject.lengthOfMonth();
    }
    fun setDayWith(year: Int, month: Int): Pair<Int, Int>{
        val date = WEEKCOUNTS[LocalDate.of(year, month, 1).dayOfWeek.toString()]!!
        Log.d("MY", getMonthDays(year, month).toString())
        return Pair(date, getMonthDays(year, month))
    }
}