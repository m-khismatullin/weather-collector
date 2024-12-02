package ru.km.weather.service

import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.vertx.core.runtime.context.VertxContextSafetyToggle
import io.smallrye.mutiny.Uni
import io.vertx.core.Vertx
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import ru.km.weather.client.ForecastClientHelper
import ru.km.weather.entity.Forecast


@ApplicationScoped
class ForecastService {
    @Inject
    private lateinit var forecastClientHelper: ForecastClientHelper

    @Inject
    private lateinit var vertx: Vertx

    fun getForecast(latitude: String, longitude: String): Uni<Forecast> {
        return forecastClientHelper
            .getForecastForPosition(latitude, longitude)
            .onItem()
            .transform { Forecast(it) }
            .call { forecast ->
                VertxContextSafetyToggle.setContextSafe(vertx.getOrCreateContext(), true)
                Panache.withTransaction {
                    forecast.persist<Forecast>()
                }
            }
    }
}