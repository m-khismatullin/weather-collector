package ru.km.weather.collector.service

import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.logging.Log
import io.quarkus.vertx.core.runtime.context.VertxContextSafetyToggle
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.infrastructure.Infrastructure
import io.vertx.core.Vertx
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
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

    @Inject
    private lateinit var vertx: Vertx

    fun getForecast(latitude: String, longitude: String): Uni<Forecast> {
        Infrastructure.setDroppedExceptionHandler { err -> Log.error("getForecast error: ${err.cause}") }
        return openWeatherMapClientHelper
            .getForecastForPosition(latitude, longitude)
            .onItem()
            .transform { Forecast(it) }
            .call { forecast ->
                VertxContextSafetyToggle.setContextSafe(vertx.getOrCreateContext(), true)
                Panache.withTransaction {
                    City.findById(forecast.city.id)
                        .onItem()
                        .ifNotNull()
                        .transform { city ->
                            city?.let {
                                forecast.city = city
                                forecast.dataList.forEach { it.city = city }
                                forecast
                            }
                        }
                        .onItem()
                        .ifNotNull()
                        .call { it -> it.persist<Forecast>() }
                        .onItem()
                        .ifNull()
                        .switchTo { forecast.persist<Forecast>() }
                }
            }
            .onFailure()
            .invoke { _ -> Log.error("forecast don't send to kafka!") }
            .onItem()
            .invoke { forecast -> forecastEmitter.send(forecast) }
    }

    fun getWeather(latitude: String, longitude: String): Uni<Weather> {
        Infrastructure.setDroppedExceptionHandler { err -> Log.error("getWeather error: ${err.cause}") }
        return openWeatherMapClientHelper
            .getCurrentForPosition(latitude, longitude)
            .onItem()
            .transform { Weather(it) }
            .call { weather ->
                VertxContextSafetyToggle.setContextSafe(vertx.getOrCreateContext(), true)
                Panache.withTransaction {
                    City.findById(weather.city.id)
                        .onItem()
                        .ifNotNull()
                        .transform { city ->
                            city?.let {
                                weather.city = city
                                weather
                            }
                        }
                        .onItem()
                        .ifNotNull()
                        .call { it -> it.persist<Weather>() }
                        .onItem()
                        .ifNull()
                        .switchTo { weather.persist<Weather>() }
                }
            }
            .onFailure()
            .invoke { _ -> Log.error("weather don't send to kafka!") }
            .onItem()
            .invoke { currentWeather -> weatherEmitter.send(currentWeather) }
    }
}