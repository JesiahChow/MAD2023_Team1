package sg.edu.np.mad.madassignmentteam1.utilities;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpRequestUtility
{
    /**
     * Reads all characters from an InputStream object and returns the result of doing so as a String
     * object. This method is solely meant to be used within the HttpRequestUtility class for now.
     * @param inputStream
     * @return A String object.
     */
    private static String ReadStream(InputStream inputStream)
    {
        String text = "";

        String currentLine = "";

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        try
        {
            while ((currentLine = bufferedReader.readLine()) != null)
            {
                text += currentLine;
            }
        }
        catch (Exception exception)
        {
            LoggerUtility.logException(exception);
        }

        return text;
    }


    /**
     * Sends a HTTP/HTTPS request using the specified URL, method (e.g. GET, POST etc) and request
     * data. Returns the response body of the response received as a String object.
     * @param url
     * @param method
     * @param requestData
     * @return A String object.
     */
    public static String SendHttpRequest(String url, String method, String requestData)
    {
        URL urlObj = null;

        try
        {
            urlObj = new URL(url);
        }
        catch (Exception exception)
        {
            LoggerUtility.logException(exception);
        }

        HttpURLConnection httpURLConnection = null;

        try
        {
            httpURLConnection = (HttpURLConnection)urlObj.openConnection();
        }
        catch (Exception exception)
        {
            LoggerUtility.logException(exception);
        }

        InputStream inputStream = null;

        String responseText = "";

        try
        {
            inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

            responseText = ReadStream(inputStream);
        }
        catch (Exception exception)
        {
            LoggerUtility.logException(exception);
        }

        httpURLConnection.disconnect();

        return responseText;
    }

    /**
     * Sends a HTTP/HTTPS request using the specified URL and method (e.g. GET, POST etc).
     * Returns the response body of the response received as a String object.
     * @param url
     * @param method
     * @return A String object.
     */
    public static String SendHttpRequest(String url, String method)
    {
        return SendHttpRequest(url, method, "");
    }
}
