package com.kksal55.anlamli_sozler;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

//import com.google.analytics.*;


public class bilmece_detay extends Activity {
   private DatabaseHelper kategoriler;
    int formul_kat,formul_sonuc,cevap_tiklama=0,yaziboyut,altgurup_acik=0;
   TextView tv_katID, tv_fikraname,tv_fikradetay, tv_fikrakat_id, tv_fikrakat_adi;
   private String str_katID,str_katname,str_fikraname,str_fikradetay,str_kategori,str_fikradetay2,str_kategori_adi,karisik_mod,font_ne,ayarlar="acik";
   float downXValue;
   private ImageView img_hit_ekle_islem,img_karisik;
    private EditText aramakelimesi;
   private InterstitialAd gecisReklam;
   private String[] SELECT_OKU = {"_id","bil_id"};
    private RelativeLayout realscr;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       // TODO sAuto-generated method stub
       super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
       setContentView(R.layout.bilmece_detay);

       kategoriler = new DatabaseHelper(this);
       img_hit_ekle_islem = (ImageView) findViewById(R.id.btn_hit_ekle);
       img_karisik = (ImageView) findViewById(R.id.karisik);
        //////////////reklam////////////////
       AdView adView = (AdView) this.findViewById(R.id.adView);
       AdRequest adRequest = new AdRequest.Builder().build();
       adView.loadAd(adRequest); //adView i yüklüyoruz

       gecisReklam = new InterstitialAd(this);
       gecisReklam.setAdUnitId("ca-app-pub-8786191356169416/7862533102");//Reklam Ýd miz.Admob da oluþturduðumuz geçiþ reklam id si
       gecisReklam.setAdListener(new AdListener() { //Geçiþ reklama listener ekliyoruz

            @Override
            public void onAdFailedToLoad(int errorCode) { //Geçiþ Reklam Yüklenemediðinde  Çalýþýr
             //Toast.makeText(getApplicationContext(), "Reklam Yüklenirken Hata Oluþtu.", Toast.LENGTH_LONG).show();
            }
            public void onAdClosed(){
                loadGecisReklam();
             }
       });
       loadGecisReklam();
       //////////////////reklam sonu/////////////

       tv_fikraname = (TextView) findViewById(R.id.detay_baslik);
       tv_fikradetay = (TextView) findViewById(R.id.detay_fikra);
       /////font ayarlama//////
       //Cursor fontcursor = fontgetir();
       fontgoster();
       boyutgoster();
       Typeface face = Typeface.createFromAsset(getAssets(),font_ne);
       tv_fikradetay.setTypeface(face);
       /////font ayarlama//////
       Intent i = getIntent();
       str_katID = i.getStringExtra("deg_bil_id");
       str_katname = i.getStringExtra("kategori");
       karisik_mod=i.getStringExtra("kategori");;
       //google analytcs kodlarý
       Tracker t = ((GoogleAnalyticsApp) getApplication()).getTracker(GoogleAnalyticsApp.TrackerName.APP_TRACKER);
       t.setScreenName("Anlamlý Sözler Detay");
       t.send(new HitBuilders.AppViewBuilder().build());
      // Toast.makeText(bilmece_detay.this, String.valueOf(str_katname), Toast.LENGTH_SHORT).show();
       //bitiþ
        try{
                if (str_katname.equals("karýþýk_fýkra_oku_rnd"))
                {
                    Cursor cursor = KayitGetir_rastgele();
                       KayitGoster(cursor);
                       Toast.makeText(bilmece_detay.this, "Rastgele fýkra modu açýk", Toast.LENGTH_SHORT).show();
                }
                else
                {
                   Cursor cursor = KayitGetir();
                   KayitGoster(cursor);
                }
            }
     finally{
                   kategoriler.close();
            }

