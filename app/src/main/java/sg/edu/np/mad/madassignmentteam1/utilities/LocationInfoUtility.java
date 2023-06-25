package sg.edu.np.mad.madassignmentteam1.utilities;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import com.google.maps.FindPlaceFromTextRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.PlacesApi;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.FindPlaceFromText;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.PlacesSearchResult;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import sg.edu.np.mad.madassignmentteam1.LocationInfo;
import sg.edu.np.mad.madassignmentteam1.R;

/*
For future reference:
- In case an error occurs related to the Google Maps API key not being found or when setting up
the project to test it out, ensure that you have an XML file named
"DO_NOT_PUSH_TO_GITHUB.xml" in the "values" folder located in the "res" folder of this
project's main folder. If you do not see the file there or do not have it, it will need to be
sent to you through other means (e.g. Microsoft Teams or Whatsapp) instead of being pushed to the
public Github repository.
*/

/**
 * This class provides helper methods for managing and working with LocationInfo objects.
 */
public class LocationInfoUtility
{
    private static GeoApiContext googleMapsGeoApiContext = null;

    /**
     * Returns a list of LocationInfo objects associated with locations that have names that are similar
     * to or the same as the specified location name.
     * @param locationName
     * @param context
     * @return An ArrayList of LocationInfo objects.
     */
    public static ArrayList<LocationInfo> getCorrespondingLocationsForLocationName(String locationName, Context context)
    {
        if (googleMapsGeoApiContext == null)
        {
            try
            {
                googleMapsGeoApiContext = new GeoApiContext.Builder().apiKey(
                    context.getString(R.string.GOOGLE_MAPS_API_KEY)
                ).build();
            }
            catch (Exception exception)
            {
                LoggerUtility.logException(exception);
            }
        }

        ArrayList<LocationInfo> locationInfoArrayList = new ArrayList<>();

        PlacesSearchResult[] locationResults = null;

        try
        {
            FindPlaceFromTextRequest findPlaceFromTextRequest = PlacesApi.findPlaceFromText(
                googleMapsGeoApiContext,
                locationName,
                FindPlaceFromTextRequest.InputType.TEXT_QUERY
            );

            findPlaceFromTextRequest.fields(
                FindPlaceFromTextRequest.FieldMask.values()
            );

            FindPlaceFromText findPlaceFromTextResult = findPlaceFromTextRequest.await();

            if (findPlaceFromTextResult != null)
            {
                locationResults = findPlaceFromTextResult.candidates;
            }
        }
        catch (Exception exception)
        {
            LoggerUtility.logException(exception);
        }

        // LoggerUtility.logInformation("Retrieving corresponding locations...");

        GeocodingResult[] currentLocationGeocodingResults = null;

        String currentLocationResultPostalCode = "";

        if (locationResults != null)
        {
            for (int currentLocationResultIndex = 0; currentLocationResultIndex < locationResults.length; currentLocationResultIndex++)
            {
                try
                {
                    currentLocationGeocodingResults = GeocodingApi.reverseGeocode(
                            googleMapsGeoApiContext,
                            locationResults[currentLocationResultIndex].geometry.location
                    ).await();
                }
                catch (Exception exception)
                {
                    LoggerUtility.logException(exception);
                }

                if (currentLocationGeocodingResults != null && currentLocationGeocodingResults.length > 0)
                {
                    for (int currentAddressComponentIndex = 0; currentAddressComponentIndex < currentLocationGeocodingResults[0].addressComponents.length; currentAddressComponentIndex++)
                    {
                        // if (Arrays.stream(currentLocationGeocodingResults[0].addressComponents[currentAddressComponentIndex].types).anyMatch(x -> x == AddressComponentType.POSTAL_CODE) == true)
                        if (Arrays.stream(currentLocationGeocodingResults[0].addressComponents[currentAddressComponentIndex].types).anyMatch(x -> x == AddressComponentType.POSTAL_CODE))
                        {
                            currentLocationResultPostalCode = currentLocationGeocodingResults[0].addressComponents[currentAddressComponentIndex].shortName;
                        }
                    }
                }

                locationInfoArrayList.add(
                    new LocationInfo(
                        locationResults[currentLocationResultIndex].name,
                        locationResults[currentLocationResultIndex].formattedAddress,
                        currentLocationResultPostalCode,
                        new LatLng(
                            locationResults[currentLocationResultIndex].geometry.location.lat,
                            locationResults[currentLocationResultIndex].geometry.location.lng
                        ),
                        locationResults[currentLocationResultIndex].placeId
                    )
                );

                // LoggerUtility.logInformation("Corresponding location found.");
            }
        }

        // LoggerUtility.logInformation("Finished retrieving corresponding locations successfully.");

        return locationInfoArrayList;
    }

