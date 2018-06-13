package mq.com.chuohapps.utils.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import mq.com.chuohapps.AppConfigs;

/**
 * Created by nguyen.dang.tho on 9/8/2017.
 */

public class DateUtils {

    private static final String DATE_FORMAT_SENT = "yyyy/MM/dd HH:mm:ss";
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String DATE_FORMAT_SIMPLE = "MM/yyyy";
    private static final String DATE_FORMAT_FULL = "HH:mm dd/MM/yyyy";
    private static final String DATE_FORMAT_FULL_ = "yyyy-MM-dd";
    private static final String TIME_FORMAT_12 = "hh:mm:ss a";
    private static final String TIME_FORMAT_24 = "HH:mm:ss";
    private static final String DATE_FORMAT_CARD = "MM/yy";
    private static final String DATE_FORMAT_JP = "yyyy年MM月dd日";
    private static final String DATE_FORMAT_FULL_JP = "HH:mm yyyy年MM月dd日";
    private DateUtils() {
    }

    public static String getJapanDateFromText(String raw) {
        if (!raw.contains("-") || raw.equalsIgnoreCase(AppConfigs.NULL_STRING)) return raw;
        return raw.replaceFirst("-", "年")
                .replaceFirst("-", "月")
                + "日";
    }

    public static String dateToString(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    public static String dateToStringSent(Date date) {
        return new SimpleDateFormat(DATE_FORMAT_SENT).format(date);
    }

    public static String dateToStringFull(Date date) {
        return new SimpleDateFormat(DATE_FORMAT_FULL_).format(date);
    }

    public static String dateToStringSimple(Date date) {
        return new SimpleDateFormat(DATE_FORMAT_SIMPLE).format(date);
    }

    public static String timeToString12(Date date) {

        return new SimpleDateFormat(TIME_FORMAT_12).format(date);
    }

    public static String timeToString24(Date date) {
        return new SimpleDateFormat(TIME_FORMAT_24).format(date);
    }

    public static String getDateCardString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_CARD);
        return format.format(date);
    }


    public static String getTimestamps(String dateStr) {
        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Date date = null;
        try {
            date = (Date) formatter.parse(dateStr.trim());
        } catch (ParseException e) {

        }
        if (date != null) return String.valueOf(date.getTime() / 1000);
        return "";
    }

    public static String getTimestamps(Calendar calendar) {

        if (calendar != null) return String.valueOf(calendar.getTime().getTime() / 1000);
        return "";
    }

    public static String getCurrentTimestamps() {
        Date date = Calendar.getInstance().getTime();
        if (date != null) return String.valueOf(date.getTime() / 1000);
        return "";
    }

    public static String getLocalDate(String timeStamp, boolean isFullDate) {
        String formatDate = DATE_FORMAT;
        if (Locale.getDefault().getCountry().equals(Locale.JAPAN.getCountry())) {
            if (isFullDate) formatDate = DATE_FORMAT_FULL_JP;
            else formatDate = DATE_FORMAT_JP;
        } else {
            if (isFullDate) formatDate = DATE_FORMAT_FULL;
            else formatDate = DATE_FORMAT;
        }
        return getDate(timeStamp, formatDate);
    }

    public static String getDate(String timeStamp, String format) {
        try {
            long numberTimeStamp = Long.parseLong(timeStamp.trim());
            DateFormat sdf = new SimpleDateFormat(format);

            Date newDate = new Date(getTimeStamp13Digits(numberTimeStamp));
            return sdf.format(newDate);
        } catch (Exception ex) {
            return "";
        }
    }

    public static long getTimeStamp13Digits(long rawTimeStamp) {
        if (String.valueOf(rawTimeStamp).length() == 10) return rawTimeStamp * 1000;
        return rawTimeStamp;
    }

    public static long getTimeStamp10Digits(long rawTimeStamp) {
        if (String.valueOf(rawTimeStamp).length() == 13) return (rawTimeStamp / 1000);
        return rawTimeStamp;
    }

    public static Date stringToDate(String date) {
        SimpleDateFormat myFormat = new SimpleDateFormat(DATE_FORMAT_SENT);
        Date result = new Date();
        try {
            result = myFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
