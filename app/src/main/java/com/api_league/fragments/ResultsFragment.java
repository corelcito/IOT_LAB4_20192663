package com.api_league.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.api_league.R;
import com.api_league.fragments.adapters.EventAdapter;
import com.api_league.models.Event;
import com.api_league.models.EventResponse;
import com.api_league.retrofit.RetrofitInstance;
import com.api_league.retrofit.SportsApiService;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultsFragment extends Fragment implements SensorEventListener {
    private SensorManager sensorManager;
    private static final float shake = 2000.0f;
    private long lastUpdate;
    private RecyclerView recyclerView;
    private MaterialButton searchButton;
    private EditText edtIdLeague;
    private EditText edtRonda;
    private EditText edtSeason;
    private ProgressBar progress;
    private EventAdapter adapter;

    private String lastIdLeague = "";
    private String lastSeason = "";
    private String lastRonda = "";
    private ArrayList<String[]> searchHistory = new ArrayList<>();
    private ArrayList<Event>  listEvents = new ArrayList<>();
    private AlertDialog dialog;
    public ResultsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);
        recyclerView = view.findViewById(R.id.rv_events);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchButton = view.findViewById(R.id.btn_search_events);
        edtIdLeague = view.findViewById(R.id.edt_id_league);
        edtSeason = view.findViewById(R.id.edt_season);
        edtRonda = view.findViewById(R.id.edt_ronda);
        progress = view.findViewById(R.id.progress_result);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progress.setVisibility(View.GONE);
        adapter = new EventAdapter(requireContext(),listEvents, "");
        recyclerView.setAdapter(adapter);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLastSearch();
               checkFields();
            }
        });
    }

    private void saveLastSearch() {
        String idLeague = edtIdLeague.getText().toString();
        String season = edtSeason.getText().toString();
        String ronda = edtRonda.getText().toString();

        if (!idLeague.isEmpty() && !season.isEmpty() && !ronda.isEmpty()) {
            searchHistory.add(new String[]{idLeague, season, ronda});
        }
    }

    private void checkFields() {

        String idLeague = edtIdLeague.getText().toString();
        String season = edtSeason.getText().toString();
        String ronda = edtRonda.getText().toString();


        if(idLeague.isEmpty() && season.isEmpty() && ronda.isEmpty()) {
            Toast.makeText(requireContext(), "Ingrese los campos requeridos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(idLeague.isEmpty() && season.isEmpty()) {
            Toast.makeText(requireContext(), "Ingrese un idLiga y una temporada válidos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(idLeague.isEmpty() && ronda.isEmpty()) {
            Toast.makeText(requireContext(), "Ingrese un idLiga y una ronda válidos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(season.isEmpty() && ronda.isEmpty()) {
            Toast.makeText(requireContext(), "Ingrese una temporada y una ronda válidos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(idLeague.isEmpty()) {
            Toast.makeText(requireContext(), "Ingrese un idLiga válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(season.isEmpty()) {
            Toast.makeText(requireContext(), "Ingrese una temporada válida.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(ronda.isEmpty()) {
            Toast.makeText(requireContext(), "Ingrese una ronda válida.", Toast.LENGTH_SHORT).show();
            return;
        }


        fetchEvents(idLeague, season, ronda);
    }

    private void fetchEvents(String idLeague, String season, String ronda) {
        progress.setVisibility(View.VISIBLE);
        SportsApiService apiService = RetrofitInstance.getApiService();

        Call<EventResponse> call;
        call = apiService.getEvents(idLeague, ronda, season);

        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<Event> events = response.body().getEvents();
                    if(events != null) {
                        adapter.updateAdapter(events, ronda);
                    } else {
                        Toast.makeText(requireContext(), "No existen registros.", Toast.LENGTH_SHORT).show();
                    }

                    progress.setVisibility(View.GONE);
                } else {
                    Toast.makeText(requireContext(), "No existen registros.", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "No existen registros.", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = curTime - lastUpdate;
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - SensorManager.GRAVITY_EARTH) / diffTime * 10000;

                if (speed > shake) {
                    showConfirmationDialog();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void showConfirmationDialog() {
        if (dialog != null) {
            if(dialog.isShowing()) {
            } else {
                dialog.show();
            }
        } else {
            dialog = new AlertDialog.Builder(requireContext())
                    .setTitle("Confirmar Acción")
                    .setMessage("¿Desea deshacer la ultima busqueda?")
                    .setPositiveButton("Confirmar", (dialog, which) -> {

                        if (searchHistory.size() > 1) {
                            String[] previousSearch = searchHistory.get(searchHistory.size() - 2);

                            edtIdLeague.setText(previousSearch[0]);
                            edtSeason.setText(previousSearch[1]);
                            edtRonda.setText(previousSearch[2]);
                            searchHistory.remove(searchHistory.size() - 1);
                            checkFields();
                        } else {
                            Toast.makeText(requireContext(), "No hay búsquedas anteriores.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        }
    }
}