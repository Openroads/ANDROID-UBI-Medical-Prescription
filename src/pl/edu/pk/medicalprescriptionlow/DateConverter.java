package pl.edu.pk.medicalprescriptionlow;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateConverter
{
    public static Date stringToDate(String dateString) throws ParseException
    {
        Date date = null;
        DateFormat format = new SimpleDateFormat("dd-M-yyyy HH:mm");
        
        date = format.parse(dateString);
        
        return date;
    }

    public static String dateToString(Date date)
    {
        String dateString = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy HH:mm");
        dateString = sdf.format(date.getTime());
       
        return dateString;
    }
}
