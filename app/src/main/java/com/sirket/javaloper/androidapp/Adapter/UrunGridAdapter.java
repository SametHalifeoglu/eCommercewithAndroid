package com.sirket.javaloper.androidapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sirket.javaloper.androidapp.R;
import com.sirket.javaloper.androidapp.UrunAyrinti;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by JavaLoper on 27.01.2018.
 */

public class UrunGridAdapter extends BaseAdapter {


    private ArrayList<String> urunresim = null;
    private ArrayList<String> urunbaslik = null;
    private ArrayList<String> urunfiyat = null;
    private ArrayList<String> urunaciklama = null;
    private ArrayList<String> urunId = null;
    private ArrayList<String> urundetay = null;
    private LayoutInflater ınflater;
    private Context context;


    public UrunGridAdapter(Context context, ArrayList<String> urunresim, ArrayList<String> urunbaslik, ArrayList<String> urunfiyat, ArrayList<String> urunaciklama, ArrayList<String> urunId,ArrayList<String> urundetay){

        this.urunbaslik=urunbaslik;
        this.urunresim=urunresim;
        this.urunfiyat=urunfiyat;
        this.context=context;
        this.urunaciklama=urunaciklama;
        this.urunId=urunId;
        this.urundetay=urundetay;


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
            gridView=ınflater.inflate(R.layout.urun,null);
        }

        ImageView icon=gridView.findViewById(R.id.imageurun);
        TextView baslik=gridView.findViewById(R.id.txturunadi);
        TextView fiyat=gridView.findViewById(R.id.txtfiyat);
        TextView aciklama=gridView.findViewById(R.id.txturunaciklama);

        Picasso.with(context).load(urunresim.get(i)).resize(180, 335).centerCrop().into(icon);
        baslik.setText(urunbaslik.get(i));
        fiyat.setText(urunfiyat.get(i));
        aciklama.setText(urunaciklama.get(i));


        gridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ıntent =new Intent(context,UrunAyrinti.class);
                ıntent.putExtra("urunfiyat",urunfiyat.get(i));
                ıntent.putExtra("urunbaslik",urunbaslik.get(i)+" "+urunaciklama.get(i));
                ıntent.putExtra("urundetay",urundetay.get(i));
                ıntent.putExtra("urunId",urunId.get(i));
                ıntent.putExtra("urunresim",urunresim.get(i));
                startActivity(context, ıntent, null);
            }
        });

        return gridView;
    }
}
