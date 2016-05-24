package fr.insalyon.pi.tabmaster;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by nicolas on 24/05/16.
 */
public class FacebookConnect extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        Log.i("Info", "Sur la page Facebook Connect");
    }
}
