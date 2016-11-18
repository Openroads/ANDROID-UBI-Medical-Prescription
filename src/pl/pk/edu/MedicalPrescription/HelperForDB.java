package pl.pk.edu.MedicalPrescription;

import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

/*
 dodac date nastepnej dawki = data start+przedzial
 wyliczyc date zakonczenia dawkowania i nie pobierac tego leku juz z  bazy danych
 po kliknieciu ze wzial dawke wyznaczyc dawke kolejna = data klikniecia + przedzial
*/
// class to be  heler for open DB
public class HelperForDB extends SQLiteOpenHelper{

	private static final int DB_VERSION=1;
	private static final String DB_NAME = "MedicalPrescription";
	protected static final String TABLE_NAME = "Drugs";
	protected static final String COL1 = "id";
	protected static final String COL2 = "name";
	protected static final String COL3 = "period";
	protected static final String COL4 = "dosesAmount";
	protected static final String COL5 = "remainingDoses";
	protected static final String COL6 = "startTime";
	protected static final String COL7 = "nextDoseDate";
	protected static final String COL8 = "eventID";
	//protected static final String COL9 = "markStatus";


	private static final String CREATE_DRUG="CREATE TABLE " + TABLE_NAME +"("
	+COL1+" INTEGER PRIMARY KEY,"
	+COL2+" VARCHAR(50),"
	+COL3+" INT,"
	+COL4+" INT,"
	+COL5+" INT,"
	+COL6+" VARCHAR(25),"
	+COL7+" VARCHAR(25),"
	+COL8+" INTEGER"
	+");";
	
	//+COL9+" INT DEFAULT 0"
	private static final String DELETE_ENTRIES = "DROP DATABASE "+TABLE_NAME; 

	public HelperForDB(Context context)
	{
		super(context,DB_NAME,null,DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db){
		
			db.execSQL(CREATE_DRUG);
		
	}

	@Override
	public	void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
		db.execSQL("DROP TABLE " + TABLE_NAME +";");
		db.execSQL(CREATE_DRUG);
	}



}