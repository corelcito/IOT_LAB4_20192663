package com.api_league.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.api_league.R;
import com.api_league.fragments.adapters.StandingAdapter;
import com.api_league.models.Standing;
import com.api_league.models.StandingResponse;
import com.api_league.retrofit.RetrofitInstance;
import com.api_league.retrofit.SportsApiService;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PositionsFragment extends Fragment {
    private RecyclerView recyclerView;
    private MaterialButton searchButton;
    private EditText edtIdLeague;
    private EditText edtSeason;
    private ProgressBar progress;
    private StandingAdapter adapter;

    public PositionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_positions, container, false);

        recyclerView = view.findViewById(R.id.rv_standing);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchButton = view.findViewById(R.id.btn_search_positions);
        edtIdLeague = view.findViewById(R.id.edt_id_league);
        edtSeason = view.findViewById(R.id.edt_season);
        progress = view.findViewById(R.id.progress_positions);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progress.setVisibility(View.GONE);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idLeague = edtIdLeague.getText().toString();
                String season = edtSeason.getText().toString();

                if(idLeague.isEmpty() && season.isEmpty()) {
                    Toast.makeText(requireContext(), "Ingrese los campos requeridos.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(idLeague.isEmpty()) {
                    Toast.makeText(requireContext(), "Ingrese un idLiga valido.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(season.isEmpty()) {
                    Toast.makeText(requireContext(), "Ingrese una temporada valida.", Toast.LENGTH_SHORT).show();
                    return;
                }
                fetchPositions(idLeague, season);
            }
        });
    }

    private void fetchPositions(String idLeague, String season) {
        progress.setVisibility(View.VISIBLE);

        SportsApiService apiService = RetrofitInstance.getApiService();

        Call<StandingResponse> call;
        call = apiService.getStandings(idLeague, season);

        call.enqueue(new Callback<StandingResponse>() {
            @Override
            public void onResponse(Call<StandingResponse> call, Response<StandingResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<Standing> standings = response.body().getTable();
                    adapter = new StandingAdapter(requireContext(), standings);
                    recyclerView.setAdapter(adapter);
                    progress.setVisibility(View.GONE);
                } else {
                    Toast.makeText(requireContext(), "No existen registros.", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<StandingResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "No existen registros.", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);
            }
        });
    }
}