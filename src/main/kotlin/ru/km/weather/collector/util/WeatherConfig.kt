package ru.km.weather.collector.util

import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "weather")
interface WeatherConfig {
    fun api(): ApiConfig
    fun city(): CityConfig

    interface ApiConfig {
        fun key(): String
        fun units(): String
        fun language(): String
    }

    interface CityConfig {
        fun latitude(): String
        fun longitude(): String
    }
}