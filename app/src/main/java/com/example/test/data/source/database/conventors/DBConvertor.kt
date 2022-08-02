package com.example.test.data.source.database.conventors

import androidx.room.TypeConverter
import java.util.*

class DBConvertor {
    class PrimitiveTypeConvertor {
        @TypeConverter
        fun booleanToInt(param: Boolean): Int = if (param) 1 else 0

        @TypeConverter
        fun intToBoolean(param: Int): Boolean = param == 1

        @TypeConverter
        fun toDate(dateLong: Long?): Date? {
            return dateLong?.let { Date(it) }
        }

        @TypeConverter
        fun fromDate(date: Date?): Long? {
            return date?.time
        }
    }
}