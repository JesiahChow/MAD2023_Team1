package sg.edu.np.mad.madassignmentteam1;

import android.content.Context;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

import sg.edu.np.mad.madassignmentteam1.utilities.LocationInfoUtility;
import sg.edu.np.mad.madassignmentteam1.utilities.LoggerUtility;

public class FavouriteLocations {
    private final String favouriteLocationsFileName = "favourite_locations.txt";

    private Context context;

    public static FavouriteLocations instance;

    public ArrayList<LocationInfo> favouriteLocationsLocationInfoArrayList = new ArrayList<>();

    private String getFavouriteLocationsAsJsonString()
    {
        JSONObject rootJsonObject = new JSONObject();

        LocationInfo currentFavouriteLocationInfo;

        for (int currentIndex = 0; currentIndex < this.favouriteLocationsLocationInfoArrayList.size(); currentIndex++)
        {
            currentFavouriteLocationInfo = this.favouriteLocationsLocationInfoArrayList.get(
                currentIndex
            );

            try
            {
                rootJsonObject.put(
                    currentFavouriteLocationInfo.name,
                    LocationInfoUtility.getLocationInfoAsJsonString(
                        currentFavouriteLocationInfo
                    )
                );
            }
            catch (Exception exception)
            {
                LoggerUtility.logInformation(
                    "Error: Failed to put value into root JSON object."
                );

                LoggerUtility.logException(exception);
            }
        }

        return rootJsonObject.toString();
    }

    private ArrayList<LocationInfo> getFavouriteLocationsFromJsonString(String favouriteLocationsJsonString)
    {
        LoggerUtility.logInformation("Currently in getFavouriteLocationsFromJsonString method");

        ArrayList<LocationInfo> favouriteLocations = new ArrayList<>();

        JSONObject favouriteLocationsRootJsonObject;

        try
        {
            favouriteLocationsRootJsonObject = new JSONObject(
                favouriteLocationsJsonString
            );
        }
        catch (Exception exception)
        {
            LoggerUtility.logInformation("Exception occurred while creating favourite locations root JSON object.");

            LoggerUtility.logInformation("JSON string: " + favouriteLocationsJsonString);

            LoggerUtility.logException(exception);

            LoggerUtility.logInformation("Returning null value for favouriteLocations.");

            return null;
        }

        Iterator<String> favouriteLocationsRootJsonObjectKeyIterator = favouriteLocationsRootJsonObject.keys();

        String currentFavouriteLocationsRootJsonObjectKey;

        try
        {
            while ((currentFavouriteLocationsRootJsonObjectKey = favouriteLocationsRootJsonObjectKeyIterator.next()) != null)
            {
                try
                {
                    favouriteLocations.add(
                            LocationInfoUtility.getLocationInfoFromJsonString(
                                    favouriteLocationsRootJsonObject.getString(currentFavouriteLocationsRootJsonObjectKey)
                            )
                    );
                }
                catch (Exception exception)
                {
                    LoggerUtility.logInformation("Error: Exception occurred while adding new LocationInfo from JSON string.");

                    LoggerUtility.logException(exception);

                    LoggerUtility.logInformation("Returning null value for favouriteLocations.");

                    return null;
                }
            }
        }
        catch (Exception exception)
        {
            LoggerUtility.logInformation("Iterator<String> load process error.");

            LoggerUtility.logException(exception);
        }

        return favouriteLocations;
    }

    private File getFavouriteLocationsFile()
    {
        File internalStorageDirectory = this.context.getFilesDir();

        if (internalStorageDirectory == null)
        {
            LoggerUtility.logInformation("Error: Failed to retrieve internal storage directory.");

            return null;
        }

        return new File(
            internalStorageDirectory.getParent(),
            this.favouriteLocationsFileName
        );
    }

    public FavouriteLocations(Context context)
    {
        this.context = context;

        this.loadFromUserDeviceInternalStorage();
    }

    public boolean hasLocation(LocationInfo locationInfo)
    {
        for (int currentFavouriteLocationInfoIndex = 0; currentFavouriteLocationInfoIndex < this.favouriteLocationsLocationInfoArrayList.size(); currentFavouriteLocationInfoIndex++)
        {
            // if (this.favouriteLocationsLocationInfoArrayList.get(currentFavouriteLocationInfoIndex).equals(locationInfo) == true)
            if (this.favouriteLocationsLocationInfoArrayList.get(currentFavouriteLocationInfoIndex).equals(locationInfo))
            {
                return true;
            }
        }

        return false;
    }

