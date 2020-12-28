package com.kksal55.anlamli_sozler;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.androidsx.rateme.OnRatingListener;
import com.androidsx.rateme.RateMeDialog;
import com.androidsx.rateme.RateMeDialogTimer;

public class ilk_ekran extends Activity {
    private DatabaseHelper kategoriler;
    private String[] SELECT_OKU = {"_id","kategori"};
    private int kat_degeri=1,kal_id=1,devam_mi=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.ilk_ekran);
        kategoriler = new DatabaseHelper(this);
        hit_resim_kontrol();
        uygulamayiOyla();
        ((Button)findViewById(R.id.btn_karisik)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent modify_intent = new Intent("com.kksal55.anlamli_sozler.BILMECE_DETAY");

                modify_intent.putExtra("kategori", "karýþýk_fýkra_oku_rnd");
                modify_intent.putExtra("deg_bil_id", "500");
                startActivity(modify_intent);


            }
        });
        ((ImageButton)findViewById(R.id.btn_tumu)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent modify_intent = new Intent("com.kksal55.anlamli_sozler.BILMECE_DETAY");
                modify_intent.putExtra("kategori", "7");
                modify_intent.putExtra("deg_bil_id","13439");
                startActivity(modify_intent);


            }
        });
        ((ImageButton)findViewById(R.id.btn_devam_et)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                kat_degeri=1;
                Cursor cursor = KayitGetir();
                KayitGoster(cursor);
                Intent modify_intent = new Intent("com.kksal55.anlamli_sozler.BILMECE_DETAY");
                modify_intent.putExtra("kategori", String.valueOf(kat_degeri));
                modify_intent.putExtra("deg_bil_id",  String.valueOf(kal_id));
                startActivity(modify_intent);


            }
        });
        ((ImageButton)findViewById(R.id.btn_cikis)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        ((ImageButton)findViewById(R.id.btn_yildiz)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                uygulamayiOyla2();
            }
        });
        ((ImageButton)findViewById(R.id.btn_favori)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent modify_intent = new Intent("com.kksal55.anlamli_sozler.FAVORI");
                startActivity(modify_intent);
            }
        });
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.checkbox_kal_bil:
                SQLiteDatabase db = kategoriler.getWritableDatabase();
                ContentValues veriler = new ContentValues();

                if (checked) {
                    veriler.put("idsi", 1);
                    veriler.put("kategori", 1);
                    db.update("kal_bil", veriler, "_id=2", null);
                    findViewById(R.id.btn_devam_et).setClickable(true);
                    findViewById(R.id.btn_devam_et).setAlpha(1f);
                    Toast.makeText(ilk_ekran.this, "\"Continuar con la última Chistes\" is Activated ", Toast.LENGTH_SHORT).show();
                } else {
                    veriler.put("idsi", 0);
                    veriler.put("kategori", 0);
                    db.update("kal_bil", veriler, "_id=2", null);
                    findViewById(R.id.btn_devam_et).setClickable(false);
                    findViewById(R.id.btn_devam_et).setAlpha(0.5f);
                    Toast.makeText(ilk_ekran.this, "\"Continuar con la última Chistes\" is Inactive", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
    private void hit_resim_kontrol() {
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox_kal_bil);
        SQLiteDatabase checkBox_kontrol = kategoriler.getReadableDatabase();
        Cursor hit_resim_kontrol_kayitlar = checkBox_kontrol.query("kal_bil", SELECT_OKU, "kategori=1 and _id=2" , null, null, null, null);
        if (hit_resim_kontrol_kayitlar.moveToNext())
        {
            checkBox.setChecked(true);
            findViewById(R.id.btn_devam_et).setClickable(true);
            findViewById(R.id.btn_devam_et).setAlpha(1f);
            devam_mi=1;

        }
        else
        {
            checkBox.setChecked(false);
            findViewById(R.id.btn_devam_et).setClickable(false);
            findViewById(R.id.btn_devam_et).setAlpha(0.5f);
            devam_mi=0;

        }

    }
    private String[] SELECT = {"_id", "idsi","kategori"};
    private Cursor KayitGetir(){
        SQLiteDatabase db = kategoriler.getReadableDatabase();
        Cursor cursor = db.query("kal_bil", SELECT, "_id=1" , null, null, null, null);
        startManagingCursor(cursor);
        return cursor;
    }
    private void KayitGoster(Cursor cursor){
        if(devam_mi==1) {
            while (cursor.moveToNext()) {
                kal_id = Integer.parseInt(cursor.getString((cursor.getColumnIndex("idsi"))));
            }
        }else {
            //if (kat_degeri==5){ kal_id = 1;}

        }
    }
    public int formul_bul( int formul_sonucu){

        MainActivity g = new MainActivity();
        formul_sonucu = formul_sonucu + g.sayitoplami(formul_sonucu) * g.sayitoplami(formul_sonucu);
        return formul_sonucu;
    }
    public void uygulamayiOyla() {

        RateMeDialogTimer.onStart(this);
        if (RateMeDialogTimer.shouldShowRateDialog(this, 2, 3)) {


            new RateMeDialog.Builder(getPackageName(), getString(R.string.app_name))
                    .setHeaderBackgroundColor(getResources().getColor(R.color.colorPrimary))
                    .setBodyBackgroundColor(getResources().getColor(R.color.white))
                    .setBodyTextColor(getResources().getColor(R.color.colorPrimary))
                    //.enableFeedbackByEmail("hokkabazsoft@gmail.com")
                    .showAppIcon(R.drawable.ic_launcher)
                    .setShowShareButton(true)
                    .setRateButtonBackgroundColor(getResources().getColor(R.color.colorPrimary))
                    .setRateButtonPressedBackgroundColor(getResources().getColor(R.color.colorPrimary))
                    .setOnRatingListener(new OnRatingListener() {
                        @Override
                        public void onRating(RatingAction action, float rating) {
//                            Toast.makeText(MainActivity.this,
//                                    "Rate Me action: " + action + " (rating: " + rating + ")", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public int describeContents() {
                            return 0;
                        }

                        @Override
                        public void writeToParcel(Parcel dest, int flags) {
                            // Nothing to write
                        }
                    })
                    .build()
                    .show(getFragmentManager(), "custom-dialog");

        }

    }
    public void uygulamayiOyla2() {

        RateMeDialogTimer.onStart(this);



        new RateMeDialog.Builder(getPackageName(), getString(R.string.app_name))
                .setHeaderBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .setBodyBackgroundColor(getResources().getColor(R.color.white))
                .setBodyTextColor(getResources().getColor(R.color.colorPrimary))
                .enableFeedbackByEmail("hokkabazsoft@gmail.com")
                .showAppIcon(R.drawable.ic_launcher)
                .setShowShareButton(true)
                .setRateButtonBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .setRateButtonPressedBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .setOnRatingListener(new OnRatingListener() {
                    @Override
                    public void onRating(RatingAction action, float rating) {
//                            Toast.makeText(MainActivity.this,
//                                    "Rate Me action: " + action + " (rating: " + rating + ")", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        // Nothing to write
                    }
                })
                .build()
                .show(getFragmentManager(), "custom-dialog");



    }

    @Override
    public void onBackPressed() {
        if(Build.VERSION.SDK_INT>=16 && Build.VERSION.SDK_INT<21){
            finishAffinity();
        } else if(Build.VERSION.SDK_INT>=21){
            finishAndRemoveTask();
        }

        super.onBackPressed();
    }

}
