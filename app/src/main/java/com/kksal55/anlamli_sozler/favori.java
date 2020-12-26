package com.kksal55.anlamli_sozler;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class favori extends Activity {
	private DatabaseHelper kategoriler;
	ListView lv;
	TextView katID_tv, katname_tv,katID_hit_tv,baslik_tv;
	private String memberID="";
	private String memberName="";
	private String islem_tur="";
	private String gelen_kelime="te";
	private Cursor cursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.favori_sayfa);
		kategoriler = new DatabaseHelper(this);
		
		 /////reklam///////
        //Burda AdView objesini oluþturuyoruz ve anasayfa.xml de oluþturduðumuz adView e baðlýyoruz
        AdView adView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest); //adView i yüklüyoruz
        /////reklam sonu/////
		
	
		
		baslik_tv =  (TextView) findViewById(R.id.text_fikralar);	
		baslik_tv.setText("Favorilerim");
		
		lv = (ListView) findViewById(R.id.listview_bilmeceler);
       
		
	
			 cursor = KayitGetir();


		String[] from = new String[] {"_id" , "bil_id", "bil_adi" };
		int[] to = new int[] {R.id.list_fikra_id_hit, R.id.list_fikra_id, R.id.list_fikra_baslik };		
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				favori.this, R.layout.bilmece_fav, cursor, from, to);

		//adapter.notifyDataSetChanged();
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				katID_hit_tv = (TextView) view.findViewById(R.id.list_fikra_id_hit);
				katID_tv = (TextView) view.findViewById(R.id.list_fikra_id);
				katname_tv = (TextView) view.findViewById(R.id.list_fikra_baslik);

				String memberID_val = katID_tv.getText().toString();
				String memberName_val = katname_tv.getText().toString();

			
				Intent modify_intent = new Intent("com.kksal55.anlamli_sozler.BILMECE_DETAY");
				
				
				
				modify_intent.putExtra("kategori", "-1");
				modify_intent.putExtra("deg_bil_id", memberID_val);
				
				startActivity(modify_intent);
			}
		});
		
		
		((ImageButton)findViewById(R.id.btn_anasayfa_img)).setOnClickListener(new View.OnClickListener(){
        	@Override
        	public void onClick(View v) {	
        		finish();
        	}
        });
	}
	
	private String[] SELECT = {"_id" ,"bil_id", "bil_adi"};
	

	
	private Cursor KayitGetir(){
	     SQLiteDatabase db = kategoriler.getReadableDatabase();
	     Cursor cursor = db.query("favori", SELECT, null , null, null, null, null);
	 
	     startManagingCursor(cursor);
	     return cursor;
	     }
	@Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();
        finish();
    }
	public int formul_bul( int formul_sonucu,int random_sayi){

		MainActivity g = new MainActivity();

		formul_sonucu = formul_sonucu - ((g.sayitoplami(random_sayi)-2) * g.sayitoplami(random_sayi)) - (g.sayitoplami(formul_sonucu-1) * g.sayitoplami(formul_sonucu+formul_sonucu));
		bilmeceler auto2=new bilmeceler();
		formul_sonucu=auto2.formul_bul(formul_sonucu);
		return formul_sonucu;
	}
}
