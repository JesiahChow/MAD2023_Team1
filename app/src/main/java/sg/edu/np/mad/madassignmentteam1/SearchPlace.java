package sg.edu.np.mad.madassignmentteam1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
    private static final String TAG = "SearchPlace";
    public TextView count;
    public TextView categoryName;
    public List<Programme> programmeList;
    public ProgrammeAdapter programmeAdapter;
    public RecyclerView recyclerView;
    public SearchView search;
    public String searchCategory;
    public ProgrammeDatabase programmeDatabase;
    private ProgressBar progressBar;
    private boolean isLoadingData = false;

    private static final Map<Integer, Pair<String, String>> cardViewMap = new HashMap<>();

    // Pagination variables
    private int offset = 0;
    private int limit = 10; // Number of records to fetch per page

    private int currentPage = 0;
    private int totalRecords = 0;
    private String currentDataset = "accommodation,attractions,bars_clubs,cruises,events,food_beverages,precincts,shops,tours,venues,walking_trails";



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
        progressBar = findViewById(R.id.progressBar);

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
                programmeList.clear();
                if (searchCategory.matches("ALL")) {
                    currentDataset = "accommodation,attractions,bars_clubs,cruises,events,food_beverages,precincts,shops,tours,venues,walking_trails";
                    // When searching in "ALL" category, pass all tags to the loadData method
                    loadData("accommodation,attractions,bars_clubs,cruises,events,food_beverages,precincts,shops,tours,venues,walking_trails", query,0);
                } else {
                    // Pass the selected category's tag to the loadData method
                    loadData(searchCategory.toLowerCase(), query,0);
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
        loadData("accommodation,attractions,bars_clubs,cruises,events,food_beverages,precincts,shops,tours,venues,walking_trails", "",0);

        // Set up RecyclerView scroll listener
        setupRecyclerViewScrollListener();
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
            if (category.equals("ALL")) {
                currentDataset = "accommodation,attractions,bars_clubs,cruises,events,food_beverages,precincts,shops,tours,venues,walking_trails";
            } else {
                currentDataset = category.toLowerCase();
            }
            loadData(tag, "", 0);
            searchCategory = category;
            search.clearFocus();
            search.setQuery("", false);
            programmeList.clear();
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
    public void loadData(String tag, String keyword, int offset) {
        // Check if the programmeList is null or empty to determine whether to create a new instance of the adapter
        if (programmeList == null || programmeList.isEmpty()) {
            programmeList = new ArrayList<>();
            programmeAdapter = new ProgrammeAdapter(this, programmeList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(programmeAdapter);
        }

        // Notify the adapter of data changes before fetching new data
        programmeAdapter.notifyDataSetChanged();
        recyclerView.stopScroll();

        Log.i(TAG, "Running the database");
        programmeDatabase = new ProgrammeDatabase(this, this, currentDataset, keyword, offset, limit);
    }



    // Set up RecyclerView scroll listener to load more data when reaching the end
    private void setupRecyclerViewScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // Check if more data can be loaded
                    if (!isLoadingData && programmeList.size() < totalRecords) {
                        isLoadingData = true;
                        int nextPage = currentPage + 1;
                        // Calculate the new offset based on the current page and limit
                        int newOffset = nextPage * limit;
                        Log.d("currentpage","the offset that is use after reach the bottom and start new data"+ newOffset);
                        Log.d("Newoffset","the offset that is use after reach the bottom and start new data"+ newOffset);
                        loadData(searchCategory.toLowerCase(), "", newOffset);
                    }
                }
            }
        });
    }


    public void onDataLoaded(List<Programme> newData, int offset) {
        // Update the current page and total records
        this.currentPage = offset / limit;
        this.totalRecords = programmeDatabase.getTotalRecords();

        // Update the existing programmeList with the new data

        this.programmeList.addAll(newData);

        // Notify the adapter of data changes
        programmeAdapter.notifyDataSetChanged();

        // Get the string resources
        String totalRecordsText = getString(R.string.total_records, programmeList.size());
        String categoryText = getString(R.string.category, searchCategory);
        count.setText(totalRecordsText);
        categoryName.setText(categoryText);

        progressBar.setVisibility(View.GONE);
        isLoadingData = false; // Reset the flag when data loading is complete
    }



    @Override
    public void onDataLoadError(Throwable error) {
        // Handle error if necessary
        Log.e(TAG, "Error loading data: " + error);
    }
}
