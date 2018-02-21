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
import com.sirket.javaloper.androidapp.Urunler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static android.support.v4.content.ContextCompat.startActivity;


public class KategoriGridAdapter extends BaseAdapter {


    private ArrayList<String> urunbaslik = null;
    private HashMap<String,ArrayList<String>> urunkategori=null;
    private Context context;
    private LayoutInflater ınflater;
    private HashMap<String,String> galerisatir=null;
    private String Model="";
    public KategoriGridAdapter(Context context,ArrayList<String> urunbaslik, HashMap<String,ArrayList<String>> urunkategori,HashMap<String,String> galerisatir,String Model){

        this.urunbaslik=urunbaslik;
        this.urunkategori=urunkategori;
        this.context=context;
        this.galerisatir=galerisatir;
        this.Model=Model;



    }



    @Override
    public int getCount() {

        return urunkategori.get(Model).size();
    }

    @Override
    public Object getItem(int i) {
        return urunkategori.get(urunbaslik.get(getCount())).get(i);
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
            gridView=ınflater.inflate(R.layout.kategori,null);
        }
        ImageView icon=gridView.findViewById(R.id.imagekategori);
        TextView baslik=gridView.findViewById(R.id.txtkategoriAdi);
        baslik.setText(urunkategori.get(Model).get(i));
        String kategori=urunkategori.get(Model).get(i);
        Picasso.with(context).load(galerisatir.get(Model+kategori)).resize(190, 160).centerCrop().into(icon);

        gridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Urunler.Modeller=Model;
                Urunler.ModelKategorileri=kategori;
                Intent ıntent =new Intent(context,Urunler.class);
                startActivity(context, ıntent, null);
            }
        });

        return gridView;
    }
}
