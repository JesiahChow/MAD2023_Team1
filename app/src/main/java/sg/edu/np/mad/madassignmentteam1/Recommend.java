package sg.edu.np.mad.madassignmentteam1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Recommend extends AppCompatActivity implements RecommendViewInterface {
    private List<String> selectedGenres;
    private ProgrammeDatabase programmeDatabase;
    private RecyclerView recyclerView;
    private RecommendAdapter programmeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        ImageView backButton = findViewById(R.id.imageView6);
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

        // Initialize ProgrammeDatabase
        programmeDatabase = new ProgrammeDatabase();

        // Log selected genres
        for (String genre : selectedGenres) {
            Log.d("GetInterest", "Selected Genre: " + genre);
        }

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recommendRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Programme> filteredProgrammes = filterProgrammesByGenre(programmeDatabase.getProgrammes(), selectedGenres);

        programmeAdapter = new RecommendAdapter(filteredProgrammes, this);
        recyclerView.setAdapter(programmeAdapter);
    }

    // Method for filtering programmes based on GetInterest
    private List<Programme> filterProgrammesByGenre(List<Programme> programmes, List<String> selectedGenres) {
        List<Programme> filteredProgrammes = new ArrayList<>();
        for (Programme programme : programmes) {
            //Loop to run through all programmes in the database for comparison.
            if (selectedGenres.contains(programme.getCategory())) {
                //Add programmes into filteredProgrammes if selectedGenres matches the programmes in database.
                filteredProgrammes.add(programme);
            }
        }
        return filteredProgrammes;
    }

    @Override
    public void onItemClick(int position) {

        // Start RecommendDetails activity
        Intent intent = new Intent(Recommend.this, RecommendDetails.class);
        // Pass any necessary data to RecommendDetails activity
        startActivity(intent);
    }
}
