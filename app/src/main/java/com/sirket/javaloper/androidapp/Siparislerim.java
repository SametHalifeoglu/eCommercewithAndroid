package com.sirket.javaloper.androidapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.sirket.javaloper.androidapp.Adapter.SiparislerimAdapter;
import com.sirket.javaloper.androidapp.Sabit.Sabitler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;

public class Siparislerim extends AppCompatActivity {

    HashMap<String, String> siparisapi = new HashMap<>();
    GridView gridlist;
    JSONArray sipkisaltma;
    ArrayList<String> sipfiyat = new ArrayList<>();
    ArrayList<String> sipaciklama = new ArrayList<>();
    ArrayList<String> sipbaslik = new ArrayList<>();
    ArrayList<String> sipresim = new ArrayList<>();
    public SharedPreferences kulbilgi;
    public SharedPreferences.Editor edit;
    String data="";
    Button btndevam;
    Button btnsiparisyok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        kulbilgi=getSharedPreferences(""+ Sabitler.xmldosyaAdi, MODE_PRIVATE);
        edit = kulbilgi.edit();
        String musteriId = kulbilgi.getString("musteriId" , "");

        /*******************************************************************************************/
        if(kulbilgi.getString(musteriId,"").equals("")){
            setContentView(R.layout.siparis_yok);
            getSupportActionBar().setTitle("Sipariş Takibi");
            btnsiparisyok=findViewById(R.id.btnsiparisyok);
            btnsiparisyok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Siparislerim.this.finish();
                }
            });
        }
        else{
            setContentView(R.layout.activity_siparislerim);
            getSupportActionBar().setTitle("Sipariş Takibi");
            gridlist = findViewById(R.id.Siparis_Grid);
            btndevam=findViewById(R.id.devamet);
            btndevam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Siparislerim.this.finish();
                    UrunAyrinti.j=0;
                    String musteriId = kulbilgi.getString("musteriId" , "");
                    edit.remove(musteriId).commit();
                    UrunAyrinti.sayac=0;
                    Intent intent =new Intent(Siparislerim.this,Guncel_Kategori.class);
                    startActivity(intent);
                }
            });

            data = kulbilgi.getString("musteriId" , "");
            String urlsiparis = "http://jsonbulut.com/json/orderList.php";
            siparisapi.put("ref","f852da6180b43207bbab131d77f4c048");
            siparisapi.put("musterilerID",data);
            new Siparislerim.jData(urlsiparis, siparisapi).execute();
        }
        /*******************************************************************************************/

    }

    public interface OnFragmentInteractionListener {
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
            try {

                JSONObject obj = new JSONObject(siparisdata);

                    try {
                        int urunadet = obj.getJSONArray("orderList").getJSONArray(0).length();

                        edit.putInt("urunadet", urunadet);
                        edit.commit();
                        for (int c = 0; c < urunadet; c++) {
                            sipkisaltma = obj.getJSONArray("orderList").getJSONArray(0);
                            String urun_adi = sipkisaltma.getJSONObject(c).getString("urun_adi").toString();
                            String kisa_aciklama = sipkisaltma.getJSONObject(c).getString("kisa_aciklama").toString();
                            String fiyat = sipkisaltma.getJSONObject(c).getString("fiyat").toString();
                            String resim = sipkisaltma.getJSONObject(c).getString("normal").toString();
                                sipfiyat.add(fiyat);
                                sipbaslik.add(urun_adi);
                                sipaciklama.add(kisa_aciklama);
                                sipresim.add(resim);
                        }
                        SiparislerimAdapter adapter = new SiparislerimAdapter(Siparislerim.this,sipbaslik,sipfiyat,sipaciklama,sipresim);
                        gridlist.setAdapter(adapter);
                    } catch (Exception e) {
                        System.err.println("sipariş listeleme hatası:" + e.getMessage());
                    }
            } catch (Exception e) {
                System.err.println("Sipariş hatası:" + e.getMessage());
            }
        }
    }
}
