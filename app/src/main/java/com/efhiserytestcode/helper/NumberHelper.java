package com.efhiserytestcode.helper;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberHelper {

    public static String formatIdr(double format) {
        String language = Locale.getDefault().getDisplayLanguage();
        String country = Locale.getDefault().getCountry();

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String idr = formatRupiah.format(format);
//        String val = idr.replace("Rp", "IDR ");
        String val = idr.replace(",00", "");

        return val;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
