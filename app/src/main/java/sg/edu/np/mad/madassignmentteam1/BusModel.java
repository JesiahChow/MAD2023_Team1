package sg.edu.np.mad.madassignmentteam1;

import java.util.List;

public class BusModel {
    public List<BusStopModel> busStopModelList;
    public List<BusArrivalModel> busArrivalModelList;
    public BusModel(List<BusStopModel> busStopModelList, List<BusArrivalModel> busArrivalModelList) {
        this.busStopModelList = busStopModelList;
        this.busArrivalModelList = busArrivalModelList;
    }
}
