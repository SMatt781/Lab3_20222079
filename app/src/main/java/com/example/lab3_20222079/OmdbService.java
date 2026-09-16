package com.example.lab3_20222079;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OmdbService {
    @GET("/")
    Call<PeliculaDto> obtenerPelicula(
            @Query("apikey") String apiKey,
            @Query("i") String imdbId
    );
}