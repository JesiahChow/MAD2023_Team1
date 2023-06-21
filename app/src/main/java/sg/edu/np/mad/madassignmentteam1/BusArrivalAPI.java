package sg.edu.np.mad.madassignmentteam1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BusArrivalAPI {
    private static final String API_URL = "http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2";

    public String getBusArrivalData() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("AccountKey", "ZLwg+friTO+5ltLR6fIoeQ==");
            connection.setRequestProperty("accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                // Parse the JSON response into BusArrivalResponse object using Gson
                Gson gson = new GsonBuilder().create();
                BusArrivalResponse busArrivalResponse = gson.fromJson(response.toString(), BusArrivalResponse.class);
                return busArrivalResponse;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}