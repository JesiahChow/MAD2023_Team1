package sg.edu.np.mad.madassignmentteam1.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.Dictionary;
import java.util.Enumeration;

/**
 * This class provides various helper methods to make working with the Google Maps Embed API
 * more convenient. Due to the current implementation of various map related features using a
 * different API from Google, this class is no longer supported and might possibly be removed
 * soon if no need for it arises.
 */
public class GoogleMapsEmbedAPIUtility
{
    private static String googleMapsAPIKey = "";

    /**
     * Performs the initialization process required for the other methods of this class
     * to work as intended without errors. This method should be called once before any calls to other
     * methods of this class are made.
     * @param context
     */
    public static void Init(Context context)
    {
        try
        {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(),
                    PackageManager.GET_META_DATA
            );

            googleMapsAPIKey = applicationInfo.metaData.getString("GOOGLE_MAPS_API_KEY");
        }
        catch (Exception exception)
        {
            LoggerUtility.logException(exception);
        }
    }

    /**
     * Generates the URL to be used when making a request to the Google Maps Embed API
     * to display a map.
     * @param mapMode
     * @param parameters
     * @return A String instance.
     */
    public static String generateGoogleMapEmbedAPIURL(String mapMode, String parameters)
    {
        return "https://www.google.com/maps/embed/v1/" + mapMode + "?key=" + googleMapsAPIKey + parameters;
    }
}
