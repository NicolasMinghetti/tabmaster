package fr.insalyon.pi.tabmaster;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/* Created by Ugo on 06/06/2016.
 */
public class RecordSampleActivity extends AppCompatActivity {
    private AudioIn ai;
    Context ctx;

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



    class AudioIn extends Thread {
        private boolean stopped = false;
        Context act;
        RequestQueue queue;
        final String url = "http://10.43.2.151:80/recup/";


        AudioIn(AppCompatActivity parent) {
            start();
            act = parent;
        }

        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

            AudioRecord recorder = null;
            short[] buffer = new short[44100];
            queue = Volley.newRequestQueue(act);

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

                //set connection
                URL urlObj = new URL(url);
                HttpURLConnection urlCon = (HttpURLConnection) urlObj.openConnection();
                //add request header
                urlCon.setRequestMethod("POST");


                // ... loop
                while (!stopped) {
                    N = recorder.read(buffer, 0, buffer.length);
                    process(buffer, urlCon);
                }

                recorder.stop();
                recorder.release();
            } catch (Throwable x) {
                Log.w("AudioIn", "Error reading voice audio", x);
            } finally {
                close();
            }
        }

        private void process(short[] buffer, HttpURLConnection urlCon) throws AuthFailureError {
            String dataToSend = "";
            dataToSend = Arrays.toString(buffer);
            //dataToSend = dataToSend.replaceAll(" ", "").replace("[", "").replace("]", "");
            System.out.println(Arrays.toString(buffer));

            try{

                // Send post request
                byte[] outputInBytes = dataToSend.getBytes("UTF-8");
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
                System.out.println(response.toString());
            }catch(Exception e){
            }


        }

        void close() {
            stopped = true;
        }

    }
}