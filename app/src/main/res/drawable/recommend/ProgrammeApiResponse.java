package sg.edu.np.mad.recommend;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProgrammeApiResponse {

    @SerializedName("data")
    private List<Programme> programmeList;

    @SerializedName("totalRecords")
    private int totalRecords;

    public List<Programme> getProgrammeList() {
        return programmeList;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public List<Programme> getData() {
        return programmeList;
    }
}
