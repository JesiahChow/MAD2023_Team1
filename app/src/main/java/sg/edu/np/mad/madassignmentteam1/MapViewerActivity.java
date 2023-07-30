package sg.edu.np.mad.madassignmentteam1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import sg.edu.np.mad.madassignmentteam1.utilities.LocationInfoUtility;
import sg.edu.np.mad.madassignmentteam1.utilities.LoggerUtility;
import sg.edu.np.mad.madassignmentteam1.utilities.NavigationUtility;
import sg.edu.np.mad.madassignmentteam1.utilities.StringUtility;

public class MapViewerActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private static final int DEFAULT_GOOGLE_MAP_POLYLINE_WIDTH = 12;

    private static final int DEFAULT_GOOGLE_MAP_POLYLINE_COLOR = Color.BLUE;

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

    private AppCompatButton findDirectionsButton = null;

    private ScrollView foundRoutesScrollView = null;

    private ImageButton foundRoutesCloseButton = null;

    private RecyclerView foundRoutesRecyclerView = null;

    private FoundRoutesAdapter foundRoutesAdapter = null;

    private ScrollView routeDetailsScrollView = null;

    private ImageButton routeDetailsCloseButton = null;

    private RecyclerView routeInstructionsRecyclerView = null;

    private TextView routeDetailsHeaderTextView = null;

    private Marker selectedRouteOriginLocationMarker = null;

    private Marker selectedRouteDestinationLocationMarker = null;

    private Polyline selectedRoutePolyline = null;

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
            View.GONE
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
                    if (selectedLocationInfo.currentGoogleMapsMarker != null)
                    {
                        selectedLocationInfo.currentGoogleMapsMarker.remove();

                        selectedLocationInfo.currentGoogleMapsMarker = null;
                    }

                    MapViewerActivity.this.selectedLocationScrollView.setVisibility(
                        View.GONE
                    );
                }
            }
        );

        this.findDirectionsButton = findViewById(R.id.FindDirectionsButton);

        this.findDirectionsButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(
                        MapViewerActivity.this,
                        FastestRouteActivity.class
                    );

                    MapViewerActivity.this.startActivity(intent);
                }
            }
        );

        this.foundRoutesScrollView = this.findViewById(R.id.FoundRoutesScrollView);

        this.foundRoutesScrollView.setVisibility(View.GONE);

        this.foundRoutesCloseButton = this.findViewById(R.id.FoundRoutesCloseButton);

        this.foundRoutesCloseButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MapViewerActivity.this.foundRoutesScrollView.setVisibility(
                        View.GONE
                    );
                }
            }
        );

        this.foundRoutesRecyclerView = this.findViewById(R.id.FoundRoutesRecyclerView);

        this.foundRoutesRecyclerView.setLayoutManager(
            new LinearLayoutManager(this)
        );

        this.routeDetailsScrollView = this.findViewById(R.id.RouteDetailsScrollView);

        this.routeDetailsScrollView.setVisibility(View.GONE);

        this.routeDetailsCloseButton = this.findViewById(R.id.RouteDetailsCloseButton);

        this.routeDetailsCloseButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MapViewerActivity.this.routeDetailsScrollView.setVisibility(
                        View.GONE
                    );
                }
            }
        );

        this.routeInstructionsRecyclerView = this.findViewById(R.id.RouteInstructionsRecyclerView);

        this.routeInstructionsRecyclerView.setLayoutManager(
            new LinearLayoutManager(this)
        );

        this.routeDetailsHeaderTextView = this.findViewById(R.id.RouteDetailsHeaderTextView);

        Bundle intentExtras = this.getIntent().getExtras();

        if (intentExtras != null)
        {
            NavigationUtility.instance.getRoutesAsync(
                intentExtras.getString("origin_location"),
                intentExtras.getString("destination_location"),
                (NavigationUtility.TransportModes) intentExtras.get("transport_mode"),
                new NavigationUtility.RoutesFoundListener() {
                    @Override
                    public void onRoutesFound(ArrayList<NavigationUtility.Route> routes) {

                    }

                    @Override
                    public void onRoutesFoundMainThread(ArrayList<NavigationUtility.Route> routes) {
                        MapViewerActivity.this.foundRoutesAdapter = new FoundRoutesAdapter(
                            routes,
                            new FoundRoutesAdapter.RouteSelectedListener() {
                                @Override
                                public void onRouteSelected(NavigationUtility.Route selectedRoute) {
                                    MapViewerActivity.this.routeInstructionsRecyclerView.setAdapter(
                                        new RouteInstructionsAdapter(selectedRoute)
                                    );

                                    MapViewerActivity.this.routeDetailsHeaderTextView.setText(
                                        selectedRoute.startLocationName + " to " + selectedRoute.endLocationName
                                    );

                                    MapViewerActivity.this.foundRoutesScrollView.setVisibility(
                                        View.GONE
                                    );

                                    MapViewerActivity.this.routeDetailsScrollView.setVisibility(
                                        View.VISIBLE
                                    );

                                    if (selectedRoute.routeSteps.size() > 0)
                                    {
                                        MarkerOptions originLocationMarkerOptions = new MarkerOptions();

                                        originLocationMarkerOptions.position(
                                            selectedRoute.routeSteps.get(0).startLocationLatLng
                                        );

                                        originLocationMarkerOptions.title(
                                            selectedRoute.startLocationName
                                        );

                                        originLocationMarkerOptions.icon(
                                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                                        );

                                        MapViewerActivity.this.selectedRouteOriginLocationMarker = MapViewerActivity.this.googleMap.addMarker(
                                            originLocationMarkerOptions
                                        );

                                        MarkerOptions destinationLocationMarkerOptions = new MarkerOptions();

                                        destinationLocationMarkerOptions.position(
                                            selectedRoute.routeSteps.get(
                                                selectedRoute.routeSteps.size() - 1
                                            ).endLocationLatLng
                                        );

                                        destinationLocationMarkerOptions.title(
                                            selectedRoute.endLocationName
                                        );

                                        destinationLocationMarkerOptions.icon(
                                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                                        );

                                        MapViewerActivity.this.selectedRouteDestinationLocationMarker = MapViewerActivity.this.googleMap.addMarker(
                                            destinationLocationMarkerOptions
                                        );

                                        PolylineOptions routePolylineOptions = new PolylineOptions();

                                        routePolylineOptions.add(
                                            selectedRoute.routeSteps.get(0).startLocationLatLng
                                        );

                                        for (int currentRouteStepIndex = 0; currentRouteStepIndex < selectedRoute.routeSteps.size(); currentRouteStepIndex++)
                                        {
                                            routePolylineOptions.add(
                                                selectedRoute.routeSteps.get(currentRouteStepIndex).endLocationLatLng
                                            );
                                        }

                                        routePolylineOptions.width(DEFAULT_GOOGLE_MAP_POLYLINE_WIDTH);

                                        routePolylineOptions.color(DEFAULT_GOOGLE_MAP_POLYLINE_COLOR);

                                        routePolylineOptions.geodesic(true);

                                        MapViewerActivity.this.selectedRoutePolyline = MapViewerActivity.this.googleMap.addPolyline(
                                            routePolylineOptions
                                        );

                                        MapViewerActivity.this.googleMap.moveCamera(
                                            CameraUpdateFactory.newCameraPosition(
                                                new CameraPosition(
                                                    MapViewerActivity.this.selectedRouteOriginLocationMarker.getPosition(),
                                                    10,
                                                    0,
                                                    0
                                                )
                                            )
                                        );
                                    }
                                }
                            }
                        );

                        MapViewerActivity.this.foundRoutesRecyclerView.setAdapter(
                            MapViewerActivity.this.foundRoutesAdapter
                        );

                        MapViewerActivity.this.foundRoutesScrollView.setVisibility(View.VISIBLE);
                    }
                },
                this
            );
        }
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

        this.googleMap.getUiSettings().setZoomGesturesEnabled(true);

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