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


public class bilmece_arama extends Activity {
	private DatabaseHelper kategoriler;
	ListView lv;
	TextView katID_tv, katname_tv;
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
		setContentView(R.layout.bilmece_arama);
		kategoriler = new DatabaseHelper(this);
		
		
		
		
		Intent i = getIntent();
		memberID = i.getStringExtra("deg_kat_id");
		gelen_kelime = i.getStringExtra("deg_bil_adi");

		
		memberName = i.getStringExtra("deg_kat_adi");
		katname_tv= (TextView) findViewById(R.id.text_fikralar_arama);	
		katname_tv.setText(memberID);
		katname_tv.setText(gelen_kelime + memberID);
		
		lv = (ListView) findViewById(R.id.listview_fikralar_arama);
        islem_tur =i.getStringExtra("islem").toString();
		

             cursor = KayitGetirArama();
            
		

		
		
		
		
		//Cursor cursor = KayitGetir();
	    //Cursor cursor = dbcon.readData();
		//lv.removeAllViews();
		String[] from = new String[] { "_id", "icerik" };
		int[] to = new int[] { R.id.list_fikra_id, R.id.list_fikra_baslik };
		
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				bilmece_arama.this, R.layout.listbilmeceara, cursor, from, to);

		//adapter.notifyDataSetChanged();
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
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
	
	private String[] SELECT = {"_id", "icerik"};
	
	private Cursor KayitGetirArama(){
		String []selectionArgs = {"'%"+gelen_kelime+"%'"};
	     SQLiteDatabase db = kategoriler.getReadableDatabase();
	     Cursor cursor = db.query("sozler", SELECT, "icerik like '%"+gelen_kelime+"%'", null, null, null, null);
	 
	     startManagingCursor(cursor);
	     return cursor;
	     }
	
	@Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();
        finish();
    }
}
