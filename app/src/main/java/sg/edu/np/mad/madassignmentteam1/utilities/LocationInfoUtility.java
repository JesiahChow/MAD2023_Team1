package sg.edu.np.mad.madassignmentteam1.utilities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import com.google.maps.FindPlaceFromTextRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.FindPlaceFromText;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.PlacesSearchResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import sg.edu.np.mad.madassignmentteam1.LocationInfo;
import sg.edu.np.mad.madassignmentteam1.R;
import sg.edu.np.mad.madassignmentteam1.utilities.LoggerUtility;

public class LocationInfoUtility
{
    private static GeoApiContext googleMapsGeoApiContext = null;

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
                        if (Arrays.stream(currentLocationGeocodingResults[0].addressComponents[currentAddressComponentIndex].types).anyMatch(x -> x == AddressComponentType.POSTAL_CODE) == true)
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

    public static void getCorrespondingLocationsForLocationNameAsync(String locationName, Context context, OnLocationInfoResultsReadyListener onLocationInfoResultsReadyListener)
    {

    }

    public interface OnLocationInfoResultsReadyListener
    {
        public void onLocationInfoResultsReady(ArrayList<LocationInfo> locationInfoResults);
    }
}
