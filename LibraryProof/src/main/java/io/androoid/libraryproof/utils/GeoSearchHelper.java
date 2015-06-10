package io.androoid.libraryproof.utils;


import android.os.AsyncTask;

import org.apache.http.impl.client.DefaultHttpClient;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.List;

import fr.dudie.nominatim.client.JsonNominatimClient;
import fr.dudie.nominatim.model.Address;

/**
 *
 * This Utility class provides the necessary methods to obtain GeoPoint from
 * an existing address.
 *
 * This class will be invoked by all generated Activities using Androoid project
 * that includes a map view.
 *
 * Uses Nominatim Java API to get GeoPoints from an existing address.
 *
 * @author Juan Carlos García
 * @since 1.0
 */
public class GeoSearchHelper extends AsyncTask<String, Integer, GeoPoint> {

    public AsyncGeoResponse delegate= null;
    private JsonNominatimClient nominatiumClient = new JsonNominatimClient(new DefaultHttpClient(), "your-email@androoid.io");


    /**
     * Method that returns GEO information about some address
     *
     * @param address
     * @return
     */
    @Override
    protected GeoPoint doInBackground(String... address) {
        try {
            List<Address> result = nominatiumClient.search(address[0]);

            for(Address resultAddress : result){
                return new GeoPoint(resultAddress.getLatitude(), resultAddress.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(GeoPoint result) {
        delegate.processFinish(result);
    }
}
