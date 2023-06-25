package sg.edu.np.mad.madassignmentteam1;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecommendDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommenddetails);

        // Retrieve the selected item's information from the intent
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String imageFileName = getIntent().getStringExtra("imageFileName");

        // Set the retrieved information to the respective TextViews
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        ImageView imageView = findViewById(R.id.imageView);

        titleTextView.setText(title);
        descriptionTextView.setText(description);

        int imageResourceId = getResources().getIdentifier(imageFileName, "drawable", getPackageName());
        imageView.setImageResource(imageResourceId);
    }
}
