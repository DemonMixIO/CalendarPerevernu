package com.many.calendarperevernu

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.many.calendarperevernu.databinding.ActivityMainBinding
import com.many.calendarperevernu.mycalendar.MyCalendur
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


private const val NUM_PAGES = 5

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var binding: ActivityMainBinding

    companion object {
        lateinit var calendar: MyCalendur
        lateinit var startDate: LocalDate
        lateinit var endDate: LocalDate
        var firstDate = true
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun setDate(date: LocalDate){
        if (firstDate){
            startDate = date
            endDate = date
            firstDate = false
        }else{
            endDate = date
            Log.d("MY", "Echo " + startDate.toEpochDay() + " " + endDate.toEpochDay())

            if (startDate.toEpochDay().toLong() > endDate.toEpochDay().toLong()){
                endDate = startDate
                startDate = date
            }
            firstDate = true
        }
        var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        findViewById<TextView>(R.id.startDate).text = formatter.format(startDate)
        findViewById<TextView>(R.id.endDate).text = formatter.format(endDate)
    }

    @SuppressLint("ResourceAsColor", "RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calendar = MyCalendur()
        startDate = LocalDate.now()
        endDate = LocalDate.now()
        binding = ActivityMainBinding.inflate(layoutInflater);
        viewPager = findViewById(R.id.pager)
//        viewPager.setPageTransformer(ZoomOutPageTransformer())

        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter
        var ym = MyCalendur.difDate(YearMonth.now())

        viewPager.currentItem = ym.year * 12 + ym.monthValue

    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = Int.MAX_VALUE

        @RequiresApi(Build.VERSION_CODES.O)
        override fun createFragment(position: Int): Fragment {
            val pair = MyCalendur.yearMonth(position);

            return FragmentCalendar(pair.first, pair.second)
        }
    }

    fun showDatePickerDialog(v: View) {
        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
    }
}