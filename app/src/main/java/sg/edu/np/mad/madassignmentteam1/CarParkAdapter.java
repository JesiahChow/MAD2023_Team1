package sg.edu.np.mad.madassignmentteam1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CarParkAdapter extends RecyclerView.Adapter<CarParkAdapter.CarParkViewHolder> {
    private List<CarParkAvailability> carParkAvailabilityList;
    private LayoutInflater inflater;

    public CarParkAdapter(CarParkActivity context, List<CarParkAvailability> carParkAvailabilityList) {
        this.carParkAvailabilityList = carParkAvailabilityList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CarParkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = inflater.inflate(R.layout.activity_carpark_layout, parent, false);
        return new CarParkViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull CarParkViewHolder holder, int position) {
        //getCarParkAvailability();
        CarParkAvailability carpark_lots = carParkAvailabilityList.get(position);
        // Set car park description
        holder.CarparkDescription.setText(carpark_lots.Development);
        // Set available lots based on lot type
        if (carpark_lots.LotType.equals("C")) {
            holder.CarLotsAvailable.setText(carpark_lots.AvailableLots);
            holder.MotorLotsAvailable.setText("---");
            holder.TruckLotsAvailable.setText("---");
        } else if (carpark_lots.LotType.equals("Y")) {
            holder.CarLotsAvailable.setText("---");
            holder.MotorLotsAvailable.setText(carpark_lots.AvailableLots);
            holder.TruckLotsAvailable.setText("---");
        } else {
            holder.CarLotsAvailable.setText("---");
            holder.MotorLotsAvailable.setText("---");
            holder.TruckLotsAvailable.setText(carpark_lots.AvailableLots);
        }
    }

    @Override
    public int getItemCount() {
        return carParkAvailabilityList.size();
    }
    //Viewholder class
    public class CarParkViewHolder extends RecyclerView.ViewHolder {
        TextView CarparkDescription;
        TextView CarLotsAvailable;
        TextView MotorLotsAvailable;
        TextView TruckLotsAvailable;
        ImageView CarImageView;
        ImageView MotorImageView;
        ImageView TruckImageView;

        public CarParkViewHolder(@NonNull View item) {
            super(item);
            CarparkDescription = item.findViewById(R.id.carparkdescription);
            CarLotsAvailable = item.findViewById(R.id.carLots);
            MotorLotsAvailable = item.findViewById(R.id.motorLots);
            TruckLotsAvailable = item.findViewById(R.id.truckLots);
            CarImageView = item.findViewById(R.id.carImage);
            MotorImageView = item.findViewById(R.id.motorImage);
            TruckImageView = item.findViewById(R.id.truckImage);
        }
    }
}