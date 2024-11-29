package ru.km.weather.client

import io.quarkus.logging.Log
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.rest.client.inject.RestClient
import ru.km.weather.dto.ForecastDto

@ApplicationScoped
class ForecastClientHelper {
    @ConfigProperty(name = "weather.api.key")
    private lateinit var apiKey: String

    @ConfigProperty(name = "weather.api.units")
    private lateinit var units: String

    @ConfigProperty(name = "weather.api.language")
    private lateinit var language: String

    @RestClient
    private lateinit var forecastClient: ForecastClient;

    fun getForecastForPosition(latitude: String, longitude: String): Uni<ForecastDto> {
        Log.info("Receiving data from api.openweathermap.org...")
        return forecastClient.getData(
            apiKey,
            latitude,
            longitude,
            units,
            language,
        )
    }
}