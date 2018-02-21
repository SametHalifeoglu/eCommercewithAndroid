package com.sirket.javaloper.androidapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sirket.javaloper.androidapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Java_sabah on 13.2.2018.
 */

public class UygulamaAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater ınflater;
    private ArrayList<String> acilis_resim=new ArrayList();

    public UygulamaAdapter(Context context ,  ArrayList<String> acilis_resim){

        this.context=context;
        this.acilis_resim=acilis_resim;
    }


    @Override
    public int getCount() {
        return acilis_resim.size();
    }

    @Override
    public Object getItem(int i) {
        return acilis_resim.get(i);
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
            gridView=ınflater.inflate(R.layout.firsatlar,null);
        }
        ImageView icon=gridView.findViewById(R.id.resim_acilis);
        Picasso.with(context).load(acilis_resim.get(i)).fit().centerCrop().into(icon);
        return gridView;
    }
}
