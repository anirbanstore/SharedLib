package home.app.shared.view.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

public class HaDateUtils {

    public HaDateUtils() {
        super();
    }

    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        return dateFormat.format(date).toString().toUpperCase();
    }

    public static String getDate(final String stringDate) {
        DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy");
        Date formattedDate = null;
        try {
            formattedDate = dateFormat1.parse(stringDate);
        } catch (ParseException e) {
            ;
        }
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String returnDate = dateFormat2.format(formattedDate).toString().toUpperCase();
        return returnDate;
    }
    
    public static String getDate(final String stringDate, final String format) {
        DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy");
        Date formattedDate = null;
        try {
            formattedDate = dateFormat1.parse(stringDate);
        } catch (ParseException e) {
            ;
        }
        DateFormat dateFormat2 = new SimpleDateFormat(format);
        String returnDate = dateFormat2.format(formattedDate).toString();
        return returnDate;
    }
    
    public static Date getDateFromString(final String stringDate, final String format) {
        DateFormat dateFormat1 = new SimpleDateFormat(format == null ? "MM/dd/yyyy" : format);
        Date formattedDate = null;
        try {
            formattedDate = dateFormat1.parse(stringDate);
        } catch (ParseException e) {
            ;
        }
        return formattedDate;
    }

    public static Date getTruncatedDate(final Date inputDate) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date formattedDate = null;
        try {
            formattedDate = dateFormat.parse(dateFormat.format(inputDate).toString());
        } catch (ParseException e) {
            ;
        }
        return formattedDate;
    }
    
    public static String getStringFromDate(final Date inputDate, final String format) {
        DateFormat dateFormat = new SimpleDateFormat(format == null ? "dd/MMM/yyyy" : format);
        String formattedDate = null;
        try {
            formattedDate = dateFormat.format(inputDate).toString();
        } catch (Exception e) {
            ;
        }
        return formattedDate;
    }
    
    public static int compareDates(final Date date1, final Date date2) {
        return date1.compareTo(date2);
    }
    
    public static int compareDates(final String date1, final String date2, final String format) {
        final Date formattedDate1 = getDateFromString(date1, format);
        final Date formattedDate2 = getDateFromString(date2, format);
        return formattedDate1.compareTo(formattedDate2);
    }
}
