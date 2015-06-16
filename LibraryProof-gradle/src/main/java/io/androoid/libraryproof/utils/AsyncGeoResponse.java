package io.androoid.libraryproof.utils;

import org.osmdroid.util.GeoPoint;

/**
 * @author Juan Carlos García
 * @since 1.0
 */
public interface AsyncGeoResponse {

    /**
     * Method that will be executed when async task finishes
     * @param output
     */
    void processFinish(GeoPoint output);
}
