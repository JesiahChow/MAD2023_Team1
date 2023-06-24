package sg.edu.np.mad.madassignmentteam1;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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