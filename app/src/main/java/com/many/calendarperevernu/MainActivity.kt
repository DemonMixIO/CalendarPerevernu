package com.many.calendarperevernu

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.internal.ContextUtils.getActivity
import com.many.calendarperevernu.databinding.ActivityMainBinding
import com.many.calendarperevernu.elems.DayCardView
import com.many.calendarperevernu.elems.DayCardViewEmpty
import com.many.calendarperevernu.mycalendar.MyCalendur
import java.time.LocalDate
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var calendar: MyCalendur
    private lateinit var date: LocalDate;
    @SuppressLint("ResourceAsColor", "RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        date = LocalDate.now()
        binding = ActivityMainBinding.inflate(layoutInflater);
        calendar = MyCalendur()
//        Log.d("MY", calendar.getMonthDays(2022, 4).toString())
        var emptyDays = calendar.setDayWith(date.year, date.monthValue)
        for (j in 1..emptyDays.first){
            var card = DayCardViewEmpty(this)
            findViewById<AutoGridLayout>(R.id.containerDays).addView(card)
        }
        for (i in 1..emptyDays.second){
            var card = DayCardView(this)
            card.setDay(i)
            findViewById<AutoGridLayout>(R.id.containerDays).addView(card)
        }
        Log.d("MY", binding.containerDays.childCount.toString())

        (getActivity(this) as AppCompatActivity).supportActionBar!!.title = date.month.toString() + " " + date.year

    }

}