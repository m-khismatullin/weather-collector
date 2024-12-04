package ru.km.weather.collector.entity

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntityBase
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import ru.km.weather.collector.dto.CityDto

@Entity
class City() : PanacheEntityBase {
    companion object : PanacheCompanion<City>

    @Id
    var id: Long = 0L
    lateinit var name: String
    lateinit var country: String
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    constructor(cityDto: CityDto) : this() {
        cityDto.let { dto ->
            this.id = dto.id
            this.name = dto.name
            this.country = dto.country
            this.latitude = dto.coord.lat
            this.longitude = dto.coord.lon
        }
    }

    override fun toString(): String {
        return "City(id=$id, name='$name', country='$country', latitude=$latitude, longitude=$longitude)"
    }
}
