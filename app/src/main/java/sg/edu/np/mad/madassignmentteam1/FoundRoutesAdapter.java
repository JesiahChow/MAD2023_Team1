package sg.edu.np.mad.madassignmentteam1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import sg.edu.np.mad.madassignmentteam1.utilities.NavigationUtility;

public class FoundRoutesAdapter extends RecyclerView.Adapter<FoundRoutesAdapter.ViewHolder>
{
    private ArrayList<NavigationUtility.Route> routes = new ArrayList<>();

    public FoundRoutesAdapter(ArrayList<NavigationUtility.Route> routes)
    {
        this.routes = routes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        return new ViewHolder(
            layoutInflater.inflate(
                R.layout.found_route_element,
                parent,
                false
            )
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

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

        public ViewHolder(View itemView)
        {
            super(itemView);

            this.routeTextView = itemView.findViewById(R.id.RouteTextView);

            this.routeDurationTextView = itemView.findViewById(R.id.RouteDurationTextView);

            this.routeDistanceTextView = itemView.findViewById(R.id.RouteDistanceTextView);
        }
    }
}
