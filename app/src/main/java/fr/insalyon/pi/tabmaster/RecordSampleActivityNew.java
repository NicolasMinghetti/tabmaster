package fr.insalyon.pi.tabmaster;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private int speed               =   25;      // Default speed
    private int oldSpeed;
    private int horizontalScrollMax   = 62800;     // A adapter à la longueur de la tablature
    private Timer scrollTimer		=	null;
    private TimerTask clickSchedule;
    private TimerTask scrollerSchedule;
    private TimerTask faceAnimationSchedule;
    private int scrollPos =	0;
    private Boolean isFaceDown      =   true;
    private Timer clickTimer		=	null;
    private Timer faceTimer         =   null;
    private TextView textView;
    public LinearLayout parentView;
    private Context ctx;


    private AudioIn ai;
    private StringBuffer finalTab;
    private FragmentManager fragmentManager;
    private String parsedResponse;

    final String filePath = "/sdcard/tabmaster/temp_recording";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_sample_activity_new);
        ctx = getApplicationContext();

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
        parentView = (LinearLayout) findViewById(R.id.ll_example);
        parentView.setOrientation(LinearLayout.HORIZONTAL);

        for(int j=0; j<10;j++){                 // To add some space at beginning
            textView = new TextView(getApplicationContext());
            textView.setText(" " + "\n" + " " + "\n" + " " + "\n" + " " + "\n" + " " + "\n" + " " + "\n");
            textView.setTextSize(30);
            textView.setTypeface(Typeface.MONOSPACE);
            parentView.addView(textView);
        }
        textView = new TextView(this);
        textView.setText("E\nB\nG\nD\nA\nE");
        textView.setTextSize(30);
        textView.setTypeface(Typeface.MONOSPACE);
        parentView.addView(textView);

        setButtonHandlers();
        enableButtons(false);

        finalTab = new StringBuffer();
        fragmentManager = getSupportFragmentManager();
    }

    private void setButtonHandlers() {
        ((Button) findViewById(R.id.btnStart)).setOnClickListener(btnClick);
        ((Button) findViewById(R.id.btnStop)).setOnClickListener(btnClick);
        ((Button) findViewById(R.id.btnSave)).setOnClickListener(btnClick);
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
                case R.id.btnSave: {
                    NewTabDialogFragment ntd = NewTabDialogFragment.newInstance(finalTab.toString(), filePath); //cast the received tab into a string
                    ntd.show(fragmentManager, "tabdialog"); //start the create new tab dialog passing it the tab
                    Toast.makeText(ctx, "Tab saved !", Toast.LENGTH_SHORT).show();
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

    //Thread managing audio recording
    class AudioIn extends Thread {
        private boolean stopped = false;
        Context act;



        AudioIn(AppCompatActivity parent) {
            start();
            act = parent;
        }

        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
            System.out.println("startUser interface is called");

            AudioRecord recorder;
            short[] audioData = new short[44100]; //buffer containing the 44100 samples

            try { // ... initialise

                //set audio recording
                int N = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

                recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                        44100,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        N * 10);

                recorder.setPositionNotificationPeriod(44100);
                recorder.startRecording();



                //set file writing stream
                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(filePath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }



                // ... loop
                while (!stopped) {
                    //Read audio data
                    N = recorder.read(audioData, 0, audioData.length);

                    //Write audio to file
                    System.out.println("Short wirting to file" + audioData.toString());
                    try {
                        // // writes the data to file from buffer
                        // // stores the voice buffer

                        byte byteAudioData[] = short2byte(audioData);

                        os.write(byteAudioData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                    process(audioData);
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
            //write audio to file


            //Parsing to string format
            String dataToSend = "";
            dataToSend = Arrays.toString(buffer);
            dataToSend = dataToSend.replaceAll(" ", "").replace("[", "").replace("]", "");
            //Sending to server
            new HttpTabManager(dataToSend).execute();
        }

        private byte[] short2byte(short[] sData) {
            int shortArrsize = sData.length;
            byte[] bytes = new byte[shortArrsize * 2];

            for (int i = 0; i < shortArrsize; i++) {
                bytes[i * 2] = (byte) (sData[i] & 0x00FF);
                bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
                sData[i] = 0;
            }
            return bytes;

        }


        private class HttpTabManager extends AsyncTask<Void, Void, Void>{

            String dataToSend;
            final String url = getResources().getString(R.string.serveur_ip)+"/recup";
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

                    parsedResponse = dataToTab(response);
                    System.out.println("tab = " + parsedResponse);
                    ///////////////////
                    final String[] parts = parsedResponse.split("/");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (String s:parts) {
                                textView = new TextView(getApplicationContext());
                                textView.setText(s.charAt(0) + "" + s.charAt(1) + "\n" + s.charAt(2) + s.charAt(3) + "\n" + s.charAt(4)+s.charAt(5) + "\n" + s.charAt(6)+s.charAt(7) + "\n" + s.charAt(8)+s.charAt(9) + "\n" + s.charAt(10)+s.charAt(11) + "\n");
                                //textView.setText("1\n2\n3\n4\n5\n5\n");
                                textView.setTextSize(30);
                                textView.setTextColor(Color.BLACK);
                                textView.setTypeface(Typeface.MONOSPACE);
                                parentView.addView(textView);
                            }
                        }
                    });

                    //buffering response
                    finalTab.append(parsedResponse);

                    //print result
                }catch(Exception e){
                    Log.e("AudioIn", "Error : ", e);
                }finally {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Void i){

            }

            public String dataToTab(StringBuffer data){
                String tab = "";
                String elem;
                String[] tabArray;

                tabArray = (data.toString()).replace("[", "").split(",|\\]");

                for(int i=0; i<tabArray.length; i++){
                    elem = tabArray[i];
                    if(elem.equals("-1")){
                        tab += "- ";
                    }else if(elem.length()>1){
                        tab += elem;
                    }else{
                        tab += elem + " ";
                    }

                    if(i%6==5){
                        tab += "/";
                    }
                }
                return tab;
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