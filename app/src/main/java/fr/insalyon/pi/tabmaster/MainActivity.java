package fr.insalyon.pi.tabmaster;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;

import fr.insalyon.pi.tabmaster.fragments.CommentsFragment;
import fr.insalyon.pi.tabmaster.fragments.FacebookConnectFragment;
import fr.insalyon.pi.tabmaster.fragments.RecordFragment;
import fr.insalyon.pi.tabmaster.fragments.RestFragment;
import fr.insalyon.pi.tabmaster.fragments.ScrollFragment;
import fr.insalyon.pi.tabmaster.fragments.TabLibraryFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Context ctx;
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

        connection();

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
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_library) {
            // Go into user's library
            // A faire UGO
            try {
                fragment = new TabLibraryFragment().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (id == R.id.nav_record) {
            try {
                fragment = new RecordFragment().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_tempory_scrolling) {
            // A faire NICO

            // Tempory page to access Scrolling Activity.
            try {
                fragment = new ScrollFragment().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_facebook_connection) {

            /*Intent secondeActivite = new Intent(ctx, FacebookConnect.class);
            startActivity(secondeActivite);*/
            try {
                fragment = new FacebookConnectFragment().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_tempory_rest) {
            try {
                fragment = new RestFragment().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_tempory_comments) {

            try {
                fragment = new CommentsFragment().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.home) {

            try {
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
                connection();
                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        ImageView imageView = (ImageView) findViewById(R.id.profile);
        imageView.setVisibility(View.INVISIBLE);
        TextView title = (TextView) findViewById(R.id.tabmasterTitle);
        title.setVisibility(View.INVISIBLE);
        TextView smallTitle= (TextView) findViewById(R.id.smallTitle);
        smallTitle.setVisibility(View.INVISIBLE);
        TextView loginState = (TextView) findViewById(R.id.loginState);
        loginState.setVisibility(View.INVISIBLE);
        Button btn = (Button) findViewById(R.id.facebook_btn);
        btn.setVisibility(View.INVISIBLE);

        fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent secondeActivite = new Intent(ctx, FacebookConnect.class);
        startActivity(secondeActivite);
    }

    public void connection() {
        ctx = getApplicationContext();
        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;

        TextView connection_state= (TextView)findViewById(R.id.loginState);
        Button btn = (Button) findViewById(R.id.facebook_btn);
        btn.setOnClickListener(this);
        if(loggedIn){
            btn.setVisibility(View.INVISIBLE);
            Log.i("User connected, profile", Profile.getCurrentProfile().getFirstName()+" "+Profile.getCurrentProfile().getLastName());
            connection_state.setText("Bonjour "+Profile.getCurrentProfile().getFirstName()+" "+Profile.getCurrentProfile().getLastName());
        } else {
            btn.setVisibility(View.VISIBLE);
            Log.i("Connection State", String.valueOf(loggedIn));
            connection_state.setText(R.string.welcom);
        }
    }
}
