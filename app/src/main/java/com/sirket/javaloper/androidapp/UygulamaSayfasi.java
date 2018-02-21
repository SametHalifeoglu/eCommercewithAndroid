package com.sirket.javaloper.androidapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sirket.javaloper.androidapp.Adapter.UygulamaAdapter;
import com.sirket.javaloper.androidapp.Sabit.Sabitler;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;

public class UygulamaSayfasi extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,GirisYap.OnFragmentInteractionListener,KayitOl.OnFragmentInteractionListener {

    public HashMap<String,String> jsongaleri=new HashMap<>();
    public HashMap<String,String> kategoriresim=new HashMap<>();
    public GridView Gridkategorilist;
    static ArrayList acilis_resim=new ArrayList();
    TextView baslik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uygulama_sayfasi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        kulbilgi=getSharedPreferences(""+Sabitler.xmldosyaAdi, MODE_PRIVATE);
        edit = kulbilgi.edit();
        Gridkategorilist = findViewById(R.id.galeri_resim);
        String urlgaleri="http://jsonbulut.com/json/gallery.php";
        jsongaleri.put("ref","f852da6180b43207bbab131d77f4c048");
        new UygulamaSayfasi.jGaleri(UygulamaSayfasi.this,urlgaleri,jsongaleri).execute();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        try {
            if(kulbilgi.getString("cikisyap","").equals("cikis")){

                UygulamaSayfasi.this.finish();
            }
        }catch (Exception e){
            System.err.println("giris yapmadan cikis olmaz" + e.getMessage());

        }


            if(kulbilgi.getString("onay","").equals("bos")){

                navigationView.inflateMenu(R.menu.activity_uygulama_sayfasi_drawer);


            }else{
                navigationView.inflateMenu(R.menu.activity_beni_hatirla_drawer);

                    String data = kulbilgi.getString("musteriadsoyad" , "").toString();
                    baslik = navigationView.getHeaderView(0).findViewById(R.id.text_ad_soyad);
                    baslik.setText(data);
            }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    public SharedPreferences kulbilgi;
    public SharedPreferences.Editor edit;
    Fragment fgt = null;
    Class fgtClas = null;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_kayit_ol) {

            fgtClas = KayitOl.class;
            acilis_resim.clear();
            try {

                fgt = (Fragment) fgtClas.newInstance();
                FragmentManager mng = getSupportFragmentManager();
                mng.beginTransaction().replace(R.id.flgContent, fgt).commit();

            } catch (Exception e) {
                Log.d("Sayfa açma hatası", e.getMessage());
            }

        } else if (id == R.id.nav_giris_yap) {

            fgtClas = GirisYap.class;
            acilis_resim.clear();
            //UygulamaSayfasi.this.finish();
            try {


                fgt = (Fragment) fgtClas.newInstance();
                FragmentManager mng = getSupportFragmentManager();
                mng.beginTransaction().replace(R.id.flgContent, fgt).commit();

            } catch (Exception e) {
                Log.d("Sayfa açma hatası", e.getMessage());
            }

        }
        else if (id == R.id.nav_sepetim) {
            Intent ınt=new Intent(getApplicationContext(),Sepetim.class);
            startActivity(ınt);

        } else if (id == R.id.nav_ara) {

            Intent ınt=new Intent(getApplicationContext(),Guncel_Kategori.class);
            startActivity(ınt);

        } 
        else if (id == R.id.nav_siparis) {

            Intent ınt=new Intent(getApplicationContext(),Siparislerim.class);
            startActivity(ınt);

        }
        else if (id == R.id.nav_cikis) {

            edit.remove("musteriadsoyad").commit();
            UygulamaSayfasi.this.finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

/*---------------------------------------------------------------------------------------*/
    int tiklama = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK  && event.getRepeatCount() == 0) {
            tiklama++;
            if(tiklama == 2) {
                AlertDialog.Builder uyari = new AlertDialog.Builder(this);
                uyari.setMessage("Falcon Heavy uygulamasından çıkmak üzeresiniz.");
                uyari.setTitle("              Uyarı").setIcon(R.drawable.kalp);
                uyari.setCancelable(true);
                uyari.setPositiveButton("Çıkış Yap", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        System.exit(0);

                    }
                });
                uyari.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tiklama = 0;
                    }
                });
                uyari.show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

/*------------------------------------------------------------------------------------------------------*/
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
/*-----------------------------------------------------------------------------------------------------*/
    class jGaleri extends AsyncTask<Void, Void, Void> {

        String urlgaleri = "";
        String datagaleri = "";
        HashMap<String, String> jsongaleri = null;
        Context cnx;


        public jGaleri(Context cnx, String urlgaleri, HashMap<String, String> jsongaleri) {


            this.urlgaleri = urlgaleri;
            this.jsongaleri = jsongaleri;
            this.cnx = cnx;


        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                datagaleri = Jsoup.connect(urlgaleri).data(jsongaleri).timeout(30000).ignoreContentType(true).execute().body();

            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {

                JSONObject objgaleri = new JSONObject(datagaleri);
                String galerikisaltma = objgaleri.getJSONArray("Galleries").getJSONObject(0).getJSONObject("Gallery").toString();
                boolean durum = objgaleri.getJSONArray("Galleries").getJSONObject(0).getBoolean("durum");
                if (durum) {
                    for (int i = 55; i < 100; i++) {
                        if (galerikisaltma.toLowerCase().contains("" + i + "".toLowerCase())) {
                            try {
                                String url = "http://jsonbulut.com/admin/upload/" + objgaleri.getJSONArray("Galleries").getJSONObject(0).getJSONObject("Gallery").getJSONArray("" + i).getJSONArray(0).getJSONObject(0).getString("url");
                                String alt = objgaleri.getJSONArray("Galleries").getJSONObject(0).getJSONObject("Gallery").getJSONArray("" + i).getJSONArray(0).getJSONObject(0).getString("alt");
                                kategoriresim.put(alt, url);
                                acilis_resim.add(url);
                            } catch (Exception E) {
                                System.err.println("Galeri resimleri getirme hatası:" + E.getMessage());

                            }
                        }
                    }
                    UygulamaAdapter adapter = new UygulamaAdapter(UygulamaSayfasi.this, acilis_resim);
                    Gridkategorilist.setAdapter(adapter);
                }
            } catch (Exception e) {
                System.err.println("Galeri resim hatasi:" + e.getMessage());
            }
        }

    }
}
