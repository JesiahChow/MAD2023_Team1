package sg.edu.np.mad.madassignmentteam1.utilities;

import android.util.Log;

/**
 * A class providing helper functions for logging information or exceptions that might be thrown
 * in the application.
 */
public class LoggerUtility
{
    private static String applicationName = "";

    /**
     * Logs a message.
     * @param msg
     */
    public static void logInformation(String msg)
    {
        Log.i(
            applicationName,
            msg
        );
    }

    /**
     * Logs a message stating that an exception has occurred and indicating the message
     * associated with the exception.
     * @param exception
     */
    public static void logException(Exception exception)
    {
        Log.e(
            applicationName,
            "Exception thrown. Exception message: " + exception.getMessage()
        );
    }

    /**
     * Logs the value returned by the toString method of an object. If the object is null,
     * a message indicating that the object is null is logged instead.
     * @param obj
     */
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
