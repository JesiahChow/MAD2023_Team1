package sg.edu.np.mad.madassignmentteam1;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecommendViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView titleView, descriptionView;

    public RecommendViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview);
        titleView = itemView.findViewById(R.id.title);
        descriptionView = itemView.findViewById(R.id.description);
    }
}
