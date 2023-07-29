package sg.edu.np.mad.madassignmentteam1.utilities;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import sg.edu.np.mad.madassignmentteam1.LocationInfo;

public class NavigationUtility
{
    private static final String ROUTE_FINDER_RESOURCE_URL = "https://api.stb.gov.sg/services/navigation/v2/experiential-route/";

    // TODO: Move the API key to the AndroidManifest.xml file to be fetched from there at runtime.
    private static final String TIH_API_KEY = "ikZE4eCj4SNqUVtit6CL2R7NDcsT0rA6";

    public static final NavigationUtility instance = new NavigationUtility();

    private NavigationUtility()
    {

    }

    private RouteStep getRouteStepFromRouteStepJsonObject(JSONObject routeStepJsonObject)
    {
        RouteStep routeStep = new RouteStep();

        try
        {
            routeStep.distance = routeStepJsonObject.getInt(
                    "distance"
            );

            routeStep.duration = routeStepJsonObject.getInt(
                    "duration"
            );

            routeStep.startLocationLatLng = new LatLng(
                    routeStepJsonObject.getJSONObject(
                            "startLocation"
                    ).getDouble("latitude"),
                    routeStepJsonObject.getJSONObject(
                            "startLocation"
                    ).getDouble("longitude")
            );

            routeStep.endLocationLatLng = new LatLng(
                    routeStepJsonObject.getJSONObject(
                            "endLocation"
                    ).getDouble("latitude"),
                    routeStepJsonObject.getJSONObject(
                            "endLocation"
                    ).getDouble("longitude")
            );

            routeStep.instruction = routeStepJsonObject.getString(
                    "htmlInstructions"
            );

            routeStep.travelMode = this.getAsTransportModesEnum(
                    routeStepJsonObject.getString(
                            "travelMode"
                    )
            );

            routeStep.polylineString = routeStepJsonObject.getJSONObject(
                    "polyline"
            ).getString("points");
        }
        catch (Exception exception)
        {
            LoggerUtility.logInformation(
                "Error: An exception occurred in the NavigationUtility.getRouteStepFromRouteStepJsonObject method."
            );

            LoggerUtility.logException(exception);
        }

        return routeStep;
    }

    private ArrayList<Route> getRoutesFromJSONString(String jsonString)
    {
        ArrayList<Route> routes = new ArrayList<>();

        JSONObject rootJSONObject = null;

        try
        {
            rootJSONObject = new JSONObject(jsonString);

            Iterator<String> rootKeys = rootJSONObject.keys();

            while (rootKeys.hasNext() == true)
            {
                String currentRootKey = rootKeys.next();

                if (currentRootKey.contains("data") == true)
                {
                    JSONObject currentDataJsonObject = rootJSONObject.getJSONObject(
                        currentRootKey
                    );

                    int currentDataDistance = currentDataJsonObject.getInt("distance");

                    int currentDataDuration = currentDataJsonObject.getInt("duration");

                    JSONArray routesJsonArray = currentDataJsonObject.getJSONArray("routes");

                    for (int currentRouteIndex = 0; currentRouteIndex < routesJsonArray.length(); currentRouteIndex++)
                    {
                        Route route = new Route();

                        route.distance = currentDataDistance;

                        route.duration = currentDataDuration;

                        JSONObject routeJsonObject = routesJsonArray.getJSONObject(
                            currentRouteIndex
                        );

                        route.overviewPolylinePointsString = routeJsonObject.getJSONObject(
                            "overview_polyline"
                        ).getString("points");

                        JSONArray routeLegsJsonArray = routeJsonObject.getJSONArray("legs");

                        for (int currentRouteLegIndex = 0; currentRouteLegIndex < routeLegsJsonArray.length(); currentRouteLegIndex++)
                        {
                            JSONArray routeLegStepsJsonArray = routeLegsJsonArray.getJSONObject(
                                currentRouteLegIndex
                            ).getJSONArray("steps");

                            for (int currentRouteLegStepIndex = 0; currentRouteLegStepIndex < routeLegStepsJsonArray.length(); currentRouteLegStepIndex++)
                            {
                                route.routeSteps.add(
                                    this.getRouteStepFromRouteStepJsonObject(
                                        routeLegStepsJsonArray.getJSONObject(
                                            currentRouteLegStepIndex
                                        )
                                    )
                                );
                            }
                        }

                        routes.add(route);
                    }
                }
            }
        }
        catch (Exception exception)
        {
            LoggerUtility.logException(exception);
        }

        return routes;
    }

