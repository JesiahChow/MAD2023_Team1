package sg.edu.np.mad.madassignmentteam1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

        Log.d("Genres", "Selected Genres: " + selectedGenres);

        List<Programme> filteredProgrammes = filterProgrammesByGenre(programmeDatabase.getProgrammes(), selectedGenres);

        programmeAdapter = new RecommendAdapter(filteredProgrammes, this);
        recyclerView.setAdapter(programmeAdapter);


    }


    //Method for filtering programmes based on GetInterest
    private List<Programme> filterProgrammesByGenre(List<Programme> programmes, List<String> selectedGenres) {
        List<Programme> filteredProgrammes = new ArrayList<>();
        for (Programme programme : programmes) {
            Log.d("programme", "Programme Category: " + programme.getCategory());
            Log.d("Comparing","First variable: "+ selectedGenres + "Program variable: " + programme.getCategory());
            if (selectedGenres.contains(programme.getCategory())) {
                Log.d("filtergod", "Adding programme: " + programme.getTitle());
                filteredProgrammes.add(programme);
            }
            else{
                Log.d("Status","Failure");
            }
        }
        return filteredProgrammes;

    }

    @Override
    public void onItemClick(int position) {


    }
}
