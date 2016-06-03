package it.gov.iiseinaudiscarpa.rivendilibro;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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

public class LibriActivity extends AppCompatActivity{
    static HttpURLConnection urlConnection;
    static BufferedReader reader;
    static public String result;
    static String[] libro = new String[50];
    static ArrayList<Libro> listalibri= new ArrayList<Libro>(50);
    static ArrayAdapter<Libro> listViewadapter;
    static int idr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new BackgroundTask().execute();
    }

    @Override
    protected void onStart() {
        listViewadapter = new ArrayAdapter<Libro>(this,android.R.layout.simple_list_item_1,listalibri);
        super.onStart();
    }

    private class BackgroundTask extends AsyncTask<Void, Integer, String>
    {

        @Override
        protected void onPreExecute() {
           // Toast.makeText(Libri.this, "Inizio...", Toast.LENGTH_SHORT).show();
            listalibri.clear();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {

            try {
                idr=getIntent().getExtras().getInt("idRegione");
                URL url = null;
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http").authority("rivendilibro.altervista.org")
                        .appendPath("android.php")
                        .appendQueryParameter("p", "libri")
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
                    libro=line.split("§");
                    listalibri.add(new Libro(Integer.parseInt(libro[0]),libro[1]));
                }

                if (buffer.length() == 0) {
                    Toast.makeText(LibriActivity.this, "Connessione non riuscita!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(LibriActivity.this, "Connessione non riuscita!", Toast.LENGTH_SHORT).show();
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
          //  Toast.makeText(Libri.this, "Connessione riuscita!", Toast.LENGTH_SHORT).show();
            ListView lv = (ListView) findViewById(R.id.listView);
            lv.setAdapter(listViewadapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i= new Intent(getApplicationContext(),AnnunciActivity.class);
                    final Libro l = (Libro)parent.getItemAtPosition(position);
                    int idLibro = l.id;
                    i.putExtra("idLibro",idLibro);
                    i.putExtra("idRegione",idr);
                    startActivity(i);
                }
            });
            super.onPostExecute(result);
        }



    }
}
