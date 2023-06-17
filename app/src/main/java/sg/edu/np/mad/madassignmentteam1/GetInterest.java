package sg.edu.np.mad.madassignmentteam1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;



public class GetInterest extends AppCompatActivity implements View.OnClickListener {
    final String TAG = "Recommendation";
    public List<String> selectedGenres = new ArrayList<>(); // List to store selected genres
    private ProgrammeDatabase programmeDatabase; // Instance of ProgrammeDatabase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getinterest);

        // Initialize ProgrammeDatabase
        programmeDatabase = new ProgrammeDatabase();

        // Set click listeners for the genre buttons
        Button[] buttons = new Button[]{
                findViewById(R.id.button1),
                findViewById(R.id.button2),
                findViewById(R.id.button3),
                findViewById(R.id.button4),
                findViewById(R.id.button5),
                findViewById(R.id.button6)
                // Add more buttons here
        };

        for (Button button : buttons) {
            button.setOnClickListener(this);
        }
    }

    // Function to sort activities based on category
    private List<Programme> sortProgrammesByCategory(List<Programme> programmesList, List<String> selectedGenres) {
        List<Programme> sortedProgrammes = new ArrayList<>();

        for (Programme programme : programmesList) {
            if (selectedGenres.contains(programme.getCategory())) {
                sortedProgrammes.add(programme);
            }
        }

        return sortedProgrammes;
    }

    // Handle button click events
    @Override
    public void onClick(View view) {
        Button clickedButton = (Button) view;
        String genre = clickedButton.getText().toString();

        if (selectedGenres.contains(genre)) {
            // Genre already selected, remove it
            selectedGenres.remove(genre);
            clickedButton.setBackgroundColor(Color.parseColor("#FF6750A3"));
        } else {
            // Genre not selected, add it
            selectedGenres.add(genre);
            Log.v(TAG, "Selected Genres: " + selectedGenres.toString());
            clickedButton.setBackgroundColor(Color.RED); // Change button color to red
        }

        if (view.getId() == R.id.button6) {
            // Update the sorted activities based on the selected genres
            List<Programme> sortedProgrammes = sortProgrammesByCategory(programmeDatabase.getProgrammes(), selectedGenres);

            // Print the sorted activities
            for (Programme programme : sortedProgrammes) {
                Log.v(TAG, "Programme Name: " + programme.getName() + ", Programme Description: " + programme.getDescription() + ", Programme Category: " + programme.getCategory());
            }

            // Pass the selected genres to HomeActivity
            Intent intent = new Intent(GetInterest.this, Recommend.class);
            intent.putStringArrayListExtra("selectedGenres", (ArrayList<String>) selectedGenres);
            startActivity(intent);
        }
    }
}

