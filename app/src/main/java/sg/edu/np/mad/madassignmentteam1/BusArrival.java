package sg.edu.np.mad.madassignmentteam1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BusArrival extends AppCompatActivity {
    RecyclerView busArrival_recyclerView;
    ArrayList<BusModel> busModelArrayList;
    private ArrayList<BusStopModel> busStopModelArrayList;
    private ProgressDialog progressDialog;

    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_arrival);
        AppCompatButton backButton = findViewById(R.id.back_button);
        busModelArrayList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching data, please wait");
        progressDialog.setCancelable(false); // Set whether the dialog can be canceled by clicking outside
        progressDialog.show();

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(BusArrival.this,HomeActivity.class);
            startActivity(intent);
            finish();
        });
/*
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });
*/
        busArrival_recyclerView = findViewById(R.id.bus_arrivalitem);
        busArrival_recyclerView.setHasFixedSize(true);
        busArrival_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        busStopModelArrayList = new ArrayList<>();

        new FetchBusArriavlAvailabilityTask().execute();
    }
/*
    private void filterList(String text) {
        List<BusStopModel> filteredList = new ArrayList<>();
        for (BusStopModel item : busStopModelArrayList) {
            if (item.RoadName.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            BusItemAdapter.setFilteredList(filteredList);
        }
    }
*/
    private class FetchBusArriavlAvailabilityTask extends AsyncTask<Void, Void, List<BusModel>> {
        //Fetches bus data and returns it

        protected List<BusModel> doInBackground(Void... voids) {
            BusAPI busAPI = new BusAPI();
            return busAPI.getbus();
        }
        protected void onPostExecute(List<BusModel> result) {
            if (result != null) {
                busModelArrayList = (ArrayList<BusModel>) result;

                busStopModelArrayList.clear();
                for (BusModel busModel : busModelArrayList) {
                    busStopModelArrayList.addAll(busModel.busStopModelList);
                }

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