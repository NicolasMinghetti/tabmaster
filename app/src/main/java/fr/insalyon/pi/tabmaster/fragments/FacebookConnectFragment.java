package fr.insalyon.pi.tabmaster.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import fr.insalyon.pi.tabmaster.R;

/**
 * Created by Ugo on 30/05/2016.
 */

public class FacebookConnectFragment extends android.support.v4.app.Fragment {

    private Context ctx;
    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Main view containing all the UI elements
        View view = inflater.inflate(R.layout.facebook_connection_fragment, container, false);

        //Instancing UI elements
        info = (TextView)view.findViewById(R.id.info);
        loginButton = (LoginButton)view.findViewById(R.id.login_button);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Get activity that uses this fragment
        ctx = getActivity();

        //Facebook login management
        callbackManager = CallbackManager.Factory.create();

        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
        Log.i("Connection State:", String.valueOf(loggedIn));
        if(loggedIn){
            Log.i("Profile info", Profile.getCurrentProfile().getLastName());
        }
        //source : http://code.tutsplus.com/tutorials/quick-tip-add-facebook-login-to-your-android-app--cms-23837
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
