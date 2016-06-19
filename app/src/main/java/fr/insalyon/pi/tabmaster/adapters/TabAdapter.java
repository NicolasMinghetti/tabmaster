package fr.insalyon.pi.tabmaster.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import fr.insalyon.pi.tabmaster.FacebookComments;
import fr.insalyon.pi.tabmaster.R;
import fr.insalyon.pi.tabmaster.Scrolling;
import fr.insalyon.pi.tabmaster.models.Music;
import fr.insalyon.pi.tabmaster.models.MusicAppli;

/**
 * Created by Ugo on 31/05/2016.
 */

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class TabAdapter extends RecyclerView.Adapter<TabAdapter.ViewHolder> {

    // Store a member variable for the tabs
    private List<MusicAppli> mTabs;
    public int positionG;
    public int newStar;
    public int suppId;
    private static Context context;
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Your holder should contain a member variable for any view that will be set as you render a rowaccess
        private TextView titleTV;
        private TextView authorTV;
        private ImageButton deleteBtn;
        private ImageButton playBtn;
        private ImageButton listenBtn;
        private ImageButton commentsBtn;
        private ImageButton star1;
        private ImageButton star2;
        private ImageButton star3;
        private ImageButton star4;
        private ImageButton star5;

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
            deleteBtn = (ImageButton) itemView.findViewById(R.id.deleteButton);
            playBtn = (ImageButton) itemView.findViewById(R.id.playButton);
            listenBtn = (ImageButton) itemView.findViewById(R.id.listenButton);
            commentsBtn = (ImageButton) itemView.findViewById(R.id.commentsButton);
            star1 = (ImageButton) itemView.findViewById(R.id.star1);
            star2 = (ImageButton) itemView.findViewById(R.id.star2);
            star3 = (ImageButton) itemView.findViewById(R.id.star3);
            star4 = (ImageButton) itemView.findViewById(R.id.star4);
            star5 = (ImageButton) itemView.findViewById(R.id.star5);
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
    public void onBindViewHolder(final TabAdapter.ViewHolder viewHolder, final int position) {
        // Get the data model based on position
        MusicAppli tab = mTabs.get(position);

        // Set item views based on the data model
        TextView title = viewHolder.titleTV;
        title.setText(tab.getTitle());

        TextView author = viewHolder.authorTV;
        author.setText(tab.getOwner()+", "+tab.getPlayer());

        final String tablature=tab.getTablature();
        final String owner=tab.getOwner();
        final String titleT=tab.getTitle();
        viewHolder.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent =  new Intent(context, Scrolling.class);
                intent.putExtra("MusicOwner", owner);
                intent.putExtra("MusicTitle", titleT);
                intent.putExtra("MusicTablature", tablature);

                context.startActivity(intent);

            }
        });
        viewHolder.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent =  new Intent(context, Scrolling.class);
                intent.putExtra("MusicOwner", owner);
                intent.putExtra("MusicTitle", titleT);
                intent.putExtra("MusicTablature", tablature);

                context.startActivity(intent);

            }
        });
        final int idMusic=tab.getId();
        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suppId=position;
                new HttpRequestTaskSendDelete().execute();
                //mTabs.remove(position);
                Toast.makeText(context, "Tab deleted.", Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.commentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fragmentJump(idMusic);
                final Intent intent;
                intent =  new Intent(context, FacebookComments.class);
                intent.putExtra("idMusic", String.valueOf(idMusic));

                context.startActivity(intent);
            }
        });

        Float numStars=tab.getNum_stars();
        ImageButton star1 = viewHolder.star1;
        ImageButton star2 = viewHolder.star2;
        ImageButton star3 = viewHolder.star3;
        ImageButton star4 = viewHolder.star4;
        ImageButton star5 = viewHolder.star5;

        if(0<=numStars && numStars<1){
            star1.setImageResource(R.drawable.ic_grade_black_36dp);
            star2.setImageResource(R.drawable.ic_grade_white_36dp);
            star3.setImageResource(R.drawable.ic_grade_white_36dp);
            star4.setImageResource(R.drawable.ic_grade_grey_36dp);
            star5.setImageResource(R.drawable.ic_grade_grey_36dp);
        } else if (1<=numStars && numStars<2) {
            star1.setImageResource(R.drawable.ic_grade_black_36dp);
            star2.setImageResource(R.drawable.ic_grade_black_36dp);
            star3.setImageResource(R.drawable.ic_grade_grey_36dp);
            star4.setImageResource(R.drawable.ic_grade_grey_36dp);
            star5.setImageResource(R.drawable.ic_grade_grey_36dp);
        } else if (2<=numStars && numStars<3) {
            star1.setImageResource(R.drawable.ic_grade_black_36dp);
            star2.setImageResource(R.drawable.ic_grade_black_36dp);
            star3.setImageResource(R.drawable.ic_grade_black_36dp);
            star4.setImageResource(R.drawable.ic_grade_grey_36dp);
            star5.setImageResource(R.drawable.ic_grade_grey_36dp);
        } else if (3<=numStars && numStars<4) {
            star1.setImageResource(R.drawable.ic_grade_black_36dp);
            star2.setImageResource(R.drawable.ic_grade_black_36dp);
            star3.setImageResource(R.drawable.ic_grade_black_36dp);
            star4.setImageResource(R.drawable.ic_grade_black_36dp);
            star5.setImageResource(R.drawable.ic_grade_grey_36dp);
        } else {
            star1.setImageResource(R.drawable.ic_grade_black_36dp);
            star2.setImageResource(R.drawable.ic_grade_black_36dp);
            star3.setImageResource(R.drawable.ic_grade_black_36dp);
            star4.setImageResource(R.drawable.ic_grade_black_36dp);
            star5.setImageResource(R.drawable.ic_grade_black_36dp);
        }
        viewHolder.star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionG=position;
                newStar=1;
                new HttpRequestTaskSendUpdate().execute();
                Toast.makeText(context, "Merci, votre vote a été pris en compte !", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionG=position;
                newStar=2;
                new HttpRequestTaskSendUpdate().execute();
                Toast.makeText(context, "Merci, votre vote a été pris en compte !", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionG=position;
                newStar=3;
                new HttpRequestTaskSendUpdate().execute();
                Toast.makeText(context, "Merci, votre vote a été pris en compte !", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionG=position;
                newStar=4;
                new HttpRequestTaskSendUpdate().execute();
                Toast.makeText(context, "Merci, votre vote a été pris en compte !", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionG=position;
                newStar=5;
                new HttpRequestTaskSendUpdate().execute();
                Toast.makeText(context, "Merci, votre vote a été pris en compte !", Toast.LENGTH_SHORT).show();
            }
        });


    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return mTabs.size();
    }
    /*private void fragmentJump(int idMusic) {
        Fragment mFragment = new CommentsFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString("idMusic", String.valueOf(idMusic));
        //mBundle.putParcelable("item_selected_key", mItemSelected);
        mFragment.setArguments(mBundle);
        switchContent(R.id.tab_lib_recycle_view, mFragment);
    }*/
    /*public void switchContent(int id, Fragment fragment) {
        if (context== null)
            return;
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag);
        }
    }*/
    private class HttpRequestTaskSendUpdate extends AsyncTask<Void, Void, Music[]> {
        @Override
        protected Music[] doInBackground(Void... params) {
            try {
                //final String url = context.getResources().getString(R.string.serveur_ip)+"/music/"; // Adresse is 10.0.2.2 and not 127.0.0.1 because on virtual machine
                MusicAppli tab = mTabs.get(positionG);
                final String url = context.getResources().getString(R.string.serveur_ip)+"music/"+tab.getId()+"/"; // Adresse is 10.0.2.2 and not 127.0.0.1 because on virtual machine

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                //restTemplate.put(url,mTabs);
                //Music[] music = restTemplate.getForObject(url, Music[].class);
                Music nouvelleMusic = new Music();
                nouvelleMusic.setTitle(tab.getTitle());
                nouvelleMusic.setOwner(tab.getOwner());
                nouvelleMusic.setPlayer(tab.getPlayer());
                nouvelleMusic.setNum_stars(new Float(newStar));
                nouvelleMusic.setNum_stars_votes(tab.getNum_stars_votes());
                nouvelleMusic.setTablature(tab.getTablature());
                restTemplate.put( url, nouvelleMusic, Music.class);


                //return music;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }
    }
    private class HttpRequestTaskSendDelete extends AsyncTask<Void, Void, Music[]> {
        @Override
        protected Music[] doInBackground(Void... params) {
            try {
                MusicAppli tab = mTabs.get(suppId);
                final String url = context.getResources().getString(R.string.serveur_ip)+"music/"+tab.getId()+"/"; // Adresse is 10.0.2.2 and not 127.0.0.1 because on virtual machine
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.delete(url, Music.class);

            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }
    }
}