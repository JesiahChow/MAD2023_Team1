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

    AppCompatButton returnToHomeActivityButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favourite_locations);

        /*
        TODOs:
        -> Close page for activities containing RecyclerViews to prevent excessive and
        unnecessary usage of memory (implement such functionality for this activity).
        */
        this.favouriteLocationsRecyclerView = findViewById(R.id.FavouriteLocationsRecyclerView);

        FavouriteLocationsAdapter favouriteLocationsAdapter = new FavouriteLocationsAdapter();

        this.favouriteLocationsRecyclerView.setAdapter(favouriteLocationsAdapter);

        this.favouriteLocationsRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        this.viewMapButton = findViewById(R.id.ViewMapButton);

        this.viewMapButton.setOnClickListener(
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

        this.returnToHomeActivityButton = findViewById(R.id.FavouriteLocationsActivityReturnToHomeActivityButton);

        this.returnToHomeActivityButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(
                        FavouriteLocationsActivity.this,
                        HomeActivity.class
                    );

                    startActivity(intent);
                }
            }
        );
    }
}