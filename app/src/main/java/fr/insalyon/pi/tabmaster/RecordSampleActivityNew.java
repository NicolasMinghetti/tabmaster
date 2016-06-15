package fr.insalyon.pi.tabmaster;

import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import fr.insalyon.pi.tabmaster.fragments.NewTabDialogFragment;

/* Created by Ugo on 06/06/2016.
 */

public class RecordSampleActivityNew extends AppCompatActivity {
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
    private TextView textView;
    private LinearLayout parent;
    private UserInterface ui;

    private AudioIn ai;
    private StringBuffer finalTab;
    private FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_sample_activity_new);

        ////////////



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
        parent = (LinearLayout) findViewById(R.id.ll_example);
        parent.setOrientation(LinearLayout.HORIZONTAL);

        for(int j=0; j<10;j++){                 // To add some space at beginning
            textView = new TextView(getApplicationContext());
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

        /////////////
        setButtonHandlers();
        enableButtons(false);

        finalTab = new StringBuffer();
        fragmentManager = getSupportFragmentManager();
    }

    private void setButtonHandlers() {
        ((Button) findViewById(R.id.btnStart)).setOnClickListener(btnClick);
        ((Button) findViewById(R.id.btnStop)).setOnClickListener(btnClick);
    }

    private void enableButton(int id, boolean isEnable) {
        ((Button) findViewById(id)).setEnabled(isEnable);
    }

    private void enableButtons(boolean isRecording) {
        enableButton(R.id.btnStart, !isRecording);
        enableButton(R.id.btnStop, isRecording);
    }

    private void startRecording() throws IOException {
        ai = new AudioIn(this);
    }
    private void startUserInteface() throws IOException {
        ui = new UserInterface(this);
    }

    private void stopRecording() {
        ai.close();
    }

    private View.OnClickListener btnClick = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnStart: {
                    enableButtons(true);
                    try {
                        startRecording();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case R.id.btnStop: {
                    enableButtons(false);
                    stopRecording();
                    break;
                }
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    class UserInterface extends Thread {

        UserInterface(AppCompatActivity parent) {
            start();
        }
        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
            try { // ... initialise


                textView = new TextView(getApplicationContext());
                //textView.setText(s.charAt(0) + "\n" + s.charAt(1) + "\n" + s.charAt(2) + "\n" + s.charAt(3) + "\n" + s.charAt(4) + "\n" + s.charAt(5) + "\n");
                textView.setText("1\n2\n3\n4\n5\n5\n6\n");
                textView.setTextSize(30);
                textView.setTypeface(Typeface.MONOSPACE);
                parent.addView(textView);


            }catch (Throwable x) {
                Log.w("AudioIn", "Error reading voice audio", x);
            }
        }
    }

    //Thread managing audio recording
    class AudioIn extends Thread {
        private boolean stopped = false;
        Context act;
        final String url = getResources().getString(R.string.serveur_ip)+"/recup";


        AudioIn(AppCompatActivity parent) {
            start();
            act = parent;
        }

        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
            try {
                startUserInteface();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AudioRecord recorder;
            short[] buffer = new short[44100]; //buffer containing the 44100 samples

            try { // ... initialise

                //set audio
                int N = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

                recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                        44100,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        N * 10);

                recorder.setPositionNotificationPeriod(44100);
                recorder.startRecording();


                // ... loop
                while (!stopped) {
                    N = recorder.read(buffer, 0, buffer.length);
                    process(buffer);
                }

                recorder.stop();
                recorder.release();
            } catch (Throwable x) {
                Log.w("AudioIn", "Error reading voice audio", x);
            } finally {
                close();
            }
        }

        //*
        private void process(short[] buffer) {
            String dataToSend = "";
            dataToSend = Arrays.toString(buffer);
            dataToSend = dataToSend.replaceAll(" ", "").replace("[", "").replace("]", "");
            String lala="ss";

            new HttpTabManager(dataToSend).execute();
        }


        private class HttpTabManager extends AsyncTask<Void, Void, Void>{

            String dataToSend;

            public HttpTabManager(String dataToSend){
                this.dataToSend = dataToSend;
            }

            @Override
            protected Void doInBackground(Void... params){
                Log.i("AudioIn", dataToSend);
                try{

                    //set connection
                    URL urlObj = new URL(url);
                    HttpURLConnection urlCon = (HttpURLConnection) urlObj.openConnection();
                    //add request header
                    urlCon.setRequestMethod("POST");

                    // Send post request
                    byte[] outputInBytes = dataToSend.getBytes("UTF-8");

                    System.out.println("sending....................");

                    OutputStream os = urlCon.getOutputStream();
                    os.write( outputInBytes );
                    os.close();


                    int responseCode = urlCon.getResponseCode();
                    System.out.println("Response Code : " + responseCode);

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(urlCon.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    System.out.println("Response is :" +response);
                    in.close();

                    //buffering response
                    finalTab.append(response);

                    //print result
                }catch(Exception e){
                    Log.e("AudioIn", "Error : ", e);
                }finally {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Void i){
                if(stopped) { //if last frame
                    NewTabDialogFragment ntd = NewTabDialogFragment.newInstance(finalTab.toString()); //cast the received tab into a string
                    ntd.show(fragmentManager, "tabdialog"); //start the create new tab dialog passing it the tab
                }
            }

        }

        void close() {
            stopped = true;
        }

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