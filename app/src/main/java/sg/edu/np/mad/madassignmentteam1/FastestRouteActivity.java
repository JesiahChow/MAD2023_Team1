package sg.edu.np.mad.madassignmentteam1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.widget.SearchView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import sg.edu.np.mad.madassignmentteam1.utilities.NavigationUtility;
import sg.edu.np.mad.madassignmentteam1.utilities.StringUtility;

public class FastestRouteActivity extends AppCompatActivity {
    private AppCompatButton findRouteButton = null;

    private AppCompatButton fastestRouteActivityToHomeActivityButton = null;

    private Spinner transportModeSpinner = null;

    private ArrayAdapter<String> transportModeArrayAdapter = null;

    private SearchView originLocationSearchView = null;

    private SearchView destinationLocationSearchView = null;

    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private ProgressDialog findRoutesLoadingProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fastest_route);

        this.findRouteButton = this.findViewById(R.id.FindRouteButton);

        this.fastestRouteActivityToHomeActivityButton = this.findViewById(
            R.id.FastestRouteActivityToHomeActivityButton
        );

        this.transportModeSpinner = this.findViewById(R.id.TransportModeSpinner);

        this.originLocationSearchView = this.findViewById(R.id.OriginLocationSearchView);

        this.destinationLocationSearchView = this.findViewById(R.id.DestinationLocationSearchView);

        this.findRouteButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FastestRouteActivity.this.mainThreadHandler.post(
                        new Runnable() {
                            @Override
                            public void run() {
                                FastestRouteActivity.this.findRoutesLoadingProgressDialog.show();
                            }
                        }
                    );

                    Intent intent = new Intent(
                        FastestRouteActivity.this,
                        MapViewerActivity.class
                    );

                    intent.putExtra(
                        "origin_location",
                        FastestRouteActivity.this.originLocationSearchView.getQuery().toString()
                    );

                    intent.putExtra(
                        "destination_location",
                        FastestRouteActivity.this.destinationLocationSearchView.getQuery().toString()
                    );

                    intent.putExtra(
                        "transport_mode",
                        NavigationUtility.instance.getAsTransportModesEnum(
                            FastestRouteActivity.this.transportModeSpinner.getSelectedItem().toString()
                        )
                    );

                    FastestRouteActivity.this.findRoutesLoadingProgressDialog.dismiss();

                    FastestRouteActivity.this.startActivity(intent);
                }
            }
        );

        this.fastestRouteActivityToHomeActivityButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FastestRouteActivity.this.startActivity(
                        new Intent(FastestRouteActivity.this, HomeActivity.class)
                    );
                }
            }
        );

        this.transportModeArrayAdapter = new ArrayAdapter<String>(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        );

        NavigationUtility.TransportModes[] transportModes = NavigationUtility.TransportModes.values();

        for (int currentModeIndex = 0; currentModeIndex < transportModes.length; currentModeIndex++)
        {
            this.transportModeArrayAdapter.add(
                StringUtility.instance.getAsCapitalizedString(
                    transportModes[currentModeIndex].toString()
                )
            );
        }

        this.transportModeSpinner.setAdapter(this.transportModeArrayAdapter);

        this.findRoutesLoadingProgressDialog = new ProgressDialog(this);

        this.findRoutesLoadingProgressDialog.setMessage("Finding routes for you. Please wait...");

        this.findRoutesLoadingProgressDialog.setCancelable(false);

        Bundle intentExtras = this.getIntent().getExtras();

        if (intentExtras != null)
        {
            String selectedDestinationLocationName = intentExtras.getString(
                "selected_destination_location_name"
            );

            if (selectedDestinationLocationName != null)
            {
                this.destinationLocationSearchView.setQuery(
                    selectedDestinationLocationName,
                    false
                );
            }
        }
    }
}