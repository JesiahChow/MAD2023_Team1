package sg.edu.np.mad.madassignmentteam1;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CarParkViewHolder extends RecyclerView.ViewHolder {
    private TextView carparkDescTextView;
    private TextView carAvailableTextView;
    private TextView motorAvailableTextView;
    private TextView truckAvailableTextView;
    private ImageView carImageView;
    private ImageView motorImageView;
    private ImageView truckImageView;

    public CarParkViewHolder(View itemView) {
        super(itemView);
        carparkDescTextView = itemView.findViewById(R.id.carparkDesc);
        carAvailableTextView = itemView.findViewById(R.id.carAvailable);
        motorAvailableTextView = itemView.findViewById(R.id.motorAvailable);
        truckAvailableTextView = itemView.findViewById(R.id.truckAvailable);
        carImageView = itemView.findViewById(R.id.carImageView);
        motorImageView = itemView.findViewById(R.id.motorImageView);
        truckImageView = itemView.findViewById(R.id.truckImageView);
    }
    // Rest of the code
}
