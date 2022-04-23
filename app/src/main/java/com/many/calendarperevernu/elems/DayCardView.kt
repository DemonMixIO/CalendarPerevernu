package com.many.calendarperevernu.elems

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
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
        binding.cardView.setCardBackgroundColor(R.color.white)
        typedArray.recycle()
    }
    fun setDay(day: Int){
        binding.textView.setText(day.toString())
    }

    @SuppressLint("ResourceAsColor")
    fun setEmpty(){
        binding.cardView.setCardBackgroundColor(R.color.white)
    }


}
