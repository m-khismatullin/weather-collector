package ru.km.weather.service

import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.quarkus.vertx.VertxContextSupport
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import ru.km.weather.client.ForecastClientHelper
import ru.km.weather.dto.ForecastDto
import ru.km.weather.entity.Forecast

@ApplicationScoped
class ForecastService {
    @Inject
    private lateinit var forecastClientHelper: ForecastClientHelper

//    @WithSession
//    @WithTransaction
    fun getForecast(latitude: String, longitude: String): Uni<Forecast> {
        return forecastClientHelper
            .getForecastForPosition(latitude, longitude)
            .onItem()
            .transform { Forecast(it) }
//            .invoke { forecast ->
//                VertxContextSupport.subscribeAndAwait { forecast ->
//                    Panache.withTransaction { forecast.persist<Forecast>() }
//                }
//            }
//            .transform { Forecast(it).persist<Forecast>() }
//            .flatMap { it }
//            .call { forecast ->
//                println(forecast)
//                Uni.createFrom(forecast).
//            }

    }
}