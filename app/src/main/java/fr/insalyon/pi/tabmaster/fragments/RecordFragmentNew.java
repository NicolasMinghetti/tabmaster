package fr.insalyon.pi.tabmaster.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import fr.insalyon.pi.tabmaster.R;
import fr.insalyon.pi.tabmaster.RecordSampleActivityNew;

/**
 * Created by Ugo on 31/05/2016.
 */
public class RecordFragmentNew extends android.support.v4.app.Fragment {

    private Context ctx;
    private TextView title;
    private Button recTestBtn;
    private TextView title2;
    private Button rec2Btn;

    public static RecordFragmentNew newInstance() {
        RecordFragmentNew newFragment = new RecordFragmentNew();
        return newFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Main view containing all the UI elements
        View view = inflater.inflate(R.layout.record_fragment, container, false);

        //Instancing UI elements
        title2 = (TextView)view.findViewById(R.id.rec2_title);
        rec2Btn = (Button)view.findViewById(R.id.rec_test2_button);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Get activity that uses this fragment
        ctx = getActivity();

        rec2Btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent nextActivity = new Intent(ctx, RecordSampleActivityNew.class);
                startActivity(nextActivity);
            }
        });
    }

}
