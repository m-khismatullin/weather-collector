package ru.km.weather.entity

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.time.ZonedDateTime

@Entity
class Forecast : PanacheEntity() {
    companion object : PanacheCompanion<Forecast>

    lateinit var receiveDate: ZonedDateTime
    @ManyToOne
    lateinit var city: City
    @OneToMany
    lateinit var data: MutableList<ForecastDatum>
}