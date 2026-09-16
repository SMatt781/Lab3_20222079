package com.example.lab3_20222079;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab3_20222079.databinding.ActivityContadorBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContadorActivity extends AppCompatActivity {

    private ActivityContadorBinding binding;
    private int contadorActual = 0;
    private boolean enEjecucion = false;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContadorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState != null) {
            contadorActual = savedInstanceState.getInt("CONTADOR_VALOR", 0);
            enEjecucion = savedInstanceState.getBoolean("EN_EJECUCION", false);
            binding.tvValorContador.setText(String.valueOf(contadorActual));

            if (enEjecucion && contadorActual < 20) {
                iniciarHiloContador();
            }
        }

        binding.btnIniciarContador.setOnClickListener(v -> {
            if (!enEjecucion) {
                contadorActual = 0;
                iniciarHiloContador();
            }
        });

        binding.btnRegresar.setOnClickListener(v -> finish());
    }

    private void iniciarHiloContador() {
        enEjecucion = true;
        binding.btnIniciarContador.setEnabled(false);

        executorService.execute(() -> {
            while (contadorActual < 20 && enEjecucion) {
                try {
                    Thread.sleep(1000); // Conteo de 1s
                } catch (InterruptedException e) {
                    break;
                }
                contadorActual++;

                mainHandler.post(() -> binding.tvValorContador.setText(String.valueOf(contadorActual)));
            }

            enEjecucion = false;
            mainHandler.post(() -> binding.btnIniciarContador.setEnabled(true));
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CONTADOR_VALOR", contadorActual);
        outState.putBoolean("EN_EJECUCION", enEjecucion);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        enEjecucion = false;
        executorService.shutdownNow();
    }
}