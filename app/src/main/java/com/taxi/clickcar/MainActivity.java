package com.taxi.clickcar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity {

    public Button btnReg;
    public Button btnOrder;
    public Button btnEnter;
    public EditText sign_login;
    public EditText sign_pass;
    public View.OnClickListener mOnClickListener;
    public UserRegistratonProfile userRegistratonProfile;
    public static String name="";
    public static String password="";
    public static String password_again="";
    public static String phone="";
    public String hashPass="";
    public static boolean fl=false;
    ViewPager pager;
    PagerAdapter pagerAdapter;
    public static void setFl(boolean _fl){fl=_fl;}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setBackgroundResource(R.color.mainblue);
        tabs.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager());

        final TabsPagerAdapter2 adapter2 = new TabsPagerAdapter2(getSupportFragmentManager());

        if (fl) {

            userRegistratonProfile = (UserRegistratonProfile)getIntent().getParcelableExtra(
                    UserRegistratonProfile.class.getCanonicalName());
            name=userRegistratonProfile.name.toString();
            password=userRegistratonProfile.pass.toString();
            password_again=userRegistratonProfile.pass_again.toString();
            phone=userRegistratonProfile.phone.toString();
           pager.setAdapter(adapter2);

           tabs.setupWithViewPager(pager);
            pager.setCurrentItem(tabs.getSelectedTabPosition()+1);

        }
        else{
            pager.setAdapter(adapter);
            tabs.setupWithViewPager(pager);
        }
        CheckConnection();



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
          if(position==0)  return EnterFragment.newInstance(position);
            else
                    return RegFragment.newInstance(position);
                        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position==0)  return "Вход";
            else return "Регистрация";
        }
    }

    public class TabsPagerAdapter2 extends FragmentPagerAdapter {

        public TabsPagerAdapter2(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0)  return EnterFragment.newInstance(position);
            else
                return ConfirmFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position==0)  return "Вход";
            else return "Регистрация";
        }
    }
    public void setmOnClickListener(View.OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public boolean CheckConnection(){

        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING||wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            return true;
        } else  {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"No connection",Snackbar.LENGTH_LONG)
                    .setAction("Hide", mOnClickListener);
            snackbar.setActionTextColor(Color.RED);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.DKGRAY);
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
            return false;
        }

    }

   /* public void OnClick(View v){
        switch (v.getId()){
            case R.id.btn_order:
                if (CheckConnection()){

                }
                break;
            case R.id.btn_register:
                if (CheckConnection()) {
                    Intent intent = new Intent(this, Register.class);
                    startActivity(intent);
                }
                break;
            case R.id.btn_sing_in:
                if (CheckConnection()) {
                    login = sign_login.getText().toString();
                    password = sign_pass.getText().toString();

                    if (login.length() <= 0) {
                        Toast.makeText(this, "Please enter login", Toast.LENGTH_SHORT).show();
                    } else if (password.length() <= 0) {
                        Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
                    } else {
                       AutorizationTask task = new AutorizationTask(this);
                        String str = "";
                        try {
                            hashPass=hashText(password);
                            task.execute(login, hashPass);
                            str = task.get();
                            JSONObject obj = new JSONObject(str);
                            Log.e("Auth JSON:", str);
                            Intent intent= new Intent(this,Order.class);
                            intent.putExtra("LOGIN",login);
                            intent.putExtra("PASSWORD",hashPass);
                            startActivity(intent);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
                break;

        }

    }*/

    public  static String convertByteToHex(byte data[])
    {
        StringBuffer hexData = new StringBuffer();
        for (int byteIndex = 0; byteIndex < data.length; byteIndex++)
            hexData.append(Integer.toString((data[byteIndex] & 0xff) + 0x100, 16).substring(1));

        return hexData.toString();
    }

    public static String hashText(String textToHash) throws Exception
    {
        final MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
        sha512.update(textToHash.getBytes());

        return convertByteToHex(sha512.digest());
    }

 }
