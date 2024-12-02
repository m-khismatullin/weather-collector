package ru.km.weather.entity

import ru.km.weather.dto.ListDto
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

//@Entity
//class ForecastDatum() : PanacheEntity() {
class ForecastDatum() {
//    companion object : PanacheCompanion<ForecastDatum>

    lateinit var weatherDate: ZonedDateTime
    var temperature: Double = 0.0
    var feelsLike: Double = 0.0
    var pressure: Double = 0.0
    var humidity: Double = 0.0
    lateinit var description: String

    constructor(listDto: ListDto, zoneOffset: ZoneOffset) : this() {
        listDto.let { dto ->
            this.weatherDate = ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(dto.dt),
                zoneOffset
            )
            this.temperature = dto.main.temp
            this.feelsLike = dto.main.feelsLike
            this.pressure = dto.main.pressure
            this.humidity = dto.main.humidity
            dto.weather.firstOrNull()?.let {
                this.description = it.description
            } ?: "undefined"
        }
    }

    override fun toString(): String {
        return "ForecastDatum(weatherDate=$weatherDate, temperature=$temperature, feelsLike=$feelsLike, pressure=$pressure, humidity=$humidity, description='$description')"
    }
}
