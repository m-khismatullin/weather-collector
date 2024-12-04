package ru.km.weather.collector.entity

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import jakarta.persistence.*
import ru.km.weather.collector.dto.ForecastDto
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

@Entity
class Forecast() : PanacheEntity() {
    companion object : PanacheCompanion<Forecast>

    lateinit var receiveDate: ZonedDateTime

    @ManyToOne(cascade = [CascadeType.ALL])
    lateinit var city: City

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    lateinit var data: MutableList<WeatherDatum>

    constructor(forecastDto: ForecastDto) : this() {
        forecastDto.let { dto ->
            val zoneOffset = ZoneOffset.ofTotalSeconds(dto.city.timezone)
            this.receiveDate = ZonedDateTime.ofInstant(
                Instant.now(),
                ZoneId.ofOffset("", zoneOffset)
            )
            this.city = City(dto.city)
            this.data = dto.list.map { WeatherDatum(it, zoneOffset) }.toMutableList()
        }
    }

    override fun toString(): String {
        return "Forecast(id=$id, receiveDate=$receiveDate, city=$city, data=$data)"
    }

    fun print() {
        println(
            "id=$id receiveDate=${
                with(this.receiveDate) {
                    "${this.toLocalDate()} ${this.hour}:${this.minute}"
                }
            }\n${this.city.name}"
        )
        this.data
            .map {
                "\t${it.weatherDate.toLocalDate()} ${
                    it.weatherDate.hour.toString().padStart(2, '0')
                } : ${it.temperature} : ${it.description}"
            }
            .forEach { println(it) }
    }
}