    /**
     * An asynchronous implementation of the getCorrespondingLocationsForLocationName method of the
     * LocationInfoUtility class.
     * @param locationName
     * @param context
     * @param onLocationInfoResultsReadyListener
     */
    public static void getCorrespondingLocationsForLocationNameAsync(String locationName, Context context, OnLocationInfoResultsReadyListener onLocationInfoResultsReadyListener)
    {

    }

    /**
     * Returns a JSON-formatted string representation of a LocationInfo object.
     * @param locationInfo
     * @return A String object.
     */
    public static String getLocationInfoAsJsonString(LocationInfo locationInfo)
    {
        JSONObject locationInfoJsonObject = new JSONObject();

        try
        {
            locationInfoJsonObject.put("name", locationInfo.name);

            locationInfoJsonObject.put("address", locationInfo.address);

            locationInfoJsonObject.put("latitude", locationInfo.latLng.latitude);

            locationInfoJsonObject.put("longitude", locationInfo.latLng.longitude);

            locationInfoJsonObject.put("postal_code", locationInfo.postalCode);

            locationInfoJsonObject.put("google_maps_place_id", locationInfo.googleMapsPlaceID);
        }
        catch (Exception exception)
        {
            LoggerUtility.logInformation(
                    "Error: Unable to put value into JSONObject for current LocationInfo instance."
            );

            LoggerUtility.logException(exception);
        }

        return locationInfoJsonObject.toString();
    }

    /**
     * Parses a JSON-formatted String and returns a LocationInfo object.
     * This method will return null in the event of an error occurring during its execution.
     * @param locationInfoJsonString
     * @return A LocationInfo object.
     */
    public static LocationInfo getLocationInfoFromJsonString(String locationInfoJsonString)
    {
        LoggerUtility.logInformation(
            "Currently in getLocationInfoFromJsonString method."
        );

        JSONObject locationInfoJsonObject;

        LocationInfo locationInfo = null;

        try
        {
            locationInfoJsonObject = new JSONObject(
                locationInfoJsonString
            );

            locationInfo = new LocationInfo(
                locationInfoJsonObject.getString("name"),
                locationInfoJsonObject.getString("address"),
                locationInfoJsonObject.getString("postal_code"),
                new LatLng(
                    locationInfoJsonObject.getDouble("latitude"),
                    locationInfoJsonObject.getDouble("longitude")
                ),
                locationInfoJsonObject.getString("google_maps_place_id")
            );
        }
        catch (Exception exception)
        {
            LoggerUtility.logInformation(
                "Error: Exception occurred while attempting to create new JSON object and LocationInfo object."
            );

            LoggerUtility.logException(exception);
        }

        return locationInfo;
    }

    /**
     * An interface that allows for the registration of callback methods to be executed when the
     * getCorrespondingLocationsForLocationNameAsync method of the LocationInfoUtility class
     * is about to finish executing.
     */
    public interface OnLocationInfoResultsReadyListener
    {
        /**
         * The method that is called when the getCorrespondingLocationsForLocationNameAsync method
         * of the LocationInfoUtility class is about to finish executing.
         * @param locationInfoResults
         */
        void onLocationInfoResultsReady(ArrayList<LocationInfo> locationInfoResults);
    }
}
