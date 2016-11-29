/**
 * Filename: PlacePickerAdapter.java
 * Author: Matthew Huie
 *
 * PlacePickerAdapter represents the adapter for attaching venue data to the RecyclerView within
 * PlacePickerActivity.  This adapter will handle a list of incoming FoursquareResults and parse them
 * into the view.
 */

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

    // The application context for getting resources
    private Context context;

    // The list of results from the Foursquare API
    private List<FoursquareResults> results;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // The venue fields to display
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

            // Gets the appropriate view for each venue detail
            name = (TextView)v.findViewById(R.id.placePickerItemName);
            address = (TextView)v.findViewById(R.id.placePickerItemAddress);
            rating = (TextView)v.findViewById(R.id.placePickerItemRating);
            distance = (TextView)v.findViewById(R.id.placePickerItemDistance);
        }

        @Override
        public void onClick(View v) {

            // Creates an intent to direct the user to a map view
            Context context = name.getContext();
            Intent i = new Intent(context, MapsActivity.class);

            // Passes the crucial venue details onto the map view
            i.putExtra("name", name.getText());
            i.putExtra("ID", id);
            i.putExtra("latitude", latitude);
            i.putExtra("longitude", longitude);

            // Transitions to the map view.
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

        // Sets the proper rating colour, referenced from the Foursquare Brand Guide
        double ratingRaw = results.get(position).venue.rating;
        if (ratingRaw >= 9.0) {
            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQKale));
        } else if (ratingRaw >= 8.0) {
            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQGuacamole));
        } else if (ratingRaw >= 7.0) {
            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQLime));
        } else if (ratingRaw >= 6.0) {
            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQBanana));
        } else if (ratingRaw >= 5.0) {
            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQOrange));
        } else if (ratingRaw >= 4.0) {
            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQMacCheese));
        } else {
            holder.rating.setBackgroundColor(ContextCompat.getColor(context, R.color.FSQStrawberry));
        }

        // Sets each view with the appropriate venue details
        holder.name.setText(results.get(position).venue.name);
        holder.address.setText(results.get(position).venue.location.address);
        holder.rating.setText(Double.toString(ratingRaw));
        holder.distance.setText(Integer.toString(results.get(position).venue.location.distance) + "m");

        // Stores additional venue details for the map view
        holder.id = results.get(position).venue.id;
        holder.latitude = results.get(position).venue.location.lat;
        holder.longitude = results.get(position).venue.location.lng;
    }

    @Override
    public int getItemCount() {
        return results.size();
    }
}