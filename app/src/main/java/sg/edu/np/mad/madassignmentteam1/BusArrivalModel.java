package sg.edu.np.mad.madassignmentteam1;

public class BusArrivalModel {
    public String ServiceNo;
    public String EstimateArrivalTime1;
    public String EstimateArrivalTime2;
    public String EstimateArrivalTime3;
    public BusArrivalModel(String ServiceNo, String EstimateArrivalTime1, String EstimateArrivalTime2, String EstimateArrivalTime3) {
        this.ServiceNo = ServiceNo;
        this.EstimateArrivalTime1 = EstimateArrivalTime1;
        this.EstimateArrivalTime2 = EstimateArrivalTime2;
        this.EstimateArrivalTime3 = EstimateArrivalTime3;
    }
}
