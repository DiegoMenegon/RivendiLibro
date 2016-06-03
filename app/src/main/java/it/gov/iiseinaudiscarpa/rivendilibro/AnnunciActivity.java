package it.gov.iiseinaudiscarpa.rivendilibro;

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

public class AnnunciActivity extends AppCompatActivity{
    static HttpURLConnection urlConnection;
    static BufferedReader reader;
    static public String result;
    static String[] inserzione = new String[50];
    static ArrayList<Inserzione> listainserzioni= new ArrayList<Inserzione>(50);
    static ArrayAdapter<Inserzione> listViewadapter;
    static int idLibro;
    static int idr;
    String[] pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new BackgroundTask().execute();
    }

    @Override
    protected void onStart() {
        listViewadapter = new ArrayAdapter<Inserzione>(this,android.R.layout.simple_list_item_1,listainserzioni);
        super.onStart();
    }

    private class BackgroundTask extends AsyncTask<Void, Integer, String>
    {

        @Override
        protected void onPreExecute() {
           // Toast.makeText(Annunci.this, "Inizio...", Toast.LENGTH_SHORT).show();
            listainserzioni.removeAll(listainserzioni);
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            idLibro=getIntent().getExtras().getInt("idLibro");
            idr=getIntent().getExtras().getInt("idRegione");
            try {
                URL url = null;
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http").authority("rivendilibro.altervista.org")
                        .appendPath("android.php")
                        .appendQueryParameter("p", "annunciLibro")
                        .appendQueryParameter("id", String.valueOf(idLibro))
                        .appendQueryParameter("idr", String.valueOf(idr));
                url = new URL(builder.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    Log.e("Errore", "Database Vuoto");
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    inserzione=line.split("§");
                    pref=inserzione[6].split(",");
                    listainserzioni.add(new Inserzione(inserzione[0],Double.parseDouble(inserzione[1]),inserzione[2],inserzione[3],inserzione[4],inserzione[5],pref));
                }

                if (buffer.length() == 0) {
                    Toast.makeText(AnnunciActivity.this, "Connessione non riuscita!", Toast.LENGTH_SHORT).show();
                    return null;
                }


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Toast.makeText(AnnunciActivity.this, "Connessione non riuscita!", Toast.LENGTH_SHORT).show();
                    }
                }
                return result;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String result) {
           // Toast.makeText(Annunci.this, "Connessione riuscita!", Toast.LENGTH_SHORT).show();
            ListView lv = (ListView) findViewById(R.id.listView);
            lv.setAdapter(listViewadapter);
           /* lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i= new Intent(getApplicationContext(),Annunci.class);
                    final Libro l = (Libro)parent.getItemAtPosition(position);
                    int idLibro = l.id;
                    i.putExtra("idLibro",idLibro);
                    startActivity(i);
                }
            });*/
            super.onPostExecute(result);
        }



    }
}
