package fr.insalyon.pi.tabmaster.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fr.insalyon.pi.tabmaster.R;
import fr.insalyon.pi.tabmaster.adapters.TabAdapter;
import fr.insalyon.pi.tabmaster.models.TabRessource;

/**
 * Created by Ugo on 31/05/2016.
 */
public class TabLibraryFragment extends android.support.v4.app.Fragment {

    private ArrayList<TabRessource> tabs;
    private TextView title;
    private Context ctx;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static TabLibraryFragment newInstance() {
        TabLibraryFragment newFragment = new TabLibraryFragment();
        return newFragment;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Get activity that uses this fragment
        ctx = getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tabs = TabRessource.createTabsList(20);
        Log.d("TabLibraryFragment", "PASSED : tabs list created : " + tabs.get(12).getAuthor());

        //Main view containing all the UI elements
        View view = inflater.inflate(R.layout.tab_library_fragment, container, false);

        //Instancing UI elements
        title = (TextView)view.findViewById(R.id.tab_lib_title);

        //Instancing recycler view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.tab_lib_recycle_view);
        Log.d("TabLibraryFragment", "PASSED : RecyclerView instantiated");
        // use this setting to improve performance if you know that changes in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(ctx);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Log.d("TabLibraryFragment", "PASSED : LayoutManager set");
        // specify an adapter (see also next example)
        mAdapter = new TabAdapter(tabs);
        mRecyclerView.setAdapter(mAdapter);
        Log.d("TabLibraryFragment", "PASSED : TabAdapter set");

        return view;
    }
}
