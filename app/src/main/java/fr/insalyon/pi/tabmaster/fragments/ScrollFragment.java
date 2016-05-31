package fr.insalyon.pi.tabmaster.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import fr.insalyon.pi.tabmaster.MainActivity;
import fr.insalyon.pi.tabmaster.R;
import fr.insalyon.pi.tabmaster.Scrolling;

/**
 * Created by Ugo on 31/05/2016.
 */
public class ScrollFragment extends android.support.v4.app.Fragment {

    TextView title;
    Button btn;
    Context ctx;
    public final static String AGE = "fr.insalyon.pi.tabmaster.AGE";


    public static ScrollFragment newInstance() {
        ScrollFragment newFragment = new ScrollFragment();
        return newFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Main view containing all the UI elements
        View view = inflater.inflate(R.layout.scroll_fragment, container, false);

        //Instancing UI elements
        title = (TextView)view.findViewById(R.id.scroll_title);
        btn = (Button)view.findViewById(R.id.scrolling_btn);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Le premier paramètre est le nom de l'activité actuelle
                // Le second est le nom de l'activité de destination
                Intent secondeActivite = new Intent(ctx, Scrolling.class);

                // On rajoute un extra
                secondeActivite.putExtra(AGE, 31);

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
