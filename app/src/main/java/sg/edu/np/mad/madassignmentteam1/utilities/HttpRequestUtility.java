package sg.edu.np.mad.madassignmentteam1.utilities;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
     * headers, request data and query parameters. This method is executed asynchronously so as to
     * avoid slowing down the main thread. The onHttpResponseReceived method of the
     * httpResponseReceivedMainThreadListener will be executed on the main thread once a HTTP/HTTPS
     * response has been received, this listener can contain code that manipulates the UI. The
     * onHttpResponseReceived method of the httpResponseReceivedWorkerThreadListener will be executed
     * on the worker thread (a separate thread from the main thread which is responsible for sending
     * the HTTP/HTTPS request) once a HTTP/HTTPS response has been received, this listener cannot
     * contain code that manipulates the UI as it will cause the application to throw an exception
     * and crash since the UI can only be manipulated in the main thread.
     * @param url
     * @param method
     * @param requestHeaders
     * @param requestData
     * @param queryParameters
     * @param httpResponseReceivedMainThreadListener
     * @param httpResponseReceivedWorkerThreadListener
     */
    public void sendHttpRequestAsync(String url, String method, Map<String, String> requestHeaders, Map<String, String> requestData, Map<String, String> queryParameters, HttpResponseReceivedMainThreadListener httpResponseReceivedMainThreadListener, HttpResponseReceivedWorkerThreadListener httpResponseReceivedWorkerThreadListener)
    {
        this.httpRequestExecutorService.execute(
            new HttpRequestTask(
                url,
                method,
                requestHeaders,
                requestData,
                queryParameters,
                httpResponseReceivedMainThreadListener,
                httpResponseReceivedWorkerThreadListener
            )
        );
    }

    /**
     * Sends a HTTP/HTTPS response. This method should not be run on the main thread so as to prevent
     * exceptions from being thrown and unintended behaviour from occurring.
     * @return The body of the response received as a result of sending the HTTP/HTTPS request.
     */
    public String sendHttpRequest(String url, String method, Map<String, String> requestHeaders, Map<String, String> requestQueryParameters, Map<String, String> requestBody)
    {
        URL urlObj = null;

        try
        {
            Uri.Builder uriBuilder = Uri.parse(url).buildUpon();

            if (requestQueryParameters != null)
            {
                ArrayList<String> queryParameterMapKeys = new ArrayList<>(
                    requestQueryParameters.keySet()
                );

                for (int currentIndex = 0; currentIndex < queryParameterMapKeys.size(); currentIndex++)
                {
                    String currentKey = queryParameterMapKeys.get(currentIndex);

                    uriBuilder.appendQueryParameter(
                        currentKey,
                        requestQueryParameters.get(currentKey)
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

            if (requestHeaders != null)
            {
                ArrayList<String> requestHeaderKeys = new ArrayList<>(
                    requestHeaders.keySet()
                );

                for (int currentKeyIndex = 0; currentKeyIndex < requestHeaderKeys.size(); currentKeyIndex++)
                {
                    String currentKey = requestHeaderKeys.get(currentKeyIndex);

                    httpURLConnection.setRequestProperty(
                        currentKey,
                        requestHeaders.get(currentKey)
                    );
                }
            }

            if (requestBody != null && method.equals("GET") == false)
            {
                OutputStream outputStream = new BufferedOutputStream(
                    httpURLConnection.getOutputStream()
                );

                BufferedWriter bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(outputStream, "UTF-8")
                );

                bufferedWriter.write(
                    new JSONObject(requestBody).toString()
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

        return responseText;
    }

    private class HttpRequestTask implements Runnable
    {
        private String currentUrl = "";

        private String currentMethod = "";

        private Map<String, String> currentRequestHeaders = null;

        private Map<String, String> currentRequestData = null;

        private Map<String, String> currentQueryParameters = null;

        private HttpResponseReceivedMainThreadListener currentHttpResponseReceivedMainThreadListener = null;

        private HttpResponseReceivedWorkerThreadListener currentHttpResponseReceivedWorkerThreadListener = null;

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        protected void executeOnMainThread(Runnable runnable)
        {
            this.mainThreadHandler.post(
                runnable
            );
        }

        public HttpRequestTask(String url, String method, Map<String, String> requestHeaders, Map<String, String> requestData, Map<String, String> queryParameters, HttpResponseReceivedMainThreadListener httpResponseReceivedMainThreadListener, HttpResponseReceivedWorkerThreadListener httpResponseReceivedWorkerThreadListener)
        {
            this.currentUrl = url;

            this.currentMethod = method;

            this.currentRequestHeaders = requestHeaders;

            this.currentRequestData = requestData;

            this.currentQueryParameters = queryParameters;

            this.currentHttpResponseReceivedMainThreadListener = httpResponseReceivedMainThreadListener;

            this.currentHttpResponseReceivedWorkerThreadListener = httpResponseReceivedWorkerThreadListener;
        }

        @Override
        public void run()
        {
            String responseText = HttpRequestUtility.this.sendHttpRequest(
                this.currentUrl,
                this.currentMethod,
                this.currentRequestHeaders,
                this.currentQueryParameters,
                this.currentRequestData
            );

            if (this.currentHttpResponseReceivedWorkerThreadListener != null)
            {
                this.currentHttpResponseReceivedWorkerThreadListener.onHttpResponseReceived(responseText);
            }

            this.executeOnMainThread(
                new HttpResponseMainThreadHandlerTask(
                    this.currentHttpResponseReceivedMainThreadListener,
                    responseText
                )
            );
        }
    }

    private class HttpResponseMainThreadHandlerTask implements Runnable
    {
        private HttpResponseReceivedMainThreadListener currentHttpResponseReceivedMainThreadListener = null;

        private String currentResponseBody = "";

        public HttpResponseMainThreadHandlerTask(HttpResponseReceivedMainThreadListener httpResponseReceivedMainThreadListener, String responseBody)
        {
            this.currentHttpResponseReceivedMainThreadListener = httpResponseReceivedMainThreadListener;

            this.currentResponseBody = responseBody;
        }

        @Override
        public void run()
        {
            if (this.currentHttpResponseReceivedMainThreadListener != null)
            {
                this.currentHttpResponseReceivedMainThreadListener.onHttpResponseReceived(
                    this.currentResponseBody
                );
            }
        }
    }

    /**
     * This interface contains methods that will be called on the main thread once a HTTP/HTTPS
     * response has been received.
     */
    public interface HttpResponseReceivedMainThreadListener
    {
        void onHttpResponseReceived(String responseBody);
    }

    /**
     * This interface contains methods that will be called on a separate thread from the main thread
     * (referred to as a worker thread in this case) once a HTTP/HTTPS response has been received.
     */
    public interface HttpResponseReceivedWorkerThreadListener
    {
        void onHttpResponseReceived(String responseBody);
    }
}
