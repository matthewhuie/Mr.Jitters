package com.matthewhuie.mrjitters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PlacePickerAdapter extends RecyclerView.Adapter<PlacePickerAdapter.ViewHolder> {

    private Context context;
    private List<FoursquareResults> results;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView address;
        TextView rating;
        TextView distance;
        String id;
        double latitude;
        double longitude;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);

            name = (TextView)v.findViewById(R.id.placePickerItemName);
            address = (TextView)v.findViewById(R.id.placePickerItemAddress);
            rating = (TextView)v.findViewById(R.id.placePickerItemRating);
            distance = (TextView)v.findViewById(R.id.placePickerItemDistance);
        }

        @Override
        public void onClick(View v) {
            Context context = name.getContext();
            Intent i = new Intent(context, MapsActivity.class);
            i.putExtra("name", name.getText());
            i.putExtra("ID", id);
            i.putExtra("latitude", latitude);
            i.putExtra("longitude", longitude);
            context.startActivity(i);
        }
    }

    public PlacePickerAdapter(Context context, List<FoursquareResults> results) {
        this.context = context;
        this.results = results;
    }

    @Override
    public PlacePickerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place_picker, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        double ratingRaw = results.get(position).venue.rating;
        if (ratingRaw <= 2.5) {
            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQStrawberry));
        } else if (ratingRaw <= 5.0) {
            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQOrange));
        } else if (ratingRaw <= 7.5) {
            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQLime));
        } else if (ratingRaw <= 10.0) {
            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQKale));
        }

        holder.name.setText(results.get(position).venue.name);
        holder.address.setText(results.get(position).venue.location.address);
        holder.rating.setText(Double.toString(ratingRaw) + "⭐");
        holder.distance.setText(Integer.toString(results.get(position).venue.location.distance) + "m");

        holder.id = results.get(position).venue.id;
        holder.latitude = results.get(position).venue.location.lat;
        holder.longitude = results.get(position).venue.location.lng;
    }

    @Override
    public int getItemCount() {
        return results.size();
    }
}