    public void getRoutesAsync(LatLng originLatLng, LatLng destinationLatLng, TransportModes transportMode, RouteGeneratedListener routeGeneratedListener)
    {
        LoggerUtility.logInformation(
            "Origin latitude and longitude: " + originLatLng.latitude + "," + originLatLng.longitude
        );

        LoggerUtility.logInformation(
            "Destination latitude and longitude: " + destinationLatLng.latitude + "," + destinationLatLng.longitude
        );

        LoggerUtility.logInformation(
            "Mode of transport: " + StringUtility.instance.getAsCapitalizedString(transportMode.toString())
        );

        LoggerUtility.logInformation(
            "API key: " + TIH_API_KEY
        );

        HashMap<String, String> requestHeaders = new HashMap<>();

        requestHeaders.put(
            "X-API-Key",
            TIH_API_KEY
        );

        HashMap<String, String> requestQueryParameters = new HashMap<>();

        requestQueryParameters.put("origin", originLatLng.latitude + "," + originLatLng.longitude);

        requestQueryParameters.put(
            "destination",
            destinationLatLng.latitude + "," + destinationLatLng.longitude
        );

        HttpRequestUtility.instance.sendHttpRequestAsync(
            ROUTE_FINDER_RESOURCE_URL + transportMode.toString().toLowerCase(Locale.ROOT),
            "GET",
            requestHeaders,
            null,
            requestQueryParameters,
            new HttpRequestUtility.HttpResponseReceivedListener() {
                @Override
                public void onHttpResponseReceived(String responseBody) {
                    LoggerUtility.logInformation("Response received. Response body:");

                    LoggerUtility.logInformation(responseBody);

                    ArrayList<Route> routes = NavigationUtility.this.getRoutesFromJSONString(responseBody);

                    if (routeGeneratedListener != null)
                    {
                        routeGeneratedListener.onRouteGenerated(routes);
                    }
                }
            }
        );
    }

    public void getRoutesAsync(String originLocationName, String destinationLocationName, TransportModes transportMode, RouteGeneratedListener routeGeneratedListener, Context context)
    {
        ArrayList<LocationInfo> originLocationInfoArrayList = LocationInfoUtility.getCorrespondingLocationsForLocationName(
            originLocationName,
            context
        );

        if (originLocationInfoArrayList.size() == 0)
        {
            return;
        }

        ArrayList<LocationInfo> destinationLocationInfoArrayList = LocationInfoUtility.getCorrespondingLocationsForLocationName(
            destinationLocationName,
            context
        );

        if (destinationLocationInfoArrayList.size() == 0)
        {
            return;
        }

        this.getRoutesAsync(
            originLocationInfoArrayList.get(0).latLng,
            destinationLocationInfoArrayList.get(0).latLng,
            transportMode,
            routeGeneratedListener
        );
    }

    public TransportModes getAsTransportModesEnum(String string)
    {
        TransportModes[] transportModes = TransportModes.values();

        string = string.toUpperCase();

        for (int currentIndex = 0; currentIndex < transportModes.length; currentIndex++)
        {
            if (transportModes[currentIndex].toString().equals(string) == true)
            {
                return transportModes[currentIndex];
            }
        }

        return TransportModes.DEFAULT;
    }

    /**
     * An enum containing the different modes of travel/transport, which will need to be taken into
     * account when finding a route between an origin and destination location.
     * <br>
     * <br>
     * Note: The default transport mode is driving. Transit refers to public transport.
     */
    public enum TransportModes
    {
        DEFAULT,
        BICYCLING,
        DRIVING,
        TRANSIT,
        WALKING
    }

    public interface RouteGeneratedListener
    {
        void onRouteGenerated(ArrayList<Route> routes);
    }

    public class Route
    {
        public ArrayList<RouteStep> routeSteps = new ArrayList<>();

        public String overviewPolylinePointsString = "";

        public int distance = 0;

