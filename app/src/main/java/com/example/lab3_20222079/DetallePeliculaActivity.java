package com.example.lab3_20222079;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab3_20222079.databinding.ActivityDetallePeliculaBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetallePeliculaActivity extends AppCompatActivity {

    private ActivityDetallePeliculaBinding binding;
    private static final String API_KEY = "bf81d461"; // API key brindada en la guía

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetallePeliculaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String imdbId = getIntent().getStringExtra("IMDB_ID");

        if (imdbId != null && !imdbId.isEmpty()) {
            buscarPeliculaOMDB(imdbId);
        }

        binding.btnRegresarDetalle.setOnClickListener(v -> mostrarDialogoConfirmacion());
    }

    private void buscarPeliculaOMDB(String imdbId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OmdbService service = retrofit.create(OmdbService.class);

        service.obtenerPelicula(API_KEY, imdbId).enqueue(new Callback<PeliculaDto>() {
            @Override
            public void onResponse(Call<PeliculaDto> call, Response<PeliculaDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PeliculaDto pelicula = response.body();
                    if ("True".equalsIgnoreCase(pelicula.getResponse())) {
                        binding.tvTituloPelicula.setText(pelicula.getTitle());
                        binding.tvAnioPelicula.setText(pelicula.getYear());
                    } else {
                        Toast.makeText(DetallePeliculaActivity.this, "Película no encontrada", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PeliculaDto> call, Throwable t) {
                Toast.makeText(DetallePeliculaActivity.this, "Error de red al consultar la API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoConfirmacion() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmación")
                .setMessage("¿Desea volver al menú principal?")
                .setPositiveButton("Sí", (dialog, which) -> finish())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}