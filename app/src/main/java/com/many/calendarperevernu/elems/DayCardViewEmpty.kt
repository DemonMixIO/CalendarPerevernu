package com.many.calendarperevernu.elems

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.many.calendarperevernu.R
import com.many.calendarperevernu.databinding.CulendarCardBinding

class DayCardViewEmpty(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
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
        inflater.inflate(R.layout.culendar_card_empty, this, true)
        binding = CulendarCardBinding.bind(this)
    }


}
