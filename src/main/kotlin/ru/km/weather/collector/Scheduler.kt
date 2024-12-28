package ru.km.weather.collector

import io.quarkus.logging.Log
import io.quarkus.scheduler.Scheduled
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    //    @Scheduled(every = "1m")
    @Scheduled(every = "15s")
    suspend fun scheduleCurrent() {
        val uniList = weatherSubscriberService
            .subscribers
            .map { subscriber ->
                openWeatherMapService.getWeather(subscriber.lat, subscriber.lon)
            }
            .toList()

        withContext(Dispatchers.IO) {
            uniList.forEach {
                it.subscribe().with { weather ->
                    Log.info("current weather for ${weather.city} run at ${LocalDateTime.now()}")
                }
            }
        }
//        // так почему-то не работает!!!
//        withContext(Dispatchers.IO) {
//            weatherSubscriberService
//                .subscribers
//                .map { openWeatherMapService.getWeather(it.lat, it.lon) }
//                .forEach {
//                    it.subscribe().with { w ->
//                        Log.info("current weather for ${w.city} run at ${LocalDateTime.now()}")
//                    }
//                }
//        }
    }

    //    @Scheduled(every = "30m")
//    @Scheduled(every = "1m")
    suspend fun scheduleForecast() {
        val uniList = weatherSubscriberService
            .subscribers
            .map { subscriber ->
                openWeatherMapService.getForecast(subscriber.lat, subscriber.lon)
            }
            .toList()

        withContext(Dispatchers.IO) {
            uniList.forEach {
                launch {
                    it.subscribe().with {
                        Log.info("forecast scheduler for ${it.city} run at ${LocalDateTime.now()}")
                    }
                }
            }
        }
    }
}