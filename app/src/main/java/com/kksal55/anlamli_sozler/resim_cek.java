package com.kksal55.anlamli_sozler;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


public class resim_cek extends Activity{
	private DatabaseHelper kategoriler;
	int formul_kat,formul_sonuc,cevap_tiklama=0,yaziboyut;
	TextView tv_katID, tv_fikraname,tv_fikradetay, tv_fikrakat_id, tv_fikrakat_adi;
	private String str_katID,str_katname,str_fikraname,str_fikradetay,str_kategori,str_fikradetay2,nerede,font_ne;
	ProgressDialog progressDialog2;
	private LinearLayout linearLayout;
	private Bitmap myBitmap;
	private Date now = new Date();


	protected boolean shouldAskPermissions() {
		return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
	}

	@TargetApi(23)
	protected void askPermissions() {
		String[] permissions = {
				"android.permission.READ_EXTERNAL_STORAGE",
				"android.permission.WRITE_EXTERNAL_STORAGE"
		};
		int requestCode = 200;
		requestPermissions(permissions, requestCode);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.res_cek);
		if (shouldAskPermissions()) {
			askPermissions();
		}
	        //google analytcs kodlarý
	        Tracker t = ((GoogleAnalyticsApp) getApplication()).getTracker(GoogleAnalyticsApp.TrackerName.APP_TRACKER);
			t.setScreenName("Anlamlý Sözler Resim");
			t.send(new HitBuilders.AppViewBuilder().build());
	        //bitiþ
	        kategoriler = new DatabaseHelper(this);
	        tv_fikraname = (TextView) findViewById(R.id.detay_baslik);
			tv_fikradetay = (TextView) findViewById(R.id.detay_fikra);
		linearLayout = (LinearLayout)findViewById(R.id.linear);
		fontgoster();
		Typeface face = Typeface.createFromAsset(getAssets(),font_ne);
		tv_fikradetay.setTypeface(face);
			Intent i = getIntent();
			str_katID = i.getStringExtra("deg_bil_id");
			str_katname = i.getStringExtra("kategori");
			Cursor cursor = KayitGetir();
            KayitGoster(cursor);
            //kategoriler.close();
			showDialog(1);

	}
	protected final Dialog onCreateDialog(final int id) {
	    Dialog dialog = null;
	    switch (id) {
	    case 1:

	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage(
	                "Fýkra resim olarak kaydedilip Ardýndan paylaþým seçeneklerini göreceksiniz.")
	                .setCancelable(false)
	                .setPositiveButton("Tamam",
	                        new DialogInterface.OnClickListener() {
	                            public void onClick(DialogInterface dialog,
	                                    int id) {
	                                //to perform on ok

	                            	//takeScreenshot();
									new LoadViewTask().execute();
	                            }
	                        })
	                .setNegativeButton("Ýptal",
	                        new DialogInterface.OnClickListener() {
	                            public void onClick(DialogInterface dialog,
	                                    int id) {

	                                //dialog.cancel();
	                            	finish();
	                            }
	                        });
	        AlertDialog alert = builder.create();
	        dialog = alert;
	        break;
			case 2:
				break;
	    default:

	    }
	    return dialog;
	}
	private class LoadViewTask extends AsyncTask<String, Integer, String>
	{
		//Before running code in separate thread
		@Override
		protected void onPreExecute()
		{
			progressDialog2 = new ProgressDialog(resim_cek.this);
			progressDialog2.setMessage("Lütfen Bekleyiniz. Paylaþým yapýlabilinecek uygulamalar getiriliyor.");
			progressDialog2.show();

		}

		@Override
		protected String doInBackground(String... values)
		{

			android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
			myBitmap = captureScreen(linearLayout);

			try {
				FileOutputStream fos = new FileOutputStream(new File(Environment
						.getExternalStorageDirectory().toString(), now +"_anlamli_sozler_google_play.PNG"));
				//myBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
			return "";
		}

		@Override
		protected void onProgressUpdate(Integer... values)
		{
			progressDialog2.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(String values)
		{
			paylas(new File(Environment
					.getExternalStorageDirectory().toString(), now +"_anlamli_sozler_google_play.PNG"));
			progressDialog2.dismiss();
			finish();

		}

	}
	private String mPath="";
	public static Bitmap captureScreen(View v) {

		Bitmap screenshot = null;
		try {

			if(v!=null) {

				screenshot = Bitmap.createBitmap(v.getMeasuredWidth(),v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
				Canvas canvas = new Canvas(screenshot);
				v.draw(canvas);
			}

		}catch (Exception e){
			Log.d("ScreenShotActivity", "Failed to capture screenshot because:" + e.getMessage());
		}

		return screenshot;
	}
	private void takeScreenshot() {

	}

	private void paylas(File imageFile){

			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		Uri screenshotUri=FileProvider.getUriForFile(resim_cek.this, BuildConfig.APPLICATION_ID + ".provider",imageFile);
			sharingIntent.setType("image/png");
			sharingIntent.putExtra(Intent.EXTRA_TEXT, "Anlamlý Sözler Uygulamasýndan");
			sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
			startActivity(Intent.createChooser(sharingIntent, "Paylaþmak istediðiniz uygulamayý seçiniz"));
			finish();



	}
	
	private void openScreenshot(File imageFile) {
	    Intent intent = new Intent();
	    intent.setAction(Intent.ACTION_VIEW);
	    Uri uri = Uri.fromFile(imageFile);
	    intent.setDataAndType(uri, "image/*");
	    startActivity(intent);
	}

	private String[] SELECT = {"_id","kat_id","kat","kat_adi","icerik"};
	private String[] SELECT_devam = {"icerik", "deg"};
	private Cursor KayitGetir(){
	     SQLiteDatabase db = kategoriler.getReadableDatabase();
	     Cursor cursor = db.query("sozler", SELECT, "_id="+ str_katID , null, null, null, null);
	     startManagingCursor(cursor);
	     return cursor;
	     }

	private void KayitGoster(Cursor cursor){	    	
        while(cursor.moveToNext()){
		str_fikradetay= cursor.getString((cursor.getColumnIndex("icerik")));
			str_kategori = cursor.getString((cursor.getColumnIndex("kat_id")));
			str_katname =cursor.getString((cursor.getColumnIndex("kat_id")));
			str_katID = cursor.getString((cursor.getColumnIndex("_id")));
			formul_kat = Integer.parseInt(cursor.getString((cursor.getColumnIndex("kat"))));
        }

		formul_sonuc=formul_bul(Integer.parseInt(str_katID),formul_kat);
		Cursor cursor_cevap = KayitGetir_cevap();
		KayitGoster_cevap(cursor_cevap);

//        str_fikraname = str_fikraname.substring(0,1).toUpperCase() + str_fikraname.substring(1);
//		tv_fikradetay.setText(str_fikraname);

}
	private void KayitGoster_cevap(Cursor cursor){
		while(cursor.moveToNext()){str_fikradetay2= cursor.getString((cursor.getColumnIndex("icerik")));
			tv_fikraname.setText(str_fikraname);
			tv_fikradetay.setText(Html.fromHtml(str_fikradetay+str_fikradetay2) + "\n");
		}
	}
	private Cursor KayitGetir_cevap(){

		SQLiteDatabase db = kategoriler.getReadableDatabase();
		Cursor cursor_cevap = db.query("tum_icerik", SELECT_devam, "deg="+ formul_sonuc , null, null, null, "deg DESC","1");
		startManagingCursor(cursor_cevap);
		return cursor_cevap;
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(resim_cek.this).reportActivityStart(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(resim_cek.this).reportActivityStop(this);
		kategoriler.close();
	}
	@Override
	protected void onResume() {

		super.onResume();
		kategoriler = new DatabaseHelper(this);
	}

	private String[] SELECT_font = {"adi", "_id"};
	private void fontgoster(){
		SQLiteDatabase db = kategoriler.getReadableDatabase();
		Cursor cursor_cevap = db.query("font", SELECT_font, "_id=10" , null, null, null, null,"1");
		startManagingCursor(cursor_cevap);
		while(cursor_cevap.moveToNext()){
			font_ne= cursor_cevap.getString((cursor_cevap.getColumnIndex("adi")));
		}
	}
	public int formul_bul( int id,int random_sayi){
		int formul_sonucu;
		MainActivity g = new MainActivity();
		formul_sonucu = (((random_sayi * (g.sayitoplami(random_sayi)-1)) + (id + g.sayitoplami(id))) * (g.sayitoplami(random_sayi) + g.sayitoplami(id)));
		favori form=new favori();
		formul_sonucu=form.formul_bul(formul_sonucu,random_sayi);
		return formul_sonucu;
	}



	
}
