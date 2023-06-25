package sg.edu.np.mad.madassignmentteam1.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.Dictionary;
import java.util.Enumeration;

public class GoogleMapsEmbedAPIUtility
{
    private static String googleMapsAPIKey = "";

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

    public static String generateGoogleMapEmbedAPIURL(String mapMode, String parameters)
    {
        return "https://www.google.com/maps/embed/v1/" + mapMode + "?key=" + googleMapsAPIKey + parameters;
    }
}
