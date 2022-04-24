package com.many.calendarperevernu.db

import android.provider.BaseColumns

object DBContract {

    /* Inner class that defines the table contents */
    class UserEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "users"
            val COLUMN_DATE_ID = "dateid"
            val COLUMN_DATE = "date"
            val COLUMN_TEMP = "temp"
        }
    }
}