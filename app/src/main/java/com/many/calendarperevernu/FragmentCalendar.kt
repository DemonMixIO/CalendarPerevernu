package com.many.calendarperevernu

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.many.calendarperevernu.MainActivity.Companion.calendar
import com.many.calendarperevernu.databinding.FragmentCalendarBinding
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

@RequiresApi(Build.VERSION_CODES.O)
class FragmentCalendar(year: Int, month: Int) : Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    var headerNames = arrayOf(
        "Пн",
        "Вт",
        "Ср",
        "Чт",
        "Пт",
        "Сб",
        "Вс"
    )

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    lateinit var intant: MainActivity
    lateinit var date: LocalDate
    var yearAlready = 0
    var monthAlready = 0
    lateinit var nowCard: CardView
    var listCardView = emptyArray<CardView>()
    var listSelected = emptyArray<Int>()

    init {
        yearAlready = year
        monthAlready = month
        date = LocalDate.of(yearAlready, monthAlready, 1)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()


    }

    private fun generateTextView(text: String): TextView {
        val textView = TextView(requireActivity().applicationContext)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        textView.layoutParams = params
        textView.gravity = Gravity.CENTER
        textView.text = text
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
        return textView
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createCalendar() {
        binding.containerDays.removeAllViews()
        listCardView = emptyArray<CardView>()
        var emptyDays = calendar.setDayWith(date.year, date.monthValue)
        for (j in 1..emptyDays.first) {
            var card = CardView(requireActivity().applicationContext)
            binding.containerDays.addView(card)
        }
        for (i in 1..emptyDays.second) {
//            var card = DayCardView(this)
//            card.setDay(i)
            val cardView = CardView(requireActivity().applicationContext)
            if ((LocalDate.now().year != date.year || LocalDate.now().month != date.month) && i == 1) {
                nowCard = cardView
                cardView.setCardBackgroundColor(Color.RED)
            }else{
                cardView.setCardBackgroundColor(Color.YELLOW)
            }
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
            layoutParams.gravity = Gravity.CENTER
            // Set the card view layout params
            cardView.layoutParams = layoutParams
            // Set the card view corner radius
            cardView.radius = 16F

            // Set the card view content padding
            cardView.setContentPadding(25, 25, 25, 25)

            // Set the card view background color

            // Set card view elevation
            cardView.cardElevation = 8F

            // Set card view maximum elevation
            cardView.maxCardElevation = 12F
            cardView.addView(generateTextView(i.toString()));
            listCardView += cardView
            binding.containerDays.addView(cardView)
            if (LocalDate.now().year == date.year && LocalDate.now().month == date.month && LocalDate.now().dayOfMonth == i) {
                cardView.setCardBackgroundColor(Color.RED)
                nowCard = cardView;
                (activity as AppCompatActivity).supportActionBar?.title =
                    i.toString() + " " + date.month.toString() + " " + date.year
            }
            cardView.setOnClickListener {
                var text = cardView.getChildAt(0) as TextView
                (activity as AppCompatActivity).supportActionBar?.title =
                    text.text.toString() + " " + date.month.toString() + " " + date.year
                nowCard.setCardBackgroundColor(Color.YELLOW)
                nowCard = cardView;
                nowCard.setCardBackgroundColor(Color.RED)
                (activity as MainActivity?)!!.setDate(LocalDate.of(date.year, date.month, text.text.toString().toInt()))
                selectDays()

                (activity as MainActivity?)!!.runLastEvents(LocalDate.of(date.year, date.month, text.text.toString().toInt()))
            }
        }
        selectDays()
//        (ContextUtils.getActivity(requireActivity().applicationContext) as AppCompatActivity).supportActionBar!!.title =
//            MainActivity.date.month.toString() + " " + MainActivity.date.year
    }
    private fun selectDays(){
        listSelected = emptyArray()
        var selectedDate = MainActivity.startDate.toEpochDay() - 1;
        for (i in listCardView){
            i.setCardBackgroundColor(Color.YELLOW)
        }
        var count = -1
        var i = 0
        while (selectedDate < MainActivity.endDate.toEpochDay()){
            count += 1
            selectedDate += 1
            var day = LocalDate.ofEpochDay(selectedDate)
            Log.d("MY", "Echo ${listCardView.size} ${LocalDate.ofEpochDay(selectedDate)} " + selectedDate + " " + MainActivity.endDate.toEpochDay())

            if (day.year == date.year && day.month == date.month){
                i++
                listSelected += i
                var c = listCardView[day.dayOfMonth - 1]
                c.setCardBackgroundColor(Color.RED)
            }

        }
//        if (count  0 || MainActivity.startDate.toEpochDay() == MainActivity.endDate.toEpochDay()){
        if (count != 0 && !MainActivity.firstDate){
            listSelected += MainActivity.startDate.dayOfMonth - 1
            (listCardView[MainActivity.startDate.dayOfMonth - 1] as CardView).setCardBackgroundColor(Color.RED)
        }
        (activity as MainActivity?)!!.runSelectedTemp(date, listSelected)
    }
    override fun onResume() {
        super.onResume()
        createCalendar()
        Log.d("MY", "onResume")
    }

    fun createHeader(){
        for (element in headerNames){
            var text = TextView(requireActivity().applicationContext)
            text.text = element

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.gravity = Gravity.CENTER
            text.layoutParams = params
            text.gravity = Gravity.CENTER
            text.foregroundGravity = Gravity.CENTER
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            text.setTypeface(text.typeface, Typeface.BOLD)
            Log.d("MY", text.gravity.toString())
            binding.containerDays.addView(text)
        }
    }
}