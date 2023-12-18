data class WeatherResponse(val DailyForecasts: List<DailyForecast>)

data class DailyForecast(
    val Date: String,
    val EpochDate: Long,
    val Temperature: Temperature,
    val Day: Day
)

data class Temperature(val Minimum: Minimum, val Maximum: Maximum)

data class Minimum(val Value: Double, val Unit: String, val UnitType: Int)

data class Maximum(val Value: Double, val Unit: String, val UnitType: Int)

data class Day(val Icon: Int, val IconPhrase: String, val HasPrecipitation: Boolean)
