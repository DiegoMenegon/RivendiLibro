package it.gov.iiseinaudiscarpa.rivendilibro;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by cremaluca on 03/06/2016.
 */
public class Conn {

    static Context context;
    private static Conn instance = new Conn();
    ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
    boolean connected = false;

    public static Conn getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void GetDataFromWebsite(DataHandler handler,String pagina,String nomeParametro,String valoreParametro){
        BackgroundTask task = new BackgroundTask(handler, pagina, new String[]{nomeParametro}, new String[]{valoreParametro});
        task.execute();
    }

    public void GetDataFromWebsite(DataHandler handler, String pagina, String[] nomiParametro, String[] valoriParametro) {
        BackgroundTask task = new BackgroundTask(handler, pagina, nomiParametro, valoriParametro);
        task.execute();
    }

    private class BackgroundTask extends AsyncTask<Void, Integer, String>
    {

        HttpURLConnection urlConnection;
        BufferedReader reader;
        String[] nomiParametro;
        String[] valoriParametro;
        String pagina;
        DataHandler handler;

        public BackgroundTask(DataHandler Handler, String Pagina, String[] NomiParametro, String[] ValoriParametro) {
            nomiParametro = NomiParametro;
            valoriParametro = ValoriParametro;
            handler = Handler;
            pagina = Pagina;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            String result = null;
            try {
                URL url = null;
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http").authority("rivendilibro.altervista.org")
                        .appendPath("android.php")
                        .appendQueryParameter("p", pagina);
                for (int i = 0; i < nomiParametro.length; i++) {
                    builder.appendQueryParameter(nomiParametro[i], valoriParametro[i]);
                }
                url = new URL(builder.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                StringBuilder sb = new StringBuilder();
                if (inputStream == null) {
                    Log.e("Errore", "Database Vuoto");
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                if (buffer.length() == 0) {
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
                        System.out.println("ERRORE");
                        return null;
                    }
                }
                return result;
            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            System.out.println("Caricamento : " + values);

        }

        @Override
        protected void onPostExecute(String result) {
            handler.HandleData(result);
            super.onPostExecute(result);
        }
    }
}
