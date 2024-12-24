package ru.km.weather.collector.entity

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import jakarta.persistence.Entity
import jakarta.persistence.Transient
import ru.km.weather.collector.dto.SubscriberDto

@Entity
class Subscriber() : PanacheEntity() {
    companion object : PanacheCompanion<Subscriber>

    private lateinit var name: String
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    @get:Transient
    val desc: String
        get() = name

    @get:Transient
    val lat: String
        get() = latitude.toString()

    @get:Transient
    val lon: String
        get() = longitude.toString()

    constructor(subscriberDto: SubscriberDto) : this() {
        subscriberDto.let {
            this.name = it.name
            this.latitude = it.latitude
            this.longitude = it.longitude
        }
    }

    override fun toString(): String {
        return "Subscriber(name='$name', latitude=$latitude, longitude=$longitude)"
    }
}