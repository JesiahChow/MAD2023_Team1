package sg.edu.np.mad.madassignmentteam1.utilities;

import android.net.Uri;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

public class HttpRequestUtility
{
    public static HttpRequestUtility instance = new HttpRequestUtility();

    /**
     * Reads all characters from an InputStream object and returns the result of doing so as a String
     * object. This method is solely meant to be used within the HttpRequestUtility class for now.
     * @param inputStream
     * @return A String object.
     */
    private String ReadStream(InputStream inputStream)
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
     * @param queryParameters
     * @return A String object.
     */
    public String SendHttpRequest(String url, String method, Map<String, String> requestData, Map<String, String> queryParameters)
    {
        URL urlObj = null;

        try
        {
            Uri.Builder uriBuilder = Uri.parse(url).buildUpon();

            if (queryParameters != null)
            {
                ArrayList<String> queryParameterMapKeys = new ArrayList<>(
                    queryParameters.keySet()
                );

                for (int currentIndex = 0; currentIndex < queryParameterMapKeys.size(); currentIndex++)
                {
                    String currentKey = queryParameterMapKeys.get(currentIndex);

                    uriBuilder.appendQueryParameter(
                            currentKey,
                            queryParameters.get(currentKey)
                    );
                }
            }

            urlObj = new URL(uriBuilder.build().toString());
        }
        catch (Exception exception)
        {
            LoggerUtility.logException(exception);
        }

        HttpURLConnection httpURLConnection = null;

        try
        {
            httpURLConnection = (HttpURLConnection)urlObj.openConnection();

            httpURLConnection.setRequestMethod(method);

            if (requestData != null && method.equals("GET") == false)
            {
                OutputStream outputStream = new BufferedOutputStream(
                    httpURLConnection.getOutputStream()
                );

                BufferedWriter bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(outputStream, "UTF-8")
                );

                bufferedWriter.write(
                    new JSONObject(requestData).toString()
                );

                bufferedWriter.flush();

                bufferedWriter.close();

                outputStream.close();
            }

            httpURLConnection.connect();
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
    public String SendHttpRequest(String url, String method)
    {
        return this.SendHttpRequest(url, method, null, null);
    }

    /**
     * Sends a HTTP/HTTPS request using the specified URL, method (e.g. GET, POST etc) and
     * query parameters.
     * Returns the response body of the response received as a String object.
     * @param url
     * @param method
     * @param queryParameters
     * @return A String object.
     */
    public String SendHttpRequest(String url, String method, Map<String, String> queryParameters)
    {
        return this.SendHttpRequest(url, method, null, queryParameters);
    }
}
