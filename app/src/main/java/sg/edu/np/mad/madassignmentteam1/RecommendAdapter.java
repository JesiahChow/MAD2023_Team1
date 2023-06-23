package sg.edu.np.mad.madassignmentteam1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {

    private final RecommendViewInterface recommendViewInterface;
    private List<Programme> programmeList;

    public RecommendAdapter(List<Programme> programmeList, RecommendViewInterface recommendViewInterface) {
        this.programmeList = programmeList;
        this.recommendViewInterface = recommendViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_view, parent, false);
        return new ViewHolder(view, recommendViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Programme programme = programmeList.get(position);
        holder.titleTextView.setText(programme.getTitle());
        holder.descriptionTextView.setText(programme.getDescription());
        // Set other views as needed
    }

    @Override
    public int getItemCount() {
        return programmeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;

        public ViewHolder(@NonNull View itemView, RecommendViewInterface recommendViewInterface) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            descriptionTextView = itemView.findViewById(R.id.description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recommendViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recommendViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}

