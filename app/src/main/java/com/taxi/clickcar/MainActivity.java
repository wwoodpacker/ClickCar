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

public class MainActivity extends AppCompatActivity {
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LOGIN="LOGIN";
    public static final String APP_PREFERENCES_PASS="PASS";
    public static final String APP_PREFERENCES_CREDIALS="CREDIALS";
    public static final String APP_PREFERENCES_REMEMBER="REMEMBER";
    public SharedPreferences mSettings;
    public Button btnReg;
    public Button btnOrder;
    public Button btnEnter;
    public EditText sign_login;
    public EditText sign_pass;
    public View.OnClickListener mOnClickListener;
    public static String name="";
    public static String password="";
    public ViewPager pager=null;
    public static String password_again="";
    public static String phone="";
    public String login="";
    public static boolean fl=false;

    public MyFragmentListener myFragmentListener;
//https://github.com/danilao/fragments-viewpager-example/blob/master/src/com/pineappslab/frcontainer/FirstFragment.java
    public static void setFl(boolean _fl){fl=_fl;}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setBackgroundResource(R.color.mainblue);
        tabs.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        pager = (ViewPager) findViewById(R.id.pager);
        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager());

        final TabsPagerAdapter2 adapter2 = new TabsPagerAdapter2(getSupportFragmentManager());

        if (fl) {
            name=GlobalVariables.getInstance().getRegisterName();
            password=GlobalVariables.getInstance().getRegisterPassword();
            password_again=GlobalVariables.getInstance().getRegisterPasswordAgain();
            phone=GlobalVariables.getInstance().getRegisterPhone();
            pager.setAdapter(adapter2);
            tabs.setupWithViewPager(pager);
            pager.setCurrentItem(tabs.getSelectedTabPosition()+1);

        }
        else{
            pager.setAdapter(adapter);
            tabs.setupWithViewPager(pager);
        }
        StaticMethods.CheckConnection(this,mOnClickListener,getString(R.string.error_connection));

        myFragmentListener=new MyFragmentListener() {
            @Override
            public void onSwitchToNextFragment() {
                pager.setCurrentItem(1);
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
                    return RegFragment.newInstance(position);
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

    public class TabsPagerAdapter2 extends FragmentPagerAdapter {

        public TabsPagerAdapter2(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0)  return EnterFragment.newInstance(position,myFragmentListener);
            else
                return ConfirmFragment.newInstance(position);
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
