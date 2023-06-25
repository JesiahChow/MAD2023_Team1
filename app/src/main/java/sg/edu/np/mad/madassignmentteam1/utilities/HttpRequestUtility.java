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
     * Sends a HTTP/HTTPS request using the specified URL, method (e.g. GET, POST etc), and request
     * data. Returns the response body of the response received as a result of sending the request
     * as a raw String.
     * @param url
     * @param method
     * @param requestData
     * @return The response body as a raw String.
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

    public static void SendHttpRequest(String url, String method)
    {
        SendHttpRequest(url, method, "");
    }
}
