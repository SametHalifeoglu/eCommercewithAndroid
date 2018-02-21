package com.sirket.javaloper.androidapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sirket.javaloper.androidapp.Adapter.KategoriGridAdapter;
import com.sirket.javaloper.androidapp.Sabit.Sabitler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Guncel_Kategori extends AppCompatActivity {

    public HashMap<String, String> jsonkategori = new HashMap<>();
    public ArrayList<String> Modeller = new ArrayList<>();
    public HashMap<String, ArrayList<String>> Modelkategorileri = new HashMap<>();
    public HashMap<String, String> Modelveid = new HashMap<>();
    public GridView Gridkategorilist;
    public HashMap<String, String> kategoriresim = new HashMap<>();
    public HashMap<String, String> jsongaleri = new HashMap<>();
    TextView ErkekModel;
    TextView KadinModel;
    TextView EvYasam;
    TextView CocukModel;
    public SharedPreferences kulbilgi;
    public SharedPreferences.Editor edit;
    String Cins = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Kategoriler");


        kulbilgi = getSharedPreferences("" + Sabitler.xmldosyaAdi, MODE_PRIVATE);
        edit = kulbilgi.edit();
        setContentView(R.layout.activity_guncel_kategori);
        ErkekModel = findViewById(R.id.txtBaslik1);
        KadinModel = findViewById(R.id.txtBaslik2);
        EvYasam = findViewById(R.id.txtBaslik3);
        CocukModel = findViewById(R.id.txtBaslik4);
        Gridkategorilist = findViewById(R.id.kategoriGrid);

        String urlkategori = "http://jsonbulut.com/json/companyCategory.php";
        jsonkategori.put("ref", "f852da6180b43207bbab131d77f4c048");
        String urlgaleri = "http://jsonbulut.com/json/gallery.php";
        jsongaleri.put("ref", "f852da6180b43207bbab131d77f4c048");

        new Guncel_Kategori.jData(Guncel_Kategori.this, urlkategori, jsonkategori).execute();
        new Guncel_Kategori.jGaleri(Guncel_Kategori.this, urlgaleri, jsongaleri).execute();
        ErkekModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cins = "" + ErkekModel.getText();
                KategoriGridAdapter adapter = new KategoriGridAdapter(Guncel_Kategori.this, Modeller, Modelkategorileri, kategoriresim, Cins);
                Gridkategorilist.setAdapter(adapter);
                Toast.makeText(Guncel_Kategori.this, "" + Cins, Toast.LENGTH_SHORT).show();
            }
        });
        KadinModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cins = "" + KadinModel.getText();
                KategoriGridAdapter adapter = new KategoriGridAdapter(Guncel_Kategori.this, Modeller, Modelkategorileri, kategoriresim, Cins);
                Gridkategorilist.setAdapter(adapter);
                Toast.makeText(Guncel_Kategori.this, "" + Cins, Toast.LENGTH_SHORT).show();
            }
        });
        EvYasam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cins = "" + EvYasam.getText();
                KategoriGridAdapter adapter = new KategoriGridAdapter(Guncel_Kategori.this, Modeller, Modelkategorileri, kategoriresim, Cins);
                Gridkategorilist.setAdapter(adapter);
                Toast.makeText(Guncel_Kategori.this, "" + Cins, Toast.LENGTH_SHORT).show();
            }
        });
        CocukModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cins = "" + CocukModel.getText();
                KategoriGridAdapter adapter = new KategoriGridAdapter(Guncel_Kategori.this, Modeller, Modelkategorileri, kategoriresim, Cins);
                Gridkategorilist.setAdapter(adapter);
                Toast.makeText(Guncel_Kategori.this, "" + Cins, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ModelDoldur() {

        for (int a = 0; a < Modeller.size(); a++) {
            ErkekModel.setText(Modeller.get(a));
            KadinModel.setText(Modeller.get(a + 1));
            EvYasam.setText(Modeller.get(a + 2));
            CocukModel.setText(Modeller.get(a + 3));
            break;

        }
    }

    public interface OnFragmentInteractionListener {
    }

    class jData extends AsyncTask<Void, Void, Void> {

        String urlkategori = "";
        Context context;
        String datakategori = "";
        HashMap<String, String> jsonkategori = null;


        public jData(Context context, String urlkategori, HashMap<String, String> jsonkategori) {

            this.urlkategori = urlkategori;
            this.context = context;
            this.jsonkategori = jsonkategori;

        }

        JSONArray kategorikisaltma;

        @Override
        protected Void doInBackground(Void... voids) {


            Modelveid.clear();
            Modelkategorileri.clear();
            Modeller.clear();

            try {
                datakategori = Jsoup.connect(urlkategori).data(jsonkategori).timeout(30000).ignoreContentType(true).execute().body();

            } catch (Exception e) {
                Log.d("kategoridata hatası:", "" + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            String Modeladi;
            String Modelid;
            try {

                JSONObject objkategori = new JSONObject(datakategori);
                boolean durum = objkategori.getJSONArray("Kategoriler").getJSONObject(0).getBoolean("durum");
                if (durum) {
                    kategorikisaltma = objkategori.getJSONArray("Kategoriler").getJSONObject(0).getJSONArray("Categories");
                    int ksayi = kategorikisaltma.length();
                    for (int c = 0; c < ksayi; c++) {
                        if (kategorikisaltma.getJSONObject(c).getString("TopCatogryId").equals("0")) {

                            Modeladi = kategorikisaltma.getJSONObject(c).getString("CatogryName");
                            Modelid = kategorikisaltma.getJSONObject(c).getString("CatogryId");
                            Modelveid.put(Modelid, Modeladi);
                            Modeller.add(Modeladi);
                        }
                    }

                    ModelDoldur();
                    Set<String> keys = Modelveid.keySet();
                    for (String item : keys) {
                        ArrayList<String> kategoriler = new ArrayList<>();
                        for (int c = 0; c < ksayi; c++) {
                            String kategoriid = kategorikisaltma.getJSONObject(c).getString("TopCatogryId").toString();
                            if (kategoriid.equals(item)) {
                                String kategoriadi = kategorikisaltma.getJSONObject(c).getString("CatogryName").toString();
                                kategoriler.add(kategoriadi);
                            }
                        }
                        Modelkategorileri.put(Modelveid.get(item), kategoriler);
                    }
                } else {
                    String kdurum = objkategori.getJSONArray("Kategoriler").getJSONObject(0).getString("mesaj");
                    Log.d("Durum sorgulama:", "" + kdurum);
                }
            } catch (Exception e) {
                Log.d("onposthatası", "" + e.getMessage());
            }
        }

    }


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
                            } catch (Exception E) {
                                //System.err.println("Galeri resimleri getirme hatası:" + E.getMessage());

                            }
                        }
                    }
                    KategoriGridAdapter adapter = new KategoriGridAdapter(Guncel_Kategori.this, Modeller, Modelkategorileri, kategoriresim, "Kadın");
                    Gridkategorilist.setAdapter(adapter);
                }
            } catch (Exception e) {
                System.err.println("Galeri resim hatasi:" + e.getMessage());
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
                Guncel_Kategori.this.finish();
                Intent i = new Intent(getApplicationContext(), Sepetim.class);
                startActivity(i);
            }
        });


        return true;
    }

    /*---------------------------------------------------------------------------------------*/
    int tiklama = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //tiklama++;
            if (tiklama == 1) {
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


        }
        return super.onKeyDown(keyCode, event);
    }

    /*------------------------------------------------------------------------------------------------------*/
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
