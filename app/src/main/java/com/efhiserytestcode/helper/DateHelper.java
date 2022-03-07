package com.efhiserytestcode.helper;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateHelper {

    // FROM FORTMAT DATE
    public static String FROM_FORMAT1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static String FROM_FORMAT2 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static String FROM_FORMAT3 = "yyyy-MM-dd";
    public static String FROM_FORMAT4 = "yyyy-MM-dd HH:mm:ss";
    public static String FROM_FORMAT5 = "yyyy-MM-dd HH:mm:ss.SSS";

    // TO FORMAT DATE
    public static String TO_FORMAT1 = "dd MMMM yyyy";

    // FORMAT
    public static String DAY_FORMAT = "dd";
    public static String MONTH_FORMAT = "MMM";
    public static String YEAR_FORMAT = "yyyy";
    public static String MONTH_YEAR = "MMM yyyy";

    // DATE FORMAT
    // FROM
    public static SimpleDateFormat fromFormat1 = new SimpleDateFormat(FROM_FORMAT1);
    public static SimpleDateFormat fromFormat2 = new SimpleDateFormat(FROM_FORMAT2);
    public static SimpleDateFormat fromFormat3 = new SimpleDateFormat(FROM_FORMAT3);
    public static SimpleDateFormat fromFormat4 = new SimpleDateFormat(FROM_FORMAT4);
    public static SimpleDateFormat fromFormat5 = new SimpleDateFormat(FROM_FORMAT5);

    // TO
    private static SimpleDateFormat toFormat1 = new SimpleDateFormat(TO_FORMAT1);

    public static Date getDateNow() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        return date;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getDateFormat(String dateStr) throws ParseException {
        Date dateParse = getDateNow();
        try {
            if (isValidFormat(FROM_FORMAT1, dateStr, Locale.ENGLISH)) {
                dateParse = fromFormat1.parse(dateStr);
            } else if (isValidFormat(FROM_FORMAT2, dateStr, Locale.ENGLISH)) {
                dateParse = fromFormat2.parse(dateStr);
            } else if (isValidFormat(FROM_FORMAT3, dateStr, Locale.ENGLISH)) {
                dateParse = fromFormat3.parse(dateStr);
            } else if (isValidFormat(FROM_FORMAT4, dateStr, Locale.ENGLISH)) {
                dateParse = fromFormat4.parse(dateStr);
            } else if (isValidFormat(FROM_FORMAT5, dateStr, Locale.ENGLISH)) {
                dateParse = fromFormat5.parse(dateStr);
            } else {
                dateParse = new Date(Long.parseLong(dateStr) * 1000);
            }
        } catch (ParseException e){
            dateParse = getDateNow();
        }

        return toFormat1.format(dateParse);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean isValidFormat(String format, String value, Locale locale) {
        LocalDateTime ldt = null;
        DateTimeFormatter fomatter = DateTimeFormatter.ofPattern(format, locale);

        try {
            ldt = LocalDateTime.parse(value, fomatter);
            String result = ldt.format(fomatter);
            return result.equals(value);
        } catch (DateTimeParseException e) {
            try {
                LocalDate ld = LocalDate.parse(value, fomatter);
                String result = ld.format(fomatter);
                return result.equals(value);
            } catch (DateTimeParseException exp) {
                try {
                    LocalTime lt = LocalTime.parse(value, fomatter);
                    String result = lt.format(fomatter);
                    return result.equals(value);
                } catch (DateTimeParseException e2) {
                    // Debugging purposes
                    //e2.printStackTrace();
                }
            }
        }

        return false;
    }
}
