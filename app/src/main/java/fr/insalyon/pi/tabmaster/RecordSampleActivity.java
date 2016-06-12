package fr.insalyon.pi.tabmaster;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/* Created by Ugo on 06/06/2016.
 */

//TODO NETTOYER CE FOUTRE
//TODO Intégrer en tant que fragment
//TODO Récupérer la tab renvoyée par le serveur
//TODO Fenêtre enregistrer la tablature avec Titre etc.
public class RecordSampleActivity extends AppCompatActivity {
    private AudioIn ai;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_sample_activity);

        setButtonHandlers();
        enableButtons(false);
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

    private void stopRecording() {ai.close();}

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

            AudioRecord recorder;
            short[] buffer = new short[44100]; //buufer containing the 44100 samples

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
            //*
            String dataToSend = "";
            dataToSend = Arrays.toString(buffer);
            dataToSend = dataToSend.replaceAll(" ", "").replace("[", "").replace("]", "");
            //*/
            new HttpTabManager(dataToSend).execute();

            /*
            try{
                //set connection
                URL urlObj = new URL(url);
                HttpURLConnection urlCon = (HttpURLConnection) urlObj.openConnection();
                //add request header
                urlCon.setRequestMethod("POST");

                // Send post request
                byte[] outputInBytes = dataToSend.getBytes("UTF-8");
                System.out.println("Sending.............................................");

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
                in.close();

                //print result
            }catch(Exception e){
                Log.e("AudioIn", "Error : ", e);
            }
            //*/
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
                    System.out.println("Sending.............................................");

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
                    in.close();

                    //print result
                }catch(Exception e){
                    Log.e("AudioIn", "Error : ", e);
                }finally {
                    return null;
                }

            }

        }

        void close() {
            stopped = true;
        }

    }

}