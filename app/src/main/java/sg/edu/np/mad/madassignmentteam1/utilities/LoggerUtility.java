package sg.edu.np.mad.madassignmentteam1.utilities;

import android.util.Log;

public class LoggerUtility
{
    private static String applicationName = "";

    public static void logInformation(String msg)
    {
        Log.i(
            applicationName,
            msg
        );
    }

    public static void logException(Exception exception)
    {
        Log.e(
            applicationName,
            "Exception thrown. Exception message: " + exception.getMessage()
        );
    }

    public static void logObjectAsStringSafe(Object obj)
    {
        if (obj == null)
        {
            logInformation("null");
        }
        else
        {
            logInformation(obj.toString());
        }
    }
}
