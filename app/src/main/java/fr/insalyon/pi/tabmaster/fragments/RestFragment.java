package fr.insalyon.pi.tabmaster.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import fr.insalyon.pi.tabmaster.R;
import fr.insalyon.pi.tabmaster.models.Music;

/**
 * Created by nicolas on 01/06/16.
 */
public class RestFragment extends android.support.v4.app.Fragment {


    Context ctx;
    View view;
    public static RestFragment newInstance() {
        RestFragment newFragment = new RestFragment();
        return newFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Main view containing all the UI elements
        view = inflater.inflate(R.layout.rest_fragment, container, false);
        //Instancing UI elements

        new HttpRequestTask().execute();

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Get activity that uses this fragment
        ctx = getActivity();
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Music[]> {
        @Override
        protected Music[] doInBackground(Void... params) {
            try {
                final String url = "http://10.0.2.2:8000/music/"; // Adresse is 10.0.2.2 and not 127.0.0.1 because on virtual machine
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Music[] music = restTemplate.getForObject(url, Music[].class);
                return music;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Music[] music) {
            for(Music elem : music) {
                TextView greetingIdText = (TextView) view.findViewById(R.id.id_value);
                TextView greetingContentText = (TextView) view.findViewById(R.id.content_value);
                greetingIdText.setText(String.valueOf(elem.getId()));
                greetingContentText.setText(elem.getTitle());
            }
        }
    }
}
