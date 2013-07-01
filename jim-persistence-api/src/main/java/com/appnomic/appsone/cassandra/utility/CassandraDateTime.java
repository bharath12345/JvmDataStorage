package com.appnomic.appsone.cassandra.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * User: bharadwaj
 * Date: 01/07/13
 * Time: 9:14 AM
 */
public class CassandraDateTime {

    private static final String pattern = "yyMMdd";
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    private static final Calendar calendar = Calendar.getInstance();

    public static String getDate(long epochTime) {
        Date epochDate = new Date(epochTime);
        return simpleDateFormat.format(epochDate);
    }

    public static int getDayTime(long epochTime) {
        calendar.setTimeInMillis(epochTime);
        int hoursOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        return (hoursOfDay * 3600 ) + (minutes * 60) + seconds;
    }
}
