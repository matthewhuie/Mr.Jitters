/**
 * Filename: FoursquareResponse.java
 * Author: Matthew Huie
 *
 * FoursquareResponse describes a response object from the Foursquare API.
 */

package com.matthewhuie.mrjitters;

import java.util.ArrayList;
import java.util.List;

public class FoursquareResponse {

    // A group object within the response.
    FoursquareGroup group;
    List<FoursquareVenue> venues = new ArrayList<>();

}