    public boolean loadFromUserDeviceInternalStorage()
    {
        File favouriteLocationsFile = this.getFavouriteLocationsFile();

        if (favouriteLocationsFile == null)
        {
            LoggerUtility.logInformation(
                "Error: Failed to retrieve favourite locations cache file."
            );

            return false;
        }

        FileInputStream favouriteLocationsFileInputStream;

        try
        {
            favouriteLocationsFileInputStream = new FileInputStream(
                    favouriteLocationsFile
            );
        }
        catch (Exception exception)
        {
            LoggerUtility.logInformation(
                "Error: Failed to retrieve FileInputStream for favourite locations cache file."
            );

            LoggerUtility.logException(exception);

            return false;
        }

        byte[] favouriteLocationsFileBytes = new byte[(int) favouriteLocationsFile.length()];

        try
        {
            favouriteLocationsFileInputStream.read(favouriteLocationsFileBytes);
        }
        catch (Exception exception)
        {
            LoggerUtility.logInformation(
                "Error: Failed to read bytes from FileInputStream for favourite locations cache file."
            );

            LoggerUtility.logException(exception);

            return false;
        }

        try
        {
            favouriteLocationsFileInputStream.close();
        }
        catch (Exception exception)
        {
            LoggerUtility.logInformation(
                "Error: Failed to close FileInputStream for favourite locations cache file."
            );

            LoggerUtility.logException(exception);

            return false;
        }

        try
        {
            LoggerUtility.logInformation("Favourite locations bytes as String: " + new String(favouriteLocationsFileBytes, StandardCharsets.UTF_8));
        }
        catch (Exception exception)
        {
            LoggerUtility.logException(exception);
        }

        /*
        try
        {
            this.favouriteLocationsLocationInfoArrayList = this.getFavouriteLocationsFromJsonString(
                new String(favouriteLocationsFileBytes, StandardCharsets.UTF_8)
            );

            if (this.favouriteLocationsLocationInfoArrayList == null)
            {
                LoggerUtility.logInformation(
                    "Attempted to initialize favourite locations ArrayList of a FavouriteLocations instance. Got null value instead. Updating field accordingly..."
                );

                this.favouriteLocationsLocationInfoArrayList = new ArrayList<>();
            }
        }
        catch (Exception exception)
        {
            LoggerUtility.logInformation(
                "Error: Exception occurred while attempting to initialize favourite locations ArrayList of a FavouriteLocations instance."
            );

            LoggerUtility.logInformation("Exception type: " + exception.getClass().getName());

            LoggerUtility.logException(exception);

            return false;
        }
        */

        this.favouriteLocationsLocationInfoArrayList = this.getFavouriteLocationsFromJsonString(
                new String(favouriteLocationsFileBytes, StandardCharsets.UTF_8)
        );

        if (this.favouriteLocationsLocationInfoArrayList == null)
        {
            LoggerUtility.logInformation(
                    "Attempted to initialize favourite locations ArrayList of a FavouriteLocations instance. Got null value instead. Updating field accordingly..."
            );

            this.favouriteLocationsLocationInfoArrayList = new ArrayList<>();
        }

        return true;
    }

    /*
    Note: Currently only supports saving the favourite locations data for a single user (the
    user currently logged into the application). Will be updated to support multiple users in
    the future.
    */
    public boolean saveToUserDeviceInternalStorage()
    {
        File favouriteLocationsFile = this.getFavouriteLocationsFile();

        if (favouriteLocationsFile == null)
        {
            return false;
        }

        FileOutputStream favouriteLocationsFileOutputStream;

        try
        {
            favouriteLocationsFileOutputStream = new FileOutputStream(
                    favouriteLocationsFile
            );
        }
        catch (Exception exception)
        {
            LoggerUtility.logInformation(
                    "Error: Failed to retrieve FileOutputStream for favourite locations cache file."
            );

            LoggerUtility.logException(exception);

            return false;
        }

        boolean wroteBytesSuccessfully = true;

        try
        {
            favouriteLocationsFileOutputStream.write(
                this.getFavouriteLocationsAsJsonString().getBytes(StandardCharsets.UTF_8)
            );
        }
        catch (Exception exception)
        {
            LoggerUtility.logInformation(
                "Error: Failed to write bytes to favourite locations cache file."
            );

            LoggerUtility.logException(exception);

            wroteBytesSuccessfully = false;
        }

        try
        {
            favouriteLocationsFileOutputStream.close();
        }
        catch (Exception exception2)
        {
            LoggerUtility.logInformation(
                    "Failed to close FileOutputStream for favourite locations cache file."
            );

            LoggerUtility.logException(exception2);
        }

        return wroteBytesSuccessfully;
    }
}
