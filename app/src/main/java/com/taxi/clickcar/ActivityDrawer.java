package com.taxi.clickcar;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
public class ActivityDrawer extends ActionBarActivity{
    static String LOGIN="";
    static String PASSWORD="";
    private String credentials;
    public static String base64EncodedCredentials ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        LOGIN=getIntent().getStringExtra("LOGIN");
        PASSWORD=getIntent().getStringExtra("PASSWORD");
        credentials=LOGIN+":"+PASSWORD;
        base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.bringToFront();
        //toolbar.getBackground().setAlpha(0);
        //toolbar.setBackgroundColor(getResources().getColor(R.color.mainyellow));
       setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       Drawer drawer= new Drawer();

                 drawer.withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_gamepad),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye).withBadge("1").withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_book).withIdentifier(3),
                        new SectionDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_cog),
                        new DividerDrawerItem()
                        )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        selectItem(position);
                    }
                })
                .build();
            selectItem(1);
    }

    private void selectItem(int position){
        Fragment fragment=null;
        switch (position){
            case 1:
                fragment=new FragmentMap();
                break;
            case 2:
                fragment=new FragmentHistory();
                break;
            case 3:
                fragment=new FragmentMassages();
                break;
            case 4:
                fragment=new FragmentSettings();
                break;
            default:break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();

            // Highlight the selected item, update the title, and close the drawer
           // mDrawerList.setItemChecked(position, true);
            //setTitle(mScreenTitles[position]);
            //mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // Error
            Log.e(this.getClass().getName(), "Error. Fragment is not created");
        }

    }
}