           ((ImageButton)findViewById(R.id.btn_hit_ekle)).setOnClickListener(new View.OnClickListener(){
               @Override
               public void onClick(View v) {
                    SQLiteDatabase db_oku = kategoriler.getReadableDatabase();
                    SQLiteDatabase db = kategoriler.getWritableDatabase();
                    Cursor kayitlar = db_oku.query("favori", SELECT_OKU, "bil_id="+str_katID , null, null, null, null);
                   if (kayitlar.moveToNext())
                   {
                       db.delete("favori", "bil_id=" + str_katID, null);
                       img_hit_ekle_islem.setImageResource(R.drawable.btn_star_big_off);
                       Toast.makeText(bilmece_detay.this, "Favorilerden Çýkarýldý", Toast.LENGTH_SHORT).show();
                   }
                   else
                   {
                       ContentValues veriler = new ContentValues();
                       veriler.put("bil_id", str_katID);
                       veriler.put("bil_adi", tv_fikradetay.getText().toString().trim());
                       db.insertOrThrow("favori", null, veriler);
                       Toast.makeText(bilmece_detay.this, "Favorilerime Eklendi", Toast.LENGTH_SHORT).show();
                       img_hit_ekle_islem.setImageResource(R.drawable.btn_star_big_on);

                   }
               }
           });
           ((ImageButton)findViewById(R.id.btn_anasayfa_img)).setOnClickListener(new View.OnClickListener(){
               @Override
               public void onClick(View v) {
                   finish();
               }
           });
           ((ImageButton)findViewById(R.id.paylas)).setOnClickListener(new View.OnClickListener(){
               @Override
               public void onClick(View v) {
                   kopyala();
                   shareIt();
               }
           });

           ((ImageButton)findViewById(R.id.btn_sonraki_fikra)).setOnClickListener(new View.OnClickListener(){
               @Override
               public void onClick(View v) {
                   reklam_gosterim_kontrol();
                   try{
                           if (karisik_mod.equals("karýþýk_fýkra_oku_rnd"))
                           {
                               Cursor cursor = KayitGetir_rastgele();
                                  KayitGoster(cursor);
                           }
                           else
                           {
                               Cursor cursor = KayitGetir_sonraki();
                               KayitGoster(cursor);
                              }
                       }
                finally{
                       kategoriler.close();
                           }
               }
           });
           ((ImageButton)findViewById(R.id.btn_onceki_fikra)).setOnClickListener(new View.OnClickListener(){
               @Override
               public void onClick(View v) {
                   reklam_gosterim_kontrol();
                   try{
                       if (karisik_mod.equals("karýþýk_fýkra_oku_rnd"))
                       {
                           Cursor cursor = KayitGetir_rastgele();
                              KayitGoster(cursor);
                       }
                       else
                       {
                           Cursor cursor = KayitGetir_onceki();
                           KayitGoster(cursor);
                          }
                   }
            finally{
                   kategoriler.close();
                       }
               }
           });

           ((ImageButton)findViewById(R.id.btn_resim_cek)).setOnClickListener(new View.OnClickListener(){
               @Override
               public void onClick(View v) {

                   Intent modify_intent = new Intent("com.kksal55.anlamli_sozler.RESIM_CEK");
                   modify_intent.putExtra("kategori", str_katname);
                   modify_intent.putExtra("deg_bil_id", str_katID);
                   startActivity(modify_intent);

               }
           });


       ((Button)findViewById(R.id.btn_tum_bilmeceler)).setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               finish();
               Intent modify_intent = new Intent("com.kksal55.anlamli_sozler.BILMECELER");
               modify_intent.putExtra("kategori", str_katname);
               startActivity(modify_intent);


           }
       });
           final ImageButton btnOpenPopup = (ImageButton)findViewById(R.id.btn_ara);
           btnOpenPopup.setOnClickListener(new Button.OnClickListener(){
      @Override
      public void onClick(View arg0) {
       LayoutInflater layoutInflater = (LayoutInflater)getBaseContext()
       .getSystemService(LAYOUT_INFLATER_SERVICE);

       View popupView = layoutInflater.inflate(R.layout.arama_popup, null);
                final PopupWindow popupWindow = new PopupWindow(popupView,
                  LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);

                popupWindow.setBackgroundDrawable (new BitmapDrawable());
                popupWindow.setOutsideTouchable(true);


                ImageButton btnDismiss = (ImageButton)popupView.findViewById(R.id.close);
                btnDismiss.setOnClickListener(new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
         // TODO Auto-generated method stub
         popupWindow.dismiss();
        }
             });

                ImageButton arama = (ImageButton)popupView.findViewById(R.id.btn_ara);
                 arama.setOnClickListener(new ImageButton.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                             LayoutInflater layoutInflater = (LayoutInflater)getBaseContext()
                                      .getSystemService(LAYOUT_INFLATER_SERVICE);
                                    View popupView = layoutInflater.inflate(R.layout.arama_popup, null);
                                    View popupView3 = popupWindow.getContentView();
                                    final EditText kelime_txt = ( EditText)popupView3.findViewById(R.id.kelime_txt);
                                    String kelime = kelime_txt.getText().toString();


                                    Intent modify_intent = new Intent("com.kksal55.anlamli_sozler.BILMECE_ARAMA");
                                    modify_intent.putExtra("deg_bil_adi", kelime);
                                    modify_intent.putExtra("deg_kat_id", "35");
                                    modify_intent.putExtra("islem", "arama");
                                    startActivity(modify_intent);
                        }
                    });
                popupWindow.showAsDropDown(btnOpenPopup, 0, 0);
      }});

