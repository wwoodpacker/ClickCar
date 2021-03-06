package com.taxi.clickcar;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.taxi.clickcar.Fragments.ConfirmFragment;
import com.taxi.clickcar.Fragments.EnterFragment;
import com.taxi.clickcar.Fragments.MyFragmentListener;
import com.taxi.clickcar.Fragments.RegFragment;
import com.taxi.clickcar.Fragments.RootFragment;

public class MainActivity extends AppCompatActivity {
    public SharedPreferences mSettings;
    public View.OnClickListener mOnClickListener;
    public static String name="";
    public ViewPager pager=null;
    public static String phone="";


    public MyFragmentListener myFragmentListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setBackgroundResource(R.color.mainblue);
        tabs.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        pager = (ViewPager) findViewById(R.id.pager);
        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

        StaticMethods.CheckConnection(this,mOnClickListener,getString(R.string.error_connection));

        myFragmentListener=new MyFragmentListener() {
            @Override
            public void onSwitchToNextFragment() {
                pager.setCurrentItem(1);
            }

            @Override
            public void onSwitchToBackFragment() {
                pager.setCurrentItem(0);
            }
        };

    }
    public void OnClickReg(){
        Toast.makeText(this,"reg",Toast.LENGTH_LONG).show();
    }
    public class TabsPagerAdapter extends FragmentPagerAdapter {

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
          if(position==0)  return EnterFragment.newInstance(position,myFragmentListener);
            else
                    return new RootFragment();
                        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position==0)  return getString(R.string.sign_in);
            else return getString(R.string.label_register);
        }
    }

    public void setmOnClickListener(View.OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }
 }
