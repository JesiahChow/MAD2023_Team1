package sg.edu.np.mad.madassignmentteam1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class FavouriteLocationsActivity extends AppCompatActivity {
    RecyclerView favouriteLocationsRecyclerView = null;

    FavouriteLocations favouriteLocations = new FavouriteLocations();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favourite_locations);

        favouriteLocationsRecyclerView = findViewById(R.id.FavouriteLocationsRecyclerView);

        favouriteLocations.CreatePlaceholderFavouriteLocationAddresses();

        FavouriteLocationsAdapter favouriteLocationsAdapter = new FavouriteLocationsAdapter(
                favouriteLocations.favouriteLocationAddresses
        );

        favouriteLocationsRecyclerView.setAdapter(favouriteLocationsAdapter);

        favouriteLocationsRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );
    }
}