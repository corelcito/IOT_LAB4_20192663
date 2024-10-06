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
import com.api_league.models.Event;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context context;
    private ArrayList<Event> eventList;
    private String ronda;
    public EventAdapter(Context context, ArrayList<Event> eventList, String ronda) {
        this.context = context;
        this.eventList = eventList;
        this.ronda = ronda;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_results, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        holder.eventsName.setText(event.getStrLeague());
        holder.eventsRonda.setText(ronda);
        holder.eventsLocalEquip.setText(event.getStrHomeTeam());
        holder.eventsVisitEquip.setText(event.getStrAwayTeam());
        holder.eventsResult.setText(event.getIntHomeScore() + " - " + event.getIntAwayScore());
        holder.eventsDate.setText(event.getDateEvent());
        holder.eventsTotal.setText(event.getIntSpectators() != null ? event.getIntSpectators() : "N/A");

        Glide.with(context)
                .load(event.getStrLeagueBadge())
                .placeholder(R.drawable.baseline_flag_24)
                .into(holder.imageViewEvent);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void updateAdapter(List<Event> listEvents,String ronda) {
        int sizeInit =  eventList.size();
        int sizeFinal = sizeInit + listEvents.size();
        this.ronda = ronda;
        this.eventList.addAll(listEvents);
        notifyItemRangeInserted(sizeInit, sizeFinal);
    }
    public static class EventViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewEvent;
        TextView eventsName, eventsRonda, eventsLocalEquip, eventsVisitEquip, eventsResult, eventsDate, eventsTotal;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewEvent = itemView.findViewById(R.id.imageEvent);

            eventsName = itemView.findViewById(R.id.events_name);
            eventsRonda = itemView.findViewById(R.id.events_ronda);
            eventsLocalEquip = itemView.findViewById(R.id.events_local_equip);
            eventsVisitEquip = itemView.findViewById(R.id.events_visit_equip);
            eventsResult = itemView.findViewById(R.id.events_result);
            eventsDate = itemView.findViewById(R.id.events_date);
            eventsTotal = itemView.findViewById(R.id.events_total);
        }
    }
}
