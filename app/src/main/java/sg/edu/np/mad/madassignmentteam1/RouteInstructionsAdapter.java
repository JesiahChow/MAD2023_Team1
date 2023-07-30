package sg.edu.np.mad.madassignmentteam1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.madassignmentteam1.utilities.NavigationUtility;
import sg.edu.np.mad.madassignmentteam1.utilities.NavigationUtility.Route;

public class RouteInstructionsAdapter extends RecyclerView.Adapter<RouteInstructionsAdapter.ViewHolder>
{
    private Route route = null;

    public RouteInstructionsAdapter(Route route)
    {
        this.route = route;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                R.layout.route_instruction_element,
                parent,
                false
            )
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        NavigationUtility.RouteStep routeStep = this.route.routeSteps.get(position);

        holder.routeInstructionTextView.setText(routeStep.instruction);
    }

    @Override
    public int getItemCount()
    {
        return this.route.routeSteps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView routeInstructionTextView = null;

        public ViewHolder(View itemView)
        {
            super(itemView);

            this.routeInstructionTextView = itemView.findViewById(
                R.id.RouteInstructionTextView
            );
        }
    }
}
