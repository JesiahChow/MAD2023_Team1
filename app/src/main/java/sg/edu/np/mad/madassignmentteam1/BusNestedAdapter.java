package sg.edu.np.mad.madassignmentteam1;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BusNestedAdapter extends RecyclerView.Adapter<BusNestedAdapter.busNested_adapterViewHolder>  {

    private List<BusArrivalModel> busArrivalModelList;


    public BusNestedAdapter(List<BusArrivalModel> busArrivalModelList) {
        this.busArrivalModelList = busArrivalModelList;

    }


    @NonNull
    @Override
    public busNested_adapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.nestedbusarrival_item , parent , false);
        return new busNested_adapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull busNested_adapterViewHolder holder, int position) {
        BusArrivalModel busArrival = busArrivalModelList.get(position);
        holder.serviceno.setText(busArrival.ServiceNo);


        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();

        // Format the time using SimpleDateFormat
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentdatetime = dateFormat.format(currentTime);

        Log.d("Current Time", currentdatetime);


        if(!Objects.equals(busArrival.EstimateArrivalTime1, "")) {
            String formattedTime1="";
            long differenceMinutes1=0;
            try {
                // Parse the input time into a Date object
                SimpleDateFormat inputFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+08:00");
                Date date1 = inputFormat1.parse(busArrival.EstimateArrivalTime1);
                // Convert the Date object into the desired format
                SimpleDateFormat outputFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                formattedTime1 = outputFormat1.format(date1);


                long currentTimeMillis = currentTime.getTime();
                long formattedTimeMillis = date1.getTime();
                long differenceMillis = formattedTimeMillis - currentTimeMillis;
                differenceMinutes1 = differenceMillis / (60 * 1000);


                Log.d("Converted Time", formattedTime1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.extimate_time1.setText(String.valueOf(differenceMinutes1)+" Min");
        }
        else{
            holder.extimate_time1.setText("NA");
        }

        if(!Objects.equals(busArrival.EstimateArrivalTime2, "")) {
            String formattedTime1="";
            long differenceMinutes1=0;
            try {
                // Parse the input time into a Date object
                SimpleDateFormat inputFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+08:00");
                Date date1 = inputFormat1.parse(busArrival.EstimateArrivalTime2);
                // Convert the Date object into the desired format
                SimpleDateFormat outputFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                formattedTime1 = outputFormat1.format(date1);


                long currentTimeMillis = currentTime.getTime();
                long formattedTimeMillis = date1.getTime();
                long differenceMillis = formattedTimeMillis - currentTimeMillis;
                differenceMinutes1 = differenceMillis / (60 * 1000);


                Log.d("Converted Time", formattedTime1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.extimate_time2.setText(String.valueOf(differenceMinutes1)+" Min");
        }
        else{
            holder.extimate_time2.setText("NA");
        }
        if(!Objects.equals(busArrival.EstimateArrivalTime3, "")) {
            String formattedTime1="";
            long differenceMinutes1=0;
            try {
                // Parse the input time into a Date object
                SimpleDateFormat inputFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+08:00");
                Date date1 = inputFormat1.parse(busArrival.EstimateArrivalTime3);
                // Convert the Date object into the desired format
                SimpleDateFormat outputFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                formattedTime1 = outputFormat1.format(date1);


                long currentTimeMillis = currentTime.getTime();
                long formattedTimeMillis = date1.getTime();
                long differenceMillis = formattedTimeMillis - currentTimeMillis;
                differenceMinutes1 = differenceMillis / (60 * 1000);


                Log.d("Converted Time", formattedTime1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.extimate_time3.setText(String.valueOf(differenceMinutes1)+" Min");
        }
        else{
            holder.extimate_time3.setText("NA");
        }


    }

    @Override
    public int getItemCount() {
        return busArrivalModelList.size();
    }
    public class busNested_adapterViewHolder extends RecyclerView.ViewHolder{
        TextView serviceno,extimate_time1,extimate_time2,extimate_time3;
        public busNested_adapterViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceno = itemView.findViewById(R.id.serviceno);
            extimate_time1 = itemView.findViewById(R.id.extimate_time1);
            extimate_time2 = itemView.findViewById(R.id.extimate_time2);
            extimate_time3 = itemView.findViewById(R.id.extimate_time3);


        }
    }
}
