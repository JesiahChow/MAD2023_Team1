package sg.edu.np.mad.madassignmentteam1.utilities;

import android.net.Uri;
import android.os.AsyncTask;
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
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpRequestUtility
{
    private ExecutorService httpRequestExecutorService = Executors.newSingleThreadExecutor();

    public static final HttpRequestUtility instance = new HttpRequestUtility();

    /**
     * Reads all characters from an InputStream object and returns the result of doing so as a String
     * object. This method is solely meant to be used within the HttpRequestUtility class for now.
     * @param inputStream
     * @return A String object.
     */
    private String readStream(InputStream inputStream)
    {
        String text = "";

        String currentLine = "";

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        try
        {
            currentLine = bufferedReader.readLine();

            while (currentLine != null)
            {
                text += currentLine;

                currentLine = bufferedReader.readLine();
            }
        }
        catch (Exception exception)
        {
            LoggerUtility.logException(exception);
        }

        return text;
    }

    /**
     * Sends a HTTP/HTTPS request using the specified URL, request method (e.g. GET, POST etc), request
     * headers, request data and query parameters. The onHttpResponseReceived method of the
     * httpResponseReceivedListener parameter will then be called once a HTTP/HTTPS response has been
     * received. This method is executed asynchronously so as to avoid slowing down the main thread.
     * @param url
     * @param method
     * @param requestHeaders
     * @param requestData
     * @param queryParameters
     * @param httpResponseReceivedListener
     */
    public void sendHttpRequestAsync(String url, String method, Map<String, String> requestHeaders, Map<String, String> requestData, Map<String, String> queryParameters, HttpResponseReceivedListener httpResponseReceivedListener)
    {
        this.httpRequestExecutorService.execute(
            new HttpRequestRunnable(
                url,
                method,
                requestHeaders,
                requestData,
                queryParameters,
                httpResponseReceivedListener
            )
        );
    }

    private class HttpRequestRunnable implements Runnable
    {
        private String currentUrl = "";

        private String currentMethod = "";

        private Map<String, String> currentRequestHeaders = null;

        private Map<String, String> currentRequestData = null;

        private Map<String, String> currentQueryParameters = null;

        private HttpResponseReceivedListener currentHttpResponseReceivedListener = null;

        public HttpRequestRunnable(String url, String method, Map<String, String> requestHeaders, Map<String, String> requestData, Map<String, String> queryParameters, HttpResponseReceivedListener httpResponseReceivedListener)
        {
            this.currentUrl = url;

            this.currentMethod = method;

            this.currentRequestHeaders = requestHeaders;

            this.currentRequestData = requestData;

            this.currentQueryParameters = queryParameters;

            this.currentHttpResponseReceivedListener = httpResponseReceivedListener;
        }

        @Override
        public void run()
        {
            URL urlObj = null;

            try
            {
                Uri.Builder uriBuilder = Uri.parse(this.currentUrl).buildUpon();

                if (this.currentQueryParameters != null)
                {
                    ArrayList<String> queryParameterMapKeys = new ArrayList<>(
                            this.currentQueryParameters.keySet()
                    );

                    for (int currentIndex = 0; currentIndex < queryParameterMapKeys.size(); currentIndex++)
                    {
                        String currentKey = queryParameterMapKeys.get(currentIndex);

                        uriBuilder.appendQueryParameter(
                                currentKey,
                                this.currentQueryParameters.get(currentKey)
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

                httpURLConnection.setRequestMethod(this.currentMethod);

                if (this.currentRequestHeaders != null)
                {
                    ArrayList<String> requestHeaderKeys = new ArrayList<>(
                        this.currentRequestHeaders.keySet()
                    );

                    for (int currentKeyIndex = 0; currentKeyIndex < requestHeaderKeys.size(); currentKeyIndex++)
                    {
                        String currentKey = requestHeaderKeys.get(currentKeyIndex);

                        httpURLConnection.setRequestProperty(
                            currentKey,
                            this.currentRequestHeaders.get(currentKey)
                        );
                    }
                }

                if (this.currentRequestData != null && this.currentMethod.equals("GET") == false)
                {
                    OutputStream outputStream = new BufferedOutputStream(
                            httpURLConnection.getOutputStream()
                    );

                    BufferedWriter bufferedWriter = new BufferedWriter(
                            new OutputStreamWriter(outputStream, "UTF-8")
                    );

                    bufferedWriter.write(
                            new JSONObject(this.currentRequestData).toString()
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

                responseText = HttpRequestUtility.this.readStream(inputStream);
            }
            catch (Exception exception)
            {
                LoggerUtility.logException(exception);
            }

            httpURLConnection.disconnect();

            if (this.currentHttpResponseReceivedListener != null)
            {
                this.currentHttpResponseReceivedListener.onHttpResponseReceived(responseText);
            }
        }
    }

    public interface HttpResponseReceivedListener
    {
        void onHttpResponseReceived(String responseBody);
    }
}
