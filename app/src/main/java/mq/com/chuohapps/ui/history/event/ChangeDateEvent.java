package mq.com.chuohapps.ui.history.event;

import java.util.List;

import mq.com.chuohapps.data.helpers.network.response.Vehicle;

public class ChangeDateEvent {
    public String startDate;
    public String endDate;
    public List<Vehicle> vehicleList;

    public ChangeDateEvent() {
    }

    public ChangeDateEvent(String startDate, String endDate, List<Vehicle> vehicleList) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.vehicleList = vehicleList;
    }
}
