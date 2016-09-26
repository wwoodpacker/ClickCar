package com.taxi.clickcar.Dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.taxi.clickcar.R;

/**
 * Created by Назар on 05.06.2016.
 */
public class DopSettings extends DialogFragment {

    private OnDopSettingsListener mCallback;
    private Button mDoneButton;
    private Button mCancelButton;
    private ImageView mLocation,mPush,mShSms;
    private static boolean mCheckLocation=false,mCheckPush=false,mCheckShSms=false;

    public DopSettings(){};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("DopSettings","onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getDialog()==null) {return;}
        getDialog().getWindow().setLayout(500, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("DopSettings","onCreateView");
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.dopsettings_dialog, null);
        mLocation=(ImageView)view.findViewById(R.id.im11);
        mPush=(ImageView)view.findViewById(R.id.im22);
        mShSms=(ImageView)view.findViewById(R.id.im33);
        changeImg();
        //******************************************************************************************
        mDoneButton=(Button)view.findViewById(R.id.done_button);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCallback != null) {
                    mCallback.onDoplnSet(mCheckLocation,mCheckPush,mCheckShSms);
                }
                dismiss();
            }
        });
        //******************************************************************************************
        mCancelButton=(Button)view.findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //******************************************************************************************
        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckLocation){
                    mCheckLocation=false;
                } else {
                    mCheckLocation = true;
                }
                changeImg();
            }
        });
        mPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckPush){
                    mCheckPush=false;
                } else {
                    mCheckPush=true;
                }
                changeImg();
            }
        });
        mShSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckShSms){
                    mCheckShSms=false;
                } else {
                    mCheckShSms=true;
                }
                changeImg();
            }
        });

        //******************************************************************************************



        return view;
    }
    public static DopSettings newInstance(OnDopSettingsListener callback,boolean loc,boolean push,boolean shsms) {

        DopSettings ret = new DopSettings();
        mCheckLocation=loc;
        mCheckPush=push;
        mCheckShSms=shsms;
        ret.initialize(callback);
        return ret;
    }
    public void initialize(OnDopSettingsListener callback) {
        mCallback = callback;

    }
    public interface OnDopSettingsListener {
        void onDoplnSet(boolean loc,boolean push,boolean shsms);
    }
    public void setOnDopSettingsListener(OnDopSettingsListener callback) {
        mCallback = callback;
    }

    public void changeImg(){
        //Resources res = getResources();
        if (mCheckLocation) mLocation.setImageResource(R.mipmap.icon_plus); else mLocation.setImageResource(R.mipmap.ic_minus);
        if (mCheckPush) mPush.setImageResource(R.mipmap.icon_plus);   else mPush.setImageResource(R.mipmap.ic_minus);
        if (mCheckShSms) mShSms.setImageResource(R.mipmap.icon_plus); else mShSms.setImageResource(R.mipmap.ic_minus);


    }
    public void dropAll(){
        mCheckLocation=false;
        mCheckPush=false;
        mCheckShSms=false;

    }
}
