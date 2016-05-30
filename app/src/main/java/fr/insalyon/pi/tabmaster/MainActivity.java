package fr.insalyon.pi.tabmaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;

import fr.insalyon.pi.tabmaster.fragments.FacebookConnectFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public final static String AGE = "fr.insalyon.pi.tabmaster.AGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // Get users lastname if connected with facebook
        TextView connection_state;
        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
        Log.i("Connection State:", String.valueOf(loggedIn));
        if(loggedIn){
            Log.i("Profile info", Profile.getCurrentProfile().getLastName());
            connection_state= (TextView)findViewById(R.id.connection_state_nav);
            connection_state.setText(Profile.getCurrentProfile().getFirstName()+" "+Profile.getCurrentProfile().getLastName());
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        Fragment fragment = null;
        Class fragmentClass;
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_library) {
            // Go into user's library
            // A faire UGO
        } else if (id == R.id.nav_record) {
            // Go into record page
            // A faire
        } else if (id == R.id.nav_tempory_scrolling) {
            // A faire NICO

            // Tempory page to access Scrolling Activity.

            // Le premier paramètre est le nom de l'activité actuelle
            // Le second est le nom de l'activité de destination
            Intent secondeActivite = new Intent(MainActivity.this, Scrolling.class);

            // On rajoute un extra
            secondeActivite.putExtra(AGE, 31);

            // Puis on lance l'intent !
            startActivity(secondeActivite);

        } else if (id == R.id.nav_facebook_connection) {
            fragmentClass = FacebookConnectFragment.class;
        }

        try {
            fragment = (FacebookConnectFragment) new FacebookConnectFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
