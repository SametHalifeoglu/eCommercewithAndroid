package com.sirket.javaloper.androidapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sirket.javaloper.androidapp.Adapter.UrunGridAdapter;
import com.sirket.javaloper.androidapp.property.urunProperty;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;

public class Urunler extends AppCompatActivity {

    GridView gridlist;
    HashMap<String, String> urunapi = new HashMap<>();
    public static String ModelKategorileri;
    public static String Modeller;
    JSONArray urunkisaltma;
    SharedPreferences kulbilgi;
    SharedPreferences.Editor edit;
    ArrayList<urunProperty> kategoriurunleri = new ArrayList<>();
    ArrayList<String> urunresim = new ArrayList<>();
    ArrayList<String> urunbaslik = new ArrayList<>();
    ArrayList<String> urunfiyat = new ArrayList<>();
    ArrayList<String> urunaciklama = new ArrayList<>();
    ArrayList<String> urundetay = new ArrayList<>();
    ArrayList<String> urunId = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urunler);
        gridlist = findViewById(R.id.gridlist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //sayfa bağlığını değiştir
        getSupportActionBar().setTitle("Ürünler");


        String urlurun = "http://jsonbulut.com/json/product.php";
        urunapi.put("ref", "f852da6180b43207bbab131d77f4c048");
        urunapi.put("start", "0");

        new jData(urlurun, urunapi).execute();

    }

    public class jData extends AsyncTask<Void, Void, Void> {


        HashMap<String, String> urunapi = null;
        String urlurun = "";
        String urundata = "";


        public jData(String urlurun, HashMap<String, String> urunapi) {

            this.urunapi = urunapi;
            this.urlurun = urlurun;
            kategoriurunleri.clear();
            urunbaslik.clear();
            urunfiyat.clear();
            urunresim.clear();
            urunaciklama.clear();
            urunId.clear();
            urundetay.clear();


        }


        @Override
        protected Void doInBackground(Void... voids) {
            try {

                urundata = Jsoup.connect(urlurun).data(urunapi).timeout(30000).ignoreContentType(true).execute().body();
            } catch (Exception e) {
                Log.d("hata","ÜRÜN URL BAĞLANTI HATASI" + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {

                JSONObject obj = new JSONObject(urundata);
                boolean durum = obj.getJSONArray("Products").getJSONObject(0).getBoolean("durum");
                String mesaj = obj.getJSONArray("Products").getJSONObject(0).getString("durum");
                if (durum) {
                    try {
                        int urunadet = obj.getJSONArray("Products").getJSONObject(0).getJSONArray("bilgiler").length();
                        for (int c = 0; c < urunadet; c++) {

                            urunkisaltma = obj.getJSONArray("Products").getJSONObject(0).getJSONArray("bilgiler");

                            String urunmodel = urunkisaltma.getJSONObject(c).getJSONArray("categories").getJSONObject(0).getString("categoryName").toString();
                            String modelkategori = urunkisaltma.getJSONObject(c).getJSONArray("categories").getJSONObject(1).getString("categoryName").toString();

                            boolean resimvarmi = urunkisaltma.getJSONObject(c).getBoolean("image");

                            if (urunmodel.equals(Modeller) && (modelkategori.equals(ModelKategorileri))) {

                                urunProperty up = new urunProperty();

                                JSONObject urunbilgi = urunkisaltma.getJSONObject(c);
                                if (resimvarmi) {
                                    up.setThumb(urunbilgi.getJSONArray("images").getJSONObject(0).getString("normal"));
                                } else {
                                    up.setThumb("null");
                                }
                                ArrayList<String> urun_resimleri = new ArrayList<>();
                                for(int a=0;a<urunbilgi.getJSONArray("images").length();a++){

                                    urun_resimleri.add(urunbilgi.getJSONArray("images").getJSONObject(a).getString("normal"));

                                }
                                UrunAyrinti.urunresimleri.put(urunbilgi.getString("productId"),urun_resimleri);
                                urunresim.add(urunbilgi.getJSONArray("images").getJSONObject(0).getString("normal"));
                                urunfiyat.add(urunbilgi.getString("price"));
                                urunbaslik.add(urunbilgi.getString("productName"));
                                urunaciklama.add(urunbilgi.getString("brief"));
                                urunId.add(urunbilgi.getString("productId"));
                                urundetay.add(urunbilgi.getString("description"));

                            }
                        }
                        UrunGridAdapter adapter = new UrunGridAdapter(Urunler.this,urunresim,urunbaslik,urunfiyat,urunaciklama,urunId,urundetay);
                        gridlist.setAdapter(adapter);

                    } catch (Exception e) {
                        Log.d("hata:","Urunleri getirme hatası " + e.getMessage());
                    }

                } else {
                    System.out.println("mesaj" + mesaj);
                }

            } catch (Exception e) {
                Log.d("hata:","json ürün ONPOST HATASI " + e.getMessage());
            }

        }
    }
    MenuItem mCartIconMenuItem;
    ImageButton mImageBtn;
    TextView mCountTv;
    int sayac;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        ///
        getMenuInflater().inflate(R.menu.guncel_kategori, menu);
        mCartIconMenuItem = menu.findItem(R.id.cart_count_menu_item);

        View actionView = mCartIconMenuItem.getActionView();

        if (actionView != null) {

            mImageBtn = actionView.findViewById(R.id.image_btn_layout);
            mCountTv = actionView.findViewById(R.id.count_tv);
            try {

                if(kulbilgi.getString("musteriId","").equals("")){
                    mCountTv.setText("0");
                }else{
                    mCountTv.setText("" + kulbilgi.getInt("sepetteki ürün sayısı", 0));
                }

            } catch (Exception e) {
                System.err.println("sepet icon hatası:" + e.getMessage());
            }
        }

        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Urunler.this.finish();
                Intent i = new Intent(getApplicationContext(), Sepetim.class);
                startActivity(i);
            }
        });


        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // ger-i git
                onBackPressed();
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


