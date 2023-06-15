package sg.edu.np.mad.madassignmentteam1;



import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    ArrayList<String>featureList;
    ArrayList<Integer>imageList;
    Context context;
    public Adapter(Context context,ArrayList<String>featureList,ArrayList<Integer>imageList) {
        this.context = context;
        this.featureList = featureList;
        this.imageList = imageList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text.setText(featureList.get(position));
        holder.image.setImageResource(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return featureList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        ImageView image2;
        ImageView image;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.description);
            image = itemView.findViewById(R.id.bus_icon);
            image2 = itemView.findViewById(R.id.email_profile);

        }
    }
}

