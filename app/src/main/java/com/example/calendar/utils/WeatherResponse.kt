package com.example.calendar.utils

import WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface WeatherService {
    @GET("forecasts/v1/daily/1day/{locationKey}")
    suspend fun getDailyForecast(
        @Path("locationKey") locationKey: String,
        @Query("apikey") apiKey: String
    ): Response<WeatherResponse>
    @GET("forecasts/v1/daily/5day/{locationKey}")
    suspend fun getFiveDayForecast(
        @Path("locationKey") locationKey: String,
        @Query("apikey") apiKey: String
    ): Response<WeatherResponse>
    @GET("locations/v1/cities/search")
    suspend fun searchCityLocation(
        @Query("apikey") apiKey: String,
        @Query("q") city: String
    ): Response<List<CityLocationResponse>>
}
object RetrofitInstance {
    val api: WeatherService by lazy {
        Retrofit.Builder()
            .baseUrl("https://dataservice.accuweather.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)
    }
}
