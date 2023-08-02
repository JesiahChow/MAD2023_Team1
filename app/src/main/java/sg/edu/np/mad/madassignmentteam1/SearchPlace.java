package sg.edu.np.mad.madassignmentteam1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchPlace extends AppCompatActivity implements ProgrammeDatabase.DataLoadListener {
    private static final String TAG = "Recommendation";
    public TextView count;
    public TextView categoryName;
    public List<Programme> programmeList;
    public ProgrammeAdapter programmeAdapter;
    public RecyclerView recyclerView;
    public SearchView search;
    public String searchCategory;
    public ProgrammeDatabase programmeDatabase;
    private static final Map<Integer, Pair<String, String>> cardViewMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize views
        count = findViewById(R.id.count);
        categoryName = findViewById(R.id.categoryName);
        search = findViewById(R.id.Searchicon);
        Button backButton = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.recyclerview);

        // Set up the card view map
        setupCardViewMap();

        // Set click listeners for category card views
        setupCategoryClickListeners();

        // Set click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchPlace.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Set search query listener
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (searchCategory.matches("ALL")) {
                    loadData("accommodation,attractions,bars_clubs,cruises,events,food_beverages,precincts,shops,tours,venues,walking_trails", query);
                } else {
                    loadData(searchCategory.toLowerCase(), query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Load initial data for "ALL" category
        searchCategory = "ALL";
        loadData("accommodation,attractions,bars_clubs,cruises,events,food_beverages,precincts,shops,tours,venues,walking_trails", "");
    }

    // Set up the card view map with IDs and their corresponding tags and categories
    private void setupCardViewMap() {
        cardViewMap.put(R.id.accommodation, new Pair<>("accommodation", "Accommodation"));
        cardViewMap.put(R.id.attractions, new Pair<>("attractions", "Attractions"));
        cardViewMap.put(R.id.barclub, new Pair<>("bars_clubs", "Bars_Clubs"));
        cardViewMap.put(R.id.cruises, new Pair<>("cruises", "Cruises"));
        cardViewMap.put(R.id.events, new Pair<>("events", "Events"));
        cardViewMap.put(R.id.food, new Pair<>("food_beverages", "Food_Beverages"));
        cardViewMap.put(R.id.precincts, new Pair<>("precincts", "Precincts"));
        cardViewMap.put(R.id.shops, new Pair<>("shops", "Shops"));
        cardViewMap.put(R.id.tours, new Pair<>("tours", "Tours"));
        cardViewMap.put(R.id.venues, new Pair<>("venues", "Venues"));
        cardViewMap.put(R.id.walking, new Pair<>("walking_trails", "Walking_trails"));
        cardViewMap.put(R.id.all, new Pair<>("accommodation,attractions,bars_clubs,cruises,events,food_beverages,precincts,shops,tours,venues,walking_trails", "ALL"));
    }

    private View.OnClickListener categoryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            Pair<String, String> tagCategoryPair = cardViewMap.get(viewId);

            // Extract the tag and category from the map entry
            String tag = tagCategoryPair.first;
            String category = tagCategoryPair.second;

            // Make the new API call for the selected category
            loadData(tag, "");
            searchCategory = category;
            search.clearFocus();
            search.setQuery("", false);
        }
    };

    // Set click listeners for all category card views
    private void setupCategoryClickListeners() {
        for (int cardViewId : cardViewMap.keySet()) {
            CardView cardView = findViewById(cardViewId);
            cardView.setOnClickListener(categoryClickListener);
        }
    }

    // Load data from the database
    public void loadData(String tag, String keyword) {
        if (programmeList != null && programmeAdapter != null) {
            programmeList.clear();
            programmeAdapter.notifyDataSetChanged();
            recyclerView.stopScroll();
        }

        programmeList = new ArrayList<>();
        programmeAdapter = new ProgrammeAdapter(this, programmeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(programmeAdapter);
        Log.i(TAG, "Running the database");
        programmeDatabase = new ProgrammeDatabase(this, this, tag, keyword);
    }

    public void onDataLoaded(List<Programme> programmeList) {
        // Update the RecyclerView with the new data
        this.programmeList = programmeList;
        programmeAdapter = new ProgrammeAdapter(this, programmeList);
        recyclerView.setAdapter(programmeAdapter);
        programmeAdapter.notifyDataSetChanged();

        // Get the string resources
        String totalRecordsText = getString(R.string.total_records, programmeList.size());
        String categoryText = getString(R.string.category, searchCategory);
        count.setText(totalRecordsText);
        categoryName.setText(categoryText);
    }

    @Override
    public void onDataLoadError(Throwable error) {
        // Handle error if necessary
        Log.e(TAG, "Error loading data: " + error.getMessage());
    }
}
