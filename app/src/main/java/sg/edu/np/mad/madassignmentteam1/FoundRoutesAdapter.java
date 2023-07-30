package sg.edu.np.mad.madassignmentteam1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import sg.edu.np.mad.madassignmentteam1.utilities.NavigationUtility;

public class FoundRoutesAdapter extends RecyclerView.Adapter<FoundRoutesAdapter.ViewHolder>
{
    private ArrayList<NavigationUtility.Route> routes = new ArrayList<>();

    private RouteSelectedListener routeSelectedListener = null;

    public FoundRoutesAdapter(ArrayList<NavigationUtility.Route> routes, RouteSelectedListener routeSelectedListener)
    {
        this.routes = routes;

        this.routeSelectedListener = routeSelectedListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                R.layout.found_route_element,
                parent,
                false
            )
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        NavigationUtility.Route route = this.routes.get(position);

        holder.routeTextView.setText(
            route.startLocationName + " to " + route.endLocationName
        );

        // TODO: Confirm the measurement units being used for duration and distance respectively.
        if (route.duration != 1)
        {
            holder.routeDurationTextView.setText(
                route.duration + " mins"
            );
        }
        else
        {
            holder.routeDurationTextView.setText(
                route.duration + " min"
            );
        }

        holder.routeDistanceTextView.setText(
            route.distance + " km"
        );
    }

    @Override
    public int getItemCount()
    {
        return this.routes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView routeTextView = null;

        public TextView routeDurationTextView = null;

        public TextView routeDistanceTextView = null;

        public CardView mainCardView = null;

        public ViewHolder(View itemView)
        {
            super(itemView);

            this.routeTextView = itemView.findViewById(R.id.RouteTextView);

            this.routeDurationTextView = itemView.findViewById(R.id.RouteDurationTextView);

            this.routeDistanceTextView = itemView.findViewById(R.id.RouteDistanceTextView);

            this.mainCardView = itemView.findViewById(R.id.MainCardView);

            this.mainCardView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (FoundRoutesAdapter.this.routeSelectedListener != null)
                        {
                            FoundRoutesAdapter.this.routeSelectedListener.onRouteSelected(
                                FoundRoutesAdapter.this.routes.get(
                                    ViewHolder.this.getAdapterPosition()
                                )
                            );
                        }
                    }
                }
            );
        }
    }

    public interface RouteSelectedListener
    {
        void onRouteSelected(NavigationUtility.Route selectedRoute);
    }
}
