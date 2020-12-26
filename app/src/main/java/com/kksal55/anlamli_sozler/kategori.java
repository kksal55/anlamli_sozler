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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

//import android.support.v4.widget.SimpleCursorAdapter;

public class kategori extends Activity  {
private DatabaseHelper kategoriler;


Button addmem_bt;
ListView lv;
TextView memID_tv, memName_tv,baslik_tv;


Cursor c=null;
Button btn_kategori;

@Override
protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.kategoriler);

//        Display display = getWindowManager().getDefaultDisplay();
//        ImageView iv_kat = (ImageView) findViewById(R.id.resim_baslik);
//        int width = display.getWidth(); // ((display.getWidth()*20)/100)
//        int height = display.getHeight();// ((display.getHeight()*30)/100)
//        iv_kat.getLayoutParams().width = width;
//        iv_kat.getLayoutParams().height = (width*2)/13;
//        iv_kat.setMaxHeight(41);
//        iv_kat.setMaxWidth(300);


    /////reklam///////


    //Burda AdView objesini oluþturuyoruz ve anasayfa.xml de oluþturduðumuz adView e baðlýyoruz
    AdView adView = (AdView) this.findViewById(R.id.adView);
    AdRequest adRequest = new AdRequest.Builder().build();
    adView.loadAd(adRequest); //adView i yüklüyoruz

    /////reklam sonu/////

    kategoriler = new DatabaseHelper(this);
    baslik_tv =  (TextView) findViewById(R.id.text_fikralar);
    baslik_tv.setText("KATEGORÝLER");


    lv = (ListView) findViewById(R.id.memberList_id);
    Cursor cursor = KayitGetir();
    //Cursor cursor = dbcon.readData();
    String[] from = new String[] { "_id", "kat_adi","sayisi" };
    int[] to = new int[] { R.id.member_id, R.id.member_name,R.id.member_name2 };


    SimpleCursorAdapter adapter = new SimpleCursorAdapter(
            kategori.this, R.layout.list_bilmeceler, cursor, from, to);

    //adapter.notifyDataSetChanged();
    lv.setAdapter(adapter);

    lv.setOnItemClickListener(new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
            memID_tv = (TextView) view.findViewById(R.id.member_id);
            memName_tv = (TextView) view.findViewById(R.id.member_name);

            String memberID_val = memID_tv.getText().toString();
            String memberName_val = memName_tv.getText().toString();


            Intent modify_intent = new Intent("com.kksal55.anlamli_sozler.BILMECELER");
            modify_intent.putExtra("kategori", memberID_val);
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

private String[] SELECT = {"_id", "kat_adi","sayisi"};
private Cursor KayitGetir(){
     SQLiteDatabase db = kategoriler.getReadableDatabase();
     Cursor cursor = db.query("kat", SELECT, null, null, null, null, null);

     startManagingCursor(cursor);
     return cursor;
     }





}