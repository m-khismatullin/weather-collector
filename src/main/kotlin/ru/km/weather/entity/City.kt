package ru.km.weather.entity

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import jakarta.persistence.Entity
import ru.km.weather.dto.CityDto

@Entity
class City() : PanacheEntity() {
    companion object : PanacheCompanion<City>

    var extId: Long = 0
    lateinit var name: String
    lateinit var country: String
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    constructor(cityDto: CityDto) : this() {
        cityDto.let { dto ->
            this.extId = dto.id
            this.name = dto.name
            this.country = dto.country
            this.latitude = dto.coord.lat
            this.longitude = dto.coord.lon
        }
    }

    override fun toString(): String {
        return "City(id=$id, extId=${extId}, name='$name', country='$country', latitude=$latitude, longitude=$longitude)"
    }
}
