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
import android.content.ContentValues;

import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.AdapterView;
import android.view.Menu;




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
		
		showDrugListview();
	}

	@Override
   protected void onStop()
   {
   	 super.onStop();
   	 addToDb();

   }
   private void showDrugListview(){

   	if(!drugList.isEmpty())
		{
			ArrayAdapter<Drug> drugAdapter = new MyListAdapter(this,R.layout.drugrowlistview,drugList);
			listView = (ListView) findViewById(R.id.druglistview);
			listView.setAdapter(drugAdapter);
			registerForContextMenu(listView);
		}

   }

	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.menucontext, menu);
    }
 
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
       AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.delete_drug:
           		 Log.i("PMP","POSITION TO DELETE "+info.position);
                drugList.get(info.position).setRemainingDoses(-1);
                showDrugListview();
                Toast.makeText(this,"czemu tak", Toast.LENGTH_LONG).show();
                return true;
        }
 
        return super.onContextItemSelected(item);
    }

   private void addToDb()
   {
   		oSQLiteDB = drugDBHelper.getWritableDatabase();
   		for(Drug d : drugList){
 		ContentValues drugCV = new ContentValues();
 		drugCV.put(drugDBHelper.COL1,d.getId());
 		drugCV.put(drugDBHelper.COL2,d.getName());
 		drugCV.put(drugDBHelper.COL3,d.getPeriodTime());
 		drugCV.put(drugDBHelper.COL4,d.getAmountOfDoses());
 		drugCV.put(drugDBHelper.COL5,d.getRemainingDoses());

 		drugCV.put(drugDBHelper.COL6,DateConverter.dateToString( d.getDateOfFirstDose() ));
 		drugCV.put(drugDBHelper.COL7,DateConverter.dateToString( d.getDateOfNextDose()  ));
 		int ids = (int)oSQLiteDB.update(drugDBHelper.TABLE_NAME,drugCV,d.getId() +" = "+ drugDBHelper.COL1,null);

   		}
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
