package fr.insalyon.pi.tabmaster;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by Ugo on 06/06/2016.
 */
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

    private void startRecording() {
        ai = new AudioIn();
    }

    private void stopRecording() {
        ai.close();
    }

    private View.OnClickListener btnClick = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnStart: {
                    enableButtons(true);
                    startRecording();
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
}

class AudioIn extends Thread {
    private boolean stopped    = false;
    private int max = 0;

    AudioIn() {

        start();
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        AudioRecord recorder = null;
        short[]   buffer  = new short[44100];

        try { // ... initialise

            int N = AudioRecord.getMinBufferSize(44100,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);

            recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    44100,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    N*10);

            recorder.setPositionNotificationPeriod(44100);

            recorder.startRecording();

            // ... loop



            while(!stopped) {
                N = recorder.read(buffer,0,buffer.length);
                process(buffer);
            }

            recorder.stop();
            recorder.release();
        } catch(Throwable x) {
            Log.w("AudioIn","Error reading voice audio",x);
        } finally {
            close();
        }
    }

    private void process(short[] buffer){
        for(int i =0; i<buffer.length;i++){
            //  Log.w("AudioIn", "Smpl[" + i +"] = " + buffer[i]);
        }
    }

    void close() {
        stopped = true;
    }

}
