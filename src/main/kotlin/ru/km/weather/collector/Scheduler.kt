package ru.km.weather.collector

import io.quarkus.logging.Log
import io.quarkus.scheduler.Scheduled
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.km.weather.collector.service.OpenWeatherMapService
import ru.km.weather.collector.service.WeatherSubscriberService
import ru.km.weather.collector.util.WeatherConfig
import java.time.LocalDateTime

@ApplicationScoped
class Scheduler {
    @Inject
    lateinit var weatherConfig: WeatherConfig

    @Inject
    private lateinit var openWeatherMapService: OpenWeatherMapService

    @Inject
    private lateinit var weatherSubscriberService: WeatherSubscriberService

    private val mutex = Mutex()

    @Scheduled(every = "1m")
//    @Scheduled(every = "15s")
    suspend fun scheduleCurrent() {
        if (weatherSubscriberService.subscribers.isEmpty())
            weatherSubscriberService.getSubscribersFromDb()

        mutex.withLock {
            weatherSubscriberService
                .subscribers
                .map { sub ->
                    val weather = openWeatherMapService.getWeather(sub.lat, sub.lon)
                    weather
                }
                .forEach { weather ->
                    Log.info("current weather for ${weather.city} run at ${LocalDateTime.now()}")
                }
        }
    }

    @Scheduled(every = "30m")
//    @Scheduled(every = "30s")
    suspend fun scheduleForecast() {
        if (weatherSubscriberService.subscribers.isEmpty())
            weatherSubscriberService.getSubscribersFromDb()

        mutex.withLock {
            weatherSubscriberService
                .subscribers
                .map { sub ->
                    val forecast = openWeatherMapService.getForecast(sub.lat, sub.lon)
                    forecast
                }
                .forEach {
                    Log.info("forecast scheduler for ${it.city} run at ${LocalDateTime.now()}")
                }
        }
    }
}