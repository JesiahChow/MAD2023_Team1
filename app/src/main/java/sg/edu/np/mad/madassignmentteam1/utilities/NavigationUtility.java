package sg.edu.np.mad.madassignmentteam1.utilities;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;

import androidx.core.text.HtmlCompat;

import sg.edu.np.mad.madassignmentteam1.LocationInfo;

public class NavigationUtility
{
    private static final String ROUTE_FINDER_RESOURCE_URL = "https://api.stb.gov.sg/services/navigation/v2/experiential-route/";

    // TODO: Move the API key to the AndroidManifest.xml file to be fetched from there at runtime.
    private static final String TIH_API_KEY = "ikZE4eCj4SNqUVtit6CL2R7NDcsT0rA6";

    private static final ArrayList<Route> EMPTY_ROUTE_ARRAY_LIST = new ArrayList<>();

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

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

            routeStep.duration = Math.round(
                routeStepJsonObject.getInt(
                    "duration"
                ) / 60.0f
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

            if (routeStep.instruction.length() > 0)
            {
                int instructionSubStringEndIndex = routeStep.instruction.indexOf("<div");

                if (instructionSubStringEndIndex != -1)
                {
                    routeStep.instruction = routeStep.instruction.substring(
                        0,
                        instructionSubStringEndIndex
                    );
                }
            }

            /*
            LoggerUtility.logInformation(
                "htmlInstructions value: " + routeStepJsonObject.getString(
                    "htmlInstructions"
                )
            );
            */

            routeStep.travelMode = this.getAsTransportModesEnum(
                    routeStepJsonObject.getString(
                        "travelMode"
                    )
            );

            routeStep.polylineString = routeStepJsonObject.getJSONObject(
                    "polyline"
            ).getString("points");

            /*
            routeStep.startAddress = routeStepJsonObject.getString("startAddress");

            routeStep.endAddress = routeStepJsonObject.getString("endAddress");
            */
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

    private ArrayList<Route> getRoutesFromJSONString(String jsonString, String originLocationName, String destinationLocationName)
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

                        route.startLocationName = originLocationName;

                        route.endLocationName = destinationLocationName;

                        route.distance = currentDataDistance;

                        route.duration = Math.round(currentDataDuration / 60.0f);

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

    public void getRoutesAsync(String originLocationName, String destinationLocationName, TransportModes transportMode, RoutesFoundListener routesFoundListener, Context context)
    {
        ArrayList<LocationInfo> originLocationInfoArrayList = LocationInfoUtility.getCorrespondingLocationsForLocationName(
            originLocationName,
            context
        );

        if (originLocationInfoArrayList.size() == 0)
        {
            routesFoundListener.onRoutesFound(EMPTY_ROUTE_ARRAY_LIST);

            routesFoundListener.onRoutesFoundMainThread(EMPTY_ROUTE_ARRAY_LIST);

            return;
        }

        ArrayList<LocationInfo> destinationLocationInfoArrayList = LocationInfoUtility.getCorrespondingLocationsForLocationName(
            destinationLocationName,
            context
        );

        if (destinationLocationInfoArrayList.size() == 0)
        {
            routesFoundListener.onRoutesFound(EMPTY_ROUTE_ARRAY_LIST);

            routesFoundListener.onRoutesFoundMainThread(EMPTY_ROUTE_ARRAY_LIST);

            return;
        }

        LatLng originLatLng = originLocationInfoArrayList.get(0).latLng;

        LatLng destinationLatLng = destinationLocationInfoArrayList.get(0).latLng;

        /*
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
        */

        this.executorService.execute(
            new RouteFinderHttpRequestTask(
                originLocationName,
                destinationLocationName,
                originLatLng,
                destinationLatLng,
                transportMode,
                routesFoundListener
            )
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

    public class Route
    {
        public ArrayList<RouteStep> routeSteps = new ArrayList<>();

        public String overviewPolylinePointsString = "";

        public int distance = 0;

        public int duration = 0;

        public String startLocationName = "[Start location name]";

        public String endLocationName = "[End location name]";

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

            stringBuilder.append(
                "Start location name: " + this.startLocationName
            );

            stringBuilder.append(
                "End location name: " + this.endLocationName
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

        public String startAddress = "[Start address]";

        public String endAddress = "[End address]";

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

            stringBuilder.append(
                "Start address: " + this.startAddress
            );

            stringBuilder.append(
                "End address: " + this.endAddress
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

    private class RouteFinderHttpRequestTask implements Runnable
    {
        private String currentOriginLocationName = "[Origin location name]";

        private String currentDestinationLocationName = "[Destination location name]";

        private LatLng currentOriginLocationLatLng = new LatLng(0, 0);

        private LatLng currentDestinationLocationLatLng = new LatLng(0, 0);

        private TransportModes currentTransportMode = TransportModes.DEFAULT;

        private RoutesFoundListener currentRoutesFoundListener = null;

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        public RouteFinderHttpRequestTask(String originLocationName, String destinationLocationName, LatLng originLocationLatLng, LatLng destinationLocationLatLng, TransportModes transportMode, RoutesFoundListener routesFoundListener)
        {
            this.currentOriginLocationName = originLocationName;

            this.currentDestinationLocationName = destinationLocationName;

            this.currentOriginLocationLatLng = originLocationLatLng;

            this.currentDestinationLocationLatLng = destinationLocationLatLng;

            this.currentTransportMode = transportMode;

            this.currentRoutesFoundListener = routesFoundListener;
        }

        @Override
        public void run()
        {
            HashMap<String, String> requestHeaders = new HashMap<>();

            requestHeaders.put(
                    "X-API-Key",
                    TIH_API_KEY
            );

            HashMap<String, String> requestQueryParameters = new HashMap<>();

            requestQueryParameters.put("origin", this.currentOriginLocationLatLng.latitude + "," + this.currentOriginLocationLatLng.longitude);

            requestQueryParameters.put(
                    "destination",
                    this.currentDestinationLocationLatLng.latitude + "," + this.currentDestinationLocationLatLng.longitude
            );

            String responseBody = HttpRequestUtility.instance.sendHttpRequest(
                ROUTE_FINDER_RESOURCE_URL + this.currentTransportMode.toString().toLowerCase(),
                "GET",
                requestHeaders,
                requestQueryParameters,
                null
            );

            ArrayList<Route> routes = NavigationUtility.this.getRoutesFromJSONString(
                responseBody,
                this.currentOriginLocationName,
                this.currentDestinationLocationName
            );

            if (this.currentRoutesFoundListener != null)
            {
                this.currentRoutesFoundListener.onRoutesFound(routes);
            }

            this.mainThreadHandler.post(
                new RoutesFoundMainThreadHandlerTask(
                    this.currentRoutesFoundListener,
                    routes
                )
            );
        }
    }

    private class RoutesFoundMainThreadHandlerTask implements Runnable
    {
        private RoutesFoundListener currentRoutesFoundListener = null;

        private ArrayList<Route> currentFoundRoutes = new ArrayList<>();

        public RoutesFoundMainThreadHandlerTask(RoutesFoundListener routesFoundListener, ArrayList<Route> foundRoutes)
        {
            this.currentRoutesFoundListener = routesFoundListener;

            this.currentFoundRoutes = foundRoutes;
        }

        @Override
        public void run()
        {
            if (this.currentRoutesFoundListener != null)
            {
                this.currentRoutesFoundListener.onRoutesFoundMainThread(this.currentFoundRoutes);
            }
        }
    }

    public interface RoutesFoundListener
    {
        void onRoutesFound(ArrayList<Route> routes);

        void onRoutesFoundMainThread(ArrayList<Route> routes);
    }
}
