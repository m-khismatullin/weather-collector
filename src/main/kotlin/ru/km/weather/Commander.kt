package ru.km.weather

import io.quarkus.runtime.QuarkusApplication
import io.quarkus.runtime.annotations.QuarkusMain
import jakarta.inject.Inject
import org.eclipse.microprofile.config.inject.ConfigProperty
import ru.km.weather.service.ForecastService

@QuarkusMain
class Commander : QuarkusApplication {
    @ConfigProperty(name = "weather.city.latitude")
    lateinit var latitude: String

    @ConfigProperty(name = "weather.city.longitude")
    lateinit var longitude: String

    @Inject
    private lateinit var forecastService: ForecastService

    override fun run(vararg args: String?): Int {
        val forecast = forecastService.getForecast(latitude, longitude).await().indefinitely()
        println("receiveDate=${with(forecast.receiveDate){
            "${this.toLocalDate()} ${this.hour}:${this.minute}"
        }}\n${forecast.city.name}")
        forecast.data
            .map {
                "\t${it.weatherDate.toLocalDate()} ${it.weatherDate.hour.toString().padStart(2,'0')} : ${it.temperature} : ${it.description}"
            }
            .forEach { println(it) }

        return 1
    }
}