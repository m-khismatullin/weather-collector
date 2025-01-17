package ru.km.weather.collector.service

import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.logging.Log
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.vertx.core.Vertx
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import kotlinx.coroutines.delay
import ru.km.weather.collector.dto.SubscriberDto
import ru.km.weather.collector.entity.Subscriber
import java.util.concurrent.ConcurrentHashMap

@Path("/subscriber")
@ApplicationScoped
class WeatherSubscriberService {
    private var _subscribers = ConcurrentHashMap<Subscriber, Int>()

    val subscribers: List<Subscriber>
        get() = _subscribers.keys().toList()

    @Inject
    private lateinit var vertx: Vertx

    suspend fun getSubscribersFromDb() {
        Log.info("suspend getSubscriberFromDb()")
        Panache
            .withTransaction { Subscriber.findAll().list() }
            .awaitSuspending()
            .forEach {
                _subscribers.putIfAbsent(it, 1)
                Log.info("subscriber from db: $it")
            };
    }

    @POST
    suspend fun addSubscribe(subscriberDto: SubscriberDto): String {
        // для имитации задержки
        delay(Math.round(Math.random() * 5000))

        Log.info("request from subscriber: ${subscriberDto.name}: lat=${subscriberDto.latitude} lon=${subscriberDto.longitude}")
        return subscribers
            .filter {
                it.lat.toDouble() == subscriberDto.latitude && it.lon.toDouble() == subscriberDto.longitude
            }
            .map { "${it.desc} is already subscribed" }
            .ifEmpty {
                val subscriber = Subscriber(subscriberDto)
                _subscribers.putIfAbsent(subscriber, 1)

                Panache
                    .withTransaction { subscriber.persist<Subscriber>() }
                    .awaitSuspending()
                Log.info("saved $subscriber")

                listOf("${subscriberDto.name} is successfully subscribed")
            }
            .first()
    }
}