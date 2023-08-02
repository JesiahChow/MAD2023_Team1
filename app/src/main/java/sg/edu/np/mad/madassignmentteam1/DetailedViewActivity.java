package sg.edu.np.mad.madassignmentteam1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import me.relex.circleindicator.CircleIndicator3;

public class DetailedViewActivity extends AppCompatActivity {
    String TAG = "DETAILED VIEW";
    ViewPager2 viewPager2 ;
    TextView programmeTitle;
    RatingBar ratingBar ;
    TextView ratingTextView;
    TextView category;
    TextView description;
    TextView address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clickthumbnail);
         viewPager2 = findViewById(R.id.imageSlider);
         programmeTitle = findViewById(R.id.programmeTitle);
         ratingBar = findViewById(R.id.ratingBar);
         category = findViewById(R.id.category);
         description = findViewById(R.id.description);
         address = findViewById(R.id.address);
        Button backButton = findViewById(R.id.back_btn);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Updated to only close the current page so that it won't call the api again
                finish();
            }
        });


        // Add other views as needed

        // Retrieve data passed from SearchPlace
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            String titleText = extras.getString("title");
            String descriptionText = extras.getString("description");
            String categoryText = extras.getString("category");
            String addressText = extras.getString("address");
            String ratingText = extras.getString("rating");
            String[] images = extras.getStringArray("images");
            programmeTitle.setText(titleText);
            description.setText(descriptionText);
            category.setText(categoryText);
            address.setText(addressText);
            // Check if rating is 0 and set the RatingBar's visibility accordingly
            float ratingValue = Float.parseFloat(ratingText);
            if (ratingValue == 0) {
                ratingBar.setVisibility(View.INVISIBLE);
            } else {
                ratingBar.setVisibility(View.VISIBLE);
                ratingBar.setRating(ratingValue);
            }
            ratingBar.setRating(Float.parseFloat(ratingText));
           //   Toast.makeText(getApplicationContext(),images.length+"",Toast.LENGTH_LONG).show();
            ProgrammeImageAdapter programmeImageAdapter = new ProgrammeImageAdapter(images);
            viewPager2.setAdapter(programmeImageAdapter);
            viewPager2.setOffscreenPageLimit (10);
            CircleIndicator3 indicator = findViewById(R.id.indicator);
            indicator.setViewPager(viewPager2);
        }
    }
}
