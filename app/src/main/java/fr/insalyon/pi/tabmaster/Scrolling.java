package fr.insalyon.pi.tabmaster;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nicolas on 24/05/16.
 */
public class Scrolling extends Activity {
    private ScrollView verticalScrollview;
    private int speed               =   2;      // A adapter dans le code
    private int verticalScrollMax   = 3280;     // A adapter à la longueur de la tablature
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
        verticalScrollview  =   (ScrollView) findViewById(R.id.vertical_scrollview_id2);

        ViewTreeObserver vto 		=	verticalScrollview.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                verticalScrollview.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                startAutoScrolling();
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
                    speed=2;
                } else {
                    Log.i("Info", "Remettre le défilement");
                    playButton.setTag("Play");
                    playButton.setImageResource(R.drawable.ic_play_circle_outline_white_48dp);
                    speed=0;
                }
            }
        });

        // On récupère l'intent qui a lancé cette activité
        Intent i = getIntent();

        // Puis on récupère le nom de la musique donné dans l'autre activité, ou 0 si cet extra n'est pas dans l'intent
        int age = i.getIntExtra(MainActivity.AGE, 0);


        String string="444---/------/--0---/------/--2---/-1--6-/-1--6-/--0---/------/--2---/------/------/--0---/------/--2---/-1--6-/-1--6-/--0---/------/--2---/------/------/--0---/------/--2---/-1--6-/-1--6-/--0---/------/--2---/------/------/--0---/------/--2---/------/------/--0---/------/--2---/-1--6-/-1--6-/--0---/------/--2---/------/------/--0---/------/--2---/-1--6-/-1--6-/------/------/--0---/------/--2---/-1--6-/-1--6-/--0---/------/--2---/------/------/--0---/------/--2---/-1--6-/-1--6-/------/------/--0---/------/--2---/-1--6-/-1--6-/--0---/------/--2---/------/------/--0---/------/--2---/-1--6-/-1--6-";
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

        List l = new LinkedList();  // LinkedList used to remove child elements from parents

        int j=0;
        for (String s:parts){

            textView = new TextView(this);
            textView.setText(s.charAt(0)+"\n"+s.charAt(1)+"\n"+s.charAt(2)+"\n"+s.charAt(3)+"\n"+s.charAt(4)+"\n"+s.charAt(5)+"\n");
            textView.setTextSize(30);
            textView.setTypeface(Typeface.MONOSPACE);
            child.addView(textView);
            j++;
            if(j==14){  //Start a new line every j columns
                l.add(child);
                parent.addView(child);
                child = new LinearLayout(this);
                j=0;

                textView = new TextView(this);
                textView.setText("E\nB\nG\nD\nA\nE");
                textView.setTextSize(30);
                textView.setTypeface(Typeface.MONOSPACE);
                child.addView(textView);
            }
        }
        if(j!=0){   // To complete last line
            l.add(child);
            parent.addView(child);
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
        scrollPos							= 	(int) (verticalScrollview.getScrollY() + speed);
        if(scrollPos >= verticalScrollMax){
            scrollPos						=	0;
        }
        verticalScrollview.scrollTo(0,scrollPos);
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