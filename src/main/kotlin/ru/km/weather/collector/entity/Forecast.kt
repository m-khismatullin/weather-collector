package ru.km.weather.collector.entity

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import jakarta.persistence.*
import ru.km.weather.collector.dto.ForecastDto
import ru.km.weather.collector.util.Util
import java.time.Instant
import java.time.ZonedDateTime

@Entity
class Forecast() : PanacheEntity() {
    companion object : PanacheCompanion<Forecast>

    var unixTime: Long = 0

    @get:Transient
    val forecastDate: ZonedDateTime
        get() = Util.unixTimeToZoned(unixTime, city.timezone)

    @ManyToOne(cascade = [], fetch = FetchType.EAGER)
    @JoinColumn(updatable = false)
    lateinit var city: City

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    lateinit var dataList: MutableList<Weather>

    constructor(forecastDto: ForecastDto) : this() {
        forecastDto.let { dto ->
            this.unixTime = Instant.now().epochSecond
            this.city = City(dto.city)
            this.dataList = dto.list.map { Weather(city, it) }.toMutableList()
        }
    }

    override fun toString(): String {
        return "Forecast(id=$id, receiveDate=$forecastDate, city=$city, dataList=$dataList)"
    }

    fun print() {
        println(
            "id=$id receiveDate=${
                with(this.forecastDate) {
                    "${this.toLocalDate()} ${this.hour}:${this.minute}"
                }
            }\n${this.city.name}"
        )
        this.dataList
            .map {
                val weatherDate = Util.unixTimeToZoned(it.data.unixTime, this.city.timezone)
                "\t${weatherDate.toLocalDate()} ${
                    weatherDate.hour.toString().padStart(2, '0')
                } : ${it.data.temperature} : ${it.data.description}"
            }
            .forEach { println(it) }
    }
}