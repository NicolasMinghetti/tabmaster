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
 * Created by nicolas on 13/06/16.
 */
public class HomeFragment extends android.support.v4.app.Fragment  {

    View view;
    Context ctx;


    public static HomeFragment newInstance() {
        HomeFragment newFragment = new HomeFragment();
        return newFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.main_content_frame, container, false);
        connection();
        return view;

    }
    public void connection() {
        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;

        TextView connection_state= (TextView) view.findViewById(R.id.loginState);
        Button btn = (Button) view.findViewById(R.id.facebook_btn);

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent secondeActivite = new Intent(ctx, FacebookConnect.class);
                startActivity(secondeActivite);
            }
        });


        if(loggedIn){
            btn.setVisibility(View.INVISIBLE);
            Log.i("User connected, profile", Profile.getCurrentProfile().getFirstName()+" "+Profile.getCurrentProfile().getLastName());
            connection_state.setText("Bonjour "+Profile.getCurrentProfile().getFirstName()+" "+Profile.getCurrentProfile().getLastName());
        } else {
            btn.setVisibility(View.VISIBLE);
            Log.i("Connection State", String.valueOf(loggedIn));
            connection_state.setText(R.string.welcom);
        }

    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Get activity that uses this fragment
        ctx = getActivity();
    }

}
