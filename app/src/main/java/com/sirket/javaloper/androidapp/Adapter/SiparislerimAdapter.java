package com.sirket.javaloper.androidapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sirket.javaloper.androidapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by JavaLoper on 27.01.2018.
 */

public class SiparislerimAdapter extends BaseAdapter {


    //private ArrayList<String> urunresim = null;
    private ArrayList<String> urunbaslik = null;
    private ArrayList<String> urunfiyat = null;
    private ArrayList<String> urunresim = null;
    private ArrayList<String> urunaciklama = null;
    private LayoutInflater ınflater;
    private Context context;
    float aratoplamfiyat=0;
    //float toplam=0;
    //int a=0;


    public SiparislerimAdapter (Context context, ArrayList<String> urunbaslik, ArrayList<String> urunfiyat, ArrayList<String> urunaciklama,ArrayList<String> urunresim){

        this.urunresim=urunresim;
        this.urunbaslik=urunbaslik;
        this.urunfiyat=urunfiyat;
        this.context=context;
        this.urunaciklama=urunaciklama;


    }

    @Override
    public int getCount() {
        return urunbaslik.size();
    }

    @Override
    public Object getItem(int i) {
        return urunbaslik.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View gridView=view;
        if(view==null){

            ınflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView=ınflater.inflate(R.layout.siparis,null);

        }

        ImageView icon=gridView.findViewById(R.id.sip_resim);
        TextView baslik=gridView.findViewById(R.id.sip_baslik);
        TextView fiyat=gridView.findViewById(R.id.sip_fiyat);
        TextView aciklama=gridView.findViewById(R.id.sip_acikla);
        Picasso.with(context).load(urunresim.get(i)).resize(130, 200).centerCrop().into(icon);
        baslik.setText(urunbaslik.get(i));
        fiyat.setText(urunfiyat.get(i));
        aciklama.setText(urunaciklama.get(i));


        return gridView;
    }


}
