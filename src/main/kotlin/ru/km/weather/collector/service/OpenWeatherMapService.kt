package ru.km.weather.collector.service

import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.logging.Log
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.LockModeType
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import ru.km.weather.collector.entity.City
import ru.km.weather.collector.entity.Forecast
import ru.km.weather.collector.entity.Weather
import ru.km.weather.collector.rest.OpenWeatherMapClientHelper


@ApplicationScoped
class OpenWeatherMapService {
    @Inject
    private lateinit var openWeatherMapClientHelper: OpenWeatherMapClientHelper

    @Inject
    @Channel("forecast")
    private lateinit var forecastEmitter: Emitter<Forecast>

    @Inject
    @Channel("weather")
    private lateinit var weatherEmitter: Emitter<Weather>

    suspend fun getForecast(latitude: String, longitude: String): Forecast {
        return openWeatherMapClientHelper
            .getForecastForPosition(latitude, longitude)
            .let { Forecast(it) }
            .let { forecast ->
                Panache.withTransaction {
                    City.findById(forecast.city.id, LockModeType.PESSIMISTIC_READ)
                        .onItem()
                        .ifNull()
                        .switchTo { forecast.city.persist<City>() }
                        .onFailure()
                        .invoke { e -> Log.error(e.cause) }
                        .onItem()
                        .ifNotNull()
                        .transform { city ->
                            forecast.city = city
                            forecast
                        }
                        .onItem()
                        .ifNotNull()
                        .call { forecast -> forecast.persist<Forecast>() }
                        .onFailure()
                        .invoke { e -> Log.error(e.cause) }
                        .onItem()
                        .invoke { forecast -> forecastEmitter.send(forecast) }
                }.awaitSuspending()
            }
    }

    suspend fun getWeather(latitude: String, longitude: String): Weather {
        return openWeatherMapClientHelper
            .getCurrentForPosition(latitude, longitude)
            .let { Weather(it) }
            .let { weather ->
                Panache.withTransaction {
                    City.findById(weather.city.id, LockModeType.PESSIMISTIC_READ)
                        .onItem()
                        .ifNull()
                        .switchTo { weather.city.persist<City>() }
                        .onFailure()
                        .invoke { e -> Log.error(e.cause) }
                        .onItem()
                        .ifNotNull()
                        .transform { city ->
                            weather.city = city
                            weather
                        }
                        .onItem()
                        .call { weather -> weather.persist<Weather>() }
                        .onFailure()
                        .invoke { e -> Log.error(e.cause) }
                        .onItem()
                        .invoke { weather -> weatherEmitter.send(weather) }
                }.awaitSuspending()
            }
    }
}