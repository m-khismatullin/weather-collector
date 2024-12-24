package ru.km.weather.collector

import io.quarkus.logging.Log
import io.quarkus.scheduler.Scheduled
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import ru.km.weather.collector.service.OpenWeatherMapService
import ru.km.weather.collector.util.WeatherConfig
import java.time.LocalDateTime

@ApplicationScoped
class Scheduler {
    @Inject
    lateinit var weatherConfig: WeatherConfig

    @Inject
    private lateinit var openWeatherMapService: OpenWeatherMapService

    @Scheduled(every = "1m")
    fun scheduleCurrent() {
        openWeatherMapService
            .getWeather(weatherConfig.city().latitude(), weatherConfig.city().longitude())
            .subscribe()
            .with { Log.info("current weather scheduler run at ${LocalDateTime.now()}") }
    }

    @Scheduled(every = "30m")
    fun scheduleForecast() {
        openWeatherMapService
            .getForecast(weatherConfig.city().latitude(), weatherConfig.city().longitude())
            .subscribe()
            .with { Log.info("forecast scheduler run at ${LocalDateTime.now()}") }
    }
}