package com.api_league.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.api_league.R;
import com.api_league.models.Standing;
import com.bumptech.glide.Glide;

import java.util.List;

public class StandingAdapter extends RecyclerView.Adapter<StandingAdapter.StandingViewHolder> {

    private Context context;
    private List<Standing> standings;

    public StandingAdapter(Context context, List<Standing> standings) {
        this.context = context;
        this.standings = standings;
    }

    @NonNull
    @Override
    public StandingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_standing, parent, false);
        return new StandingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StandingViewHolder holder, int position) {
        Standing standing = standings.get(position);

        holder.standingName.setText(standing.getStrTeam());
        holder.standingRanking.setText(standing.getIntRank());
        holder.standingResults.setText(
                String.format("%s/%s/%s", standing.getIntWin(), standing.getIntDraw(), standing.getIntLoss())
        );

        // Cargar la imagen del badge usando Glide
        Glide.with(context)
                .load(standing.getStrBadge())
                .placeholder(R.drawable.baseline_flag_24) // Imagen por defecto mientras carga
                .into(holder.badgeImage);
    }

    @Override
    public int getItemCount() {
        return standings.size();
    }

    public static class StandingViewHolder extends RecyclerView.ViewHolder {

        ImageView badgeImage;
        TextView standingName, standingRanking, standingResults;

        public StandingViewHolder(@NonNull View itemView) {
            super(itemView);
            badgeImage = itemView.findViewById(R.id.imageLocal);
            standingName = itemView.findViewById(R.id.standing_name);
            standingRanking = itemView.findViewById(R.id.standing_ranking);
            standingResults = itemView.findViewById(R.id.standing_results);
        }
    }
}