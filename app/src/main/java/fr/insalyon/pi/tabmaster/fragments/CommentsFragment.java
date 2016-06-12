package fr.insalyon.pi.tabmaster.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import fr.insalyon.pi.tabmaster.R;

/**
 * Created by nicolas on 08/06/16.
 */
public class CommentsFragment extends android.support.v4.app.Fragment {

    Context ctx;
    private WebView mWebView;

    View view;
    public static CommentsFragment newInstance() {
        CommentsFragment newCommentsFragment = new CommentsFragment();
        return newCommentsFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Main view containing all the UI elements
        view = inflater.inflate(R.layout.connect_fragment, container, false);
        //Instancing UI elements


        mWebView = (WebView) view.findViewById(R.id.activity_main_webview);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);

        }
        mWebView.loadUrl(getResources().getString(R.string.serveur_ip)+"/facebook/");
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Get activity that uses this fragment
        ctx = getActivity();
    }
}
