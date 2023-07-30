package sg.edu.np.mad.recommend;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ProgrammeApiService {

    @Headers({
            "ApiEndPointTitle: Search Multiple Datasets By Keyword",
            "Content-Type: application/json",
            "X-Content-Language: en",
            "X-API-Key: h9maakGa0NsA85mqsNTZRGASCRtYwhGs"
    })

    @GET("content/common/v2/search")
    Call<ProgrammeApiResponse> getProgrammes(
            @Query("dataset") String dataset,
            @Query("offset") int offset,
            @Query("limit") int limit,
            @Query("keyword") String keyword
    );

}