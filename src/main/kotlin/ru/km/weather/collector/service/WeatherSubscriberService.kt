package ru.km.weather.collector.service

import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.logging.Log
import io.quarkus.vertx.core.runtime.context.VertxContextSafetyToggle
import io.smallrye.common.annotation.NonBlocking
import io.vertx.core.Vertx
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import ru.km.weather.collector.dto.SubscriberDto
import ru.km.weather.collector.entity.Subscriber
import java.util.concurrent.CopyOnWriteArrayList

@Path("/subscriber")
@ApplicationScoped
class WeatherSubscriberService {
    //    private var _subscribers = mutableListOf<Subscriber>()
    private var _subscribers = CopyOnWriteArrayList<Subscriber>()

    val subscribers: List<Subscriber>
        get() = _subscribers.toList()

    @Inject
    private lateinit var vertx: Vertx

    //    @Startup
    @NonBlocking
    fun getSubscribersFormDb() {
        Log.info("enter getSubscribersFormDb()")
        VertxContextSafetyToggle.setContextSafe(vertx.getOrCreateContext(), true)
        Panache.withTransaction { Subscriber.findAll().list() }
            .subscribe()
            .with {
                Log.info("it.length=${it.size}")
                it.forEach { sub ->
                    _subscribers.add(sub)
                    Log.info("sub form db: $sub")
                }
            }
    }

//    @Shutdown
//    @NonBlocking
//    suspend fun onShutdown() {
//        Log.info("enter onShutdown()")
//        /*
//        VertxContextSafetyToggle.setContextSafe(vertx.getOrCreateContext(), true)
//        Panache
//            .withTransaction { Subscriber.persist(_subscribers) }
////            .await() // как необходимо сохранять при заверешении работы - блокирующе или нет?
////            .indefinitely()
//            .subscribe()
//            .with { Log.info("save on shutdown") }
//         */
//        coroutineScope {
//            Subscriber.persist(_subscribers).await().indefinitely()
//        }
//    }


    @POST
    @NonBlocking
    // без добавления аннотации @NonBlocking ИЛИ добавления suspend и coroutineScope метод падает с ошибкой
    // "This method should exclusively be invoked from a Vert.x EventLoop thread;
    // currently running on thread 'executor-thread-15'"
    fun addSubscribe(subscriberDto: SubscriberDto): String {
        Log.info("request from subscriber: ${subscriberDto.name}: lat=${subscriberDto.latitude} lon=${subscriberDto.longitude}")
        return _subscribers
            .filter {
                it.lat.toDouble() == subscriberDto.latitude && it.lon.toDouble() == subscriberDto.longitude
            }
            .map { "${it.desc} is already subscribed" }
            .ifEmpty {
                val subscriber = Subscriber(subscriberDto)
                _subscribers.add(subscriber)

//                coroutineScope {
                VertxContextSafetyToggle.setContextSafe(vertx.getOrCreateContext(), true)
                Panache
                    .withTransaction { subscriber.persist<Subscriber>() }
                    .subscribe()
                    .with {
                        Log.info("saved $it")
                    }
//                }

                listOf("${subscriberDto.name} is successfully subscribed")
            }
            .first()
    }
}