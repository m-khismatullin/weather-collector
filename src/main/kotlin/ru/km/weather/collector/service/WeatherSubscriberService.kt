package ru.km.weather.collector.service

import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.logging.Log
import io.quarkus.vertx.core.runtime.context.VertxContextSafetyToggle
import io.vertx.core.Vertx
import jakarta.inject.Inject
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.km.weather.collector.dto.SubscriberDto
import ru.km.weather.collector.entity.Subscriber

@Path("/subscriber")
class WeatherSubscriberService {
    private var _subscribers = mutableListOf<Subscriber>()

    val subscribers: List<Subscriber>
        get() = _subscribers.toList()

//    @Startup
//    fun onInit() {
//        WeatherSubscriber
//            .findAll()
//            .list()
//            .subscribe()
//            .with {
//                _subscribers = it.toMutableList()
//            }
//    }
//
//    @Shutdown
//    fun onShutdown() {
//        WeatherSubscriber
//            .persist(_subscribers)
//            .await()
//            .indefinitely()
//    }

    @Inject
    private lateinit var vertx: Vertx

    @POST
    suspend fun addSubscribe(subscriberDto: SubscriberDto): String {
        Log.info("request from subscriber: ${subscriberDto.name}: lat=${subscriberDto.latitude} lon=${subscriberDto.longitude}")
        return _subscribers
            .filter {
                it.lat.toDouble() == subscriberDto.latitude && it.lon.toDouble() == subscriberDto.longitude
            }
            .map { "${it.desc} is already subscribed" }
            .ifEmpty {
                val subscriber = Subscriber(subscriberDto)
                _subscribers.add(subscriber)

                coroutineScope {
                    launch {
                        VertxContextSafetyToggle.setContextSafe(vertx.getOrCreateContext(), true)
                        Panache.withTransaction { subscriber.persist<Subscriber>() }.subscribe().with {
                            Log.info("saved $it")
                        }
                    }
                }

                listOf("${subscriberDto.name} is successfully subscribed")
            }
            .first()
    }

}