package fr.insalyon.pi.tabmaster.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.insalyon.pi.tabmaster.R;

/**
 * Created by Ugo on 31/05/2016.
 */
public class RecordFragment extends android.support.v4.app.Fragment {

    TextView title;
    Context ctx;

    public static RecordFragment newInstance() {
        RecordFragment newFragment = new RecordFragment();
        return newFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Main view containing all the UI elements
        View view = inflater.inflate(R.layout.record_fragment, container, false);

        //Instancing UI elements
        title = (TextView)view.findViewById(R.id.rec_title);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Get activity that uses this fragment
        ctx = getActivity();
    }

}
