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

    /**
     * Generates and returns a JSON-formatted string representation of the value of the
     * favouriteLocationsLocationInfoArrayList field of the FavouriteLocations instance.
     * @return A String instance.
     */
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

    /**
     * Parses a JSON string and extracts location information for each of the favourite locations
     * of a user. The JSON string should preferably be generated using the
     * getFavouriteLocationsAsJsonString method of the FavouriteLocations class.
     * @param favouriteLocationsJsonString
     * @return An ArrayList of LocationInfo instances.
     */
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

    /**
     * Retrieves the file used to store data about a user's favourite locations locally.
     * @return A File instance.
     */
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

    /**
     * The default constructor method for the FavouriteLocations class. This constructor method
     * should be called and have its return value be assigned to the instance field of the
     * FavouriteLocations class as soon as an Activity has been started. This action only needs
     * to be performed once and should preferably be performed when the onCreate method of the
     * FavouriteLocationsActivity class is called.
     * @param context
     */
    public FavouriteLocations(Context context)
    {
        this.context = context;

        this.loadFromUserDeviceInternalStorage();
    }

    /**
     * Checks if a location is one of the favourite locations of a user. Returns true if the location
     * is one of the user's favourite locations, returns false otherwise.
     * @param locationInfo
     * @return A boolean value.
     */
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

    /**
     * Loads the saved data regarding the favourite locations of a user, this data is stored in a file
     * that is stored locally on the user's device. This method returns true if the saved data has
     * been loaded successfully, otherwise it returns false if the saved data has not been loaded
     * successfully or if the file containing the saved data has not been found.
     * @return A boolean value.
     */
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

    /**
     * Saves the data regarding a user's favourite locations to a file that is then stored locally
     * on the user's device. This method returns true if the data was saved successfully, otherwise
     * it returns false.
     * @return A boolean value.
     * @implNote The current implementation of this method only supports saving the data regarding
     * the favourite locations of the user currently logged into the application, this method
     * does not support saving data regarding the favourite locations of multiple users yet
     * (in the case of someone who might log in and out of different accounts while using the
     * application on the same device). However, this functionality is planned to be introduced
     * and included in future releases.
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
