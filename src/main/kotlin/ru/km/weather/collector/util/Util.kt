package ru.km.weather.collector.util

import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

class Util {
    companion object {
        fun unixTimeToZoned(unixTime: Long, timeZone: Int): ZonedDateTime {
            val zoneOffset = ZoneOffset.ofTotalSeconds(timeZone)
            return ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(unixTime),
                ZoneId.ofOffset("", zoneOffset)
            )
        }
    }
}