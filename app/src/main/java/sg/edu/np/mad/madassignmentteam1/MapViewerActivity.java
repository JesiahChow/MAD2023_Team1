package sg.edu.np.mad.madassignmentteam1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import sg.edu.np.mad.madassignmentteam1.utilities.LocationInfoUtility;
import sg.edu.np.mad.madassignmentteam1.utilities.LoggerUtility;

public class MapViewerActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private final LatLng SINGAPORE_CENTER_LATLNG = new LatLng(
        1.3521,
        103.8198
    );

    private SupportMapFragment mainSupportMapFragment = null;

    private GoogleMap googleMap = null;

    private EditText searchBar = null;

    private RecyclerView searchBarResultsRecyclerView = null;

    private SearchBarResultsAdapter searchBarResultsAdapter = null;

    private ArrayList<LocationInfo> currentSearchBarResultsLocationInfoArrayList = new ArrayList<LocationInfo>();

    private ScrollView selectedLocationScrollView = null;

    private AppCompatButton toggleLocationFavouriteStatusButton = null;

    private TextView selectedLocationNameTextView = null;

    private TextView selectedLocationAddressTextView = null;

    private TextView selectedLocationPostalCodeTextView = null;

    private LocationInfo selectedLocationInfo = null;

    private AppCompatButton returnToFavouriteLocationsActivityButton = null;

    private ImageButton selectedLocationCloseButton = null;

    /*
    List of Google Maps APIs required for core functionality (for finalized implementation):
    1. Maps SDK for Android (required for displaying maps on Android devices).
    2. Places API (required for finding places corresponding to a location search string, as well
    as obtaining information about the corresponding places).
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map_viewer);

        this.mainSupportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.MainSupportMapFragment);

        this.mainSupportMapFragment.getMapAsync(this);

        this.searchBar = (EditText) findViewById(R.id.SearchBar);

        this.searchBar.setOnEditorActionListener(
            new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN))
                    {
                        currentSearchBarResultsLocationInfoArrayList.clear();

                        currentSearchBarResultsLocationInfoArrayList.addAll(
                                LocationInfoUtility.getCorrespondingLocationsForLocationName(
                                        v.getText().toString(),
                                        v.getContext()
                                )
                        );

                        searchBarResultsAdapter.notifyDataSetChanged();

                        searchBarResultsRecyclerView.setVisibility(
                            View.VISIBLE
                        );

                        return true;
                    }

                    return false;
                }
            }
        );

        this.selectedLocationScrollView = findViewById(R.id.SelectedLocationScrollView);

        this.selectedLocationScrollView.setVisibility(
            View.INVISIBLE
        );

        this.toggleLocationFavouriteStatusButton = findViewById(R.id.ToggleLocationFavouriteStatusButton);

        this.toggleLocationFavouriteStatusButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MapViewerActivity.this.toggleLocationFavouriteStatusButton.getText() == getString(R.string.toggle_location_favourite_status_button_add_mode_text))
                    {
                        FavouriteLocations.instance.favouriteLocationsLocationInfoArrayList.add(
                            MapViewerActivity.this.selectedLocationInfo
                        );

                        MapViewerActivity.this.toggleLocationFavouriteStatusButton.setText(
                            getString(R.string.toggle_location_favourite_status_button_remove_mode_text)
                        );
                    }
                    else if (MapViewerActivity.this.toggleLocationFavouriteStatusButton.getText() == getString(R.string.toggle_location_favourite_status_button_remove_mode_text))
                    {
                        FavouriteLocations.instance.favouriteLocationsLocationInfoArrayList.remove(
                            MapViewerActivity.this.selectedLocationInfo
                        );

                        MapViewerActivity.this.toggleLocationFavouriteStatusButton.setText(
                                getString(R.string.toggle_location_favourite_status_button_add_mode_text)
                        );
                    }
                }
            }
        );

        this.selectedLocationNameTextView = findViewById(R.id.SelectedLocationNameTextView);

        this.selectedLocationAddressTextView = findViewById(R.id.SelectedLocationAddressTextView);

        this.selectedLocationPostalCodeTextView = findViewById(R.id.SelectedLocationPostalCodeTextView);

        this.returnToFavouriteLocationsActivityButton = findViewById(R.id.ReturnToFavouriteLocationsActivityButton);

        this.returnToFavouriteLocationsActivityButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(
                        MapViewerActivity.this,
                        FavouriteLocationsActivity.class
                    );

                    startActivity(intent);
                }
            }
        );

        this.selectedLocationCloseButton = findViewById(R.id.SelectedLocationCloseButton);

        this.selectedLocationCloseButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MapViewerActivity.this.selectedLocationScrollView.setVisibility(
                        View.INVISIBLE
                    );
                }
            }
        );
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;

        this.searchBarResultsRecyclerView = (RecyclerView) findViewById(
                R.id.SearchBarResultsRecyclerView
        );

        this.searchBarResultsAdapter = new SearchBarResultsAdapter(
                this.currentSearchBarResultsLocationInfoArrayList,
                this.googleMap
        );

        this.searchBarResultsAdapter.onSearchBarResultClickListeners.add(
            new SearchBarResultsAdapter.OnSearchBarResultClickListener() {
                @Override
                public void onSearchBarResultClick(View searchBarResultView, int searchBarResultIndex)
                {
                    MapViewerActivity.this.selectedLocationInfo = MapViewerActivity.this.currentSearchBarResultsLocationInfoArrayList.get(
                        searchBarResultIndex
                    );

                    MapViewerActivity.this.selectedLocationNameTextView.setText(
                            MapViewerActivity.this.selectedLocationInfo.name
                    );

                    MapViewerActivity.this.selectedLocationAddressTextView.setText(
                            "Address: " + MapViewerActivity.this.selectedLocationInfo.address
                    );

                    MapViewerActivity.this.selectedLocationPostalCodeTextView.setText(
                            "Postal code: " + MapViewerActivity.this.selectedLocationInfo.postalCode
                    );

                    if (FavouriteLocations.instance.hasLocation(MapViewerActivity.this.selectedLocationInfo) == true)
                    {
                        MapViewerActivity.this.toggleLocationFavouriteStatusButton.setText(
                                getString(R.string.toggle_location_favourite_status_button_remove_mode_text)
                        );
                    }
                    else
                    {
                        MapViewerActivity.this.toggleLocationFavouriteStatusButton.setText(
                                getString(R.string.toggle_location_favourite_status_button_add_mode_text)
                        );
                    }

                    MapViewerActivity.this.selectedLocationScrollView.setVisibility(View.VISIBLE);

                    MapViewerActivity.this.searchBarResultsRecyclerView.setVisibility(
                        View.INVISIBLE
                    );
                }
            }
        );

        this.searchBarResultsRecyclerView.setAdapter(
                this.searchBarResultsAdapter
        );

        this.searchBarResultsRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        this.googleMap.moveCamera(
            CameraUpdateFactory.newCameraPosition(
                new CameraPosition(
                    SINGAPORE_CENTER_LATLNG,
                    10,
                    0,
                    0
                )
            )
        );

        /*
        TODO: Implement procedure to show alert dialog box to user prompting them to indicate
        whether they would be alright with granting the permissions requested below, as well
        as stating the reason for needing them (and what features would be disabled if the user
        chose to not grant the requested permissions to the application).
        TODO: Implement procedure for handling cases where the user still chose to deny the
        application access to the requested permissions below.
        */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED)
            {
                requestPermissions(
                    new String[] {
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE
                );

                return;
            }

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
            {
                requestPermissions(
                    new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE
                );

                return;
            }
        }

        this.googleMap.setMyLocationEnabled(true);
    }
}