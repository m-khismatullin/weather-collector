package ru.km.weather.collector.entity

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import jakarta.persistence.*
import ru.km.weather.collector.dto.CurrentDto
import ru.km.weather.collector.dto.ListDto
import ru.km.weather.collector.util.Util
import java.time.ZonedDateTime

@Entity
class Weather() : PanacheEntity() {
    companion object : PanacheCompanion<Weather>

    @get:Transient
    val weatherDate: ZonedDateTime
        get() = Util.unixTimeToZoned(data.unixTime, city.timezone)

    @ManyToOne(cascade = [CascadeType.ALL])
    lateinit var city: City

    @Embedded
    lateinit var data: Data

    constructor(currentDto: CurrentDto) : this() {
        currentDto.let { dto ->
            this.city = City().apply {
                this.id = dto.id
                this.name = dto.name
                this.country = dto.sys.country
                this.latitude = dto.coord.lat
                this.longitude = dto.coord.lon
                this.timezone = dto.timezone
            }

            this.data = Data().apply {
                this.unixTime = dto.dt
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

    constructor(city: City, listDto: ListDto) : this() {
        this.city = city
        this.data = Data(listDto)
    }

    fun print() {
        println(
            "id=$id : receiveDate=${
                with(this.weatherDate) {
                    "${this.toLocalDate()} ${this.hour}:${this.minute}"
                }
            } : ${this.city.name} : ${
                with(this.data) {
                    "${weatherDate.toLocalDate()} ${
                        weatherDate.hour.toString().padStart(2, '0')
                    } : $temperature : $description"
                }
            }")
    }

    override fun toString(): String {
        return "CurrentWeather(id=$id, weatherDate=$weatherDate, city=$city, data=$data)"
    }
}