package ru.km.weather

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import org.jboss.resteasy.reactive.RestQuery
import ru.km.weather.dto.ForecastDto

@RegisterRestClient(configKey = "weather-api")
interface WeatherClient {
    @GET
    @Path("/forecast")
    fun getData(
        @RestQuery("appid") apiKey: String,
        @RestQuery("lat") latitude: String,
        @RestQuery("lon") longitude: String,
        @RestQuery("units") measureUnits: String,
        @RestQuery("lang") language: String,
    ): ForecastDto
}