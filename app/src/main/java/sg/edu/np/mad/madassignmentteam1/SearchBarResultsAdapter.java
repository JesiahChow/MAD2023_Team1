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

    public ArrayList<OnBindViewHolderListener> onBindViewHolderListeners = new ArrayList<>();

    public ArrayList<OnSearchBarResultClickListener> onSearchBarResultClickListeners = new ArrayList<>();

    /**
     * The default constructor method for the SearchBarResultsAdapter class.
     * @param searchResultsLocationInfoArrayList
     * @param googleMap
     */
    public SearchBarResultsAdapter(ArrayList<LocationInfo> searchResultsLocationInfoArrayList, GoogleMap googleMap)
    {
        this.resultsLocationInfoArrayList = searchResultsLocationInfoArrayList;

        this.googleMap = googleMap;
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

        /**
         * The default constructor method of the SearchBarResultsAdapter.ViewHolder class.
         * @param itemView
         */
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

                        int searchBarResultIndex = ViewHolder.this.getAdapterPosition();

                        for (int currentIndex = 0; currentIndex < SearchBarResultsAdapter.this.onSearchBarResultClickListeners.size(); currentIndex++)
                        {
                            SearchBarResultsAdapter.this.onSearchBarResultClickListeners.get(currentIndex).onSearchBarResultClick(
                                ViewHolder.this.resultLocationLinearLayout,
                                searchBarResultIndex
                            );
                        }
                    }
                }
            );

            this.resultLocationNameTextView = itemView.findViewById(R.id.ResultLocationNameTextView);

            this.resultLocationAddressTextView = itemView.findViewById(R.id.ResultLocationAddressTextView);
        }
    }

    /**
     * An interface allowing for the registration of callback methods to be executed when the
     * onBindViewHolder method of the SearchBarResultsAdapter.ViewHolder class is called.
     */
    public interface OnBindViewHolderListener
    {
        /**
         * The method that is called when the onBindViewHolder method of the
         * SearchBarResultsAdapter.ViewHolder class is called.
         * @param viewHolder
         * @param position
         */
        void onBindViewHolder(SearchBarResultsAdapter.ViewHolder viewHolder, int position);
    }

    /**
     * An interface allowing for the registration of callback methods to be executed when a search
     * bar result is clicked on by a user.
     */
    public interface OnSearchBarResultClickListener
    {
        /**
         * The method that is called when a search bar result is clicked on by a user.
         * @param searchBarResultView
         * @param searchBarResultIndex
         */
        void onSearchBarResultClick(View searchBarResultView, int searchBarResultIndex);
    }
}
