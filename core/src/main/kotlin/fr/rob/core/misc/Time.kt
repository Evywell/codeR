package fr.rob.core.misc

import java.time.Duration
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Date

class Time {

    companion object {
        fun addMinutes(amount: Long, date: Date = Date()): Date = addTime(date, amount, ChronoUnit.MINUTES)

        fun addHours(amount: Long, date: Date = Date()): Date = addTime(date, amount, ChronoUnit.HOURS)

        /**
         * @link https://www.javaprogramto.com/2020/04/java-add-minutes-to-date.html
         */
        private fun addTime(date: Date, amount: Long, unit: ChronoUnit): Date {
            val dateTime = date.toInstant().plus(Duration.of(amount, unit))

            return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant())
        }
    }
}
