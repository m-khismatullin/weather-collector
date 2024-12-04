package ru.km.weather.collector.rest

import io.quarkus.logging.Log
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.rest.client.inject.RestClient
import ru.km.weather.collector.dto.CurrentWeatherDto
import ru.km.weather.collector.dto.ForecastDto

@ApplicationScoped
class OpenWeatherMapClientHelper {
    @ConfigProperty(name = "weather.api.key")
    private lateinit var apiKey: String

    @ConfigProperty(name = "weather.api.units")
    private lateinit var units: String

    @ConfigProperty(name = "weather.api.language")
    private lateinit var language: String

    @RestClient
    private lateinit var openWeatherMapClient: OpenWeatherMapClient;

    fun getForecastForPosition(latitude: String, longitude: String): Uni<ForecastDto> {
        return openWeatherMapClient.getForecast(
            apiKey,
            latitude,
            longitude,
            units,
            language,
        )
    }

    fun getWeatherForPosition(latitude: String, longitude: String): Uni<CurrentWeatherDto> {
        return openWeatherMapClient.getWeather(
            apiKey,
            latitude,
            longitude,
            units,
            language,
        )
    }
}