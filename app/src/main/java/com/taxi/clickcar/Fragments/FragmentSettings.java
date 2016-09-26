package com.taxi.clickcar.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pickerclickcar.time.TimePickerDialog;
import com.taxi.clickcar.ActivityDrawer;
import com.taxi.clickcar.Dialogs.DopSettings;
import com.taxi.clickcar.Dialogs.DoplnDialog;
import com.taxi.clickcar.Dialogs.EnterDialog;
import com.taxi.clickcar.Dialogs.LanguageDialog;
import com.taxi.clickcar.Dialogs.SendDialog;
import com.taxi.clickcar.Intro.ViewPagerActivity;
import com.taxi.clickcar.Mail.ExtendedMail;
import com.taxi.clickcar.R;
import com.taxi.clickcar.StaticMethods;

import java.util.Locale;

/**
 * Created by Назар on 26.03.2016.
 */
public class FragmentSettings extends Fragment {
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LOCATION="location";
    public static final String APP_PREFERENCES_PUSH="PUSH";
    public static final String APP_PREFERENCES_SMS="SHOWSMS";
    public static final String APP_PREFERENCES_REMEMBER="REMEMBER";
    public static final String APP_PREFERENCES_LOGIN="LOGIN";
    public static final String APP_PREFERENCES_PASS="PASS";
    public SharedPreferences mSettings;
    public LanguageDialog.OnLanguageDialogListener onLanguageSetListener=null;
    public DopSettings.OnDopSettingsListener onDopSettingsListener=null;
    public EnterDialog.OnEnterDialogListener onEnterDialogListener=null;
    public SendDialog.OnSendDialogListener onSendDialogListener=null;
    private Locale myLocale;
    public Button btn_language;
    public TextView txt_language,txt_about;
    public boolean rus=false,uk=false,eng=false;
    public boolean loc=false,push=false,shsms=false,remember=false;
    public FragmentSettings(){};
    public View.OnClickListener mOnClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_settings,null);
        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, getActivity().MODE_PRIVATE);


        txt_language=(TextView)view.findViewById(R.id.txt_language);
        txt_about=(TextView)view.findViewById(R.id.txt_about);
        btn_language=(Button)view.findViewById(R.id.btn_language);
        Button btn_entersettings=(Button)view.findViewById(R.id.btn_enter_settings);
        Button btn_dopsettings=(Button)view.findViewById(R.id.btn_dop_settings);
        Button btn_writedev=(Button)view.findViewById(R.id.btn_write_dev);
        Button btn_phonedev=(Button)view.findViewById(R.id.btn_phone_dev);
        //language
        loadLocale();
        //version
        txt_about.setText(loadVersion());
        //Settings
        readSettings();
        //Buttons
        btn_writedev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StaticMethods.CheckConnection(getActivity(),mOnClickListener,getString(R.string.error_connection))) {
                    SendDialog.newInstance(onSendDialogListener).show(getFragmentManager(), "SendDialog");
                }
            }
        });
        onSendDialogListener= new SendDialog.OnSendDialogListener() {
            @Override
            public void onEnterSet(String text) {

                ExtendedMail extendedMail= new ExtendedMail(getContext(),text, ActivityDrawer.LOGIN.toString());
                extendedMail.Run();
            }
        };

        btn_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               LanguageDialog.newInstance(onLanguageSetListener,rus,uk,eng).show(getFragmentManager(),"language");
            }
        });
        onLanguageSetListener=new LanguageDialog.OnLanguageDialogListener() {
            @Override
            public void onLanguageSet(boolean rus, boolean uk, boolean eng) {
                if (rus){changeLang("ru");}
                else
                if (uk){changeLang("uk");}
                else
                if (eng){changeLang("en");}
                Intent intent=new Intent(getContext(), ViewPagerActivity.class);
                startActivity(intent);
            }
        };

        btn_entersettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnterDialog.newInstance(onEnterDialogListener,remember).show(getFragmentManager(),"entersettings");
            }
        });
        onEnterDialogListener= new EnterDialog.OnEnterDialogListener() {
            @Override
            public void onEnterSet(boolean remember) {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putBoolean(APP_PREFERENCES_REMEMBER, remember);
                if(!remember) {
                    editor.remove(APP_PREFERENCES_LOGIN);
                    editor.remove(APP_PREFERENCES_PASS);
                }
                editor.apply();
            }
        };

        btn_dopsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DopSettings.newInstance(onDopSettingsListener,loc,push,shsms).show(getFragmentManager(),"dopsettings");
            }
        });
        onDopSettingsListener=new DopSettings.OnDopSettingsListener() {
            @Override
            public void onDoplnSet(boolean loc, boolean push, boolean shsms) {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putBoolean(APP_PREFERENCES_LOCATION, loc);
                editor.putBoolean(APP_PREFERENCES_PUSH, push);
                editor.putBoolean(APP_PREFERENCES_SMS, shsms);
                editor.apply();
            }
        };
        btn_phonedev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Intent.ACTION_CALL, Uri.parse("tel:+380631234567"));
                startActivity(in);
            }
        });
        Log.e("Fragmentsettings","onCreateView");
        return view;
}
    public void readSettings(){
        if(mSettings.contains(APP_PREFERENCES_LOCATION)) {
            loc=mSettings.getBoolean(APP_PREFERENCES_LOCATION, false);
        }
        if(mSettings.contains(APP_PREFERENCES_PUSH)) {
            push=mSettings.getBoolean(APP_PREFERENCES_PUSH, false);
        }
        if(mSettings.contains(APP_PREFERENCES_SMS)) {
            shsms=mSettings.getBoolean(APP_PREFERENCES_SMS, false);
        }
        if(mSettings.contains(APP_PREFERENCES_REMEMBER)) {
            remember=mSettings.getBoolean(APP_PREFERENCES_REMEMBER, false);
        }
    }
    public String loadVersion(){
        PackageInfo pInfo = null;
        String version="1.0";
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
    public void loadLocale()
    {
        String langPref = "Language";

        SharedPreferences prefs = getActivity().getSharedPreferences("CommonPrefs", getActivity().MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLang(language);
    }
    public void changeLang(String lang)
    {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
       updateTexts(lang);
    }
    public void saveLocale(String lang)
    {
        String langPref = "Language";
        SharedPreferences prefs = getActivity().getSharedPreferences("CommonPrefs", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }
    private void updateTexts(String lang)
    {
        if (lang.contains("en")){
            txt_language.setText(getString(R.string.eng));
            eng=true;
        }else
        if(lang.contains("ru")){
            txt_language.setText(getString(R.string.rus));
            rus=true;
        }
        else
        if(lang.contains("uk")){
            txt_language.setText(getString(R.string.ua));
            uk=true;
        }

    }
}
