package fr.insalyon.pi.tabmaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by nicolas on 24/05/16.
 */
public class FacebookConnect extends Activity{


    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.facebook_connection);
        info = (TextView)findViewById(R.id.info);

        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
        Log.i("Connection State:", String.valueOf(loggedIn));
        if(loggedIn){
            Log.i("Profile info", Profile.getCurrentProfile().getLastName());
        }
        loginButton = (LoginButton)findViewById(R.id.login_button);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
