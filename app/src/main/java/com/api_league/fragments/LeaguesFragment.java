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
import com.api_league.fragments.adapters.LeaguesAdapter;
import com.api_league.models.League;
import com.api_league.models.LeaguesResponse;
import com.api_league.retrofit.RetrofitInstance;
import com.api_league.retrofit.SportsApiService;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LeaguesFragment extends Fragment {
    private RecyclerView recyclerView;
    private MaterialButton searchButton;
    private EditText edtSearch;
    private ProgressBar progress;
    private LeaguesAdapter adapter;

    public LeaguesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leagues, container, false);
        recyclerView = view.findViewById(R.id.rv_leagues);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchButton = view.findViewById(R.id.btn_search);
        edtSearch = view.findViewById(R.id.edt_search);
        progress = view.findViewById(R.id.progress_league);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progress.setVisibility(View.GONE);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = edtSearch.getText().toString();
                fetchLeagues(query);
            }
        });
    }

    private void fetchLeagues(String country) {
        progress.setVisibility(View.VISIBLE);
        SportsApiService apiService = RetrofitInstance.getApiService();

        Call<LeaguesResponse> call;
        if (country.isEmpty()) {
            call = apiService.getAllLeagues();
        } else {
            call = apiService.getLeaguesByCountry(country);
        }

        call.enqueue(new Callback<LeaguesResponse>() {
            @Override
            public void onResponse(Call<LeaguesResponse> call, Response<LeaguesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if(country.isEmpty()) {
                        List<League> leagues = response.body().getLeagues();
                        if(leagues == null) {
                            leagues = new ArrayList<>();
                            Toast.makeText(requireContext(), "No existen registros.", Toast.LENGTH_SHORT).show();
                        }
                        adapter = new LeaguesAdapter(leagues);
                        recyclerView.setAdapter(adapter);
                    } else {
                        List<League> countries = response.body().getCountries();
                        if(countries == null) {
                            countries = new ArrayList<>();
                            Toast.makeText(requireContext(), "No existen registros.", Toast.LENGTH_SHORT).show();

                        }
                        adapter = new LeaguesAdapter(countries);
                        recyclerView.setAdapter(adapter);
                    }
                    progress.setVisibility(View.GONE);

                } else {
                    Toast.makeText(requireContext(), "No existen registros.", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<LeaguesResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Ocurrio un error", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);

            }
        });
    }
}