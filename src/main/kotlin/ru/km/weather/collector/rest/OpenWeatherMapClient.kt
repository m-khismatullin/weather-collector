package ru.km.weather.collector.rest

import io.smallrye.mutiny.Uni
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import org.jboss.resteasy.reactive.RestQuery
import ru.km.weather.collector.dto.CurrentWeatherDto
import ru.km.weather.collector.dto.ForecastDto

@RegisterRestClient(configKey = "weather-api")
interface OpenWeatherMapClient {
    @GET
    @Path("/forecast")
    fun getForecast(
        @RestQuery("appid") apiKey: String,
        @RestQuery("lat") latitude: String,
        @RestQuery("lon") longitude: String,
        @RestQuery("units") measureUnits: String,
        @RestQuery("lang") language: String,
    ): Uni<ForecastDto>

    @GET
    @Path("/weather")
    fun getWeather(
        @RestQuery("appid") apiKey: String,
        @RestQuery("lat") latitude: String,
        @RestQuery("lon") longitude: String,
        @RestQuery("units") measureUnits: String,
        @RestQuery("lang") language: String,
    ): Uni<CurrentWeatherDto>
}