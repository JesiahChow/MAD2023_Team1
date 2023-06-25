package sg.edu.np.mad.madassignmentteam1;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.GeoApiContext;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.madassignmentteam1.utilities.LoggerUtility;

public class LocationInfo
{
    private String googleMapsPlaceID = "";

    public String name = "";

    public String address = "";

    public LatLng latLng = new LatLng(0, 0);

    public String postalCode = "";

    public LocationInfo(String locationName, String locationAddress, String locationPostalCode, LatLng locationLatLng)
    {
        this.name = locationName;

        this.address = locationAddress;

        this.postalCode = locationPostalCode;

        this.latLng = locationLatLng;
    }

    public LocationInfo(String locationName, String locationAddress, String locationPostalCode, LatLng locationLatLng, String locationGoogleMapsPlaceID)
    {
        this.name = locationName;

        this.address = locationAddress;

        this.postalCode = locationPostalCode;

        this.latLng = locationLatLng;

        this.googleMapsPlaceID = locationGoogleMapsPlaceID;
    }

    public boolean isFavouriteLocation()
    {
        return FavouriteLocations.instance.hasLocation(this);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof LocationInfo == false)
        {
            return false;
        }

        LocationInfo otherLocationInfo = (LocationInfo) object;

        return this.name.equals(otherLocationInfo.name) &&
                this.address.equals(otherLocationInfo.address) &&
                this.postalCode.equals(otherLocationInfo.postalCode) &&
                this.googleMapsPlaceID.equals(otherLocationInfo.googleMapsPlaceID) &&
                this.latLng.equals(otherLocationInfo.latLng);
    }
}
