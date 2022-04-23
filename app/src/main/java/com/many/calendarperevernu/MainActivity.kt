package com.many.calendarperevernu

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.internal.ContextUtils.getActivity
import com.many.calendarperevernu.databinding.ActivityMainBinding
import com.many.calendarperevernu.elems.DayCardView
import com.many.calendarperevernu.mycalendar.MyCalendur
import java.time.LocalDate
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var calendar: MyCalendur
    private lateinit var date: LocalDate
    private lateinit var nowCard: CardView

    @SuppressLint("ResourceAsColor", "RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        date = LocalDate.now()
        binding = ActivityMainBinding.inflate(layoutInflater);
        calendar = MyCalendur()
        createCalendar()
//        Log.d("MY", calendar.getMonthDays(2022, 4).toString())


    }
    private fun generateTextView(text: String): TextView {
        val textView = TextView(this)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        textView.layoutParams = params
        textView.gravity = Gravity.CENTER
        textView.text = text
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
        return textView
    }
    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createCalendar(){
        var emptyDays = calendar.setDayWith(date.year, date.monthValue)
        for (j in 1..emptyDays.first) {
            var card = CardView(this)
            findViewById<GridLayout>(R.id.containerDays).addView(card)
        }
        for (i in 1..emptyDays.second) {
//            var card = DayCardView(this)
//            card.setDay(i)
            val cardView = CardView(this)

            // Initialize a new LayoutParams instance, CardView width and height
            val layoutParams = LinearLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.dayButtonWight), // CardView width
                resources.getDimensionPixelSize(R.dimen.dayButtonWight)// CardView height
            )

            // Set margins for card view
            layoutParams.setMargins(
                resources.getDimensionPixelSize(R.dimen.dayMargin),
                resources.getDimensionPixelSize(R.dimen.dayMargin),
                resources.getDimensionPixelSize(R.dimen.dayMargin),
                resources.getDimensionPixelSize(R.dimen.dayMargin)
            )

            // Set the card view layout params
            cardView.layoutParams = layoutParams

            // Set the card view corner radius
            cardView.radius = 16F

            // Set the card view content padding
            cardView.setContentPadding(25, 25, 25, 25)

            // Set the card view background color
            cardView.setCardBackgroundColor(Color.YELLOW)

            // Set card view elevation
            cardView.cardElevation = 8F

            // Set card view maximum elevation
            cardView.maxCardElevation = 12F
            cardView.addView(generateTextView(i.toString()));
            findViewById<GridLayout>(R.id.containerDays).addView(cardView)
            if (LocalDate.now().year == date.year && LocalDate.now().month == date.month && LocalDate.now().dayOfMonth == i) {
                cardView.setCardBackgroundColor(Color.RED)
                nowCard = cardView;
            }
            cardView.setOnClickListener {
                var text = cardView.getChildAt(0) as TextView
                (getActivity(this) as AppCompatActivity).supportActionBar!!.title = text.text.toString() + " " + date.month.toString() + " " + date.year
                nowCard.setCardBackgroundColor(Color.YELLOW)
                nowCard = cardView;
                nowCard.setCardBackgroundColor(Color.RED)
            }
        }
        Log.d("MY", binding.containerDays.childCount.toString())

        (getActivity(this) as AppCompatActivity).supportActionBar!!.title =
            date.month.toString() + " " + date.year
    }
}