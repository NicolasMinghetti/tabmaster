package fr.insalyon.pi.tabmaster.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import fr.insalyon.pi.tabmaster.R;
import fr.insalyon.pi.tabmaster.adapters.TabAdapter;
import fr.insalyon.pi.tabmaster.models.Music;

/**
 * Created by Ugo on 31/05/2016.
 */

//TODO adapter à Music model
public class TabLibraryFragment extends android.support.v4.app.Fragment {

    private ArrayList<Music> tabs =new ArrayList<Music>();
    private TextView title;
    private Context ctx;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

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
        //Test object list
        tabs = Music.createTabsList(1);
        //new HttpUpdateTabLib().execute();
        //tabs.add(new Music());

        //Main view containing all the UI elements
        View view = inflater.inflate(R.layout.tab_library_fragment, container, false);

        //Instancing UI elements
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("TabLibFgt", "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        new HttpUpdateTabLib().execute();
                        mSwipeRefreshLayout.setRefreshing(false); //to use ine updateOperation
                    }
                }
        );

        //Instancing recycler view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.tab_lib_recycle_view);
        Log.d("TabLibraryFragment", "PASSED : RecyclerView instantiated");
        // use this setting to improve performance if you know that changes in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(ctx);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Log.d("TabLibraryFragment", "PASSED : LayoutManager set");
        // specify an adapter
        mAdapter = new TabAdapter(tabs);
        System.out.print(tabs);
        mRecyclerView.setAdapter(mAdapter);
        Log.d("TabLibraryFragment", "PASSED : TabAdapter set");

        mRecyclerView.addItemDecoration(new Divider(ctx));
        return view;
    }

    private class HttpUpdateTabLib extends AsyncTask<Void, Void, Music[]> {
        @Override
        protected Music[] doInBackground(Void... params) {
            try {
                final String url = "http://10.0.2.2:8000/music/"; // Adresse is 10.0.2.2 and not 127.0.0.1 because on virtual machine
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Music[] tabslocal = restTemplate.getForObject(url, Music[].class);
                return tabslocal;
            } catch (Exception e) {
                Log.e("TabLibFragment", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Music[] music) {
            if(music != null) {
                for (Music elem : music) {
                    System.out.println("Music:"+String.valueOf(elem.getId()));
                    System.out.println("Music:"+elem.getTitle());
                    //tabs.add(elem);
                }
            }
            mSwipeRefreshLayout.setRefreshing(false);

        }
    }

    //Define item decoration : line divider
    public class Divider extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public Divider(Context context) {
            mDivider = ResourcesCompat.getDrawable(getResources(), R.drawable.line_divider, null);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

}
