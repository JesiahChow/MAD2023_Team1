package sg.edu.np.mad.madassignmentteam1;

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

public class CarParkAvailabilityAPI {
    private static final String API_URL = "http://datamall2.mytransport.sg/ltaodataservice/CarParkAvailabilityv2";


     /* Retrieves car park availability data from the API.
      @return List of CarParkAvailability objects representing car park availability data.*/


    public List<CarParkAvailability> getCarParkAvailabilityData() {
        List<CarParkAvailability> carParkAvailabilityList = new ArrayList<>();

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

                JSONObject jsonObject = new JSONObject(response.toString());
                JSONArray itemsArray = jsonObject.getJSONArray("items");
                //Creating arraylist
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject itemObject = itemsArray.getJSONObject(i);
                    String carparkID =  itemObject.getString("CarParkID");
                    String area = itemObject.get("Area").toString();
                    String development = itemObject.get("Development").toString();
                    String availablelots = itemObject.getString("AvailableLots");
                    String lotType = itemObject.get("LotType").toString();
                    //Adding object to array
                    carParkAvailabilityList.add(new CarParkAvailability(carparkID, area, development, availablelots, lotType));
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return carParkAvailabilityList;
    }
}

