package sg.edu.np.mad.madassignmentteam1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {

    private List<Programme> programmes;
    private Context context;

    public RecommendAdapter(List<Programme> programmes, Context context) {
        this.programmes = programmes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recommendview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Programme programme = programmes.get(position);
        holder.titleTextView.setText(programme.getTitle());
        holder.descriptionTextView.setText(programme.getDescription());
    }

    @Override
    public int getItemCount() {
        return programmes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTextView;
        TextView descriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            descriptionTextView = itemView.findViewById(R.id.description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Get the clicked item's position
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                // Get the selected item from the list
                Programme selectedProgramme = programmes.get(position);

                // Create an intent to start the RecommendDetails activity
                Intent intent = new Intent(context, RecommendDetails.class);
                intent.putExtra("title", selectedProgramme.getTitle());
                intent.putExtra("description", selectedProgramme.getDescription());

                // Start the RecommendDetails activity
                context.startActivity(intent);
            }
        }
    }
}
