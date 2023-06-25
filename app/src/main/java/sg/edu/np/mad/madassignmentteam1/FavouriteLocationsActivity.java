package sg.edu.np.mad.madassignmentteam1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FavouriteLocationsActivity extends AppCompatActivity {
    RecyclerView favouriteLocationsRecyclerView = null;

    AppCompatButton viewMapButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favourite_locations);

        /*
        TODOs:
        -> Close page for activities containing RecyclerViews to prevent excessive and
        unnecessary usage of memory (implement such functionality for this activity).
        */
        favouriteLocationsRecyclerView = findViewById(R.id.FavouriteLocationsRecyclerView);

        FavouriteLocationsAdapter favouriteLocationsAdapter = new FavouriteLocationsAdapter();

        favouriteLocationsRecyclerView.setAdapter(favouriteLocationsAdapter);

        favouriteLocationsRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        viewMapButton = findViewById(R.id.ViewMapButton);

        viewMapButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(
                            FavouriteLocationsActivity.this,
                            MapViewerActivity.class
                        );

                        startActivity(intent);
                    }
                }
        );
    }
}