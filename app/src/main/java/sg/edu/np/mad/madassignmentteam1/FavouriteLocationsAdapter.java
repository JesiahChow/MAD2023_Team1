package sg.edu.np.mad.madassignmentteam1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavouriteLocationsAdapter extends RecyclerView.Adapter<FavouriteLocationsAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView locationAddressTextView = null;

        public ViewHolder(View itemView)
        {
            super(itemView);

            locationAddressTextView = (TextView)itemView.findViewById(R.id.LocationAddressTextView);
        }
    }

    private ArrayList<String> favouriteLocationAddresses = null;

    public FavouriteLocationsAdapter(ArrayList<String> favouriteLocationAddressArrayList)
    {
        super();

        favouriteLocationAddresses = favouriteLocationAddressArrayList;
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
        holder.locationAddressTextView.setText(favouriteLocationAddresses.get(position));
    }

    @Override
    public int getItemCount() {
        return favouriteLocationAddresses.size();
    }
}
