package it.gov.iiseinaudiscarpa.rivendilibro;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class LibriActivity extends AppCompatActivity implements DataHandler {
    static ArrayList<Libro> listalibri= new ArrayList<Libro>(50);
    static ArrayAdapter<Libro> listViewadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CaricaLibri();
    }

    @Override
    protected void onStart() {
        listViewadapter = new ArrayAdapter<Libro>(this,android.R.layout.simple_list_item_1,listalibri);
        super.onStart();
    }

    public void CaricaLibri() {
        listalibri.clear();
        int idRegione = getIntent().getExtras().getInt("idRegione");
        String[] nomiParametri = new String[]{"idr"};
        String[] valoriParametri = new String[]{"" + idRegione};
        Conn.getInstance(this).GetDataFromWebsite(this, "libri", nomiParametri, valoriParametri);
    }

    @Override
    public void HandleData(String data) {
        ListView lv = (ListView) findViewById(R.id.listView);
        if (data != null && data != "") {
            String[] linee = data.split("\n");
            String linea = null;
            for (int i = 0; i < linee.length; i++) {
                linea = linee[i];
                String[] valori = linea.split("§");
                listalibri.add(new Libro(Integer.parseInt(valori[0]), valori[1]));
            }
            //Impostiamo l'adapter alla listView
            lv.setAdapter(listViewadapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i= new Intent(getApplicationContext(),AnnunciActivity.class);
                    final Libro l = (Libro)parent.getItemAtPosition(position);
                    int idLibro = l.id;
                    int idRegione = getIntent().getExtras().getInt("idRegione");
                    i.putExtra("idLibro",idLibro);
                    i.putExtra("idRegione",idRegione);
                    startActivity(i);
                }
            });
        } else {
            Toast.makeText(LibriActivity.this, "Pagina vuota", Toast.LENGTH_LONG).show();
        }
    }
}
