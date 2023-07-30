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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recommend extends AppCompatActivity implements RecommendViewInterface {
    private List<String> selectedGenres;
    private ProgrammeDatabase programmeDatabase;
    private RecyclerView recyclerView;
    private RecommendAdapter programmeAdapter;

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
        getdataset.put("Shows & Entertainment", "attractions,events");
        getdataset.put("Sightseeing & Tours", "tours,precincts");
        getdataset.put("Food & Dining", "food_beverages");
        getdataset.put("Outdoor Exploration", "walking_trails");



        // Initialize ProgrammeDatabase


        // Log selected genres
        for (String genre : selectedGenres) {
            Log.d("GetInterest", "These databases: " + genre);
        }

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recommendRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //programmeAdapter = new RecommendAdapter(filteredProgrammes, this);
        recyclerView.setAdapter(programmeAdapter);
    }

    // Method for filtering programmes based on GetInterest

    @Override
    public void onItemClick(int position) {

        // Start RecommendDetails activity
        Intent intent = new Intent(Recommend.this, RecommendDetails.class);
        // Pass any necessary data to RecommendDetails activity
        startActivity(intent);
    }
}