//       ScrollView myLayout = (ScrollView)findViewById(R.id.scrollView2);
//           myLayout.setOnTouchListener(new ScrollView.OnTouchListener() {
//                       public boolean onTouch(View v,MotionEvent m) {
//
//                           switch (m.getAction())
//                           {
//                               case MotionEvent.ACTION_DOWN:
//                               {
//                                   // store the X value when the user's finger was pressed down
//                                   downXValue = m.getX();
//                                   break;
//                               }
//
//                               case MotionEvent.ACTION_UP:
//                               {
//                                   // Get the X value when the user released his/her finger
//                                   float currentX = m.getX();
//
//                                   // going backwards: pushing stuff to the right
//                                   if (downXValue < currentX & (currentX-downXValue)>150)
//                                   {
//                                       //Toast.makeText(getApplicationContext(), String.valueOf(downXValue)+"-"+String.valueOf(currentX) , Toast.LENGTH_SHORT).show();
//                                       reklam_gosterim_kontrol();
//                                       try{
//                                           if (karisik_mod.equals("karýþýk_fýkra_oku_rnd"))
//                                           {
//                                               Cursor cursor = KayitGetir_rastgele();
//                                                  KayitGoster(cursor);
//                                           }
//                                           else
//                                           {
//                                               Cursor cursor = KayitGetir_onceki();
//                                               KayitGoster(cursor);
//                                              }
//                                       }
//                                finally{
//                                       kategoriler.close();
//                                           }
//                                   }
//
//                                   // going forwards: pushing stuff to the left
//                                   if (downXValue > currentX & (downXValue-currentX)>150)
//                                   {
//                                       //Toast.makeText(getApplicationContext(), String.valueOf(downXValue)+"-"+String.valueOf(currentX) , Toast.LENGTH_SHORT).show();
//                                       reklam_gosterim_kontrol();
//                                       try{
//                                           if (karisik_mod.equals("karýþýk_fýkra_oku_rnd"))
//                                           {
//                                               Cursor cursor = KayitGetir_rastgele();
//                                                  KayitGoster(cursor);
//                                           }
//                                           else
//                                           {
//                                               Cursor cursor = KayitGetir_sonraki();
//                                               KayitGoster(cursor);
//                                              }
//                                       }
//                                finally{
//                                       kategoriler.close();
//                                           }
//                                   break;
//                               }
//                           }
//                   }
//                           return true;	}
//                       }
//                  );


       ((ImageButton)findViewById(R.id.what)).setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               PackageManager pm=getPackageManager();
               try {

                   Intent waIntent = new Intent(Intent.ACTION_SEND);
                   waIntent.setType("text/plain");
                   String text =tv_fikradetay.getText().toString().trim();
                   PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                   waIntent.setPackage("com.whatsapp");
                   waIntent.putExtra(Intent.EXTRA_TEXT, text);
                   startActivity(Intent.createChooser(waIntent, "Share with"));
               } catch (PackageManager.NameNotFoundException e) {
                   Toast.makeText(bilmece_detay.this, "WhatsApp kurulu deðil", Toast.LENGTH_SHORT).show();
               }
           }
       });
       ((ImageButton)findViewById(R.id.sms)).setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               Toast.makeText(bilmece_detay.this, tv_fikradetay.getText().toString(), Toast.LENGTH_SHORT).show();
               String smsBody=tv_fikradetay.getText().toString().trim();
               Uri uri = Uri.parse("smsto:");
               Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
               sendIntent.putExtra("sms_body", smsBody);
               sendIntent.setData(Uri.parse("smsto:"));
               startActivity(sendIntent);
           }
       });

       ((ImageButton)findViewById(R.id.face)).setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               Intent modify_intent = new Intent("com.kksal55.anlamli_sozler.RESIM_CEK_TWIT");
               modify_intent.putExtra("kategori", str_katname);
               modify_intent.putExtra("deg_bil_id", str_katID);
               modify_intent.putExtra("nerede", "facebook");
               startActivity(modify_intent);
           }
       });
       ((Button)findViewById(R.id.kopyala)).setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
            kopyala();
           }
       });
       ((ImageButton)findViewById(R.id.yaziarti)).setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               yaziboyutugetir();
               TextView detay_tv = (TextView)findViewById(R.id.detay_fikra);
               detay_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, yaziboyut+1);
               boyut_kayit(yaziboyut+2);
           }
       });
       ((ImageButton)findViewById(R.id.yazieksi)).setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               yaziboyutugetir();
               TextView detay_tv = (TextView)findViewById(R.id.detay_fikra);
               detay_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, yaziboyut-1);
               boyut_kayit(yaziboyut-2);
           }
       });
       ((ImageButton)findViewById(R.id.yazieksi)).setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               yaziboyutugetir();
               TextView detay_tv = (TextView)findViewById(R.id.detay_fikra);
               detay_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, yaziboyut-1);
               boyut_kayit(yaziboyut-2);
           }
       });
       ((ImageButton)findViewById(R.id.karisik)).setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               if (!karisik_mod.equals("karýþýk_fýkra_oku_rnd")) {
                   karisik_mod="karýþýk_fýkra_oku_rnd";
                   str_katname="karýþýk_fýkra_oku_rnd";
                   img_karisik.setImageResource(R.drawable.duzdegil);
                   Toast.makeText(bilmece_detay.this, "Rastgele okuma modu açýldý",Toast.LENGTH_LONG).show();
               }else
               {   karisik_mod="7";
                   str_katname="7";
                   img_karisik.setImageResource(R.drawable.duz);
                   Toast.makeText(bilmece_detay.this, "Karýþýk okuma modu kapatýldý",Toast.LENGTH_LONG).show();}
           }
       });
       ((ImageButton)findViewById(R.id.tum_kay)).setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               finish();
               Intent modify_intent = new Intent("com.kksal55.anlamli_sozler.BILMECELER");
               modify_intent.putExtra("kategori", "7");
               startActivity(modify_intent);
           }
       });
       ((ImageButton)findViewById(R.id.menu)).setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               ImageButton menuimage=(ImageButton)findViewById(R.id.menu);
               if (ayarlar.equals("kapali")) {
                   ayarlar="acik";
                   findViewById(R.id.altgurup).setVisibility(View.VISIBLE);
                   menuimage.setImageResource(R.drawable.ic_settings_ok);
               }else
               {                      ayarlar="kapali";
                   findViewById(R.id.altgurup).setVisibility(View.GONE);
                   menuimage.setImageResource(R.drawable.ic_settings);}
