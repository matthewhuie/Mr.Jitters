package com.matthewhuie.mrjitters;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlacePickerActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private TextView snapToPlace;
    private RecyclerView placePicker;
    private RecyclerView.LayoutManager placePickerManager;
    private RecyclerView.Adapter placePickerAdapter;
    private String foursquareBaseURL = "https://api.foursquare.com/v2/";
    private String foursquareClientID;
    private String foursquareClientSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        snapToPlace = (TextView)findViewById(R.id.snapToPlace);
        placePicker = (RecyclerView)findViewById(R.id.coffeeList);
        placePicker.setHasFixedSize(true);
        placePickerManager = new LinearLayoutManager(this);
        placePicker.setLayoutManager(placePickerManager);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build();

        foursquareClientID = getResources().getString(R.string.foursquare_client_id);
        foursquareClientSecret = getResources().getString(R.string.foursquare_client_secret);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        double userLatitude;
        double userLongitude;
        double userLLAcc;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION }, 0);
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            userLatitude = mLastLocation.getLatitude();
            userLongitude = mLastLocation.getLongitude();
            userLLAcc = mLastLocation.getAccuracy();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(foursquareBaseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            FoursquareService foursquare = retrofit.create(FoursquareService.class);
            Call<FoursquareJSON> stpCall = foursquare.snapToPlace(
                    foursquareClientID,
                    foursquareClientSecret,
                    userLatitude + "," + userLongitude,
                    userLLAcc);
            stpCall.enqueue(new Callback<FoursquareJSON>() {
                @Override
                public void onResponse(Call<FoursquareJSON> call, Response<FoursquareJSON> response) {
                    FoursquareJSON fjson = response.body();
                    FoursquareResponse fr = fjson.response;
                    List<FoursquareVenue> frs = fr.venues;
                    FoursquareVenue fv = frs.get(0);
                    snapToPlace.setText("You're at " + fv.name + ". Here's some ☕ nearby.");
                }

                @Override
                public void onFailure(Call<FoursquareJSON> call, Throwable t) {}
            });

            Call<FoursquareJSON> cCall = foursquare.searchCoffee(
                    foursquareClientID,
                    foursquareClientSecret,
                    userLatitude + "," + userLongitude,
                    userLLAcc);
            cCall.enqueue(new Callback<FoursquareJSON>() {
                @Override
                public void onResponse(Call<FoursquareJSON> call, Response<FoursquareJSON> response) {
                    FoursquareJSON fjson = response.body();
                    FoursquareResponse fr = fjson.response;
                    FoursquareGroup fg = fr.group;
                    List<FoursquareResults> frs = fg.results;
                    placePickerAdapter = new PlacePickerAdapter(frs);
                    placePicker.setAdapter(placePickerAdapter);
                }

                @Override
                public void onFailure(Call<FoursquareJSON> call, Throwable t) {}
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
}