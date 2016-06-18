package fr.insalyon.pi.tabmaster.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.Profile;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.File;

import fr.insalyon.pi.tabmaster.R;
import fr.insalyon.pi.tabmaster.models.Music;

/**
 * Created by Ugo on 13/06/2016.
 */
public class NewTabDialogFragment extends DialogFragment {
    private EditText titleET;
    private EditText authorET;
    private Music newMusic;
    private String fileName;
    private File oldFile;
    private File renamedFile;
    private boolean fbool;

    public static NewTabDialogFragment newInstance(String tab, String audioFileName) {
        NewTabDialogFragment ntdf = new NewTabDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("tab", tab);
        args.putString("audioFileName", audioFileName);
        ntdf.setArguments(args);

        return ntdf;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout. newtab_dialog_layout, null);

        titleET = (EditText) view.findViewById(R.id.title_field);
        authorET = (EditText) view.findViewById(R.id.author_field);

        newMusic = new Music();
        newMusic.setTablature(getArguments().getString("tab")); //get the tab from RecordSampleActivity

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)// Add action buttons
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String title = titleET.getText().toString();
                        String owner = authorET.getText().toString();
                        fileName = title.replace(" ","_").replace(" ", "_") + title.replace(" ","_").replace(" ", "_");

                        //rename file
                        try{
                            // create new File objects
                            oldFile = new File(getArguments().getString("audioFileName"));
                            renamedFile = new File(getArguments().getString("audioFileName").replace("temp_recording", "fileName"));

                            // rename file
                            fbool = oldFile.renameTo(renamedFile);

                            // print
                            System.out.print("File renamed? "+fbool);

                        }catch(Exception e){
                            // if any error occurs
                            e.printStackTrace();
                        }


                        newMusic.setTitle(title);
                        newMusic.setOwner(owner);
                        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;

                        if(loggedIn){
                            newMusic.setPlayer(Profile.getCurrentProfile().getFirstName());
                        } else {
                            newMusic.setPlayer("Admin");
                        }
                        newMusic.setNum_stars((float)3);
                        newMusic.setAudio_file(renamedFile.getAbsolutePath());
                        new HttpRequestTaskSendUpdate().execute();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NewTabDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private class HttpRequestTaskSendUpdate extends AsyncTask<Void, Void, Music[]> {
        @Override
        protected Music[] doInBackground(Void... params) {
            try {
                final String url = getResources().getString(R.string.serveur_ip) + "/music/";

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Log.i("NewTabDialogFragment", "Sending new tab  : " + newMusic.getTitle() + " " + newMusic.getOwner() + " " + newMusic.getTablature());
                restTemplate.postForObject(url,newMusic,Music.class);

                //return music;
            } catch (Exception e) {
                Log.e("NewTabDialogFragment", e.getMessage(), e);
            }
            return null;
        }
    }
}
