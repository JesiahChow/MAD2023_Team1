package sg.edu.np.mad.madassignmentteam1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recommend extends AppCompatActivity implements ProgrammeDatabase.DataLoadListener {
    private List<String> selectedGenres;
    private ProgrammeDatabase programmeDatabase;
    private RecyclerView recyclerView;
    private ProgrammeAdapter programmeAdapter;

    private List<Programme> programmeList;

    private List<String> dataset;


    // Define the mapping dictionary to associate user-visible categories with backend values
    private final Map<String, String> getdataset = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        Button backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Recommend.this, GetInterest.class);
                startActivity(intent);
                finish();
            }
        });

        // Retrieve selected genres from the intent
        selectedGenres = getIntent().getStringArrayListExtra("selectedGenres");
        //convert the genres to dataset
        // Initialize the mapping dictionary
        // Retrieve selected genres from the intent
        // Initialize the mapping dictionary
        getdataset.put("Shows & Entertainment", "attractions,events");
        getdataset.put("Sightseeing & Tours", "tours,precincts");
        getdataset.put("Food & Dining", "food_beverages");
        getdataset.put("Events", "events");
        getdataset.put("Urban Exploration", "walking_trails");

        //Dataset for splitting commas
        List<String> dataset = new ArrayList<>();
        // Use a single string to represent the combined backend values
        StringBuilder rawdataset = new StringBuilder();

        // Log selected genres and their corresponding backend values
        for (String genre : selectedGenres) {
            // Get the corresponding backend value from the map
            String backendValue = getdataset.get(genre);
            Log.d("GetInterest", "Genre: " + genre + ", Backend Value: " + backendValue);
            // Append the backend value to the combined string
            rawdataset.append(backendValue).append(",");
        }
        Log.i("CHEECKKK","dataset value" + rawdataset);
        // Remove the trailing comma if there are backend values appended
        if (rawdataset.length() > 0) {
            rawdataset.deleteCharAt(rawdataset.length() - 1);
        }

        String combinedrawdataset = rawdataset.toString();


        Log.i("combine","dataset value" + combinedrawdataset);
        // Split the string using commas and add its parts to the dataset list
        String[] backendValuesArray = combinedrawdataset.split(",");
        dataset.addAll(Arrays.asList(backendValuesArray));

        Log.i("Dataset", "Check dataset: " + dataset);

        recyclerView = findViewById(R.id.recommendRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        programmeList = new ArrayList<>();
        programmeAdapter = new ProgrammeAdapter(this,programmeList);
        recyclerView.setAdapter(programmeAdapter);
        Log.i("Recommend dataset","Running the database");

        // Initialize the ProgrammeDatabase instance
        programmeDatabase = new ProgrammeDatabase(this, this, "", ""); // Pass appropriate arguments here


        int limitPerDataset = 2; // Set the limit for records per dataset

        for (String backendValue : dataset) {
            Log.i("Runns2", "Running this data:" + backendValue);
            programmeDatabase.getLimitedProgrammes(backendValue, limitPerDataset); // Fetch limited records for each dataset
        }
    }

    // Method for filtering programmes based on GetInterest

    @Override
    public void onDataLoaded(List<Programme> programmeList) {
        this.programmeList.clear();
        this.programmeList.addAll(programmeList); // Add the fetched data
        programmeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDataLoadError(Throwable error) {
        Log.d("MissingImage", "Error above occurred due to data record not having images since i implemented randomisation :)");
    }
}
