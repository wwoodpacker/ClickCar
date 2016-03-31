package com.taxi.clickcar;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * Created by Назар on 27.03.2016.
 */
public class ActivityDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    static String LOGIN="";
    public static String Name="";
    static String PASSWORD="";
    private String credentials;
    public static String base64EncodedCredentials ;

    FragmentMap fmap;
    FragmentHistory fhistory;
    FragmentMassages fmassages;
    FragmentSettings fsettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.bringToFront();



        LOGIN=getIntent().getStringExtra("LOGIN");
        PASSWORD=getIntent().getStringExtra("PASSWORD");
        credentials=LOGIN+":"+PASSWORD;
        base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView text_name=(TextView) header.findViewById(R.id.text_name);
        if (Name!="")  text_name.setText(Name);
        fmap = new FragmentMap();
        fsettings = new FragmentSettings();
        fmassages = new FragmentMassages();
        fhistory = new FragmentHistory();
        FragmentManager ftrans = getSupportFragmentManager();
        ftrans.beginTransaction().replace(R.id.container, fmap).commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        FragmentManager ftrans = getSupportFragmentManager();

        if (id == R.id.nav_map) {
            ftrans.beginTransaction().replace(R.id.container, fmap).commit();
        } else if (id == R.id.nav_history) {
            ftrans.beginTransaction().replace(R.id.container, fhistory).commit();

        } else if (id == R.id.nav_massages) {
            ftrans.beginTransaction().replace(R.id.container, fmassages).commit();

        } else if (id == R.id.nav_settings) {
            ftrans.beginTransaction().replace(R.id.container, fsettings).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
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


}
