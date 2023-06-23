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
        List<Programme> chosenProgrammes = new ArrayList<>();

        for (Programme programme : programmesList) {
            if (selectedGenres.contains(programme.getCategory())) {
                chosenProgrammes.add(programme);
            }
        }

        return chosenProgrammes;
    }

    // Handle button click events
    @Override
    public void onClick(View view) {
        Button clickedButton = (Button) view;
        String genre = clickedButton.getText().toString();

        if (view.getId() == R.id.button6) {
            // Pass the selected genres to Recommend activity
            Intent intent = new Intent(GetInterest.this, Recommend.class);
            intent.putStringArrayListExtra("selectedGenres", (ArrayList<String>) selectedGenres);
            startActivity(intent);
        } else if (selectedGenres.contains(genre)) {
            // Remove genres that are selected
            selectedGenres.remove(genre);
            clickedButton.setBackgroundColor(Color.parseColor("#FF991A1A"));
        } else {
            // Genre not selected, add it
            selectedGenres.add(genre);
            Log.v(TAG, "Selected Genres: " + selectedGenres.toString());
            clickedButton.setBackgroundColor(Color.parseColor("#800080")); // Change button color to red
        }
    }

}
