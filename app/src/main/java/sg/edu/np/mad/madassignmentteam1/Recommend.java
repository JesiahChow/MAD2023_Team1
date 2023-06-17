package sg.edu.np.mad.madassignmentteam1;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Recommend extends AppCompatActivity {
    private List<String> selectedGenres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        // Retrieve selected genres from the intent
        selectedGenres = getIntent().getStringArrayListExtra("selectedGenres");

        // Log selected genres
        for (String genre : selectedGenres) {
            Log.d("HomeActivity", "Selected Genre: " + genre);
        }
    }
}
