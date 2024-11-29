package ru.km.weather.entity

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import jakarta.persistence.Entity
import java.time.ZonedDateTime

@Entity
class ForecastDatum : PanacheEntity() {
    companion object : PanacheCompanion<ForecastDatum>

    lateinit var weatherDate: ZonedDateTime
    var temperature: Double = 0.0
    var feelsLike: Double = 0.0
    var pressure: Double = 0.0
    var humidity: Double = 0.0
}
