package it.gov.iiseinaudiscarpa.rivendilibro;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import it.gov.iiseinaudiscarpa.rivendilibro.R;

public class Finestra extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        setCopiabile();
    }

    private void setCopiabile(){
        Toast.makeText(Finestra.this,"Faccio copiabile",Toast.LENGTH_SHORT).show();
        TextView telefono = (TextView) findViewById(R.id.contatto1);
        telefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("ciao", "ciaone");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(Finestra.this,"Copiato qualcosa",Toast.LENGTH_LONG).show();
            }
        });
        TextView mail = (TextView) findViewById(R.id.contatto2);
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("ciao", "ciaone");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(Finestra.this,"Copiato qualcosa",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(Finestra.this,"Copiato qualcosa",Toast.LENGTH_LONG);
        final String nome= getIntent().getExtras().getString("nome");
        final double prezzo=getIntent().getExtras().getDouble("prezzo");
        final String prezzosped=getIntent().getExtras().getString("prezzosped");
        final String residenza= getIntent().getExtras().getString("residenza");
        final String idimm=getIntent().getExtras().getString("idimm");
        final String desc=getIntent().getExtras().getString("desc");
        final String email=getIntent().getExtras().getString("mail");
        final String numero=getIntent().getExtras().getString("numero");
        final String[] pref=getIntent().getExtras().getStringArray("pref");


        ImageView telefono=(ImageView)findViewById(R.id.tel);
        ImageView mail=(ImageView)findViewById(R.id.mail);
        ImageView whatsapp=(ImageView)findViewById(R.id.whatsapp);
        ImageView sms=(ImageView)findViewById(R.id.sms);

        telefono.setVisibility(View.INVISIBLE);
        mail.setVisibility(View.INVISIBLE);
        whatsapp.setVisibility(View.INVISIBLE);
        sms.setVisibility(View.INVISIBLE);

        TextView tvutente=(TextView) findViewById(R.id.utente);
        TextView tvprezzo=(TextView) findViewById(R.id.prezzo);
        TextView tvresidenza=(TextView) findViewById(R.id.residenza);
        TextView tvsped=(TextView) findViewById(R.id.spedizione);
        TextView tvdescrizione=(TextView) findViewById(R.id.descrizione);
        TextView tvcontatto1=(TextView) findViewById(R.id.contatto1);
        tvcontatto1.setKeyListener(null);
        tvcontatto1.setFocusable(true);
        TextView tvcontatto2=(TextView) findViewById(R.id.contatto2);
        tvcontatto2.setKeyListener(null);
        tvcontatto2.setFocusable(true);


        tvutente.setText(nome);
        tvprezzo.setText(""+prezzo+"€");
        tvresidenza.setText(residenza);
        if(prezzosped.contains("No")||prezzosped.contains("Spedizione gratuita")){
            tvsped.setText(prezzosped);
        }else{
            tvsped.setText(prezzosped+"€");
        }
        tvdescrizione.setText(desc);


        if (pref[0].toString().equals("")){
            telefono.setVisibility(View.VISIBLE);
            mail.setVisibility(View.VISIBLE);
            sms.setVisibility(View.VISIBLE);
            whatsapp.setVisibility(View.VISIBLE);
            tvcontatto1.setText(numero);
            tvcontatto2.setText(email);
        }else{
                for (int i=0;i<pref.length;i++){
                    if(pref[i].contains("Telefono")){
                        telefono.setVisibility(View.VISIBLE);
                        if(tvcontatto1.getText().equals("")||tvcontatto1.getText().toString().contains("@")==false){
                            tvcontatto1.setText(numero);
                        }else{
                            if(tvcontatto2.getText().equals("")||tvcontatto1.getText().toString().contains("@")==false){
                                tvcontatto2.setText(numero);
                            }
                        }
                    }
                    if(pref[i].contains("Mail")){
                        mail.setVisibility(View.VISIBLE);
                        if(tvcontatto1.getText().equals("")){
                            tvcontatto1.setText(email);
                        }else{
                            if(tvcontatto2.getText().equals("")){
                                tvcontatto2.setText(email);
                            }
                        }
                    }
                    if(pref[i].contains("Whatsapp")){
                        whatsapp.setVisibility(View.VISIBLE);
                        if(tvcontatto1.getText().equals("")||tvcontatto1.getText().toString().contains("@")==false){
                            tvcontatto1.setText(numero);
                        }else{
                            if(tvcontatto2.getText().equals("")||tvcontatto1.getText().toString().contains("@")==false){
                                tvcontatto2.setText(numero);
                            }
                        }
                    }
                    if(pref[i].contains("Messaggio")){
                        sms.setVisibility(View.VISIBLE);
                        if(tvcontatto1.getText().equals("")||tvcontatto1.getText().toString().contains("@")==false){
                            tvcontatto1.setText(numero);
                        }else{
                            if(tvcontatto2.getText().equals("")||tvcontatto1.getText().toString().contains("@")==false){
                                tvcontatto2.setText(numero);
                            }
                        }
                    }
                }
        }
    }


}




