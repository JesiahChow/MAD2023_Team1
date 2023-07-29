package sg.edu.np.mad.madassignmentteam1;

import static android.content.ContentValues.TAG;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BusAPI {


    private static final String Bus_Stop_API_URL = "http://datamall2.mytransport.sg/ltaodataservice/BusStops";



    public List<BusModel> getbus() {
        List<BusModel> busModelList = new ArrayList<>();

        try {
            // Connecting to Bus Stop API URL
            URL url = new URL(Bus_Stop_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("AccountKey", "ZLwg+friTO+5ltLR6fIoeQ==");
            connection.setRequestProperty("accept", "application/json");

            // Getting response code from API
            int responseCode = connection.getResponseCode();
            Log.i("responseCode", String.valueOf(responseCode));
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Reading JSON response from API
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                // Parsing JSON response
                JSONObject jsonObject = new JSONObject(response.toString());
                JSONArray itemsArray = jsonObject.getJSONArray("value");
                // Loop through the array of bus stops and retrieve their information
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject itemObject = itemsArray.getJSONObject(i);
                    String BusStopCode = itemObject.getString("BusStopCode");
                    String RoadName = itemObject.get("RoadName").toString();
                    List<BusStopModel> busstopdataList = new ArrayList<>();
                    List<BusArrivalModel> busArrivalModelList = new ArrayList<>();
                    //Adding object to array
                    busstopdataList.add(new BusStopModel(BusStopCode,RoadName));

                    // Retrieve Bus Arrival details for the current bus stop

                    URL urlbusArriavl = new URL("http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2?BusStopCode="+BusStopCode);
                    HttpURLConnection connection1 = (HttpURLConnection) urlbusArriavl.openConnection();
                    connection1.setRequestMethod("GET");
                    connection1.setRequestProperty("AccountKey", "ZLwg+friTO+5ltLR6fIoeQ==");
                    connection1.setRequestProperty("accept", "application/json");
                    int responseCode1 = connection1.getResponseCode();

                    if (responseCode1 == HttpURLConnection.HTTP_OK) {
                        BufferedReader in1 = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
                        String inputLine1;
                        StringBuilder response1 = new StringBuilder();

                        while ((inputLine1 = in1.readLine()) != null) {
                            response1.append(inputLine1);
                        }

                        in.close();
                        JSONObject jsonObject1 = new JSONObject(response1.toString());
                        JSONArray itemsArray1 = jsonObject1.getJSONArray("Services");

                        // Loop through the bus arrival details and retrieve relevant information
                        for (int j = 0; j < itemsArray1.length(); j++) {
                            JSONObject itemObject1 = itemsArray1.getJSONObject(j);
                            String ServicNo = itemObject1.getString("ServiceNo");
                            JSONObject itemsArray2 = itemObject1.getJSONObject("NextBus");
                            String EstimatedArrival1 = itemsArray2.get("EstimatedArrival").toString();
                            JSONObject itemsArray3 = itemObject1.getJSONObject("NextBus2");
                            String EstimatedArrival2 = itemsArray3.get("EstimatedArrival").toString();
                            JSONObject itemsArray4 = itemObject1.getJSONObject("NextBus3");
                            String EstimatedArrival3 = itemsArray4.get("EstimatedArrival").toString();

                            busArrivalModelList.add(new BusArrivalModel(ServicNo,EstimatedArrival1,EstimatedArrival2,EstimatedArrival3));
                        }
                        if(busArrivalModelList.size()>0){
                            busModelList.add(new BusModel(busstopdataList,busArrivalModelList));
                        }

                    }
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return busModelList;
    }
}
