package com.api_league.fragments.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.api_league.R;
import com.api_league.models.League;

import java.util.List;

public class LeaguesAdapter extends RecyclerView.Adapter<LeaguesAdapter.LeagueViewHolder> {

    private List<League> leagueList;

    public LeaguesAdapter(List<League> leagues) {
        this.leagueList = leagues;
    }

    @NonNull
    @Override
    public LeagueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_league, parent, false);
        return new LeagueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeagueViewHolder holder, int position) {
        League league = leagueList.get(position);
        holder.leagueName.setText(league.getStrLeague());
        holder.leagueSport.setText(league.getStrSport());
        try {
            holder.leagueAlternative.setText(league.getStrLeagueAlternate());

        }catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return leagueList.size();
    }

    public static class LeagueViewHolder extends RecyclerView.ViewHolder {

        TextView leagueName, leagueSport;
        TextView leagueAlternative;
        public LeagueViewHolder(View itemView) {
            super(itemView);
            leagueName = itemView.findViewById(R.id.leagueName);
            leagueSport = itemView.findViewById(R.id.leagueSport);
            leagueAlternative = itemView.findViewById(R.id.leagueAlternative);
        }
    }
}
