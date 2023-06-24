package sg.edu.np.mad.madassignmentteam1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CarParkAdapter extends RecyclerView.Adapter<CarParkViewHolder> {
    private List<CarParkAvailability> carParkAvailabilityList;
    private LayoutInflater inflater;

    public CarParkAdapter(CarParkActivity context, List<CarParkAvailability> carParkAvailabilityList) {
        this.carParkAvailabilityList = carParkAvailabilityList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public sg.edu.np.mad.madassignmentteam1.CarParkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_carpark_layout, null, false);
        return new sg.edu.np.mad.madassignmentteam1.CarParkViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull sg.edu.np.mad.madassignmentteam1.CarParkViewHolder holder, int position) {
        //getCarParkAvailability();
        CarParkAvailability carpark_lots = carParkAvailabilityList.get(position);

        holder.CarparkDescription.setText(carpark_lots.Development);

        if (carpark_lots.LotType.equals("C")){
            holder.CarLotsAvailable.setText(carpark_lots.AvailableLots);
            holder.MotorLotsAvailable.setText("---");
            holder.TruckLotsAvailable.setText("---");
        }
        else if (carpark_lots.LotType.equals("Y")){
            holder.CarLotsAvailable.setText("---");
            holder.MotorLotsAvailable.setText(carpark_lots.AvailableLots);
            holder.TruckLotsAvailable.setText("---");
        }
        else{
            holder.CarLotsAvailable.setText("---");
            holder.MotorLotsAvailable.setText("---");
            holder.TruckLotsAvailable.setText(carpark_lots.AvailableLots);
        }

    }

    @Override
    public int getItemCount() {
        return carParkAvailabilityList.size();
    }

}
