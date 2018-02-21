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


public class GirisYap extends Fragment {


    EditText kulmail,kulsifre;
    Button btnGiris;
    HashMap<String,String> hm=new HashMap<>();
    public SharedPreferences kulbilgi;
    public SharedPreferences.Editor edit;
    static String musteriId="";
    String data;
    int i=0;





    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GirisYap() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static GirisYap newInstance(String param1, String param2) {
        GirisYap fragment = new GirisYap();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_giris_yap, container, false);
        kulmail=v.findViewById(R.id.userMail);
        kulsifre=v.findViewById(R.id.userPass);
        btnGiris=v.findViewById(R.id.userEnter);
        kulbilgi=this.getActivity().getSharedPreferences(""+Sabitler.xmldosyaAdi, MODE_PRIVATE);
        edit = kulbilgi.edit();




        btnGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hm.put("ref","f852da6180b43207bbab131d77f4c048");
                hm.put("userEmail",kulmail.getText().toString().trim());
                hm.put("userPass",kulsifre.getText().toString().trim());
                hm.put("face","no");
                String url="http://jsonbulut.com/json/userLogin.php";
                new jData(url,v.getContext(),hm).execute();
            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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


    class jData extends AsyncTask<Void,Void,Void>{

        String url="";
        HashMap<String,String> hm=null;
        String data="";
        Context cnx;


        public jData(String url,Context cnx, HashMap<String,String> hm){

            this.url=url;
            this.hm=hm;
            this.cnx=cnx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                data= Jsoup.connect(url).data(hm).timeout(30000).ignoreContentType(true).execute().body();
            }catch (Exception e){
                System.err.println("Giriş bilgileri hatası: "+e.getMessage());

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {

                JSONObject obj=new JSONObject(data);
                JSONObject gobj=obj.getJSONArray("user").getJSONObject(0);

                boolean durum=gobj.getBoolean("durum");
                //String mesaj=gobj.getString("mesaj");
                String ad=obj.getJSONArray("user").getJSONObject(0).getJSONObject("bilgiler").getString("userName");
                String soyad=obj.getJSONArray("user").getJSONObject(0).getJSONObject("bilgiler").getString("userSurname");
                musteriId=obj.getJSONArray("user").getJSONObject(0).getJSONObject("bilgiler").getString("userId");

                if(durum){
                    Toast.makeText(getContext(), "Giriş Başarılı", Toast.LENGTH_SHORT).show();
                    //edit.putString(kulmail.getText().toString().trim(),ad+" "+soyad );
                    //edit.commit();
                    edit.putString("musteriId", obj.getJSONArray("user").getJSONObject(0).getJSONObject("bilgiler").getString("userId"));
                    edit.commit();
                    edit.putString("musteriadsoyad",ad+" "+soyad);
                    edit.commit();
                    Intent intent=new Intent(getContext(),BaslangicSayfasi.class);
                    startActivity(intent);
                    getActivity().finish();

                }else{
                    Toast.makeText(cnx, "Giriş bilgileriniz hatalıdır.", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                System.err.println("" + e.getMessage());
            }
        }
    }



}