        public int duration = 0;

        public Route()
        {

        }

        @Override
        public String toString()
        {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(
                "Displaying information for route..." + StringUtility.LINE_SEPARATOR
            );

            stringBuilder.append(
                "Route distance: " + this.distance + StringUtility.LINE_SEPARATOR
            );

            stringBuilder.append(
                "Route duration: " + this.duration + StringUtility.LINE_SEPARATOR
            );

            stringBuilder.append(
                "Route overview polyline: " + this.overviewPolylinePointsString + StringUtility.LINE_SEPARATOR
            );

            if (this.routeSteps.size() > 0)
            {
                stringBuilder.append(
                        "List of steps for route:" + StringUtility.LINE_SEPARATOR
                );

                for (int currentRouteStepIndex = 0; currentRouteStepIndex < this.routeSteps.size(); currentRouteStepIndex++)
                {
                    stringBuilder.append(
                            this.routeSteps.get(currentRouteStepIndex).toString()
                    );
                }
            }
            else
            {
                stringBuilder.append("No steps found for route." + StringUtility.LINE_SEPARATOR);
            }

            return stringBuilder.toString();
        }

        public void log()
        {
            String[] toStringComponents = this.toString().split(StringUtility.LINE_SEPARATOR);

            for (int currentComponentIndex = 0; currentComponentIndex < toStringComponents.length; currentComponentIndex++)
            {
                LoggerUtility.logInformation(toStringComponents[currentComponentIndex]);
            }
        }
    }

    public class RouteStep
    {
        public int distance = 0;

        public int duration = 0;

        public LatLng startLocationLatLng = new LatLng(0, 0);

        public LatLng endLocationLatLng = new LatLng(0, 0);

        public String instruction = "";

        public TransportModes travelMode = TransportModes.DEFAULT;

        public String polylineString = "";

        public ArrayList<RouteStep> routeSteps = new ArrayList<>();

        public RouteStep()
        {

        }

        @Override
        public String toString()
        {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(
                "Displaying information for route step..." + StringUtility.LINE_SEPARATOR
            );

            stringBuilder.append(
                "Distance: " + this.distance + StringUtility.LINE_SEPARATOR
            );

            stringBuilder.append(
                "Duration: " + this.duration + StringUtility.LINE_SEPARATOR
            );

            stringBuilder.append(
                "Start location latitude: " + this.startLocationLatLng.latitude + StringUtility.LINE_SEPARATOR
            );

            stringBuilder.append(
                "Start location longitude: " + this.startLocationLatLng.longitude + StringUtility.LINE_SEPARATOR
            );

            stringBuilder.append(
                "End location latitude: " + this.endLocationLatLng.latitude + StringUtility.LINE_SEPARATOR
            );

            stringBuilder.append(
                "End location longitude: " + this.endLocationLatLng.longitude + StringUtility.LINE_SEPARATOR
            );

            stringBuilder.append(
                "Instruction: " + this.instruction + StringUtility.LINE_SEPARATOR
            );

            stringBuilder.append(
                "Travel mode: " + this.travelMode + StringUtility.LINE_SEPARATOR
            );

            stringBuilder.append(
                "Polyline: " + this.polylineString + StringUtility.LINE_SEPARATOR
            );

            if (this.routeSteps.size() > 0)
            {
                stringBuilder.append(
                    "List of sub steps:" + StringUtility.LINE_SEPARATOR
                );

                for (int currentRouteSubStepIndex = 0; currentRouteSubStepIndex < this.routeSteps.size(); currentRouteSubStepIndex++)
                {
                    stringBuilder.append(
                        this.routeSteps.get(currentRouteSubStepIndex).toString()
                    );
                }
            }
            else
            {
                stringBuilder.append(
                    "No sub steps found." + StringUtility.LINE_SEPARATOR
                );
            }

            return stringBuilder.toString();
        }

        public void log()
        {
            String[] toStringComponents = this.toString().split(StringUtility.LINE_SEPARATOR);

            for (int currentComponentIndex = 0; currentComponentIndex < toStringComponents.length; currentComponentIndex++)
            {
                LoggerUtility.logInformation(toStringComponents[currentComponentIndex]);
            }
        }
    }
}
