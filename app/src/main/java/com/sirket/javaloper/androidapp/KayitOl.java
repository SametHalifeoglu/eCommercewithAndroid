package com.sirket.javaloper.androidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sirket.javaloper.androidapp.Sabit.Sabitler;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;


public class KayitOl extends Fragment  {

    EditText Ad,Soyad,Tel,Mail,Sifre;
    Button Kayit;
    HashMap<String,String> hm=new HashMap<>();
    public SharedPreferences kulbilgi;
    public SharedPreferences.Editor edit;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public KayitOl() {

    }



    public static KayitOl newInstance(String param1, String param2) {
        KayitOl fragment = new KayitOl();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_kayit_ol, container, false);
        Ad=v.findViewById(R.id.txtAd);
        Soyad=v.findViewById(R.id.txtSoyad);
        Tel=v.findViewById(R.id.txtTel);
        Mail=v.findViewById(R.id.txtMail);
        Sifre=v.findViewById(R.id.txtSifre);
        Kayit=v.findViewById(R.id.btnKayit);

        kulbilgi=this.getActivity().getSharedPreferences(""+ Sabitler.xmldosyaAdi, MODE_PRIVATE);
        edit = kulbilgi.edit();


        Kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hm.put("ref","f852da6180b43207bbab131d77f4c048");
                hm.put("userName",Ad.getText().toString().trim());
                hm.put("userSurname",Soyad.getText().toString().trim());
                hm.put("userPhone",Tel.getText().toString().trim());
                hm.put("userMail",Mail.getText().toString().trim());
                hm.put("userPass",Sifre.getText().toString().trim());
                String url="http://jsonbulut.com/json/userRegister.php";

                edit.putString("musteriadsoyad",Ad.getText().toString().trim()+" "+Soyad.getText().toString().trim());
                edit.commit();
                Toast.makeText(getContext(), Mail.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                new jData(hm,url,v.getContext()).execute();
            }
        });

        return v;
    }
    class jData extends AsyncTask<Void, Void, Void>{

        HashMap<String,String> hm=null;
        String url="";
        Context cnx;
        String data="";

        public jData(HashMap<String,String> hm, String url , Context cnx){
            this.cnx=cnx;
            this.hm=hm;
            this.url=url;
        }

        protected void sayfacagir(){

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                data= Jsoup.connect(url).data(hm).timeout(30000).ignoreContentType(true).execute().body();
                System.out.println("Datalar başarıyla geldi" + data);
            }catch (Exception e){

                System.err.println("Data Getirme Hatası" + e.getMessage());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {

                JSONObject obj=new JSONObject(data);
                JSONObject kobj=obj.getJSONArray("user").getJSONObject(0);
                boolean durum=kobj.getBoolean("durum");
                String mesaj=kobj.getString("mesaj");
                if(durum){
                    edit.putString("musteriId",kobj.getString("kullaniciId"));
                    edit.commit();

                    Intent intent=new Intent(getContext(),BaslangicSayfasi.class);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                System.err.println("Kayıt işlemi başarısız . Yeni sayfaya geçilemedi:" + e.getMessage());

            }
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


}
