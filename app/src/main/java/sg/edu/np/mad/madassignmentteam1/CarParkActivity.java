package sg.edu.np.mad.madassignmentteam1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CarParkActivity extends AppCompatActivity {
    ArrayList<CarParkAvailability> carParkAvailabilityList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpark_layout);

        carParkAvailabilityList = new ArrayList<>();
        CarParkAdapter adapter = new CarParkAdapter(CarParkActivity.this, carParkAvailabilityList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // Fetch car park availability data
        FetchCarParkAvailabilityTask fetchCarParkAvailabilityTask = new FetchCarParkAvailabilityTask();
        fetchCarParkAvailabilityTask.execute();
    }

    private class FetchCarParkAvailabilityTask extends AsyncTask<Void, Void, List<CarParkAvailability>> {

        @Override
        protected List<CarParkAvailability> doInBackground(Void... voids) {
            CarParkAvailabilityAPI carParkAvailabilityAPI = new CarParkAvailabilityAPI();
            return carParkAvailabilityAPI.getCarParkAvailabilityData();
        }

        @Override
        protected void onPostExecute(List<CarParkAvailability> result) {
            CarParkAdapter adapter = new CarParkAdapter(CarParkActivity.this, carParkAvailabilityList);
            if (result != null) {
                carParkAvailabilityList.clear();
                carParkAvailabilityList.addAll(result);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(CarParkActivity.this, "Failed to fetch car park availability data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
