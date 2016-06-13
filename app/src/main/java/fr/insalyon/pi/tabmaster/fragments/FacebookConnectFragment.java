package fr.insalyon.pi.tabmaster.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.Profile;

import fr.insalyon.pi.tabmaster.FacebookConnect;
import fr.insalyon.pi.tabmaster.R;

/**
 * Created by Ugo on 30/05/2016.
 */

public class FacebookConnectFragment extends android.support.v4.app.Fragment {

    TextView title;
    Button btn;
    Context ctx;

    /*public static FacebookConnectFragment newInstance() {
        FacebookConnectFragment newFragment = new FacebookConnectFragment();
        return newFragment;
    }*/
    public FacebookConnectFragment() {
        // Empty constructor required for fragment subclasses
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Main view containing all the UI elements
        View view = inflater.inflate(R.layout.facebook_connection_fragment, container, false);

        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
        TextView info= (TextView)view.findViewById(R.id.info);
        Button btn = (Button) view.findViewById(R.id.facebook_btn);
        if(loggedIn){
            btn.setText("Se déconnecter de Facebook");
            info.setText("Tu es déjà connecté "+Profile.getCurrentProfile().getFirstName());
            Log.i("User connected, profile", Profile.getCurrentProfile().getFirstName()+" "+Profile.getCurrentProfile().getLastName());
        } else {
            btn.setText("Se connecter avec Facebook");
            info.setText("Tu n'es pas encore connecté");
            Log.i("Connection State", String.valueOf(loggedIn));
        }


        //Instancing UI elements
        btn = (Button)view.findViewById(R.id.facebook_btn);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Le premier paramètre est le nom de l'activité actuelle
                // Le second est le nom de l'activité de destination
                Intent secondeActivite = new Intent(ctx, FacebookConnect.class);
                // Puis on lance l'intent !
                startActivity(secondeActivite);
            }
        });
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Get activity that uses this fragment
        ctx = getActivity();
    }


}