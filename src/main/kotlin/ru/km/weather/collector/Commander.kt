package ru.km.weather.collector

import io.quarkus.runtime.QuarkusApplication
import io.vertx.core.Vertx
import jakarta.inject.Inject
import org.eclipse.microprofile.config.inject.ConfigProperty
import ru.km.weather.collector.service.OpenWeatherMapService

//@QuarkusMain
class Commander : QuarkusApplication {
    @ConfigProperty(name = "weather.city.latitude")
    lateinit var latitude: String

    @ConfigProperty(name = "weather.city.longitude")
    lateinit var longitude: String

    @Inject
    private lateinit var openWeatherMapService: OpenWeatherMapService

    @Inject
    lateinit var vertx: Vertx

    override fun run(vararg args: String?): Int {
        /*val forecast = openWeatherMapService
            .getForecast(latitude, longitude)
            .await()
            .indefinitely()
        forecast.print()

//        println("===================================")
//        VertxContextSupport.subscribeAndAwait {
//            Panache.withTransaction {
//                Forecast.listAll()
//            }
//        }.forEach { println(it) }
//        println("===================================")

        val currentWeather = openWeatherMapService
            .getWeather(latitude, longitude)
            .await()
            .indefinitely()
        currentWeather.print()

//        println("===================================")
//        VertxContextSupport.subscribeAndAwait {
//            Panache.withTransaction {
//                CurrentWeather.listAll()
//            }
//        }.forEach { println(it) }
//        println("===================================")

        println("===================================")
        VertxContextSupport.subscribeAndAwait {
            Panache.withTransaction {
                City.listAll()
            }
        }.forEach { println(it) }
        println("===================================")
*/
        return 1
    }
}