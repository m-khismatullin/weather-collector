package ru.km.weather.entity

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import jakarta.persistence.Entity

@Entity
class City : PanacheEntity() {
    companion object : PanacheCompanion<City>

    lateinit var name: String
    lateinit var country: String
    var latitude: Double = 0.0
    var longitude: Double = 0.0
}
