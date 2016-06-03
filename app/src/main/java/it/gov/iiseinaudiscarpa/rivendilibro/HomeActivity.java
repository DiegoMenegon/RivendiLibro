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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity{
    static HttpURLConnection urlConnection;
    static BufferedReader reader;
    static public String result;
    static String[] regione = new String[50];
    static ArrayList<Regione> listaregioni= new ArrayList<Regione>(50);
    static ArrayAdapter<Regione> listViewadapter;



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new BackgroundTask().execute();
    }

    @Override
    protected void onStart() {
        listViewadapter = new ArrayAdapter<Regione>(this,android.R.layout.simple_list_item_1,listaregioni);
        super.onStart();
    }

    private class BackgroundTask extends AsyncTask<Void, Integer, String>
    {

        @Override
        protected void onPreExecute() {
           // Toast.makeText(Home.this, "Inizio...", Toast.LENGTH_SHORT).show();
            listaregioni.removeAll(listaregioni);
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {

            try {
                URL url = null;
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http").authority("rivendilibro.altervista.org")
                        .appendPath("android.php")
                        .appendQueryParameter("p", "listaRegioni");
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
                listaregioni.add(new Regione("Tutta Italia",0));
                while ((line = reader.readLine()) != null) {
                    regione=line.split("§");
                    listaregioni.add(new Regione(regione[1],Integer.parseInt(regione[0])));
                }

                if (buffer.length() == 0) {
                    Toast.makeText(HomeActivity.this, "Connessione non riuscita!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(HomeActivity.this, "Connessione non riuscita!", Toast.LENGTH_SHORT).show();
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
           // Toast.makeText(Home.this, "Connessione riuscita!", Toast.LENGTH_SHORT).show();
            ListView lv = (ListView) findViewById(R.id.listView);
            lv.setAdapter(listViewadapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(position==0){
                        Intent i= new Intent(getApplicationContext(),LibriTutti.class);
                        startActivity(i);
                    }else{
                        Intent i= new Intent(getApplicationContext(),Libri.class);
                        final Regione r = (Regione)parent.getItemAtPosition(position);
                        int idRegione = r.id;
                        i.putExtra("idRegione",idRegione);
                        startActivity(i);
                    }
                }
            });
            super.onPostExecute(result);
        }



    }
}
