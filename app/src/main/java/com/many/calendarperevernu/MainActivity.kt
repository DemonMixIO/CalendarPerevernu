package com.many.calendarperevernu

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.many.calendarperevernu.databinding.ActivityMainBinding
import com.many.calendarperevernu.mycalendar.MyCalendur
import java.time.YearMonth

private const val NUM_PAGES = 5

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPager: ViewPager2

    companion object {
        lateinit var calendar: MyCalendur

    }

    @SuppressLint("ResourceAsColor", "RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calendar = MyCalendur()
        binding = ActivityMainBinding.inflate(layoutInflater);

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager)
//        viewPager.setPageTransformer(ZoomOutPageTransformer())

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(this)

        viewPager.adapter = pagerAdapter
        var ym = MyCalendur.difDate(YearMonth.now())
        Log.d("My", "months: " + (ym.year * 12 + ym.monthValue -1))
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

}