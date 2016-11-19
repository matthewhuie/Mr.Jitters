package com.matthewhuie.mrjitters;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlacePickerActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mGoogleApiClient;
    double userLatitude;
    double userLongitude;
    double userLLAcc;
    TextView snapToPlace;
    RecyclerView placePicker;
    RecyclerView.LayoutManager placePickerManager;
    RecyclerView.Adapter placePickerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);

        snapToPlace = (TextView)findViewById(R.id.snapToPlace);
        placePicker = (RecyclerView)findViewById(R.id.coffeeList);
        placePicker.setHasFixedSize(true);
        placePicker.setLayoutManager(new LinearLayoutManager(this));

        mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION }, 0);
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            userLatitude = mLastLocation.getLatitude();
            userLongitude = mLastLocation.getLongitude();
            userLLAcc = mLastLocation.getAccuracy();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(FoursquareService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            FoursquareService foursquare = retrofit.create(FoursquareService.class);
            Call<FoursquareJSON> stpCall = foursquare.snapToPlace(userLatitude + "," + userLongitude, userLLAcc);
            stpCall.enqueue(new Callback<FoursquareJSON>() {
                @Override
                public void onResponse(Call<FoursquareJSON> call, Response<FoursquareJSON> response) {
                    FoursquareJSON fjson = response.body();
                    FoursquareResponse fr = fjson.response;
                    List<FoursquareVenue> venues = fr.venues;
                    FoursquareVenue fv = venues.get(0);
                    snapToPlace.setText("You're at " + fv.toString() + ". Here's some ☕ nearby.");
                }

                @Override
                public void onFailure(Call<FoursquareJSON> call, Throwable t) {}
            });

            Call<FoursquareJSON> cCall = foursquare.coffee(userLatitude + "," + userLongitude, userLLAcc);
            cCall.enqueue(new Callback<FoursquareJSON>() {
                @Override
                public void onResponse(Call<FoursquareJSON> call, Response<FoursquareJSON> response) {
                    FoursquareJSON fjson = response.body();
                    FoursquareResponse fr = fjson.response;
                    List<FoursquareVenue> venues = fr.venues;
                    placePickerAdapter = new PlacePickerAdapter(venues);
                    placePicker.setAdapter(placePickerAdapter);
                }

                @Override
                public void onFailure(Call<FoursquareJSON> call, Throwable t) {}
            });
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
}