package ru.km.weather.collector.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class CurrentWeatherDto(
    val coord: CoordDto,
    val weather: List<WeatherDto>,
    val base: String,
    val main: MainDto,
    val visibility: Int,
    val wind: WindDto,
    val rain: RainSnowDto?,
//    val clouds: CloudsDto,
    val dt: Long,
    val sys: SysDto,
    val timezone: Int,
    val id: Long,
    val name: String,
    val cod: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ForecastDto(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<ListDto>,
    val city: CityDto,
)

data class CityDto(
    val id: Long,
    val name: String,
    val coord: CoordDto,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Int,
    val sunset: Int,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class CloudsDto(val all: Double?)

data class CoordDto(val lat: Double, val lon: Double)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ListDto(
    val dt: Long,
    val main: MainDto,
    val weather: List<WeatherDto>,
//    val clouds: CloudsDto?,
    val wind: WindDto,
)

data class MainDto(
    val temp: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure: Double,
    val seaLevel: Double,
    val grndLevel: Double,
    val humidity: Double,
    val tempKf: Double,
)

data class RainSnowDto(val threeHour: Int)

data class SysDto(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Long,
    val sunset: Long,
)

data class WeatherDto(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String,
)

data class WindDto(val speed: Double, val deg: Double, val gust: Double)