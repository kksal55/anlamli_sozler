package com.kksal55.anlamli_sozler;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DatabaseHelper extends SQLiteOpenHelper{

	String DB_PATH =null;

	private static String DB_NAME = "config";

	private SQLiteDatabase myDataBase;
	private final Context myContext;

	public DatabaseHelper(Context context) {

		super(context, DB_NAME, null, 1);
		this.myContext = context;

		DB_PATH="/data/data/"+context.getPackageName()+"/"+"databases/";


	}

	public void createDataBase() throws IOException{

		boolean dbExist = databaseExist();

		if(dbExist){
		}else{

			this.getReadableDatabase();
this.close();
			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	private boolean databaseExist(){

		SQLiteDatabase checkDB = null;

		try{
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

		}catch(SQLiteException e){

		}

		if(checkDB != null){

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}


//    public boolean databaseExist()
//    {
//
//    	   boolean checkdb = false;
//    	    try{
//    	        String myPath = DB_PATH + DB_NAME;
//    	        File dbfile = new File(myPath);
//    	        //checkdb = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
//    	        checkdb = dbfile.exists();
//    	    }
//    	    catch(SQLiteException e){
//    	        System.out.println("Database doesn't exist");
//    	    }
//
//    	    return checkdb;
//
//    }


	private void copyDataBase() throws IOException{

		//Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		//Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		//transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}

		//Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException{

		//Open the database
		String myPath = DB_PATH + DB_NAME;
		//myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
	}

	@Override
	public synchronized void close() {

		if(myDataBase != null)
			myDataBase.close();

		super.close();

	}


	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	//return cursor
	public Cursor query(String table,String[] columns, String selection,String[] selectionArgs,String groupBy,String having,String orderBy){
		return myDataBase.query("kat", null, null, null, null, null, null);


	}
}