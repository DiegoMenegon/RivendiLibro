package it.gov.iiseinaudiscarpa.rivendilibro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.graphics.Movie;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Diego on 30/05/2016.
 */
public class Splash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        WebView web = (WebView) findViewById(R.id.webView);
        web.setBackgroundColor(Color.TRANSPARENT); //for gif without background
        web.loadUrl("file:///android_asset/htmls/gif.html");

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(Splash.this,Home.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
