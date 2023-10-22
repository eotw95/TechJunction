package com.example.techjunction.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DateTimeException
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date

class DateConverter {
    companion object {
        /**
         * StringをDateに変換する際にException発生した場合は、現在の日時を返却する
         */
        @RequiresApi(Build.VERSION_CODES.O)
        fun asDate(strDate: String): Date {
            try {
                // '2011-12-03T10:15:30Z'
                val dateTimeFormatter = DateTimeFormatter.ISO_INSTANT.parse(strDate)
                val instant = Instant.from(dateTimeFormatter)
                return Date.from(instant)
            } catch (e: DateTimeParseException) {
                // do nothing
            }

            try {
                // 'Sun, 24 Sep 2023 12:24:48 GMT'
                val dateTimeFormatter = DateTimeFormatter.RFC_1123_DATE_TIME.parse(strDate)
                val instant = Instant.from(dateTimeFormatter)
                return Date.from(instant)
            } catch (e: DateTimeParseException) {
                // do nothing
            }

            return Date()
        }
    }
}