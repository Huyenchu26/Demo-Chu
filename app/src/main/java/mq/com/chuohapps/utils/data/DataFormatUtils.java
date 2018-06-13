package mq.com.chuohapps.utils.data;

import com.google.gson.GsonBuilder;

import mq.com.chuohapps.AppConfigs;

/**
 * Created by chu.thi.ngoc.huyen on 2/7/2018.
 */

public class DataFormatUtils {
    private DataFormatUtils() {
    }

    public static String roundNumber(double number) {
        return String.format("%.2f", (float) number);
    }

    public static String roundNumberInteger(double number) {
        return String.format("%.0f", (float) number);
    }

    public static String getString(String str) {
        if (str == null || str.trim().equals(AppConfigs.EMPTY) || str.trim().equalsIgnoreCase("null"))
            return AppConfigs.NULL_STRING;
        return str;
    }

    public static String getStringZero(String str) {
        if (str == null || str.trim().equals(AppConfigs.EMPTY) || str.trim().equalsIgnoreCase("null"))
            return "0";
        return str;
    }

    public static boolean textIsNull(String str) {
        return str == null || str.trim().equals(AppConfigs.EMPTY);
    }

    public static int getIntFromInteger(Integer integer) {
        if (integer != null) return integer;
        return -1;
    }

    public static String getJsonPretty(Object data) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(data);
    }

    public static boolean equalsKg(String value) {
        if (value == null) return false;
        return value.equalsIgnoreCase("㎏") || value.equalsIgnoreCase("kg");
    }
}
