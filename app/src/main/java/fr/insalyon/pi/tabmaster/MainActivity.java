package fr.insalyon.pi.tabmaster;

import android.content.Context;
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
import android.view.MenuItem;

import com.facebook.FacebookSdk;

import fr.insalyon.pi.tabmaster.fragments.FacebookConnectFragment;
import fr.insalyon.pi.tabmaster.fragments.HomeFragment;
import fr.insalyon.pi.tabmaster.fragments.RecordFragmentNew;
import fr.insalyon.pi.tabmaster.fragments.TabLibraryFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

        // Insert the fragment by replacing any existing fragment
        Fragment fragment=new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main_frame, fragment).commit();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Log.i("menuItem", menuItem.toString());
        Fragment fragment = null;
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_library) {
            // Go into user's library
            // A faire UGO
            try {
                fragment = new TabLibraryFragment();
            } catch (Exception e) {
                e.printStackTrace();
            }
        /*} else if (id == R.id.nav_record) {
            try {
                fragment = new RecordFragment();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        } else if (id == R.id.nav_record_new) {
            try {
                fragment = new RecordFragmentNew();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_facebook_connection) {

            /*Intent secondeActivite = new Intent(ctx, FacebookConnect.class);
            startActivity(secondeActivite);*/
            try {
                fragment = new FacebookConnectFragment();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.home) {
            try {
                fragment = new HomeFragment();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main_frame, fragment).commit();

        Log.i("item ID", String.valueOf(menuItem.getItemId()));
        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);

        // Set action bar title
        setTitle(menuItem.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void switchContent(int id, Fragment fragment) {
        //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        //getSupportFragmentManager().beginTransaction().replace(R.id.content_main_frame, fragment).commit();

        /*ft.replace(id, fragment, fragment.toString());
        ft.addToBackStack(null);
        ft.commit();*/
    }
}
