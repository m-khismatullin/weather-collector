package ru.km.weather.entity

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import ru.km.weather.dto.ForecastDto
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

    @OneToMany(cascade = [CascadeType.ALL])
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
        return "Forecast(id=$id, receiveDate=$receiveDate, city=$city, data=$data)"
    }
}