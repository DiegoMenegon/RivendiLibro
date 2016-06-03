package it.gov.iiseinaudiscarpa.rivendilibro;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class AnnunciActivity extends AppCompatActivity implements DataHandler {
    static ArrayList<Inserzione> listainserzioni= new ArrayList<Inserzione>(50);
    static ArrayAdapter<Inserzione> listViewadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ciaone
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CaricaAnnunci();
    }

    @Override
    protected void onStart() {
        listViewadapter = new ArrayAdapter<Inserzione>(this,android.R.layout.simple_list_item_1,listainserzioni);
        super.onStart();
    }

    public void CaricaAnnunci() {
        listainserzioni.clear();
        int idLibro = getIntent().getExtras().getInt("idLibro");
        int idRegione = getIntent().getExtras().getInt("idRegione");
        String[] nomiParametri = new String[]{"id", "idr"};
        String[] valoriParametri = new String[]{"" + idLibro, "" + idRegione};
        Conn.getInstance(this).GetDataFromWebsite(this, "annunciLibro", nomiParametri, valoriParametri);
    }

    public void HandleData(String result) {
        System.out.println("Sono arrivato qua");
        if (result != null) {
            System.out.println(result);
            String[] linee = result.split("\n");
            String linea = null;
            for (int i = 0; i < linee.length; i++) {
                linea = linee[i];
                System.out.println(""+linea);
                String[] valori = linea.split("§");
                String[] preferenze = new String[1];
                if (valori.length > 8) {
                    if (valori[8].contains(",")) {
                        preferenze = valori[8].split(",");
                    } else {
                        preferenze[0] = valori[8];
                    }
                } else {
                    preferenze[0] = "";
                }
                listainserzioni.add(new Inserzione(valori[0], Double.parseDouble(valori[1]), valori[2], valori[3], valori[4], valori[5],valori[6],valori[7],preferenze));
            }
            //Impostiamo l'adapter alla listView
            ListView lv = (ListView) findViewById(R.id.listView);
            lv.setAdapter(listViewadapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(getApplicationContext(), Finestra.class);
                    final Inserzione in = (Inserzione) parent.getItemAtPosition(position);
                    i.putExtra("nome", in.nome);
                    i.putExtra("prezzo", in.prezzo);
                    i.putExtra("prezzosped", in.prezzosped);
                    i.putExtra("residenza", in.residenza);
                    i.putExtra("idimm", in.idimm);
                    i.putExtra("desc", in.desc);
                    i.putExtra("mail", in.mail);
                    i.putExtra("numero", in.numero);
                    i.putExtra("pref", in.pref);
                    startActivity(i);
                }
            });
        } else {
            System.out.println("Risultato della pagina nullo");
        }
    }

}
