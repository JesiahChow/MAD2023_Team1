package sg.edu.np.mad.madassignmentteam1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
   /*private RecyclerView recyclerView;
   ArrayList<String>featureList = new ArrayList<>();
   ArrayList<Integer>imageList = new ArrayList<>();*/

    private RecyclerView.Adapter Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        CardView profile = findViewById(R.id.profile);
        CardView recommendations = findViewById(R.id.recommended);
        CardView favourites = findViewById(R.id.favourites);
        CardView busTiming = findViewById(R.id.bus_timings);
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
                Intent intent = new Intent(HomeActivity.this, Settings.class);
                startActivity(intent);
            }
        });
        //when user clicks on favourite locations card
        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Settings.class);
                startActivity(intent);
            }
        });
        //when user clicks on bus timing card
        busTiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Settings.class);
                startActivity(intent);
            }
        });


        //add the names of features to featureList
        /*featureList.add("Recommendations");
        featureList.add("Favourite locations");
        featureList.add("Bus timings");
        featureList.add("Profile");

        //add images to imageList
        imageList.add(R.drawable.recommended);
        imageList.add(R.drawable.place);
        imageList.add(R.drawable.bus);
        imageList.add(R.drawable.profile_icon);

        &recyclerView = findViewById(R.id.home_recycler);
        Adapter  = new Adapter(this,featureList,imageList);
        //set a gridlayoutmanager to set the positioning of the views
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(Adapter);*/


    }

}