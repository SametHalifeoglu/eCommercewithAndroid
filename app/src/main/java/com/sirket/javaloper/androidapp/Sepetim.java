package com.sirket.javaloper.androidapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.sirket.javaloper.androidapp.Adapter.SiparislerimAdapter;
import com.sirket.javaloper.androidapp.Sabit.Sabitler;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;

public class Sepetim extends AppCompatActivity {


    public SharedPreferences kulbilgi;
    public SharedPreferences.Editor edit;
    String[] urunlerDizi;
    String[] urunBilgiDizi;
    static ArrayList<String> listurunresim = new ArrayList<>();
    static ArrayList<String> listurunbaslik = new ArrayList<>();
    static ArrayList<String> listurunfiyat = new ArrayList<>();
    static ArrayList<String> listurunaciklama = new ArrayList<>();
    GridView gridlist;
    Button btnPasif;
    Button btnAktif;
    Button Satinal;
    Button btnsipdevam;
    Button btnsiptakip;
    HashMap<String, String> siparis;
    String siparisUrl;
    TextView toplamfiyat;
    static float toplam_fiyat=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kulbilgi=getSharedPreferences(""+ Sabitler.xmldosyaAdi, MODE_PRIVATE);
        edit = kulbilgi.edit();
        String adsoyad=kulbilgi.getString("musteriadsoyad", "");
        String musteriId = kulbilgi.getString("musteriId", "");
        urunlerDizi = kulbilgi.getString(musteriId, "").split("-");

        getSupportActionBar().setTitle("Sepetim");

        if(adsoyad.equals("")){
            setContentView(R.layout.sepet_bos_pasif);
            btnPasif=findViewById(R.id.pasifSepet);
            btnPasif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Sepetim.this.finish();
                }
            });
        }
        else if (urunlerDizi[0].equals("")|| kulbilgi.getString("sepetdurumu","").equals("sepetonaylandı")) {
            setContentView(R.layout.sepet_bos_aktif);
            btnAktif=findViewById(R.id.aktifSepet);
            btnAktif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Sepetim.this.finish();

                }
            });

        } else if(kulbilgi.getString("sepetdurumu","").equals("sepetdoluyor")) {
            setContentView(R.layout.activity_sepetim);
            toplamfiyat=findViewById(R.id.toplamfiyat);
            toplamfiyat.setText(""+toplam_fiyat);
            Satinal=findViewById(R.id.satinal);
            Satinal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    edit.remove("sepetteki ürün sayısı").commit();

                    UrunAyrinti.sayac=0;
                    edit.putString("sepetdurumu" , "sepetonaylandı");
                    edit.commit();
                    edit.remove("sepetteki ürün sayısı").commit();
                    for (String item:urunlerDizi) {

                        siparis=new HashMap<>();
                        String data = kulbilgi.getString("musteriId" , "");
                        siparis.put("ref", "f852da6180b43207bbab131d77f4c048");
                        siparis.put("productId", item);
                        siparis.put("customerId", data);
                        siparis.put("html","12");
                        siparisUrl="http://jsonbulut.com/json/orderForm.php";
                        new jData(siparisUrl,siparis).execute();
                    }
                    setContentView(R.layout.siparis_onay);
                    btnsipdevam=findViewById(R.id.btnsipdevam);

                    btnsiptakip=findViewById(R.id.btnsiptakip);
                    btnsiptakip.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            Sepetim.this.finish();
                            Intent intent = new Intent(Sepetim.this,Siparislerim.class);
                            startActivity(intent);


                        }
                    });
                    btnsipdevam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Sepetim.this.finish();
                            String musteriId = kulbilgi.getString("musteriId" , "");
                            edit.remove(musteriId).commit();
                            UrunAyrinti.sayac=0;
                            UrunAyrinti.j=0;
                            Intent i=new Intent(Sepetim.this,Guncel_Kategori.class);
                            startActivity(i);
                        }
                    });
                }
            });
            listurunbaslik.clear();
            listurunaciklama.clear();
            listurunfiyat.clear();
            listurunresim.clear();
            gridlist = findViewById(R.id.Sepet_Grid);

            for (String item:urunlerDizi) {

                urunBilgiDizi=kulbilgi.getString(item,"").split("-");
                String kayitliurunbaslik=urunBilgiDizi[0];
                String kayitliurundetay=urunBilgiDizi[1];
                String kayitliurunfiyat=urunBilgiDizi[2];
                String kayitliurunresim=urunBilgiDizi[3];
                listurunbaslik.add(kayitliurunbaslik);
                listurunfiyat.add(kayitliurunfiyat);
                listurunaciklama.add(kayitliurundetay);
                listurunresim.add(kayitliurunresim);
                urunBilgiDizi=null;
            }

            SiparislerimAdapter adapter = new SiparislerimAdapter(Sepetim.this,listurunbaslik,listurunfiyat,listurunaciklama,listurunresim);
            gridlist.setAdapter(adapter);

        }

    }
    class jData extends AsyncTask<Void, Void, Void> {


        HashMap<String, String> siparisapi = null;
        String urlsiparis = "";
        String siparisdata = "";

        public jData(String urlsiparis, HashMap<String, String> siparisapi) {

            this.siparisapi = siparisapi;
            this.urlsiparis = urlsiparis;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                siparisdata = Jsoup.connect(urlsiparis).data(siparisapi).timeout(30000).ignoreContentType(true).execute().body();
            } catch (Exception e) {
                System.err.println("Sipariş bağlantı hatası:" + e.getMessage());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


}
