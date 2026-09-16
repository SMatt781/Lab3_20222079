package com.example.lab3_20222079;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab3_20222079.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnIrContador.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ContadorActivity.class);
            startActivity(intent);
        });

        binding.btnComprobarConexion.setOnClickListener(v -> {
            if (tengoConexionInternet()) {
                Toast.makeText(this, "¡Conexión a Internet exitosa!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error: Sin conexión a Internet", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnBuscar.setOnClickListener(v -> {
            String imdbId = binding.etIdPelicula.getText().toString().trim();
            if (imdbId.isEmpty()) {
                Toast.makeText(this, "Ingrese un ID de IMDb válido", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(MainActivity.this, DetallePeliculaActivity.class);
            intent.putExtra("IMDB_ID", imdbId);
            startActivity(intent);
        });
    }

    private boolean tengoConexionInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
            return capabilities != null && (
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            );
        }
        return false;
    }
}