package sg.edu.np.mad.madassignmentteam1;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProgrammeDatabase {

    private Context context;
    private List<Programme> programmeList;
    private List<Programme> sortedProgrammes;
    private DataLoadListener dataLoadListener; // Interface to communicate data loading status
    private String tag;
    private String KeyWord;
    final String TAG = "Recommendation";
    String apiKey = "h9maakGa0NsA85mqsNTZRGASCRtYwhGs";
    int offset = 0;
    int limit = 50;
    int totalRecords = 0;
    int fetchedRecords = 0;

    // Retrofit instance and API service
    private ProgrammeApiService apiService;

    // Constructor with DataLoadListener parameter
    public ProgrammeDatabase(Context context, DataLoadListener dataLoadListener,String tags,String KeyWord) {
        Log.i(TAG,"Starting ProgrammeDatabase");
        programmeList = new ArrayList<>();
        sortedProgrammes = new ArrayList<>();
        this.context = context;
        this.dataLoadListener = dataLoadListener;
        this.tag = tags;
        this.KeyWord = KeyWord;
        // Create the Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.stb.gov.sg/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Create the API service
        apiService = retrofit.create(ProgrammeApiService.class);
        Log.i(TAG,"Running fetchdata");
        fetchData(tag,KeyWord);
        Log.i(TAG,"Completed fetchdata");
    }

    public interface DataLoadListener {
        void onDataLoaded(List<Programme> programmeList);

        void onDataLoadError(Throwable error);
    }

    public void fetchData(String tag, String keyWord) {
        String dataset = tag;

        Call<ProgrammeApiResponse> call = apiService.getProgrammes(dataset, offset, limit,keyWord);
        call.enqueue(new Callback<ProgrammeApiResponse>() {
            @Override
            public void onResponse(Call<ProgrammeApiResponse> call, Response<ProgrammeApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProgrammeApiResponse apiResponse = response.body();
                    List<Programme> fetchedData = apiResponse.getData();


                    List<Programme> filteredData = new ArrayList<>();
                    for (Programme programme : fetchedData) {
                        if (checkRatings(programme) && hasImages(programme)) {
                            filteredData.add(programme);
                            Log.i(TAG,"Thumbnail: "+ programme.getThumbnails());
                        }
                    }

                    programmeList.addAll(filteredData);

                    // Update the total and fetched records count
                    totalRecords = apiResponse.getTotalRecords();
                    fetchedRecords += filteredData.size();


                    //Pass the data immediately in a way it is following "Pagination"
                    dataLoadListener.onDataLoaded(programmeList);

                    // Check if there are more records
                    if (fetchedRecords < totalRecords) {
                        offset += limit;
                        fetchData(tag,keyWord); // Fetch the next page
                    } else {
                         //Data loading is complete, notify the listener
                        if (dataLoadListener != null) {
                            Log.e(TAG, "Successfully fetch all datas" );
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

    // Helper method to check if a Programme has ratings
    private boolean checkRatings(Programme programme) {
        Double rating = programme.getRating();
        if (rating == null) {
            // Rating is null, set it to 0 (default value)
            programme.setRating(0.0);
        }
        return true;
    }

    // Helper method to check if a Programme has images in the url section
    private boolean hasImages(Programme programme) {
        List<ImageInfo> images = programme.getImages();
        if (images == null || images.isEmpty()) {
            return false; // No images, reject the programme
        }
        List<ImageInfo> thumbnails = programme.getThumbnails();
        if (thumbnails == null || thumbnails.isEmpty()) {
            return false; // No images, reject the programme
        }
        return true; // All image URLs are valid
    }
    // Other methods for data manipulation, filtering, etc.
}
