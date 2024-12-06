package ru.km.weather.collector.entity

import jakarta.persistence.Embeddable
import ru.km.weather.collector.dto.ListDto

@Embeddable
class Data() {
    var unixTime: Long = 0
    var temperature: Double = 0.0
    var feelsLike: Double = 0.0
    var pressure: Double = 0.0
    var humidity: Double = 0.0
    lateinit var description: String

    constructor(listDto: ListDto) : this() {
        listDto.let { dto ->
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

    override fun toString(): String {
        return "Data(unixTime=$unixTime, temperature=$temperature, feelsLike=$feelsLike, pressure=$pressure, humidity=$humidity, description='$description')"
    }

}
