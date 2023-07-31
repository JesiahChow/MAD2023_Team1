package sg.edu.np.mad.madassignmentteam1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchPlace extends AppCompatActivity implements ProgrammeDatabase.DataLoadListener, ItemViewInterface {
    private static final String TAG = "Recommendation";
    public TextView count;
    public TextView categoryName;
    public List<Programme> programmeList;
    public ProgrammeAdapter programmeAdapter;
    public RecyclerView recyclerView;
    public CardView accommodation;
    public CardView attractions;
    public CardView barclub;
    public CardView cruises;
    public CardView events;
    public CardView food;
    public CardView precincts;
    public CardView shops;
    public CardView tours;
    public CardView venues;
    public CardView walking;
    public CardView all;
    public SearchView search;
    public String searchCategory;
    public ProgrammeDatabase programmeDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        accommodation = findViewById(R.id.accommodation);
        attractions = findViewById(R.id.attractions);
        barclub = findViewById(R.id.barclub);
        cruises = findViewById(R.id.cruises);
        events = findViewById(R.id.events);
        food = findViewById(R.id.food);
        precincts = findViewById(R.id.precincts);
        shops = findViewById(R.id.shops);
        tours = findViewById(R.id.tours);
        venues = findViewById(R.id.venues);
        walking = findViewById(R.id.walking);
        all = findViewById(R.id.all);
        count = findViewById(R.id.count);
        categoryName = findViewById(R.id.categoryName);
        search = findViewById(R.id.Searchicon);
        Button backButton = findViewById(R.id.back_btn);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchPlace.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        accommodation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadData("accommodation","");
                searchCategory = "Accomodation";
                search.clearFocus();
                search.setQuery("",false);
            }
        });
        attractions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData("attractions","");
                searchCategory = "Attractions";
                search.clearFocus();
                search.setQuery("",false);
            }
        });
        barclub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData("bars_clubs","");
                searchCategory = "Bars_Clubs";
                search.clearFocus();
                search.setQuery("",false);
            }
        });
        cruises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData("cruises","");
                searchCategory = "Cruises";
                search.clearFocus();
                search.setQuery("",false);
            }
        });
        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData("events","");
                searchCategory = "Events";
                search.clearFocus();
                search.setQuery("",false);
            }
        });
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData("food_beverages","");
                searchCategory = "Food_Beverages";
                search.clearFocus();
                search.setQuery("",false);
            }
        });
        shops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData("shops","");
                searchCategory = "Shops";
                search.clearFocus();
                search.setQuery("",false);
            }
        });
        venues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData("venues","");
                searchCategory = "Venues";
                search.clearFocus();
                search.setQuery("",false);
            }
        });
        walking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData("walking_trails","");
                searchCategory = "Walking_trails";
                search.clearFocus();
                search.setQuery("",false);
            }
        });
        tours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData("tours","");
                searchCategory = "Tours";
                search.clearFocus();
                search.setQuery("",false);
            }
        });
        precincts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData("precincts","");
                searchCategory = "Precincts";
                search.clearFocus();
                search.setQuery("",false);
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadData("accommodation,attractions,bars_clubs,cruises,events,food_beverages,precincts,shops,tours,venues,walking_trails","");
                searchCategory = "ALL";
                search.clearFocus();
                search.setQuery("",false);
            }
        });
        // Assuming you have a TextView with this ID in your layout

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //searchCategory = "ALL";
                if(searchCategory.matches("ALL")) {
                    loadData("accommodation,attractions,bars_clubs,cruises,events,food_beverages,precincts,shops,tours,venues,walking_trails", query);
                }
                else{
                    loadData(searchCategory.toLowerCase(), query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        recyclerView = findViewById(R.id.recyclerview);
        searchCategory = "ALL";
        loadData("accommodation,attractions,bars_clubs,cruises,events,food_beverages,precincts,shops,tours,venues,walking_trails","");
    }
    public void loadData( String tag, String keyword)
    {
        if(programmeList!=null && programmeAdapter!=null)
        {
            programmeList.clear();
            programmeAdapter.notifyDataSetChanged();
            recyclerView.stopScroll();
        }

        count.setText("Total Records: ");
        categoryName.setText("Category: ");
        programmeList = new ArrayList<>();
        programmeAdapter = new ProgrammeAdapter(this, programmeList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(programmeAdapter);
        Log.i(TAG,"Running the database");
        programmeDatabase = new ProgrammeDatabase(this,this,tag,keyword);

    }
    @SuppressLint("NotifyDataSetChanged")
    public void onDataLoaded(List<Programme> programmeList) {
        // Update the RecyclerView with the new data

        this.programmeList = programmeList;
        programmeAdapter = new ProgrammeAdapter(this, programmeList, this);
        recyclerView.setAdapter(programmeAdapter);
        programmeAdapter.notifyDataSetChanged();
        count.setText("Total Records: " + programmeList.size());
        categoryName.setText("Category: "+searchCategory);
    }

    @Override
    public void onDataLoadError(Throwable error) {

    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(SearchPlace.this, DetailedViewActivity.class);

        startActivity(intent);
    }
}


