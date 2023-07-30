package sg.edu.np.mad.recommend;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProgrammeApplication extends Application {
    private ProgrammeApiService apiService;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.stb.gov.sg/") // Base URL of the API
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create the ProgrammeApiService instance
        apiService = retrofit.create(ProgrammeApiService.class);
    }

    // Getter method to access the ProgrammeApiService instance
    public ProgrammeApiService getApiService() {
        return apiService;
    }
}