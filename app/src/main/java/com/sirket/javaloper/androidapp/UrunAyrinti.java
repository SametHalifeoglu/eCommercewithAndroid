package com.sirket.javaloper.androidapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.sirket.javaloper.androidapp.Sabit.Sabitler;

import java.util.ArrayList;
import java.util.HashMap;

public class UrunAyrinti extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {


    static HashMap<String, ArrayList> urunresimleri = new HashMap<>();
    private SliderLayout mDemoSlider;
    ArrayList<String> ls = new ArrayList<>();
    int i=0;
    String siparisUrl;
    public SharedPreferences kulbilgi;
    public SharedPreferences.Editor edit;
    MenuItem mCartIconMenuItem;
    ImageButton mImageBtn;
    TextView mCountTv;
    Button btnIdsizsepetekleme;
    static int sayac;
    static String urunlerid;
    String[] urunlerDizi;
    String[] urunBilgiDizi;
    static int j=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_ayrinti);
        kulbilgi=getSharedPreferences(""+ Sabitler.xmldosyaAdi, MODE_PRIVATE);
        edit = kulbilgi.edit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ürün Detayı");
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        TextView urun_aciklama = findViewById(R.id.sip_baslik);
        TextView urun_fiyat = findViewById(R.id.sip_fiyat);
        TextView urun_detay = findViewById(R.id.urun_detay);
        Button sepetekle = findViewById(R.id.sepetekle);

        urun_aciklama.setText(getIntent().getExtras().getString("urunbaslik", ""));
        urun_fiyat.setText(getIntent().getExtras().getString("urunfiyat", ""));
        urun_detay.setText(getIntent().getExtras().getString("urundetay", ""));


        String urunid=getIntent().getExtras().getString("urunId", "");
        String urunbaslik=getIntent().getExtras().getString("urunbaslik", "");
        String urundetay=getIntent().getExtras().getString("urundetay", "");
        String urunfiyat=getIntent().getExtras().getString("urunfiyat", "");
        String urunresim=getIntent().getExtras().getString("urunresim", "");


        String musteriId = kulbilgi.getString("musteriId" , "");
        urunlerid=kulbilgi.getString(musteriId,"");

        /******************************************************************************************/
        HashMap<String, String> url_maps = new HashMap<>();
        ls = urunresimleri.get(urunid);
        for (int i = 0; i < ls.size(); i++) {
            url_maps.put("" + i, ls.get(i));
        }
        /******************************************************************************************/
        sepetekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(kulbilgi.getString("musteriId","").equals("")){
                    setContentView(R.layout.sepet_bos_pasif);
                    btnIdsizsepetekleme=findViewById(R.id.pasifSepet);
                    btnIdsizsepetekleme.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            UrunAyrinti.this.finish();
                        }
                    });
                }
                edit.putString("sepetdurumu" , "sepetdoluyor");
                edit.commit();
                Sepetim.toplam_fiyat=Sepetim.toplam_fiyat+Float.parseFloat(urunfiyat);
                urunlerid=urunlerid+urunid+"-";
                /*******************************************************************/
                edit.putString(musteriId,urunlerid);
                edit.commit();
                urunlerDizi=kulbilgi.getString(musteriId,"").split("-");
                edit.putInt("sepetteki ürün sayısı", urunlerDizi.length);
                edit.commit();

                /*******************************************************************/

                edit.putString(urunlerDizi[j],urunbaslik+"-"+urundetay+"-"+urunfiyat+"-"+urunresim);
                edit.commit();
                urunBilgiDizi=kulbilgi.getString(urunlerDizi[j],"").split("-");

                /******************************************************************/
                j++;

                sayac=kulbilgi.getInt("id",0);
                sayac++;
                edit.putInt("id",sayac).commit();

                if(kulbilgi.getString("musteriId","").equals("")){
                    mCountTv.setText("0");
                }else{
                    mCountTv.setText(""+sayac);
                }
            }
        });

        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);

            textSliderView

                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);
            mDemoSlider.setPresetTransformer("Tablet");
            mDemoSlider.addSlider(textSliderView);
        }

    }
    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    public void onPageScrollStateChanged(int state) {
    }


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
            if(kulbilgi.getString("musteriId","").equals("")){
                mCountTv.setText("0");
            }else{
                mCountTv.setText(""+sayac);
            }

        }

        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UrunAyrinti.this.finish();
                Intent i = new Intent(getApplicationContext(), Sepetim.class);
                startActivity(i);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                // ger-i git
                onBackPressed();
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
