package fr.insalyon.pi.tabmaster.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import fr.insalyon.pi.tabmaster.MainActivity;
import fr.insalyon.pi.tabmaster.R;
import fr.insalyon.pi.tabmaster.Scrolling;
import fr.insalyon.pi.tabmaster.fragments.CommentsFragment;
import fr.insalyon.pi.tabmaster.models.MusicAppli;

/**
 * Created by Ugo on 31/05/2016.
 */

//TODO adapter à Music model
// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class TabAdapter extends RecyclerView.Adapter<TabAdapter.ViewHolder> {

    // Store a member variable for the tabs
    private List<MusicAppli> mTabs;
    private static Context context;

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Your holder should contain a member variable for any view that will be set as you render a rowaccess
        private TextView titleTV;
        private TextView authorTV;
        private ImageButton openBtn;
        private ImageButton playBtn;
        private ImageButton commentsBtn;

        public ViewHolder(View itemView) {

            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            context = itemView.getContext();

            // Define click listener for the ViewHolder's View.



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("cliqué","click");

                }
            });

            titleTV = (TextView) itemView.findViewById(R.id.titleTV);
            authorTV = (TextView) itemView.findViewById(R.id.authorTV);
            openBtn = (ImageButton) itemView.findViewById(R.id.openButton);
            playBtn = (ImageButton) itemView.findViewById(R.id.playButton);
            commentsBtn = (ImageButton) itemView.findViewById(R.id.commentsButton);
        }
    }

    // Pass in the tab array into the constructor
    public TabAdapter(List<MusicAppli> tabs) {
        mTabs = tabs;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View tabView = inflater.inflate(R.layout.tab_lib_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(tabView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TabAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        MusicAppli tab = mTabs.get(position);

        // Set item views based on the data model
        TextView title = viewHolder.titleTV;
        title.setText(tab.getTitle());

        TextView author = viewHolder.authorTV;
        author.setText(tab.getOwner());

        final String tablature=tab.getTablature();
        viewHolder.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent =  new Intent(context, Scrolling.class);
                intent.putExtra("MusicTablature", tablature);

                context.startActivity(intent);

            }
        });

        final int idMusic=tab.getId();
        viewHolder.commentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentJump(idMusic);
            }
        });

    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return mTabs.size();
    }
    private void fragmentJump(int idMusic) {
        Fragment mFragment = new CommentsFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString("idMusic", String.valueOf(idMusic));
        //mBundle.putParcelable("item_selected_key", mItemSelected);
        mFragment.setArguments(mBundle);
        switchContent(R.id.tab_lib_recycle_view, mFragment);
    }
    public void switchContent(int id, Fragment fragment) {
        if (context== null)
            return;
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag);
        }
    }
}
