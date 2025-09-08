package org.tymosh.retrofit;

import org.tymosh.model.Data;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface WeatherApiService {
    @GET("forecast.json?aqi=no&alerts=no")
    Call<Data> getData(@Query("q") String city, @Query("key") String apiKey);
}
