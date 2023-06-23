/*package sg.edu.np.mad.madassignmentteam1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;*/

/*public class CarParkAdapter extends RecyclerView.Adapter<CarParkAdapter.CarParkViewHolder> {
    private List<CarParkAvailability> carParkAvailabilityList;
    private LayoutInflater inflater;

    public CarParkAdapter(CarParkActivity context, List<CarParkAvailability> carParkAvailabilityList) {
        this.carParkAvailabilityList = carParkAvailabilityList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CarParkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.activity_carpark_layout, parent, false);
        return new CarParkViewHolder(itemView);
    }



        @Override
        public void onBindViewHolder(CarParkViewHolder holder, int position) {
            CarParkAvailability carParkAvailability = carParkAvailabilityList.get(position);
            holder.carparkDescTextView.setText(carParkAvailabilityList.get(position).getDescription());
            holder.carAvailableTextView.setText(String.valueOf(carParkAvailability.getCarAvailability()));
            holder.motorAvailableTextView.setText(String.valueOf(carParkAvailability.getMotorcycleAvailability()));
            holder.truckAvailableTextView.setText(String.valueOf(carParkAvailability.getTruckAvailability()));
        }

    @Override
    public int getItemCount() {
        return carParkAvailabilityList.size();
    }

    static class CarParkViewHolder extends RecyclerView.ViewHolder {
        private TextView carparkDescTextView;
        private TextView carAvailableTextView;
        private TextView motorAvailableTextView;
        private TextView truckAvailableTextView;
        private ImageView carImageView;
        private ImageView motorImageView;
        private ImageView truckImageView;

        public CarParkViewHolder(@NonNull View itemView) {
            super(itemView);
            carparkDescTextView = itemView.findViewById(R.id.carparkDesc);
            carAvailableTextView = itemView.findViewById(R.id.carAvailable);
            motorAvailableTextView = itemView.findViewById(R.id.motorAvailable);
            truckAvailableTextView = itemView.findViewById(R.id.truckAvailable);
            carImageView = itemView.findViewById(R.id.carImageView);
            motorImageView = itemView.findViewById(R.id.motorImageView);
            truckImageView = itemView.findViewById(R.id.truckImageView);
        }

        public void BindViewHolder(CarParkAvailability carParkAvailability) {
            carparkDescTextView.setText(carParkAvailability.getCarParkId());
            carAvailableTextView.setText(String.valueOf(carParkAvailability.getLotsAvailable()));
            motorAvailableTextView.setText(String.valueOf(carParkAvailability.getLotsAvailable()));
            truckAvailableTextView.setText(String.valueOf(carParkAvailability.getLotsAvailable()));

            // Set the visibility of image views based on the lot availability
            if (carParkAvailability.getLotsAvailable() > 0) {
                carImageView.setVisibility(View.VISIBLE);
                carAvailableTextView.setVisibility(View.VISIBLE);
            } else {
                carImageView.setVisibility(View.GONE);
                carAvailableTextView.setVisibility(View.GONE);
            }

            if (carParkAvailability.getLotsAvailable() > 0) {
                motorImageView.setVisibility(View.VISIBLE);
                motorAvailableTextView.setVisibility(View.VISIBLE);
            } else {
                motorImageView.setVisibility(View.GONE);
                motorAvailableTextView.setVisibility(View.GONE);
            }

            if (carParkAvailability.getLotsAvailable() > 0) {
                truckImageView.setVisibility(View.VISIBLE);
                truckAvailableTextView.setVisibility(View.VISIBLE);
            } else {
                truckImageView.setVisibility(View.GONE);
                truckAvailableTextView.setVisibility(View.GONE);
            }
        }
    }
}*/
