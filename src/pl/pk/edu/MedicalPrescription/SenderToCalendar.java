package pl.pk.edu.MedicalPrescription;

import java.util.Date;
import android.widget.Toast;
import android.content.Intent;
import android.content.ContentValues;
import android.content.ContentResolver;
import android.util.Log;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Reminders;
import android.net.Uri;
import java.util.TimeZone;
import android.content.Context;

public class SenderToCalendar{

	private Date doseTime;
	private String description;
	private int remainDoses;
	
	public SenderToCalendar(){}

	public  void setEventInCalendar(Context context, Drug drug)
	{
		makeAtribute(drug);

			boolean automat_event_SHP =true;
			if(automat_event_SHP==false)
			{
				Intent calendarI = new Intent(Intent.ACTION_INSERT)
                                    .setData(Events.CONTENT_URI)
                                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, this.doseTime.getTime())
                                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, this.doseTime.getTime())
                                    .putExtra(Events.TITLE, drug.getName())
                                    .putExtra(Events.DESCRIPTION, this.description)
                                    .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_FREE);
                context.startActivity(calendarI);
             }else{
             	long calID = drug.getId();

             	TimeZone timeZone = TimeZone.getDefault();
				ContentResolver cr = context.getContentResolver();
				ContentValues values = new ContentValues();
				values.put(Events.DTSTART, this.doseTime.getTime());
				values.put(Events.DTEND, this.doseTime.getTime());
				values.put(Events.TITLE, drug.getName());
				values.put(Events.DESCRIPTION, this.description);
				values.put(Events.CALENDAR_ID, calID);
				values.put(Events.EVENT_TIMEZONE, timeZone.getID());
				Uri uri = cr.insert(Events.CONTENT_URI, values);//Events.CONTENT_URI  URL for the top-level calendar authority

				long eventID = Long.parseLong(uri.getLastPathSegment());

				values = new ContentValues();
				values.put(Reminders.MINUTES, 10);
				values.put(Reminders.EVENT_ID, eventID);
				values.put(Reminders.METHOD, Reminders.METHOD_ALERT);
				uri = cr.insert(Reminders.CONTENT_URI, values);

				Toast.makeText(context, "Reminder and event added.",Toast.LENGTH_SHORT).show();

             	/*Intent intent = new Intent(context,MainAcMedicalPrescription.class);
            	context.startActivity(intent);*/
             }
	}

	public void updateEventInCalendar(Context context, Drug drug){
		makeAtribute(drug);
		boolean automat_event_SHP =true;
			if(automat_event_SHP==false)
			{

			}

	}

	public void deleteEventFromCalendar(Context context, Drug drug)
	{
		
	}
	private void makeAtribute(Drug drug)
	{
		this.doseTime = drug.getDateOfNextDose();
		this.remainDoses= drug.getRemainingDoses();
		 if(remainDoses>1){
            this.description = this.remainDoses + " remaining doses to finish drug";
        }else{
            this.description = this.remainDoses + " remaining dose to finish drug";
        }
	}

}