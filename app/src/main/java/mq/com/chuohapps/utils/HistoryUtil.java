package mq.com.chuohapps.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.utils.data.DateUtils;

public class HistoryUtil {

    // TODO: 6/4/2018 trunk / camera
    // TODO: 6/4/2018 từ history chia ra từng đoạn mở két
    public static List<Vehicle> getTimeLine(List<Vehicle> vehicles) {
        List<Vehicle> vehiclesTrunk = new ArrayList<>();
        if (vehicles != null) {
            for (int i = 0; i < vehicles.size() - 1; i++) {
                if (vehicles.get(i).trunk.equals("0") && vehicles.get(i + 1).trunk.equals("1")) {
                    vehiclesTrunk.add(vehicles.get(i + 1));
                } else if (vehicles.get(i).trunk.equals("1") && vehicles.get(i + 1).trunk.equals("0")){
                    vehiclesTrunk.add(vehicles.get(i));
                }
            }
        }
        return vehiclesTrunk;
    }

    public static class ItemTrunk {
        String timeLine;
        int frontCam, backCam;
        long time;

        public String getTimeLine() {
            return timeLine;
        }

        public void setTimeLine(String timeLine) {
            this.timeLine = timeLine;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getFrontCam() {
            return frontCam;
        }

        public void setFrontCam(int frontCam) {
            this.frontCam = frontCam;
        }

        public int getBackCam() {
            return backCam;
        }

        public void setBackCam(int backCam) {
            this.backCam = backCam;
        }
    }

    // TODO: 6/4/2018 từ từng đoạn mở két lấy số camera
    public static ItemTrunk getItemTrunk(Vehicle vehicle1, Vehicle vehicle2) {
        ItemTrunk itemTrunk = new ItemTrunk();

        Vehicle vehicleStart = vehicle1;
        Vehicle vehicleEnd = vehicle2;

        itemTrunk.setTimeLine(DateUtils.convertServerDateToUserTimeZone(vehicleStart.dateTime));

        int frontCam = Integer.parseInt(vehicleEnd.frontCam) - Integer.parseInt(vehicleStart.frontCam);
        itemTrunk.setFrontCam(frontCam);
        int backCam = Integer.parseInt(vehicleEnd.backCam) - Integer.parseInt(vehicleStart.backCam);
        itemTrunk.setBackCam(backCam);
        Date date1 = DateUtils.stringToDate(DateUtils.convertServerDateToUserTimeZone(vehicleEnd.dateTime));
        Date date2 = DateUtils.stringToDate(DateUtils.convertServerDateToUserTimeZone(vehicleStart.dateTime));
        long time = (date1.getTime() - date2.getTime()) / 1000;
        itemTrunk.setTime(time);

        return itemTrunk;
    }

    // TODO: 6/4/2018 CPUtime
    // TODO: 6/4/2018 từ history chia ra từng đoạn khởi động lại <CPUtime lớn rồi bé đột ngột>
    public static List<Vehicle> getListRestartCPU(List<Vehicle> vehicles) {
        List<Vehicle> vehiclesCPU = new ArrayList<>();
        if (vehicles != null) {
            for (int i = 1; i < vehicles.size(); i++) {
                if (Integer.parseInt(vehicles.get(i).cpuTime) <
                        Integer.parseInt(vehicles.get(i - 1).cpuTime)) {
                    vehiclesCPU.add(vehicles.get(i));
                }
            }
        }
        return vehiclesCPU;
    }

}
