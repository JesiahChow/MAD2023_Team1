package sg.edu.np.mad.madassignmentteam1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.madassignmentteam1.utilities.LoggerUtility;

public class FavouriteLocationsAdapter extends RecyclerView.Adapter<FavouriteLocationsAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatButton removeFromFavouriteLocationsButton = null;

        public TextView favouriteLocationNameTextView = null;

        public TextView favouriteLocationAddressTextView = null;

        public TextView favouriteLocationPostalCodeTextView = null;

        public ViewHolder(View itemView)
        {
            super(itemView);

            this.removeFromFavouriteLocationsButton = (AppCompatButton) itemView.findViewById(R.id.RemoveFromFavouriteLocationsButton);

            this.removeFromFavouriteLocationsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int adapterPosition = ViewHolder.this.getAdapterPosition();

                        FavouriteLocations.instance.favouriteLocationsLocationInfoArrayList.remove(
                            FavouriteLocations.instance.favouriteLocationsLocationInfoArrayList.get(
                                adapterPosition
                            )
                        );

                        FavouriteLocationsAdapter.this.notifyItemRemoved(adapterPosition);
                    }
                }
            );

            this.favouriteLocationNameTextView = (TextView) itemView.findViewById(R.id.FavouriteLocationNameTextView);

            this.favouriteLocationAddressTextView = (TextView) itemView.findViewById(R.id.FavouriteLocationAddressTextView);

            this.favouriteLocationPostalCodeTextView = (TextView) itemView.findViewById(R.id.FavouriteLocationPostalCodeTextView);
        }
    }

    public FavouriteLocationsAdapter()
    {
        super();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(
                R.layout.favourite_location_list_element,
                parent,
                false
            )
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LocationInfo associatedLocationInfo = FavouriteLocations.instance.favouriteLocationsLocationInfoArrayList.get(
            position
        );

        holder.favouriteLocationNameTextView.setText(
            associatedLocationInfo.name
        );

        holder.favouriteLocationAddressTextView.setText(
            "Address: " + associatedLocationInfo.address
        );

        holder.favouriteLocationPostalCodeTextView.setText(
            "Postal code: " + associatedLocationInfo.postalCode
        );
    }

    @Override
    public int getItemCount() {
        return FavouriteLocations.instance.favouriteLocationsLocationInfoArrayList.size();
    }
}
