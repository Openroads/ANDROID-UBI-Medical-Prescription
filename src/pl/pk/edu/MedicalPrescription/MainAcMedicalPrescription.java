package pl.pk.edu.MedicalPrescription;

import android.app.Activity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import java.util.ArrayList;
import android.database.Cursor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.util.Log;
import java.text.ParseException;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;




public class MainAcMedicalPrescription extends Activity
{
	private HelperForDB drugDBHelper;
	private SQLiteDatabase oSQLiteDB;
	private ArrayList<Drug> drugList;
	private ListView listView;

	

	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        drugDBHelper = new HelperForDB(this);

        
        
    }

    @Override
  	protected void onResume(){
	    super.onResume();
	    oSQLiteDB = drugDBHelper.getWritableDatabase();
	    Cursor oCursor = oSQLiteDB.query(drugDBHelper.TABLE_NAME, new String[]{"*"} ,drugDBHelper.COL5+" > 0", null, null, null, null,null);
		drugList = new ArrayList<Drug>();
		makeList(oCursor,drugList);
		
		//ArrayAdapter<Drug> drugAdapter = new ArrayAdapter<Drug>(this,android.R.layout.simple_list_item_1, drugList);
		ArrayAdapter<Drug> drugAdapter = new MyListAdapter(this,R.layout.drugrowlistview,drugList);
		listView = (ListView) findViewById(R.id.druglistview);
		//listView.setItemsCanFocus(true);
		listView.setAdapter(drugAdapter);
	}
	

	public void addDrugonClick(View v){
		Intent intent = new Intent(this,MedicalPrescription.class);
		startActivity(intent);
	}

	private void makeList(Cursor cursor,ArrayList<Drug> list)
	{
		boolean move = cursor.moveToFirst();
		while(move)
		{
			Drug drug = new Drug();
			drug.setId(cursor.getInt(0));
			drug.setName(cursor.getString(1));
			drug.setPeriodTime(cursor.getInt(2));
			drug.setAmountOfDoses(cursor.getInt(3));
			drug.setRemainingDoses(cursor.getInt(4));
			try{
				Date date = DateConverter.stringToDate(cursor.getString(5));
				drug.setDateOfFirstDose(date);
				date = DateConverter.stringToDate(cursor.getString(6));
				drug.setDateOfNextDose(date);
			}catch(ParseException exc){
				Log.i("PMP",exc.toString());
			}
			
			move = cursor.moveToNext();
			list.add(drug);
		}
		cursor.close();
	}
}