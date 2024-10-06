package com.api_league;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.api_league.network.NetworkUtils;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    MaterialButton btnInit;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnInit = findViewById(R.id.btn_init);
        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar la conexión a Internet, desde que inicia la app y cuando se ya esta en la app (2 casos)
                if (!NetworkUtils.isConnected(MainActivity.this)) {
                    showNoConnectionDialog();
                } else {
                    startActivity(new Intent(MainActivity.this, AppActivity.class));
                }
            }
        });
    }
//Ayuda de chatgpt como activar un mensaje de configuracion de internet
    private void showNoConnectionDialog() {

        dialog = new AlertDialog.Builder(this)
                .setTitle("Sin conexión a Internet")
                .setMessage("No se ha detectado conexión a Internet. Conéctate a una red para continuar.")
                .setCancelable(false)
                .setPositiveButton("Configuración", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!NetworkUtils.isConnected(this)) {
            showNoConnectionDialog();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }
    }
}