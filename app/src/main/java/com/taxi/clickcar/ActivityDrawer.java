package com.taxi.clickcar;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.taxi.clickcar.Fragments.FragmentHistory;
import com.taxi.clickcar.Fragments.FragmentMap;
import com.taxi.clickcar.Fragments.FragmentMassages;
import com.taxi.clickcar.Fragments.FragmentSettings;

/**
 * Created by Назар on 27.03.2016.
 */
public class ActivityDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LOGIN="LOGIN";
    public static final String APP_PREFERENCES_CREDIALS="CREDIALS";
    public static final String APP_PREFERENCES_REMEMBER="REMEMBER";
    public static final String APP_PREFERENCES_PASS="PASS";
    public SharedPreferences mSettings;
    public static String LOGIN="";
    public static String PASSWORD="";
    public String Name="";
    public String Phone="";

    private String credentials;
    private Toolbar toolbar;

    FragmentMap fmap;
    FragmentHistory fhistory;
    FragmentMassages fmassages;
    FragmentSettings fsettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Name=GlobalVariables.getInstance().getName();
        Phone=GlobalVariables.getInstance().getPhone();
        mSettings = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.drawer_item_map);
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        setSupportActionBar(toolbar);
        toolbar.bringToFront();


        LOGIN=getIntent().getStringExtra("LOGIN");
        PASSWORD=getIntent().getStringExtra("PASSWORD");
        credentials=LOGIN+":"+PASSWORD;
        GlobalVariables.getInstance().setBase64EncodedCredentials(Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP));


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

        FragmentManager ftrans = getSupportFragmentManager();
        ftrans.beginTransaction().replace(R.id.container, fmap).commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentMap fmap2 = new FragmentMap();
        fsettings = new FragmentSettings();
        fmassages = new FragmentMassages();
        fhistory = new FragmentHistory();
        FragmentManager ftrans = getSupportFragmentManager();

        //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (id == R.id.nav_map) {
            toolbar.setTitle(R.string.drawer_item_map);
            ftrans.beginTransaction().replace(R.id.container, fmap2).commit();
        } else if (id == R.id.nav_history) {
            toolbar.setTitle(R.string.drawer_item_history);
            ftrans.beginTransaction().replace(R.id.container, fhistory).commit();

        } else if (id == R.id.nav_massages) {
            toolbar.setTitle(R.string.drawer_item_massages);
            ftrans.beginTransaction().replace(R.id.container, fmassages).commit();

        } else if (id == R.id.nav_settings) {
            toolbar.setTitle(R.string.drawer_item_settings);
            ftrans.beginTransaction().replace(R.id.container, fsettings).commit();
        }else if(id==R.id.nav_exit) {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.remove(APP_PREFERENCES_LOGIN);
            editor.remove(APP_PREFERENCES_PASS);
            editor.putBoolean(APP_PREFERENCES_REMEMBER,false);
            editor.apply();
            startActivity(new Intent(this , MainActivity.class));
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
