package ru.km.weather.collector.service

import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.vertx.core.runtime.context.VertxContextSafetyToggle
import io.smallrye.mutiny.Uni
import io.vertx.core.Vertx
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import ru.km.weather.collector.entity.City
import ru.km.weather.collector.entity.CurrentWeather
import ru.km.weather.collector.entity.Forecast
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
    private lateinit var currentWeatherEmitter: Emitter<CurrentWeather>

    @Inject
    private lateinit var vertx: Vertx

    fun getForecast(latitude: String, longitude: String): Uni<Forecast> {
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
                                forecast
                            }
                        }
                        .onItem()
                        .ifNotNull()
                        .call { forecast -> forecast.persist<Forecast>() }
                        .onItem()
                        .ifNull()
                        .switchTo {
                            forecast.persist<Forecast>()
                        }
                }
            }
            .invoke { forecast -> forecastEmitter.send(forecast) }
    }

    fun getWeather(latitude: String, longitude: String): Uni<CurrentWeather> {
        return openWeatherMapClientHelper
            .getWeatherForPosition(latitude, longitude)
            .onItem()
            .transform { CurrentWeather(it) }
            .call { currentWeather ->
                VertxContextSafetyToggle.setContextSafe(vertx.getOrCreateContext(), true)
                Panache.withTransaction {
                    City.findById(currentWeather.city.id)
                        .onItem()
                        .ifNotNull()
                        .transform { city ->
                            city?.let {
                                currentWeather.city = city
                                currentWeather
                            }
                        }
                        .onItem()
                        .ifNotNull()
                        .call { currentWeather -> currentWeather.persist<CurrentWeather>() }
                        .onItem()
                        .ifNull()
                        .switchTo {
                            currentWeather.persist<CurrentWeather>()
                        }
                }
            }
            .invoke { currentWeather -> currentWeatherEmitter.send(currentWeather) }
    }
}