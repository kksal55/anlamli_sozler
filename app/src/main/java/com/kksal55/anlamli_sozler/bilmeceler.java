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

public class bilmeceler extends Activity  {
    private DatabaseHelper kategoriler;


    Button addmem_bt;
    ListView lv;
    TextView memID_tv, memName_tv,baslik_tv;


    Cursor c=null;
    Button btn_kategori;
    private String memberName="";
    private String memberID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.bilmeceler);

        /////reklam///////


        //Burda AdView objesini oluþturuyoruz ve anasayfa.xml de oluþturduðumuz adView e baðlýyoruz
        AdView adView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest); //adView i yüklüyoruz

        /////reklam sonu/////

        Intent i = getIntent();
        memberID = i.getStringExtra("kategori");



        kategoriler = new DatabaseHelper(this);
        baslik_tv =  (TextView) findViewById(R.id.text_fikralar);
        baslik_tv.setText("Kategorideki Tüm Sözler(7992 Ad)");

        lv = (ListView) findViewById(R.id.memberList_id);
        Cursor cursor = KayitGetir();
        //Cursor cursor = dbcon.readData();
        String[] from = new String[] { "_id","icerik" };
        int[] to = new int[] { R.id.member_id, R.id.member_name};


        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                bilmeceler.this, R.layout.list_bilmeceler, cursor, from, to);

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


                Intent modify_intent = new Intent("com.kksal55.anlamli_sozler.BILMECE_DETAY");

                modify_intent.putExtra("kategori", memberID);
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
    private String[] SELECT = {"_id","kat_id","icerik"};
    private Cursor KayitGetir(){
        SQLiteDatabase db = kategoriler.getReadableDatabase();
        Cursor cursor = db.query("sozler", SELECT, "kat_id='"+memberID+"'", null, null, null, "_id ASC");

        startManagingCursor(cursor);
        return cursor;
    }

    public int formul_bul( int formul_sonucu){

        MainActivity g = new MainActivity();
        formul_sonucu = formul_sonucu - g.sayitoplami(formul_sonucu);
        ilk_ekran ilk3=new ilk_ekran();
        formul_sonucu=ilk3.formul_bul(formul_sonucu);
        return formul_sonucu;
    }



}