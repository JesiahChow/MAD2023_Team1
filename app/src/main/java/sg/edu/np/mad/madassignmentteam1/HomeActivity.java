package sg.edu.np.mad.madassignmentteam1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        CardView profile = findViewById(R.id.profile);
        CardView recommendations = findViewById(R.id.recommended);
        CardView favourites = findViewById(R.id.favourites);
        CardView busTiming = findViewById(R.id.bus_timing);
        CardView languageTranslator = findViewById(R.id.language);
        CardView fastestRoute = findViewById(R.id.fastest_route);
        CardView searchHotels = findViewById(R.id.search_places);
        CardView carparkFinder = findViewById(R.id.carpark_finder);
        //when user clicks on profile card
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Settings.class);
                startActivity(intent);
            }
        });
        //when user clicks on recommendations card
        recommendations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, GetInterest.class);
                startActivity(intent);
            }
        });
        //when user clicks on favourite locations card
        favourites.setOnClickListener(new View.OnClickListener() {
            /*
            Redirects user to the Favourite Locations activity when the favourite locations card
            is clicked.
            */
            @Override public void onClick(View view)
            {
                Intent intent = new Intent(HomeActivity.this, FavouriteLocationsActivity.class);

                startActivity(intent);
            }
        });
        //when user clicks on bus timing card
        busTiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CarParkActivity.class);
                startActivity(intent);
            }
        });
        languageTranslator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Translator.class);
                startActivity(intent);
            }
        });
        carparkFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,HomeActivity.class );
                startActivity(intent);
            }
        });
       fastestRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        searchHotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });




    }

}