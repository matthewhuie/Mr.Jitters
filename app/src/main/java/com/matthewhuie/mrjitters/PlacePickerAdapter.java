package com.matthewhuie.mrjitters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

public class PlacePickerAdapter extends RecyclerView.Adapter<PlacePickerAdapter.ViewHolder> {

    private List<FoursquareVenue> venues;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView venuename;

        public ViewHolder(View v) {
            super(v);
            venuename = (TextView)v.findViewById(R.id.placePickerItemText);
        }
    }

    public PlacePickerAdapter(List<FoursquareVenue> venues) {
        this.venues = venues;
    }

    @Override
    public PlacePickerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place_picker, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.venuename.setText(venues.get(position).name);
    }

    @Override
    public int getItemCount() {
        return venues.size();
    }
}