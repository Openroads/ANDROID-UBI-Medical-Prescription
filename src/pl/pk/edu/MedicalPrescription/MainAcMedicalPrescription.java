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
	    Cursor oCursor = oSQLiteDB.query(drugDBHelper.TABLE_NAME, new String[]{"*"} ,drugDBHelper.COL6+" = 0", null, null, null, null,null);
		drugList = new ArrayList<Drug>();
		makeList(oCursor,drugList);
		Log.i("PMP",drugList.size()+"");
		
		ArrayAdapter<Drug> drugAdapter = new ArrayAdapter<Drug>(this,android.R.layout.simple_list_item_1, drugList);
		listView = (ListView) findViewById(R.id.drugListView);
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
			drug.setName(cursor.getString(1));
			drug.setPeriodTime(cursor.getInt(2));
			drug.setAmountOfDoses(cursor.getInt(3));
			try{
				DateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm");
				Date date = format.parse(cursor.getString(4));

				drug.setDateOfFirstDose(date);
			}catch(ParseException exc){
				Log.i("PMP",exc.toString());
			}
			drug.setMarkStatus(cursor.getInt(5));
			move = cursor.moveToNext();
			list.add(drug);
		}
		cursor.close();
	}
}