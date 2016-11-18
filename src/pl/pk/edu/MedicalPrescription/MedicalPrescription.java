package pl.pk.edu.MedicalPrescription;

import android.app.Activity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.Toast;
import android.view.View;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import android.app.TimePickerDialog;
import android.widget.TimePicker;
import android.database.sqlite.SQLiteDatabase;
import android.content.Intent;
import android.content.ContentValues;
import android.content.ContentResolver;
import android.util.Log;



public class MedicalPrescription extends Activity
{
 
    private SQLiteDatabase oSQLiteDB;
 	private HelperForDB myHelper;
 	/* ** References to widget object ** */
    private EditText oNameET;
    private EditText oPeriodET;
    private EditText oAmountET;
    private int year, month, day,hour,minute;
    private Calendar calendar;
    private Calendar startDate;

	private TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener(){
    	@Override
    	public void onTimeSet(TimePicker view, int hourOfDay, int minute){

    	  	startDate.set(Calendar.HOUR_OF_DAY,hourOfDay);
         	startDate.set(Calendar.MINUTE,minute);
         	
    	}
    };

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
      	@Override
      	public void onDateSet(DatePicker arg0, int year, int month, int day) {
	       
	         startDate.set(Calendar.YEAR,year);
	         startDate.set(Calendar.MONTH,month);
	         startDate.set(Calendar.DAY_OF_MONTH,day);
	         showDialog(665);
	         
      }
   };


//*********************************************************************************************************************\
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adddrug);
        
        calendar = Calendar.getInstance();
      	year = calendar.get(calendar.YEAR);
        month = calendar.get(calendar.MONTH);
     	  day = calendar.get(calendar.DAY_OF_MONTH);
      	hour = calendar.get(calendar.HOUR_OF_DAY);
        minute = calendar.get(calendar.MINUTE);
        startDate = Calendar.getInstance();
        myHelper = new HelperForDB(this);

    }
    @Override
  	protected void onResume(){
	    super.onResume();
	    oSQLiteDB = myHelper.getWritableDatabase();
	}

	public void addDrugonClick(View v) 
	{
	   
   		oNameET   	= (EditText) findViewById(R.id.name);
   		oPeriodET 	= (EditText) findViewById(R.id.period);
   		oAmountET 	= (EditText)findViewById(R.id.amount);

   		String nameToBD,periodToBD,amountToBD;

   		nameToBD	= oNameET.getText().toString();
   		periodToBD	= oPeriodET.getText().toString();
   		amountToBD	= oAmountET.getText().toString();
   		/* checking correction of data */
   		if(nameToBD.length()==0 || periodToBD.length()==0 || amountToBD.length()==0){
   			Toast.makeText(this, "Empty field. Enter data.",Toast.LENGTH_LONG).show();
   		}else if(checkCorrectionOfIntegerInput(periodToBD) == false ){
   			Toast.makeText(this, "Invalid data.",Toast.LENGTH_SHORT).show();
   		}
   		else if (checkCorrectionOfIntegerInput(amountToBD)== false) {
   			Toast.makeText(this, "Invalid data.",Toast.LENGTH_SHORT).show();
   			
   		}else{
   			Drug oDrug = new Drug(nameToBD,Integer.valueOf(periodToBD),Integer.valueOf(amountToBD),startDate.getTime());

   			addDrugToDB(oDrug);
   			SenderToCalendar sender = new SenderToCalendar();
   			sender.setEventInCalendar(this,oDrug);
        finish();
   		}
   	
	}

	private void addDrugToDB(Drug drug)
	{
		ContentValues drugCV = new ContentValues();
		drugCV.put(myHelper.COL2,drug.getName());
		drugCV.put(myHelper.COL3,drug.getPeriodTime());
		drugCV.put(myHelper.COL4,drug.getAmountOfDoses());
		drugCV.put(myHelper.COL5,drug.getRemainingDoses());
   		String dateToDB = DateConverter.dateToString(startDate.getTime());
		drugCV.put(myHelper.COL6,dateToDB);

		drugCV.put(myHelper.COL7, DateConverter.dateToString( drug.getDateOfNextDose() ) );
    drugCV.put(myHelper.COL8, drug.getEventId());
		int id = (int)oSQLiteDB.insert(myHelper.TABLE_NAME,null,drugCV);
		if(id<0){
			Log.i("PMP","-1 while adding to database");
		}
		
	}
	private boolean checkCorrectionOfIntegerInput(String iStr)
	{
		int length=iStr.length();
		if(length == 0) return false;

		if(iStr.charAt(0)=='-') return false;
		for(int i = 0;i<length;i++)
		{
			char c = iStr.charAt(i);
			if(c < '0' || c > '9') return false;
		}
		return true;
	}


    public void setDateonClick(View v)
    {
    	showDialog(663);
    }

    protected Dialog onCreateDialog(int id)
    {
    	if (id == 663) 
    	{
        	return new DatePickerDialog(this,myDateListener,year, month, day);
      	}
      	if(id==665)
      	{
      		return new TimePickerDialog(this,myTimeListener,hour,minute,true);
      	}
      return null;
    }
   
}
	