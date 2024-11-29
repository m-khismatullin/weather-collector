package ru.km.weather

import io.quarkus.logging.Log
import io.quarkus.runtime.QuarkusApplication
import io.quarkus.runtime.annotations.QuarkusMain
import jakarta.inject.Inject
import org.eclipse.microprofile.config.inject.ConfigProperty
import ru.km.weather.client.ForecastClientHelper

@QuarkusMain
class Commander : QuarkusApplication {
    @ConfigProperty(name = "weather.city.latitude")
    lateinit var latitude: String

    @ConfigProperty(name = "weather.city.longitude")
    lateinit var longitude: String

    @Inject
    private lateinit var forecastClientHelper: ForecastClientHelper

    override fun run(vararg args: String?): Int {
        val data = forecastClientHelper
            .getForecastForPosition(latitude, longitude)
            .await()
            .indefinitely()
        Log.info("Weather data for latitude=${latitude} longitude=${longitude}: ${data.city}")

        return 1
    }
}