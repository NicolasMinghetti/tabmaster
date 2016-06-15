package fr.insalyon.pi.tabmaster;

/**
 * Created by nicolas on 08/06/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class FacebookConnect extends Activity {


    private TextView info;
    private LoginButton loginButton;
    private Button btn;
    private CallbackManager callbackManager;
    Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FacebookSdk.sdkInitialize(getApplicationContext());
        //removed becouse initialized in main
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.facebook_connection);
        info = (TextView)findViewById(R.id.info);

        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
        Log.i("Connection State:", String.valueOf(loggedIn));
        if(loggedIn){
            Log.i("Profile info", Profile.getCurrentProfile().getLastName());
        }
        ctx = getApplicationContext();
        final Button returnButton = (Button)findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Le premier paramètre est le nom de l'activité actuelle
                // Le second est le nom de l'activité de destination
                Intent secondeActivite = new Intent(ctx, MainActivity.class);
                // Puis on lance l'intent !
                startActivity(secondeActivite);
            }
        });

        loginButton = (LoginButton)findViewById(R.id.login_button);
        //source : http://code.tutsplus.com/tutorials/quick-tip-add-facebook-login-to-your-android-app--cms-23837
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                info.setText("Vous êtes mainenant connecté !");
                returnButton.setVisibility(View.VISIBLE);
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