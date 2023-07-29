package sg.edu.np.mad.madassignmentteam1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BusItemAdapter extends RecyclerView.Adapter<BusItemAdapter.busitem_adapterViewHolder> {

    private List<BusModel> busModelList;
    private LayoutInflater inflater;
    public BusItemAdapter(BusArrival context, List<BusModel> busModelList) {
        this.busModelList = busModelList;
        inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public busitem_adapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_arrival_each_item , parent , false);
        return new busitem_adapterViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull busitem_adapterViewHolder holder, int position) {
        // Get the data for the current BusModel item at the given position
        BusModel busModel = busModelList.get(position);
        BusStopModel busStopModel = busModel.busStopModelList.get(0);
        List<BusArrivalModel> busArrivalModelList = busModel.busArrivalModelList;

        // Set the Bus Stop code and road name to the corresponding TextView
        holder.busCode.setText(busStopModel.RoadName + "-" + busStopModel.BusStopCode);

        // Create and set up the nested RecyclerView with the adapter for BusArrivalModel
        BusNestedAdapter adapter = new BusNestedAdapter(busArrivalModelList);
        holder.child_bus_item.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.child_bus_item.setHasFixedSize(true);
        holder.child_bus_item.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }
    @Override
    public int getItemCount() {
        return busModelList.size();
    }
    public class busitem_adapterViewHolder extends RecyclerView.ViewHolder{
        TextView busCode;
        RecyclerView child_bus_item;
        public busitem_adapterViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize TextView and RecyclerView from the layout
            busCode = itemView.findViewById(R.id.RoadName_BusStopCode);
            child_bus_item = itemView.findViewById(R.id.child_bus_item);

        }
    }


}
