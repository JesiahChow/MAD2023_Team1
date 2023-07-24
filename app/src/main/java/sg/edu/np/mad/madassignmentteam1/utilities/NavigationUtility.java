package sg.edu.np.mad.madassignmentteam1.utilities;

import com.google.android.gms.maps.model.LatLng;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NavigationUtility
{
    private static final String ROUTE_FINDER_RESOURCE_URL = "https://api.stb.gov.sg/services/navigation/v2/experiential-route/";

    private static final String TIH_API_KEY = "ikZE4eCj4SNqUVtit6CL2R7NDcsT0rA6";

    public static NavigationUtility instance = new NavigationUtility();

    private NavigationUtility()
    {

    }

    public void GetRoute(LatLng originLatLng, LatLng destinationLatLng, TransportModes transportMode)
    {
        HashMap<String, String> requestQueryParameters = new HashMap<>();

        requestQueryParameters.put("origin", originLatLng.latitude + "," + originLatLng.longitude);

        requestQueryParameters.put(
            "destination",
            destinationLatLng.latitude + "," + destinationLatLng.longitude
        );

        HttpRequestUtility.instance.SendHttpRequest(
                ROUTE_FINDER_RESOURCE_URL + transportMode.toString().toLowerCase(Locale.ROOT),
                "GET",
                requestQueryParameters
        );
    }

    /**
     * An enum containing the different modes of travel/transport, which will need to be taken into
     * account when finding a route between an origin and destination location.
     * <br>
     * <br>
     * Note: The default transport mode is driving. Transit refers to public transport.
     */
    enum TransportModes
    {
        DEFAULT,
        BICYCLING,
        DRIVING,
        TRANSIT,
        WALKING
    }
}