//               PopupMenu popup = new PopupMenu(bilmece_detay.this, v);
//               popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
//               popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                   public boolean onMenuItemClick(MenuItem item) {
//                       Toast.makeText(bilmece_detay.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
//                       return true;
//                   }
//               });
//               popup.show();//showing popup menu
               //bilmece_detay.this.openOptionsMenu();

           }
       });
       ((ImageButton)findViewById(R.id.font)).setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {

               PopupMenu popup = new PopupMenu(bilmece_detay.this, v);
               popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
               popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                   public boolean onMenuItemClick(MenuItem item) {
                       SQLiteDatabase db = kategoriler.getWritableDatabase();
                       ContentValues boyut = new ContentValues();
                       switch (item.getTitle().toString())
                       {

                           case "Yazý Stili 1":
                           {

                               boyut.put("adi", "a1.ttf");
                               db.update("font", boyut, "_id=10", null);
                               Typeface face = Typeface.createFromAsset(getAssets(),"a1.ttf");
                               tv_fikradetay.setTypeface(face);
                               break;
                           }
                           case "Yazý Stili 2":
                           {

                               boyut.put("adi", "a2.ttf");
                               db.update("font", boyut, "_id=10", null);
                               Typeface face = Typeface.createFromAsset(getAssets(),"a2.ttf");
                               tv_fikradetay.setTypeface(face);
                               break;
                           }
                           case "Yazý Stili 3":
                           {

                               boyut.put("adi", "a3.ttf");
                               db.update("font", boyut, "_id=10", null);
                               Typeface face = Typeface.createFromAsset(getAssets(),"a3.ttf");
                               tv_fikradetay.setTypeface(face);
                               break;
                           }
                           case "Yazý Stili 4":
                           {

                               boyut.put("adi", "a4.otf");
                               db.update("font", boyut, "_id=10", null);
                               Typeface face = Typeface.createFromAsset(getAssets(),"a4.otf");
                               tv_fikradetay.setTypeface(face);
                               break;
                           }
                           case "Yazý Stili 5":
                           {

                               boyut.put("adi", "a5.ttf");
                               db.update("font", boyut, "_id=10", null);
                               Typeface face = Typeface.createFromAsset(getAssets(),"a5.ttf");
                               tv_fikradetay.setTypeface(face);
                               break;
                           }
                           case "Yazý Stili 6":
                           {

                               boyut.put("adi", "a6.ttf");
                               db.update("font", boyut, "_id=10", null);
                               Typeface face = Typeface.createFromAsset(getAssets(),"a6.ttf");
                               tv_fikradetay.setTypeface(face);
                               break;
                           }
                           case "Yazý Stili 7":
                           {

                               boyut.put("adi", "a7.TTF");
                               db.update("font", boyut, "_id=10", null);
                               Typeface face = Typeface.createFromAsset(getAssets(),"a7.TTF");
                               tv_fikradetay.setTypeface(face);
                               break;
                           }
                           case "Yazý Stili 8":
                           {

                               boyut.put("adi", "a8.ttf");
                               db.update("font", boyut, "_id=10", null);
                               Typeface face = Typeface.createFromAsset(getAssets(),"a8.ttf");
                               tv_fikradetay.setTypeface(face);
                               break;
                           }
                           case "Yazý Stili 9":
                           {

                               boyut.put("adi", "a9.ttf");
                               db.update("font", boyut, "_id=10", null);
                               Typeface face = Typeface.createFromAsset(getAssets(),"a9.ttf");
                               tv_fikradetay.setTypeface(face);
                               break;
                           }

                       }
                       //Toast.makeText(bilmece_detay.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                       return true;
                   }
               });
               popup.show();//showing popup menu
               //bilmece_detay.this.openOptionsMenu();

           }
       });
       }
    private Menu optionsMenu;
   @Override
   public void startManagingCursor(Cursor c) {
   // TODO Auto-generated method stub
   if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
   super.startManagingCursor(c);
   }
   }
   private String[] SELECT = {"_id","kat_id","kat","kat_adi","icerik"};
    private String[] SELECT_devam = {"icerik", "deg"};
   private Cursor KayitGetir(){
      // Toast.makeText(bilmece_detay.this, "kayitgetir" + String.valueOf(str_katID), Toast.LENGTH_SHORT).show();
        SQLiteDatabase db = kategoriler.getReadableDatabase();
        Cursor cursor = db.query("sozler", SELECT, "_id="+ str_katID , null, null, null, null);
        startManagingCursor(cursor);
        return cursor;
        }
   private Cursor KayitGetir_rastgele(){
        //String orderBy = "RANDOM()";
        SQLiteDatabase db = kategoriler.getReadableDatabase();
        Cursor cursor = db.query("sozler Order BY RANDOM() LIMIT 1", SELECT, null , null, null,null, null);
        startManagingCursor(cursor);
        return cursor;
        }
   private Cursor KayitGetir_sonraki(){
        SQLiteDatabase db = kategoriler.getReadableDatabase();
        Cursor cursor = db.query("sozler", SELECT, "_id > " + str_katID , null, null, null, "_id ASC","1");
        startManagingCursor(cursor);
        return cursor;
        }
   private Cursor KayitGetir_onceki(){
        SQLiteDatabase db = kategoriler.getReadableDatabase();
        Cursor cursor = db.query("sozler", SELECT, "_id < " + str_katID , null, null, null, "_id DESC","1");
        startManagingCursor(cursor);
        return cursor;
        }
   private void KayitGoster(Cursor cursor){
            while(cursor.moveToNext()){
                //str_fikraname = cursor.getString((cursor.getColumnIndex("baslik")));
                str_fikradetay= cursor.getString((cursor.getColumnIndex("icerik")));
                str_kategori = cursor.getString((cursor.getColumnIndex("kat_id")));
                str_katname =cursor.getString((cursor.getColumnIndex("kat_id")));
                str_katID = cursor.getString((cursor.getColumnIndex("_id")));
                formul_kat = Integer.parseInt(cursor.getString((cursor.getColumnIndex("kat"))));
                str_kategori_adi = cursor.getString((cursor.getColumnIndex("kat_adi")));


            }
       resim_cek test2= new resim_cek();
       formul_sonuc=test2.formul_bul(Integer.parseInt(str_katID),formul_kat);
       Cursor cursor_cevap = KayitGetir_cevap();
       KayitGoster_cevap(cursor_cevap);
       //Toast.makeText(bilmece_detay.this, str_katID+"-"+String.valueOf(formul_kat),Toast.LENGTH_SHORT).show();


        tv_fikraname.setText(str_fikraname);
        hit_resim_kontrol();
       kaldigin_yer();
       ((Button) findViewById(R.id.btn_tum_bilmeceler)).setText( ""+str_kategori_adi+" Kategorisindeki Tüm Bilmeceler");
       //Toast.makeText(bilmece_detay.this, String.valueOf(formul_sonuc), Toast.LENGTH_SHORT).show();

   }
    private void KayitGoster_cevap(Cursor cursor){
        while(cursor.moveToNext()){str_fikradetay2= cursor.getString((cursor.getColumnIndex("icerik")));
            String yazitam=str_fikradetay+str_fikradetay2;
            tv_fikradetay.setText(Html.fromHtml(yazitam.trim()) + "\n");
        }
    }
    private Cursor KayitGetir_cevap(){

        SQLiteDatabase db = kategoriler.getReadableDatabase();
        Cursor cursor_cevap = db.query("tum_icerik", SELECT_devam, "deg="+ formul_sonuc , null, null, null, "deg DESC","1");
        startManagingCursor(cursor_cevap);
        return cursor_cevap;
    }
   @Override
   public void onBackPressed() {
       finish();
   }
   private void hit_resim_kontrol() {

       SQLiteDatabase hit_resim_kontrol_oku = kategoriler.getReadableDatabase();
       Cursor hit_resim_kontrol_kayitlar = hit_resim_kontrol_oku.query("favori", SELECT_OKU, "bil_id="+str_katID , null, null, null, null);
       if (hit_resim_kontrol_kayitlar.moveToNext())
       {
           img_hit_ekle_islem.setImageResource(R.drawable.btn_star_big_on);
       }
       else
       {
           img_hit_ekle_islem.setImageResource(R.drawable.btn_star_big_off);
       }

       }
   private void shareIt() {

       Intent sharingIntent = new Intent(Intent.ACTION_SEND);
       sharingIntent.setType("text/plain");
       String shareBody = tv_fikradetay.getText().toString().trim();
       sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Anlamlý Sözler Uygulamasýndan");
       sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
       startActivity(Intent.createChooser(sharingIntent, "Paylaþ"));

       }
   public void loadGecisReklam() {
         AdRequest adRequest = new AdRequest.Builder()
             .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
             .addTestDevice("B3EEABB8EE11C2BE770B684D95219E")
             .build();
         gecisReklam.loadAd(adRequest);
       }
   public void showGecisReklam() {

         if (gecisReklam.isLoaded()) {
             gecisReklam.show(); //Reklam yüklenmiþsse gösterilecek
         } else {
             //Toast.makeText(getApplicationContext(), "Reklam Gösterim Ýçin Hazýr Deðil.", Toast.LENGTH_LONG).show();
         }
       }
   public void reklam_gosterim_kontrol() {
            int id_sayi_int = modulo(Integer.parseInt(str_katID),15);
            if (id_sayi_int==0){
                showGecisReklam();
            }
           }
   public int modulo( int m, int n ){
               int mod =  m % n ;
               return ( mod < 0 ) ? mod + n : mod;
           }
   @Override
   protected void onStart() {
       // TODO Auto-generated method stub
       super.onStart();
       GoogleAnalytics.getInstance(bilmece_detay.this).reportActivityStart(this);
   }
   @Override
   protected void onStop() {
       // TODO Auto-generated method stub
       super.onStop();
       GoogleAnalytics.getInstance(bilmece_detay.this).reportActivityStop(this);
   }
    public void kaldigin_yer(){
        SQLiteDatabase db = kategoriler.getWritableDatabase();
if (!karisik_mod.equals("karýþýk_fýkra_oku_rnd")) {
    ContentValues veriler = new ContentValues();
    veriler.put("idsi", str_katID);
    //veriler.put("bil_adi", str_fikraname);
    veriler.put("kategori", str_katname);
    db.update("kal_bil", veriler, "_id=1", null);
    //Toast.makeText(bilmece_detay.this, "Eklendi" + String.valueOf(str_katID)+"-"+String.valueOf(str_katname), Toast.LENGTH_SHORT).show();
}
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
    public void kopyala(){
        str_fikradetay = str_fikradetay.replace("<BR>", "\n");
        int sdk = Build.VERSION.SDK_INT;
        if(sdk < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(tv_fikradetay.getText().toString().trim() + "\n");
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("text label",tv_fikradetay.getText().toString().trim() + "\n");
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(getApplicationContext(),"Söz kopyalandý. Ýstediðiniz yere yapýþtrabilirsiniz.", Toast.LENGTH_LONG).show();
    }
    private void boyutgoster(){
        SQLiteDatabase db = kategoriler.getReadableDatabase();
        Cursor cursor_cevap = db.query("font", SELECT_font, "_id=11" , null, null, null, null,"1");
        startManagingCursor(cursor_cevap);
        while(cursor_cevap.moveToNext()){
            yaziboyut=  Integer.parseInt(cursor_cevap.getString((cursor_cevap.getColumnIndex("adi"))));
        }
        TextView detay_tv = (TextView)findViewById(R.id.detay_fikra);
        detay_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, yaziboyut);
    }
    public  void yaziboyutugetir(){

        SQLiteDatabase db_oku = kategoriler.getReadableDatabase();
        Cursor boyut = db_oku.query("font", SELECT_font, "_id=11" , null, null, null, null);
        if (boyut.moveToNext())
        {
            yaziboyut = Integer.parseInt(boyut.getString((boyut.getColumnIndex("adi"))));
        }
        else
        {
            yaziboyut=23;
        }
    }
    public void boyut_kayit(int gelen_boyut){
        SQLiteDatabase db = kategoriler.getWritableDatabase();
        ContentValues boyut = new ContentValues();
        boyut.put("adi", gelen_boyut);
        db.update("font", boyut, "_id=11", null);
       // Toast.makeText(bilmece_detay.this, String.valueOf(gelen_boyut), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        realscr=(RelativeLayout) findViewById(R.id.realscrl);
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            findViewById(R.id.resim_baslik).setVisibility(View.VISIBLE);
            if (ayarlar.equals("acik")) {
                findViewById(R.id.altgurup).setVisibility(View.VISIBLE);
            }
            realscr.setBackgroundResource(R.drawable.arkaorta);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            findViewById(R.id.resim_baslik).setVisibility(View.GONE);
            findViewById(R.id.altgurup).setVisibility(View.GONE);
            realscr.setBackgroundResource(R.drawable.arkaorta2);
        }
    }
}
