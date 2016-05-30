package fr.insalyon.pi.tabmaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicolas on 24/05/16.
 */
public class Scrolling extends Activity {
    ListView liste = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrolling_activity2);


        final ImageView playButton=(ImageView) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vue) {
                if(playButton.getTag().equals("Play")){
                    Log.i("Info", "Arrêter le défilement");
                    playButton.setTag("Pause");
                    playButton.setImageResource(R.drawable.ic_pause_circle_outline_white_48dp);
                } else {
                    Log.i("Info", "Remettre le défilement");
                    playButton.setTag("Play");
                    playButton.setImageResource(R.drawable.ic_play_circle_outline_white_48dp);
                }
            }
        });

        // On récupère l'intent qui a lancé cette activité
        Intent i = getIntent();

        // Puis on récupère l'âge donné dans l'autre activité, ou 0 si cet extra n'est pas dans l'intent
        int age = i.getIntExtra(MainActivity.AGE, 0);


        liste = (ListView) findViewById(R.id.listView);
        List<String> exemple = new ArrayList<String>();
        exemple.add("Item 1");
        exemple.add("Item 2");
        exemple.add("Item 3");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, exemple);
        liste.setAdapter(adapter);
    }
}
