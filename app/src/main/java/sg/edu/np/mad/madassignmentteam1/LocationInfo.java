package sg.edu.np.mad.madassignmentteam1;

import com.google.android.gms.maps.model.LatLng;

public class LocationInfo
{
    public String name;

    public String address;

    public LatLng latLng;

    public String postalCode;

    public String googleMapsPlaceID;

    public LocationInfo(String locationName, String locationAddress, String locationPostalCode, LatLng locationLatLng)
    {
        this.name = locationName;

        this.address = locationAddress;

        this.postalCode = locationPostalCode;

        this.latLng = new LatLng(0, 0);

        this.latLng = locationLatLng;
    }

    public LocationInfo(String locationName, String locationAddress, String locationPostalCode, LatLng locationLatLng, String locationGoogleMapsPlaceID)
    {
        this.name = locationName;

        this.address = locationAddress;

        this.postalCode = locationPostalCode;

        this.latLng = new LatLng(0, 0);

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
        // if ((object instanceof LocationInfo) == false)
        if (!(object instanceof LocationInfo))
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
