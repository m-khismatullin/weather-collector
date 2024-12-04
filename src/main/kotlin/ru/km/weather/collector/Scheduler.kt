package ru.km.weather.collector

import io.quarkus.logging.Log
import io.quarkus.scheduler.Scheduled
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.config.inject.ConfigProperty
import ru.km.weather.collector.service.OpenWeatherMapService
import java.time.LocalDateTime

@ApplicationScoped
class Scheduler {
    @ConfigProperty(name = "weather.city.latitude")
    lateinit var latitude: String

    @ConfigProperty(name = "weather.city.longitude")
    lateinit var longitude: String

    @Inject
    private lateinit var openWeatherMapService: OpenWeatherMapService

    @Scheduled(every = "1m")
    fun scheduleCurrentWeather() {
        openWeatherMapService.getWeather(latitude, longitude).await().indefinitely()
        Log.info(LocalDateTime.now())
    }
}