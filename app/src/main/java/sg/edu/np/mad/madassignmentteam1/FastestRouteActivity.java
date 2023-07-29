package sg.edu.np.mad.madassignmentteam1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import sg.edu.np.mad.madassignmentteam1.utilities.LoggerUtility;
import sg.edu.np.mad.madassignmentteam1.utilities.NavigationUtility;
import sg.edu.np.mad.madassignmentteam1.utilities.StringUtility;

public class FastestRouteActivity extends AppCompatActivity {
    private AppCompatButton findRouteButton = null;

    private AppCompatButton fastestRouteActivityToHomeActivityButton = null;

    private Spinner transportModeSpinner = null;

    private ArrayAdapter<String> transportModeArrayAdapter = null;

    private EditText originLocationTextEdit = null;

    private EditText destinationLocationTextEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fastest_route);

        this.findRouteButton = this.findViewById(R.id.FindRouteButton);

        this.fastestRouteActivityToHomeActivityButton = this.findViewById(
            R.id.FastestRouteActivityToHomeActivityButton
        );

        this.transportModeSpinner = this.findViewById(R.id.TransportModeSpinner);

        this.originLocationTextEdit = this.findViewById(R.id.OriginLocationTextEdit);

        this.destinationLocationTextEdit = this.findViewById(R.id.DestinationLocationTextEdit);

        this.findRouteButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(
                        FastestRouteActivity.this,
                        MapViewerActivity.class
                    );

                    intent.putExtra(
                        "origin_location",
                        FastestRouteActivity.this.originLocationTextEdit.getText().toString()
                    );

                    intent.putExtra(
                        "destination_location",
                        FastestRouteActivity.this.destinationLocationTextEdit.getText().toString()
                    );

                    intent.putExtra(
                        "transport_mode",
                        NavigationUtility.instance.getAsTransportModesEnum(
                            FastestRouteActivity.this.transportModeSpinner.getSelectedItem().toString()
                        )
                    );

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
    }
}