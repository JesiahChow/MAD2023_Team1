package sg.edu.np.mad.madassignmentteam1;

import com.google.android.gms.maps.model.LatLng;

/**
 * The LocationInfo class stores information about a particular location, this information
 * includes the location's name, address, latitude, longitude, postal code, and Google Maps Place ID
 * (if it has one) which can be used in subsequent requests to Google Maps' Places API (if necessary).
 */
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

    /**
     * Checks if the location associated with the current LocationInfo instance is one of the user's
     * favourite locations. This method returns true if the associated location is one of the user's
     * favourite locations, otherwise this method returns false.
     * @return A boolean value.
     */
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
