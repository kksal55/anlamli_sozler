package com.kksal55.anlamli_sozler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;

import java.io.IOException;


public class MainActivity extends Activity {
    private ProgressDialog progressDialog2;
    Cursor c=null;
    private DatabaseHelper kategoriler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

//        DatabaseHelper myDbHelper = new DatabaseHelper(MainActivity.this);
//        try { myDbHelper.createDataBase();}
//        catch (IOException ioe) {throw new Error("Veritabaný oluþturulamadý! Uygulamayý yeniden baþlatýn");}
//        try { myDbHelper.openDataBase("hulyaam");}
//        catch(SQLException sqle){	throw sqle;	}
//        Intent intent = new Intent("com.kksal55.anlamli_sozler.ILK_EKRAN");
//        startActivity(intent);
//        finish();

        new LoadViewTask().execute();


    }
    private class LoadViewTask extends AsyncTask<Void, Integer, Void>
    {
        //Before running code in separate thread
        @Override
        protected void onPreExecute()
        {
            progressDialog2 = ProgressDialog.show(MainActivity.this, "Yaklaþýk 5~ sn kadar", "Lütfen Bekleyiniz...");
        }

        //The code to be executed in a background thread.
        @Override
        protected Void doInBackground(Void... params)
        {
            /* This is just a code that delays the thread execution 4 times,
             * during 850 milliseconds and updates the current progress. This
             * is where the code that is going to be executed on a background
             * thread must be placed.
             */

//
            DatabaseHelper myDbHelper = new DatabaseHelper(MainActivity.this);
            try { myDbHelper.createDataBase();}
            catch (IOException ioe) {throw new Error("Veritabaný oluþturulamadý! Uygulamayý yeniden baþlatýn");}
            try { myDbHelper.openDataBase();}
            catch(SQLException sqle){	throw sqle;	}


            return null;
        }

        //Update the progress
        @Override
        protected void onProgressUpdate(Integer... values)
        {
            //set the current progress of the progress dialog
            progressDialog2.setProgress(values[0]);
        }

        //after executing the code in the thread
        @Override
        protected void onPostExecute(Void result)
        {
            //close the progress dialog
            progressDialog2.dismiss();
            //initialize the View
            //setContentView(R.layout.main);

            Intent intent = new Intent("com.kksal55.anlamli_sozler.ILK_EKRAN");
            startActivity(intent);
            finish();
        }

    }
    public int sayitoplami( int sayi){
        int sonuc = 0;
        while (sayi > 0)
        {
            sonuc += (sayi % 10);
            sayi = sayi / 10;
        }
        return sonuc;
    }
}
