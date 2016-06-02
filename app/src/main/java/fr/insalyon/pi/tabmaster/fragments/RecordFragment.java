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
import fr.insalyon.pi.tabmaster.RecordTestActivity;

/**
 * Created by Ugo on 31/05/2016.
 */
public class RecordFragment extends android.support.v4.app.Fragment {

    private TextView title;
    private Context ctx;
    private Button recTestBtn;
    private Button recBtn;
    private Button playBtn;

    public static RecordFragment newInstance() {
        RecordFragment newFragment = new RecordFragment();
        return newFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Main view containing all the UI elements
        View view = inflater.inflate(R.layout.record_fragment, container, false);

        //Instancing UI elements
        title = (TextView)view.findViewById(R.id.rec_title);
        recTestBtn = (Button)view.findViewById(R.id.rec_test_button);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Get activity that uses this fragment
        ctx = getActivity();
        recTestBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent nextActivity = new Intent(ctx, RecordTestActivity.class);
                startActivity(nextActivity);
            }
        });
    }

}
