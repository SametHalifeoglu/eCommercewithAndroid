package com.sirket.javaloper.androidapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.sirket.javaloper.androidapp.Sabit.Sabitler;
import com.squareup.picasso.Picasso;

import java.util.Map;
import java.util.Set;

public class BaslangicSayfasi extends AppCompatActivity {

    public SharedPreferences kulbilgi;
    public SharedPreferences.Editor edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        ImageView kapak;
        kapak=findViewById(R.id.kapak);
        Picasso.with(BaslangicSayfasi.this).load("https://s-media-cache-ak0.pinimg.com/originals/65/3e/c0/653ec08359cf6cdf2820c888dc8ca9f2.jpg").fit().centerCrop().into(kapak);
        Runnable rn = () -> {
            try {
                Thread.sleep(2000);


                kulbilgi=getSharedPreferences(""+ Sabitler.xmldosyaAdi, MODE_PRIVATE);
                edit = kulbilgi.edit();
                Map<String, ?> all = kulbilgi.getAll();
                Set<String> keys = all.keySet();
                for (String i : keys) {
                    //edit.remove(i).commit();
                    //Log.d("durum", i);
                }

                if(kulbilgi.getString("musteriadsoyad","").equals("")){
                    Intent sayfagec=new Intent(BaslangicSayfasi.this,UygulamaSayfasi.class);
                    edit.putString("onay","bos");
                    edit.commit();
                    startActivity(sayfagec);
                }else{
                    Intent sayfagec=new Intent(BaslangicSayfasi.this,UygulamaSayfasi.class);

                    edit.putString("onay","dolu");
                    edit.commit();
                    startActivity(sayfagec);
                }
                BaslangicSayfasi.this.finish();

            }catch (Exception ex) {
                Log.e("Hata", ex.toString());
            }
        };
        new Thread(rn).start();


    }
    }

