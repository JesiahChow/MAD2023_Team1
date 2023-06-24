package sg.edu.np.mad.madassignmentteam1;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
/*
public class BusArrivalAdapter extends RecyclerView.Adapter<BusArrivalAdapter.ViewHolder> {
    private ArrayList<BusArrivalResponse> busArrivalList;

    public BusArrivalAdapter(ArrayList<BusArrivalResponse> busArrivalList) {
        this.busArrivalList = busArrivalList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_busarrival, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BusArrivalResponse busArrival = busArrivalList.get(position);

        holder.busStopCodeTextView.setText(busArrival.getBusStopCode());
        holder.serviceNoTextView.setText(busArrival.getServiceNo());
        holder.nextBusTextView.setText(busArrival.getNextBus());
        holder.nextBus2TextView.setText(busArrival.getNextBus2());
        holder.nextBus3TextView.setText(busArrival.getNextBus3());
    }

    @Override
    public int getItemCount() {
        return busArrivalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView busStopCodeTextView;
        public TextView serviceNoTextView;
        public TextView nextBusTextView;
        public TextView nextBus2TextView;
        public TextView nextBus3TextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            busStopCodeTextView = itemView.findViewById(R.id.busStopCodeTextView);
            serviceNoTextView = itemView.findViewById(R.id.serviceNoTextView);
            nextBusTextView = itemView.findViewById(R.id.nextBusTextView);
            nextBus2TextView = itemView.findViewById(R.id.nextBus2TextView);
            nextBus3TextView = itemView.findViewById(R.id.nextBus3TextView);
        }
    }
}
*/