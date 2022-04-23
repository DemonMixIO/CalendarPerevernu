package com.many.calendarperevernu.elems

import android.annotation.SuppressLint
import android.content.Context
import android.text.Layout
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import com.many.calendarperevernu.R
import com.many.calendarperevernu.databinding.CulendarCardBinding

class DayCardView(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
    FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    private lateinit var binding: CulendarCardBinding

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    init {
        var inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.culendar_card, this, true)
        binding = CulendarCardBinding.bind(this)
        InitAttrs(attrs, defStyleAttr, defStyleRes)
    }

    @SuppressLint("ResourceAsColor")
    private fun InitAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        findViewById<LinearLayout>(R.id.backGround).setBackgroundColor(R.color.yellow)
        if (attrs == null) {
            return
        }
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.DayCardView,
            defStyleAttr,
            defStyleRes
        )

        val dayText = typedArray.getString(R.styleable.DayCardView_dayText)
        binding.textView.setText(dayText ?: "OK")

//        val background = typedArray.getBoolean(R.styleable.DayCardView_emptyCard)
        findViewById<LinearLayout>(R.id.backGround).setBackgroundColor(R.color.yellow)
        typedArray.recycle()
    }
    fun setDay(day: Int){
        binding.textView.setText(day.toString())
    }
    fun getText(): Int{
        return binding.textView.text.toString().toInt()
    }
    @SuppressLint("ResourceAsColor")
    fun setColor(color: Int){
        binding.backGround.setBackgroundColor(R.color.white)
//        Log.d("MY",  )
    }

    @SuppressLint("ResourceAsColor")
    fun setEmpty(){
        binding.cardView.setCardBackgroundColor(R.color.white)
    }



}
