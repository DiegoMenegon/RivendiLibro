package it.gov.iiseinaudiscarpa.rivendilibro;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements DataHandler {
    static public String result;
    static HttpURLConnection urlConnection;
    static BufferedReader reader;
    static String[] regione = new String[50];
    static ArrayList<Regione> listaregioni= new ArrayList<Regione>(50);
    static ArrayAdapter<Regione> listViewadapter;



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Conn.getInstance(this).isNetworkAvailable()) {
            setContentView(R.layout.activity_main);
            CaricaRegioni();
        } else {
            setContentView(R.layout.no_conn);
        }
    }

    @Override
    protected void onStart() {
        listViewadapter = new ArrayAdapter<Regione>(this,android.R.layout.simple_list_item_1,listaregioni);
        super.onStart();
    }

    public void CaricaRegioni() {
        listaregioni.clear();
        ((TextView)findViewById(R.id.textCaricamento)).setVisibility(View.VISIBLE);
        Conn.getInstance(this).GetDataFromWebsite(this, "listaRegioni", new String[0], new String[0]);
    }

    @Override
    public void HandleData(String data) {
        ((TextView)findViewById(R.id.textCaricamento)).setVisibility(View.INVISIBLE);
        String[] linee = data.split("♣");
        String linea = null;
        listaregioni.add(new Regione("Tutte le regioni", 0));
        for (int i = 0; i < linee.length; i++) {
            linea = linee[i];
            String[] valori = linea.split("§");
            listaregioni.add(new Regione(valori[1], Integer.parseInt(valori[0])));
        }
        //Impostiamo l'adapter alla listView
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(listViewadapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), LibriActivity.class);
                final Regione r = (Regione) parent.getItemAtPosition(position);
                int idRegione = r.id;
                i.putExtra("idRegione", idRegione);
                startActivity(i);
            }
        });

    }
}
