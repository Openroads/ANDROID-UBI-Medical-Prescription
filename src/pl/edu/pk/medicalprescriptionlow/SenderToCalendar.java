package pl.edu.pk.medicalprescriptionlow;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.TimeZone;

public class SenderToCalendar {

	private Date doseTime;
	private String description;
	private int remainDoses;

	public SenderToCalendar() {
	}

	public long getCalendarId(Context context) {
		final String[] EVENT_PROJECTION = new String[]{
				Calendars._ID,
				Calendars.OWNER_ACCOUNT
		};
		final int PROJECTION_ID_INDEX = 0;

		Cursor cur = null;
		ContentResolver cr = context.getContentResolver();
		Uri uri = Calendars.CONTENT_URI;
		String selection = "((" + Calendars.OWNER_ACCOUNT + " LIKE '%@gmail.com'))";
		if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
			Log.i("PMP", "Permission denied ");
			return 1;
		}
		cur = cr.query(uri, EVENT_PROJECTION, selection, null, null);
		long calID = 1; //Calendars in table starts from 1 if it doesn't find any google  account 
		while (cur.moveToNext()) {
			calID = cur.getLong(PROJECTION_ID_INDEX);
		}
		cur.close();
		return calID;


	}

	public void setEventInCalendar(Context context, Drug drug, int kind) {
		makeAtribute(drug);

		if (kind == 1) {
			Intent calendarI = new Intent(Intent.ACTION_INSERT)
					.setData(Events.CONTENT_URI)
					.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, this.doseTime.getTime())
					.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, this.doseTime.getTime())
					.putExtra(Events.TITLE, drug.getName())
					.putExtra(Events.DESCRIPTION, this.description)
					.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_FREE);
			context.startActivity(calendarI);
		} else {

			long calID = getCalendarId(context);

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
			drug.setEventId(eventID);
			Log.i("PMP", "event id na swiezo " + drug.getEventId());
			values = new ContentValues();
			values.put(Reminders.MINUTES, 10);
			values.put(Reminders.EVENT_ID, eventID);
			values.put(Reminders.METHOD, Reminders.METHOD_ALERT);
			if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
				Log.i("PMP", "Permission denied event upd ");
				return;
			}
			uri = cr.insert(Reminders.CONTENT_URI, values);

				Toast.makeText(context, "Reminder added. ",Toast.LENGTH_SHORT).show();

             }
	}

	public void updateEventInCalendar(Context context, Drug drug){
		makeAtribute(drug);

		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
		Uri updateUri = null;
		//  new data for the event
		values.put(Events.DTSTART, this.doseTime.getTime());
		values.put(Events.DTEND, this.doseTime.getTime());
		updateUri = ContentUris.withAppendedId(Events.CONTENT_URI, drug.getEventId());
		int rows = cr.update(updateUri, values, null, null);
		if(rows>0)
			Toast.makeText(context, "Reminder updated. ",Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(context, "Reminder cannot be found. ",Toast.LENGTH_SHORT).show();

	}

	public void deleteEventFromCalendar(Context context, Drug drug)
	{
		if(drug.getEventId()!=0) {
			ContentResolver cr = context.getContentResolver();
			ContentValues values = new ContentValues();
			Uri deleteUri = null;
			deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, drug.getEventId());
			int rows = cr.delete(deleteUri, null, null);
			if(rows>0) {
				Toast.makeText(context, "Reminder deleted. ", Toast.LENGTH_SHORT).show();
			}
		}

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