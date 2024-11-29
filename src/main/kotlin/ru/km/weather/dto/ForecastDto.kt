package ru.km.weather.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ForecastDto(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<ListDto>,
    val city: CityDto,
)

data class CityDto(
    val id: Int,
    val name: String,
    val coord: CoordDto,
    val country: String,
    val population: Int,
    val sunrise: Int,
    val sunset: Int,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class CloudsDto(val all: Double?)

data class CoordDto(val lat: Double, val lon: Double)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ListDto(
    val dt: Int,
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

data class SysDto(val pod: String)

data class WeatherDto(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String,
)

data class WindDto(val speed: Double, val deg: Double, val gust: Double)

/*
{
  "cod": "200",
  "message": 0,
  "cnt": 40,
  "list": [
    {
      "dt": 1732806000,
      "main": {
        "temp": -5.96,
        "feels_like": -5.96,
        "temp_min": -5.96,
        "temp_max": -5.02,
        "pressure": 1023,
        "sea_level": 1023,
        "grnd_level": 1008,
        "humidity": 88,
        "temp_kf": -0.94
      },
      "weather": [
        {
          "id": 804,
          "main": "Clouds",
          "description": "пасмурно",
          "icon": "04n"
        }
      ],
      "clouds": {
        "all": 88
      },
      "wind": {
        "speed": 0.3,
        "deg": 111,
        "gust": 0.37
      },
      "visibility": 10000,
      "pop": 0,
      "sys": {
        "pod": "n"
      },
      "dt_txt": "2024-11-28 15:00:00"
    }
  ],
  "city": {
    "id": 476344,
    "name": "Вавилово",
    "coord": {
      "lat": 54.8647,
      "lon": 55.7445
    },
    "country": "RU",
    "population": 0,
    "timezone": 18000,
    "sunrise": 1732767171,
    "sunset": 1732795058
  }
}
 */