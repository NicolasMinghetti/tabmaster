package fr.insalyon.pi.tabmaster;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by nicolas on 24/05/16.
 */
public class Scrolling extends Activity {
    GridView liste = null;

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

        // Puis on récupère le nom de la musique donné dans l'autre activité, ou 0 si cet extra n'est pas dans l'intent
        int age = i.getIntExtra(MainActivity.AGE, 0);


        String string="------/------/--0---/------/--2---/-1--6-/-1--6-/--0---/------/--2---/------/------/--0---/------/--2---/-1--6-/-1--6-/--0---/------/--2---/------/------/--0---/------/--2---/-1--6-/-1--6-/--0---/------/--2---/------/------/--0---/------/--2---/------/------/--0---/------/--2---/-1--6-/-1--6-/--0---/------/--2---/------/------/--0---/------/--2---/-1--6-/-1--6-/------/------/--0---/------/--2---/-1--6-/-1--6-/--0---/------/--2---/------/------/--0---/------/--2---/-1--6-/-1--6-/------/------/--0---/------/--2---/-1--6-/-1--6-/--0---/------/--2---/------/------/--0---/------/--2---/-1--6-/-1--6-";
        String[] parts = string.split("/");

        LinearLayout parent = (LinearLayout) findViewById(R.id.ll_example);
        parent.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(this);
        LinearLayout child = new LinearLayout(this);

        textView = new TextView(this);
        textView.setText("E\nB\nG\nD\nA\nE");
        textView.setTextSize(30);
        textView.setTypeface(Typeface.MONOSPACE);
        child.addView(textView);

        int j=1;
        for (String s:parts){

            textView = new TextView(this);
            textView.setText(s.charAt(0)+"\n"+s.charAt(1)+"\n"+s.charAt(2)+"\n"+s.charAt(3)+"\n"+s.charAt(4)+"\n"+s.charAt(5)+"\n");
            textView.setTextSize(30);
            textView.setTypeface(Typeface.MONOSPACE);
            child.addView(textView);

            if(j==14){  //Start a new line every j columns
                parent.addView(child);
                child = new LinearLayout(this);
                j=0;

                textView = new TextView(this);
                textView.setText("E\nB\nG\nD\nA\nE");
                textView.setTextSize(30);
                textView.setTypeface(Typeface.MONOSPACE);
                child.addView(textView);
            }
            j++;
        }

    }
}
