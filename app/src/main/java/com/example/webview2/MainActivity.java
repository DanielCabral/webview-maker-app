package com.example.webview2;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Base64;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = getApplicationContext();

        class JsObject {
            @JavascriptInterface
            public void onClicked() {
                Log.d("My App", "Help button clicked");
            }

            @JavascriptInterface
            public void saveXml(String xml) {
                Log.d("My App", xml);
                CharSequence text = "Salvo com Sucesso!";
                Toast.makeText(context, text, Toast.LENGTH_LONG).show();
            }

            @JavascriptInterface
            public void blocksToCode(String code) {
                Log.d("My App", code);
                CharSequence text = "Codigo enviado para arduino com Sucesso!";
                Toast.makeText(context, text, Toast.LENGTH_LONG).show();
            }
        }

        final WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.addJavascriptInterface(new JsObject(), "Android");
        myWebView.loadUrl("http://10.0.2.2:5501/");
        WebSettings webSettings = myWebView.getSettings();
        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                injectJS(view);
                myWebView.loadUrl(
                        "javascript:(function() { " +
                                "id=1;project=1;loadXML();"+
                                "})()");
                super.onPageFinished(view, url);
            }
        });
        webSettings.setJavaScriptEnabled(true);
    }

    private void injectJS(WebView myWebView) {
        Log.d("MyApp","I am here");
        try {
            /*InputStream inputStream = getAssets().open("jscript.js");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            // preserve non-english letters
            String uriEncoded = URLEncoder.encode(new String(buffer, "UTF-8"), "UTF-8").replace("+", "%20");

            String encoded = Base64.encodeToString(uriEncoded.getBytes(), Base64.NO_WRAP);*/
            myWebView.loadUrl("javascript:(function() {" +
                    "id=1; project=1; alert(id)"+
                    "})()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}