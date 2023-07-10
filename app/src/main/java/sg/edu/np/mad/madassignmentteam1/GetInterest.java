package sg.edu.np.mad.madassignmentteam1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;



public class GetInterest extends AppCompatActivity implements View.OnClickListener {
    final String TAG = "Recommendation";
    public List<String> selectedGenres = new ArrayList<>(); // List to store selected genres

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getinterest);
        AppCompatButton backButton = findViewById(R.id.backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetInterest.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });


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

        Button doneButton = buttons[5];
        doneButton.setEnabled(false);
        doneButton.setVisibility(View.GONE);
    }

    /* Function to sort activities based on category (Future implementation when data in database.)
    private List<Programme> sortProgrammesByCategory(List<Programme> programmesList, List<String> selectedGenres) {
        List<Programme> chosenProgrammes = new ArrayList<>();

        for (Programme programme : programmesList) {
            if (selectedGenres.contains(programme.getCategory())) {
                chosenProgrammes.add(programme);
            }
        }

        return chosenProgrammes;
    }*/

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
            clickedButton.setBackgroundTintList(ContextCompat.getColorStateList(GetInterest.this,R.color.red));
        } else {
            // Genre not selected, add it
            selectedGenres.add(genre);
            Log.v(TAG, "Selected Genres: " + selectedGenres.toString());
            clickedButton.setBackgroundTintList(ContextCompat.getColorStateList(GetInterest.this,R.color.dark_green)); // Change button color to red
        }
        //The enable
        Button doneButton = findViewById(R.id.button6);
        if (selectedGenres.size() >= 1) {
            doneButton.setEnabled(true);
            doneButton.setVisibility(View.VISIBLE);
        } else {
            doneButton.setEnabled(false);
            doneButton.setVisibility(View.GONE);
        }
    }

}
