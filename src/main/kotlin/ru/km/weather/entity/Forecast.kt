package ru.km.weather.entity

import ru.km.weather.dto.ForecastDto
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

//@Entity
//class Forecast() : PanacheEntity() {
class Forecast() {
//    companion object : PanacheCompanion<Forecast>

    lateinit var receiveDate: ZonedDateTime

    //    @ManyToOne
    lateinit var city: City

    //    @OneToMany
    lateinit var data: MutableList<ForecastDatum>

    constructor(forecastDto: ForecastDto) : this() {
        forecastDto.let { dto ->
            val zoneOffset = ZoneOffset.ofTotalSeconds(dto.city.timezone)
            this.receiveDate = ZonedDateTime.ofInstant(
                Instant.now(),
                ZoneId.ofOffset("", zoneOffset)
            )
            this.city = City(dto.city)
            this.data = dto.list.map { ForecastDatum(it, zoneOffset) }.toMutableList()
        }
    }

    override fun toString(): String {
        return "Forecast(receiveDate=$receiveDate, city=$city, data=$data)"
//        return "Forecast(id=$id, receiveDate=$receiveDate, city=$city, data=$data)"
    }
}