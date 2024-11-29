package ru.km.weather

import io.quarkus.logging.Log
import io.quarkus.runtime.QuarkusApplication
import io.quarkus.runtime.annotations.QuarkusMain
import jakarta.inject.Inject
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.rest.client.inject.RestClient

@QuarkusMain
class Commander : QuarkusApplication {
    @Inject
    @RestClient
    lateinit var weatherClient: WeatherClient

    @ConfigProperty(name = "weather.api.key")
    lateinit var apiKey: String

    @ConfigProperty(name = "weather.city.latitude")
    lateinit var latitude: String

    @ConfigProperty(name = "weather.city.longitude")
    lateinit var longitude: String

    override fun run(vararg args: String?): Int {
        Log.info("Receiving data from api.openweathermap.org...")
        val data = weatherClient.getData(
            apiKey,
            latitude,
            longitude,
            "metric",
            "ru",
        )
        Log.info(data.city)

        return 1
    }
}