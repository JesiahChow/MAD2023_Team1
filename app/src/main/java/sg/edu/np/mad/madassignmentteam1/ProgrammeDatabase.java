package sg.edu.np.mad.madassignmentteam1;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProgrammeDatabase {

    private Context context;
    private List<Programme> programmeList;
    private DataLoadListener dataLoadListener; // Interface to communicate data loading status
    private String tag;
    private String KeyWord;
    final String TAG = "ProgrammeDatabase";

    String apiKey = "h9maakGa0NsA85mqsNTZRGASCRtYwhGs";
    int offset = 0;
    int limit = 50;
    private int totalRecords = 0;
    int fetchedRecords = 0;

    private boolean isLoadingMoreData = false;

    // Retrofit instance and API service
    private ProgrammeApiService apiService;

    // Constructor with DataLoadListener parameter
    public ProgrammeDatabase(Context context, DataLoadListener dataLoadListener, String tags, String KeyWord, int offset, int limit) {
        Log.i(TAG, "Starting ProgrammeDatabase");
        programmeList = new ArrayList<>();
        this.context = context;
        this.dataLoadListener = dataLoadListener;
        this.tag = tags;
        this.KeyWord = KeyWord;
        this.offset = offset;
        this.limit = limit;
        // Create the Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.stb.gov.sg/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Create the API service
        apiService = retrofit.create(ProgrammeApiService.class);
        Log.i(TAG, "Running fetchdata");
        fetchData(tag, KeyWord,offset);
        Log.i(TAG, "Completed fetchdata");
    }

    public interface DataLoadListener {
        void onDataLoaded(List<Programme> programmeList, int offset);

        void onDataLoadError(Throwable error);
    }

    public void fetchData(String tag, String keyWord, int offset) {
        String dataset = tag;


        if (isLoadingMoreData) {
            return; // Do nothing, as loading is already in progress this helps to optimize getting image done first
        }
        isLoadingMoreData = true;
        Call<ProgrammeApiResponse> call = apiService.getProgrammes(dataset, offset, limit, keyWord);
        call.enqueue(new Callback<ProgrammeApiResponse>() {
            @Override
            public void onResponse(Call<ProgrammeApiResponse> call, Response<ProgrammeApiResponse> response) {
                isLoadingMoreData = false;
                if (response.isSuccessful() && response.body() != null) {
                    ProgrammeApiResponse apiResponse = response.body();
                    List<Programme> fetchedData = apiResponse.getData();

                    List<Programme> filteredData = new ArrayList<>();
                    for (Programme programme : fetchedData) {
                        if (checkRatings(programme) && hasImagesAndWebsite(programme)) {
                            filteredData.add(programme);
                            Log.i(TAG, "Thumbnail: " + programme.getThumbnails());
                        }
                    }

                    programmeList.addAll(filteredData);

                    // Update the total and fetched records count
                    totalRecords = apiResponse.getTotalRecords();
                    fetchedRecords += filteredData.size();

                    //Pass the data immediately in a way it is following "Pagination"
                    dataLoadListener.onDataLoaded(programmeList, offset);//Pass offset to Searchplace.java so that if it decides to scrolldown and view more data, it loads the right data.
                    Log.d("OFFSET","The offset that is passed to searchpalce: " + offset);
                    Log.e(TAG, "Data fetch successfully:: " + response);
                    // Check if there are more records
                    if (fetchedRecords < totalRecords) {
                        Log.d("totalRecords","total: " + totalRecords);
                        Log.d("fetchedRecords","total: " + fetchedRecords);
                        Log.d("MOre data are left","More records are left: " + offset);
                    } else {
                        //Data loading is complete, notify the listener
                        if (dataLoadListener != null) {
                            Log.e(TAG, "Successfully fetch all datas");
                        }
                    }
                } else {
                    // Handle the error if needed
                    Log.e(TAG, "Failed to fetch data: " + response);

                    // Notify the listener of an error
                    if (dataLoadListener != null) {
                        dataLoadListener.onDataLoadError(new Throwable(response.message()));
                    }
                }
            }

            @Override
            public void onFailure(Call<ProgrammeApiResponse> call, Throwable t) {
                // Notify the listener of an error
                if (dataLoadListener != null) {
                    dataLoadListener.onDataLoadError(t);
                }
            }
        });
    }

    public void getLimitedProgrammes(String backendValue, int limit) {
        // Generate a random offset within the valid range
        int randomOffset = new Random().nextInt(50 - limit + 1);
        // Perform the database retrieval here with the given backendValue, randomOffset, and limit
        Call<ProgrammeApiResponse> call = apiService.getProgrammes(backendValue, randomOffset, limit, "");
        call.enqueue(new Callback<ProgrammeApiResponse>() {
            @Override
            public void onResponse(Call<ProgrammeApiResponse> call, Response<ProgrammeApiResponse> response) {
                isLoadingMoreData = false;
                if (response.isSuccessful() && response.body() != null) {
                    ProgrammeApiResponse apiResponse = response.body();
                    List<Programme> fetchedData = apiResponse.getData();

                    List<Programme> filteredData = new ArrayList<>();
                    for (Programme programme : fetchedData) {
                        if (checkRatings(programme) && hasImagesAndWebsite(programme)) {
                            filteredData.add(programme);
                            Log.i(TAG, "Thumbnail: " + programme.getThumbnails());
                        }
                    }

                    // If there are no images available in the current response,
                    // recursively call getLimitedProgrammes with a new random offset
                    if (filteredData.isEmpty()) {
                        getLimitedProgrammes(backendValue, limit);
                    } else {
                        programmeList.addAll(filteredData);
                        // Pass the data immediately in a way it is following "Pagination"
                        dataLoadListener.onDataLoaded(programmeList,offset);
                    }
                } else {
                    // Handle the error if needed
                    Log.e(TAG, "Failed to fetch data: " + response);

                    // Notify the listener of an error
                    if (dataLoadListener != null) {
                        dataLoadListener.onDataLoadError(new Throwable(response.message()));
                    }
                }
            }

            @Override
            public void onFailure(Call<ProgrammeApiResponse> call, Throwable t) {
                // Notify the listener of an error
                if (dataLoadListener != null) {
                    dataLoadListener.onDataLoadError(t);
                }
            }
        });
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    // Helper method to check if a Programme has ratings
    private boolean checkRatings(Programme programme) {
        Double rating = programme.getRating();
        if (rating == null) {
            // Rating is null, set it to 0 (default value)
            programme.setRating(0.0);
        }
        return true;
    }

    // Helper method to check if a Programme has images and a website URL
    private boolean hasImagesAndWebsite(Programme programme) {
        List<ImageInfo> images = programme.getImages();
        if (images == null || images.isEmpty()) {
            return false; // No images, reject the programme
        }
        List<ImageInfo> thumbnails = programme.getThumbnails();
        if (thumbnails == null || thumbnails.isEmpty()) {
            return false; // No Thumbnail images, reject the programme
        }

        // Check if any thumbnail has a null UUID and an empty URL
        for (ImageInfo thumbnail : thumbnails) {
            if (thumbnail.getUuid().isEmpty() && thumbnail.getUrl().isEmpty()) {
                Log.d("name", "useless thumbnail " + programme.getName());
                return false; // Reject the programme if any thumbnail has a null UUID and an empty URL
            }
        }

        String officialWebsite = programme.getOfficialWebsite();
        if (officialWebsite == null || officialWebsite.isEmpty()) {
            return false; // No website URL, reject the programme
        }
        return true; // All image URLs and website URL are valid
    }

    // Other methods for data manipulation, filtering, etc.
}
