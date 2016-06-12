package fr.insalyon.pi.tabmaster;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by nicolas on 12/06/16.
 */
public class FacebookComments extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Main view containing all the UI elements
        setContentView(R.layout.connect_fragment);

        //Instancing UI elements
        String idMusic;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                idMusic= null;
            } else {
                idMusic= extras.getString("idMusic");
            }
        } else {
            idMusic= (String) savedInstanceState.getSerializable("idMusic");
        }
        System.out.println(idMusic);



        WebView mWebView = (WebView) findViewById(R.id.activity_main_webview);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);

        }
        mWebView.loadUrl(getResources().getString(R.string.serveur_ip)+"/facebook/"+idMusic);
        Log.i("Comments Fragment", "url requested: "+getResources().getString(R.string.serveur_ip)+"/facebook/"+idMusic);
    }


}
