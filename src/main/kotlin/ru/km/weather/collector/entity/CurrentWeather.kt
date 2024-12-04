package ru.km.weather.collector.entity

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import jakarta.persistence.*
import ru.km.weather.collector.dto.CurrentWeatherDto
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

@Entity
class CurrentWeather() : PanacheEntity() {
    companion object : PanacheCompanion<CurrentWeather>

    lateinit var receiveDate: ZonedDateTime

    @ManyToOne(cascade = [CascadeType.ALL])
    lateinit var city: City

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    lateinit var datum: WeatherDatum

    constructor(currentWeatherDto: CurrentWeatherDto) : this() {
        currentWeatherDto.let { dto ->
            val zoneOffset = ZoneOffset.ofTotalSeconds(dto.timezone)
            this.receiveDate = ZonedDateTime.ofInstant(
                Instant.now(),
                ZoneId.ofOffset("", zoneOffset)
            )

            this.city = City().apply {
                this.id = dto.id
                this.name = dto.name
                this.country = dto.sys.country
                this.latitude = dto.coord.lat
                this.longitude = dto.coord.lon
            }

            this.datum = WeatherDatum().apply {
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
    }

    fun print() {
        println(
            "id=$id : receiveDate=${
                with(this.receiveDate) {
                    "${this.toLocalDate()} ${this.hour}:${this.minute}"
                }
            } : ${this.city.name} : ${
                with(this.datum) {
                    "t${weatherDate.toLocalDate()} ${
                        weatherDate.hour.toString().padStart(2, '0')
                    } : ${temperature} : ${description}"
                }
            }")
    }

    override fun toString(): String {
        return "CurrentWeather(id=$id, receiveDate=$receiveDate, city=$city, datum=$datum)"
    }
}