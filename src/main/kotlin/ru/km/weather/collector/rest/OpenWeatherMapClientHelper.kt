package ru.km.weather.collector.rest

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.rest.client.inject.RestClient
import ru.km.weather.collector.util.WeatherConfig

@ApplicationScoped
class OpenWeatherMapClientHelper {
    @Inject
    private lateinit var weatherConfig: WeatherConfig

    @RestClient
    private lateinit var openWeatherMapClient: OpenWeatherMapClient;

    suspend fun getForecastForPosition(latitude: String, longitude: String) =
        openWeatherMapClient.getForecast(
            weatherConfig.api().key(),
            latitude,
            longitude,
            weatherConfig.api().units(),
            weatherConfig.api().language(),
        )

    suspend fun getCurrentForPosition(latitude: String, longitude: String) =
        openWeatherMapClient.getCurrent(
            weatherConfig.api().key(),
            latitude,
            longitude,
            weatherConfig.api().units(),
            weatherConfig.api().language(),
        )
}