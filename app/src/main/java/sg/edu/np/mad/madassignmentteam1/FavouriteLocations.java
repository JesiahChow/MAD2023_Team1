package sg.edu.np.mad.madassignmentteam1;

import java.util.ArrayList;

import sg.edu.np.mad.madassignmentteam1.utilities.LoggerUtility;

public class FavouriteLocations {
    public static FavouriteLocations instance = new FavouriteLocations();

    public ArrayList<LocationInfo> favouriteLocationsLocationInfoArrayList = new ArrayList<>();

    public FavouriteLocations()
    {

    }

    public boolean hasLocation(LocationInfo locationInfo)
    {
        for (int currentFavouriteLocationInfoIndex = 0; currentFavouriteLocationInfoIndex < this.favouriteLocationsLocationInfoArrayList.size(); currentFavouriteLocationInfoIndex++)
        {
            if (this.favouriteLocationsLocationInfoArrayList.get(currentFavouriteLocationInfoIndex).equals(locationInfo) == true)
            {
                return true;
            }
        }

        return false;
    }
}
