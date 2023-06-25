package sg.edu.np.mad.madassignmentteam1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class SearchBarResultsAdapter extends RecyclerView.Adapter<SearchBarResultsAdapter.ViewHolder>
{
    private ArrayList<LocationInfo> resultsLocationInfoArrayList = null;

    private GoogleMap googleMap = null;

    private ArrayList<OnBindViewHolderListener> onBindViewHolderListeners = new ArrayList<>();

    public SearchBarResultsAdapter(ArrayList<LocationInfo> searchResultsLocationInfoArrayList, GoogleMap googleMap)
    {
        this.resultsLocationInfoArrayList = searchResultsLocationInfoArrayList;

        this.googleMap = googleMap;
    }

    public void addOnBindViewHolderListener(OnBindViewHolderListener onBindViewHolderListener)
    {
        this.onBindViewHolderListeners.add(onBindViewHolderListener);
    }

    public void removeOnBindViewHolderListener(OnBindViewHolderListener onBindViewHolderListener)
    {
        this.onBindViewHolderListeners.remove(onBindViewHolderListener);
    }

    @Override
    public SearchBarResultsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View searchBarResultView = layoutInflater.inflate(
            R.layout.search_bar_result_element,
            parent,
            false
        );

        SearchBarResultsAdapter.ViewHolder searchBarResultViewHolder = new SearchBarResultsAdapter.ViewHolder(
            searchBarResultView
        );

        return searchBarResultViewHolder;
    }

    @Override
    public void onBindViewHolder(SearchBarResultsAdapter.ViewHolder viewHolder, int position)
    {
        LocationInfo associatedResultLocationInfo = this.resultsLocationInfoArrayList.get(position);

        viewHolder.resultLocationNameTextView.setText(associatedResultLocationInfo.name);

        viewHolder.resultLocationAddressTextView.setText(associatedResultLocationInfo.address);

        viewHolder.resultLocationLatLng = associatedResultLocationInfo.latLng;

        for (int currentListenerIndex = 0; currentListenerIndex < this.onBindViewHolderListeners.size(); currentListenerIndex++)
        {
            this.onBindViewHolderListeners.get(currentListenerIndex).onBindViewHolder(
                viewHolder,
                position
            );
        }
    }

    @Override
    public int getItemCount()
    {
        return this.resultsLocationInfoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private LinearLayout resultLocationLinearLayout = null;

        public TextView resultLocationNameTextView = null;

        public TextView resultLocationAddressTextView = null;

        public LatLng resultLocationLatLng = new LatLng(0, 0);

        public ViewHolder(View itemView)
        {
            super(itemView);

            this.resultLocationLinearLayout = itemView.findViewById(R.id.ResultLocationLinearLayout);

            this.resultLocationLinearLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        googleMap.animateCamera(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition.builder().target(
                                    ViewHolder.this.resultLocationLatLng
                                ).zoom(14).build()
                            )
                        );
                    }
                }
            );

            this.resultLocationNameTextView = itemView.findViewById(R.id.ResultLocationNameTextView);

            this.resultLocationAddressTextView = itemView.findViewById(R.id.ResultLocationAddressTextView);
        }
    }

    public interface OnBindViewHolderListener
    {
        public void onBindViewHolder(SearchBarResultsAdapter.ViewHolder viewHolder, int position);
    }
}
