package fr.insalyon.pi.tabmaster;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import fr.insalyon.pi.tabmaster.fragments.ScrollFragment;

/**
 * Created by nicolas on 24/05/16.
 */
public class Scrolling extends Activity {
    private HorizontalScrollView horizontalScrollview;
    private int speed               =   3;      // Default speed
    private int oldSpeed;
    private int horizontalScrollMax   = 6280;     // A adapter à la longueur de la tablature
    private Timer scrollTimer		=	null;
    private TimerTask clickSchedule;
    private TimerTask scrollerSchedule;
    private TimerTask faceAnimationSchedule;
    private int scrollPos =	0;
    private Boolean isFaceDown      =   true;
    private Timer clickTimer		=	null;
    private Timer faceTimer         =   null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrolling_activity2);

        // For verticalSrollview, from : https://github.com/blessenm/AndroidAutoScrollListView
        horizontalScrollview  =   (HorizontalScrollView) findViewById(R.id.horizontal_scrollview_id2);

        ViewTreeObserver vto 		=	horizontalScrollview.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                horizontalScrollview.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                startAutoScrolling();
            }
        });
        final ImageView rewindButton = (ImageView) findViewById(R.id.rewindButton);
        rewindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vue) {
                horizontalScrollview.scrollTo(0,0);
            }
        });
        final Button okButton = (Button) findViewById(R.id.button);
        final EditText bpmRate = (EditText) findViewById(R.id.bpm);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vue) {
                int nBpmRate;
                try
                {
                    nBpmRate = Integer.parseInt(String.valueOf(bpmRate.getText()));
                }
                catch (NumberFormatException e)
                {
                    nBpmRate=90;
                }
                Log.i("New speed set to", String.valueOf(nBpmRate));
                speed=(int)(nBpmRate*71/2000);      // A Adapter par la suite, le nombre dépend de la largeur de l'écran du téléphone. Mais c'est suffisant pour le MVP.
                Log.i("Speed is now", String.valueOf(speed));
                horizontalScrollview.scrollTo(0,0);
            }
        });
        final ImageView playButton=(ImageView) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vue) {
                if(playButton.getTag().equals("Play")){
                    Log.i("Info", "Arrêter le défilement");
                    playButton.setTag("Pause");
                    playButton.setImageResource(R.drawable.ic_pause_circle_outline_white_48dp);
                    speed=oldSpeed;
                } else {
                    Log.i("Info", "Remettre le défilement");
                    playButton.setTag("Play");
                    playButton.setImageResource(R.drawable.ic_play_circle_outline_white_48dp);
                    oldSpeed=speed;
                    speed=0;
                }
            }
        });

        // On récupère l'intent qui a lancé cette activité
        Intent i = getIntent();

        // Puis on récupère le nom de la musique donné dans l'autre activité, ou 0 si cet extra n'est pas dans l'intent
        int age = i.getIntExtra(ScrollFragment.AGE, 0);


        String string="444---/------/--0---/------/--2---/-1--6-/-1--6-/--0---/------/--2---/------/------/--0---/------/--2---/-1--6-/-1--6-/--0---/------/--2---/------/------/--0---/------/--2---/-1--6-/-1--6-/--0---/------/--2---/------/------/--0---/------/--2---/------/------/--0---/------/--2---/-1--6-/-1--6-/--0---/------/--2---/------/------/--0---/------/--2---/-1--6-/-1--6-/------/------/--0---/------/--2---/-1--6-/-1--6-/--0---/------/--2---/------/------/--0---/------/--2---/-1--6-/-1--6-/------/------/--0---/------/--2---/-1--6-/-1--6-/--0---/------/--2---/------/------/--0---/------/--2---/-1--6-/-1--6-";
        String[] parts = string.split("/");

        LinearLayout parent = (LinearLayout) findViewById(R.id.ll_example);
        parent.setOrientation(LinearLayout.HORIZONTAL);

        TextView textView;
        for(int j=0; j<10;j++){                 // To add some space at beginning
            textView = new TextView(this);
            textView.setText(" " + "\n" + " " + "\n" + " " + "\n" + " " + "\n" + " " + "\n" + " " + "\n");
            textView.setTextSize(30);
            textView.setTypeface(Typeface.MONOSPACE);
            parent.addView(textView);
        }
        textView = new TextView(this);
        textView.setText("E\nB\nG\nD\nA\nE");
        textView.setTextSize(30);
        textView.setTypeface(Typeface.MONOSPACE);
        parent.addView(textView);
        for (String s:parts) {

            textView = new TextView(this);
            textView.setText(s.charAt(0) + "\n" + s.charAt(1) + "\n" + s.charAt(2) + "\n" + s.charAt(3) + "\n" + s.charAt(4) + "\n" + s.charAt(5) + "\n");
            textView.setTextSize(30);
            textView.setTypeface(Typeface.MONOSPACE);
            parent.addView(textView);
        }
        //parent.removeView((LinearLayout)l.get(0));    // To remove the first tablature line
    }

    public void startAutoScrolling(){
        if (scrollTimer == null) {
            scrollTimer					=	new Timer();
            final Runnable Timer_Tick 	= 	new Runnable() {
                public void run() {
                    moveScrollView();
                }
            };

            if(scrollerSchedule != null){
                scrollerSchedule.cancel();
                scrollerSchedule = null;
            }
            scrollerSchedule = new TimerTask(){
                @Override
                public void run(){
                    runOnUiThread(Timer_Tick);
                }
            };

            scrollTimer.schedule(scrollerSchedule, 30, 30);

        }
    }

    public void moveScrollView(){
        scrollPos							= 	(int) (horizontalScrollview.getScrollX() + speed);
        if(scrollPos >= horizontalScrollMax){
            scrollPos						=	0;
        }
        horizontalScrollview.scrollTo(scrollPos,0);
        //Log.e("moveScrollView","moveScrollView");
    }

    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    public void onPause() {
        super.onPause();
        finish();
    }

    public void onDestroy(){
        clearTimerTaks(clickSchedule);
        clearTimerTaks(scrollerSchedule);
        clearTimerTaks(faceAnimationSchedule);
        clearTimers(scrollTimer);
        clearTimers(clickTimer);
        clearTimers(faceTimer);

        clickSchedule         = null;
        scrollerSchedule      = null;
        faceAnimationSchedule = null;
        scrollTimer           = null;
        clickTimer            = null;
        faceTimer             = null;

        super.onDestroy();
    }

    private void clearTimers(Timer timer){
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void clearTimerTaks(TimerTask timerTask){
        if(timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }
}