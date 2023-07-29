package sg.edu.np.mad.madassignmentteam1;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BusArrival extends AppCompatActivity {
    RecyclerView busArrival_recyclerView;
    ArrayList<BusModel> busModelArrayList;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_arrival);

        busModelArrayList = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching data, please wait");
        progressDialog.setCancelable(false); // Set whether the dialog can be canceled by clicking outside
        progressDialog.show();



        busArrival_recyclerView = findViewById(R.id.bus_arrivalitem);
        busArrival_recyclerView.setHasFixedSize(true);
        busArrival_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new FetchBusArriavlAvailabilityTask().execute();
    }

    private class FetchBusArriavlAvailabilityTask extends AsyncTask<Void, Void, List<BusModel>> {
        //Fetches bus data and returns it

        protected List<BusModel> doInBackground(Void... voids) {
            BusAPI busAPI = new BusAPI();
            return busAPI.getbus();
        }
        protected void onPostExecute(List<BusModel> result) {
            if (result != null) {
                busModelArrayList = (ArrayList<BusModel>) result;
                BusItemAdapter adapter = new BusItemAdapter(BusArrival.this, busModelArrayList);
                busArrival_recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            } else {
                Toast.makeText(BusArrival.this, "Failed to fetch FetchBusStopAvailabilityTask data", Toast.LENGTH_SHORT).show();
            }
        }



    }
}