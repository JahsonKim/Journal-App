package com.oceanscan.journal.app.utils;

import android.content.Context;

import com.oceanscan.journal.app.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String formatDate(Context mContext, long time) {
        try {

            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTimeInMillis(time);
            cal2.setTimeInMillis(System.currentTimeMillis());
            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
            if (sameDay) {
                Timestamp stamp = new Timestamp(time);
                Date date = new Date(stamp.getTime());
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                // if(time==System.currentTimeMillis())
                return mContext.getString(R.string.today)+", "+sdf.format(date);

            }
            boolean yesterday = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    (cal1.get(Calendar.DAY_OF_YEAR) + 1) == cal2.get(Calendar.DAY_OF_YEAR);
            if (yesterday) {
                return mContext.getString(R.string.yesterday);
            }

            Timestamp stamp = new Timestamp(time);
            Date date = new Date(stamp.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());

            return sdf.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
