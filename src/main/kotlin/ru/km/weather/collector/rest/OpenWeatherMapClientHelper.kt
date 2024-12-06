package ru.km.weather.collector.rest

import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.rest.client.inject.RestClient
import ru.km.weather.collector.dto.CurrentDto
import ru.km.weather.collector.dto.ForecastDto
import ru.km.weather.collector.util.WeatherConfig

@ApplicationScoped
class OpenWeatherMapClientHelper {
    @Inject
    private lateinit var weatherConfig: WeatherConfig

    @RestClient
    private lateinit var openWeatherMapClient: OpenWeatherMapClient;

    fun getForecastForPosition(latitude: String, longitude: String): Uni<ForecastDto> {
        return openWeatherMapClient.getForecast(
            weatherConfig.api().key(),
            latitude,
            longitude,
            weatherConfig.api().units(),
            weatherConfig.api().language(),
        )
    }

    fun getCurrentForPosition(latitude: String, longitude: String): Uni<CurrentDto> {
        return openWeatherMapClient.getCurrent(
            weatherConfig.api().key(),
            latitude,
            longitude,
            weatherConfig.api().units(),
            weatherConfig.api().language(),
        )
    }
}