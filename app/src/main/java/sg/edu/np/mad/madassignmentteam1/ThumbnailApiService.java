package sg.edu.np.mad.madassignmentteam1;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ThumbnailApiService {

    @Headers({
            "ApiEndPointTitle: Download Media File",
            "Content-Type: application/json",
            "X-API-Key: h9maakGa0NsA85mqsNTZRGASCRtYwhGs"
    })

    @GET("media/download/v2/{uuid}")
    Call<ResponseBody> getThumbnailImage(@Path("uuid") String uuid, @Query("fileType") String fileType);
}
