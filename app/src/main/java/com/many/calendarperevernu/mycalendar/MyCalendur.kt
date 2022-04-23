package com.many.calendarperevernu.mycalendar

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
class MyCalendur {
    companion object {
        var year = 1970;
        var month = 1;
        fun getDate(): Pair<Int, Int> {
            return Pair(year, month)
        }

        fun prevMonth(): Pair<Int, Int> {
//            Log.d("MY", "Before $year $month")
            var m = month - 1
            var y = year
            if (m <= 1) {
                m = 12
                y = year - 1
            }
//            Log.d("MY", "After $year $month")
            return Pair(y, m)
        }
        fun difDate(date: YearMonth): YearMonth{
            return YearMonth.now().minusYears(year.toLong()).minusMonths(month.toLong())
        }

        fun yearMonth(current: Int): Pair<Int, Int> {
//            Log.d("MY", "Before $year $month")
//            YearMonth.of(year, month) + Month(current)
            val newym = YearMonth.of(year, month).plusMonths(current.toLong())
            return Pair(newym.year, newym.monthValue)
//            var m = month + current
//            y = (m - (m % 12)) / 12
//            if (m == 0){
//                m = 12
//            }
//            var y = year
//            if (m >= 13) {
//                m = 1
//                y = year+1
//            }
////            Log.d("MY", "After $year $month")
//            return Pair(y, m)
        }

        val WEEKCOUNTS = hashMapOf(
            "MONDAY" to 0,
            "TUESDAY" to 1,
            "WEDNESDAY" to 2,
            "THURSDAY" to 3,
            "FRIDAY" to 4,
            "SATURDAY" to 5,
            "SUNDAY" to 6,
        )
    }

//    init {
//        year = LocalDate.now().year
//        month = LocalDate.now().monthValue
//    }

    private fun getMonthDays(year: Int, month: Int): Int {
        val yearMonthObject = YearMonth.of(year, month)
        return yearMonthObject.lengthOfMonth();
    }

    fun setDayWith(year: Int, month: Int): Pair<Int, Int> {
        val date = WEEKCOUNTS[LocalDate.of(year, month, 1).dayOfWeek.toString()]!!
        return Pair(date, getMonthDays(year, month))
    }


}