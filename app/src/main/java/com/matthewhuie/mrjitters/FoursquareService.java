package com.matthewhuie.mrjitters;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoursquareService {

    final String BASE_URL = "https://api.foursquare.com/v2/";
    final String clientID = "GC1GONEPBAOOZTVGAPVLNTPKXCU4RMY0N5DFEHFTZKWHFPTT";
    final String clientSecret = "QZI3RNOYAPAN1R0LP11LDIUYQTRSF3LO2UPSVCMIEDNVC5AC";

    @GET("search/recommendations?client_id=" + clientID + "&client_secret=" + clientSecret + "&v=20161101&limit=1&sortByDistance=true")
    Call<FoursquareJSON> snapToPlace(@Query("ll") String ll, @Query("llAcc") double llAcc);

    @GET("search/recommendations?client_id=" + clientID + "&client_secret=" + clientSecret + "&v=20161101&intent=coffee")
    Call<FoursquareJSON> searchCoffee(@Query("ll") String ll, @Query("llAcc") double llAcc);
}