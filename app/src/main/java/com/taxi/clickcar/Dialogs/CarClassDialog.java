package com.taxi.clickcar.Dialogs;


import android.content.res.Resources;
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
import android.widget.TextView;

import com.example.pickerclickcar.time.RadialPickerLayout;
import com.taxi.clickcar.R;

/**
 * Created by Назар on 11.05.2016.
 */
public class CarClassDialog extends DialogFragment {

    private OnCarDialogListener mCallback;
    private Button mDoneButton;
    private Button mCancelButton;
    private ImageView mStandart,mPremium,mUniversal,mMicroBus;
    private boolean mCheckStandart=false,mCheckPremium=false,mCheckUniversal=false,mCheckMicrobus=false;

    public CarClassDialog(){};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("carClassDialog","onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getDialog()==null) {return;}
        getDialog().getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("carClassDialog","onCreateView");
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.car_class_dialog, null);
        mStandart=(ImageView)view.findViewById(R.id.im11);
        mPremium=(ImageView)view.findViewById(R.id.im22);
        mUniversal=(ImageView)view.findViewById(R.id.im33);
        mMicroBus=(ImageView)view.findViewById(R.id.im44);
        //******************************************************************************************
        mDoneButton=(Button)view.findViewById(R.id.done_button);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCallback != null) {
                    mCallback.onCarClassSet(mCheckStandart,mCheckPremium,mCheckUniversal,mCheckMicrobus);
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
        mStandart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckStandart){
                    dropAll();
                } else {
                    mCheckStandart = true;
                    mCheckPremium=false;
                    mCheckUniversal=false;
                    mCheckMicrobus=false;
                }
                changeImg();
            }
        });
        mPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckPremium){
                    dropAll();
                } else {
                    mCheckStandart = false;
                    mCheckPremium=true;
                    mCheckUniversal=false;
                    mCheckMicrobus=false;
                }
                changeImg();
            }
        });
        mUniversal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckUniversal){
                    dropAll();
                } else {
                    mCheckStandart = false;
                    mCheckPremium=false;
                    mCheckUniversal=true;
                    mCheckMicrobus=false;
                }
                changeImg();
            }
        });
        mMicroBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckMicrobus){
                    dropAll();
                } else {
                    mCheckStandart = false;
                    mCheckPremium=false;
                    mCheckUniversal=false;
                    mCheckMicrobus=true;
                }
                changeImg();
            }
        });
        //******************************************************************************************



        return view;
    }
    public static CarClassDialog newInstance(OnCarDialogListener callback) {
        CarClassDialog ret = new CarClassDialog();
        ret.initialize(callback);
        return ret;
    }
    public void initialize(OnCarDialogListener callback) {
        mCallback = callback;

    }
    public interface OnCarDialogListener {
       void onCarClassSet(boolean standart,boolean premium,boolean universal,boolean microbus);
    }
    public void setOnCarDialogListener(OnCarDialogListener callback) {
        mCallback = callback;
    }

    public void changeImg(){
        //Resources res = getResources();
        if (mCheckStandart) mStandart.setImageResource(R.mipmap.icon_plus); else mStandart.setImageResource(R.mipmap.ic_minus);
        if (mCheckPremium) mPremium.setImageResource(R.mipmap.icon_plus);   else mPremium.setImageResource(R.mipmap.ic_minus);
        if (mCheckUniversal) mUniversal.setImageResource(R.mipmap.icon_plus); else mUniversal.setImageResource(R.mipmap.ic_minus);
        if (mCheckMicrobus) mMicroBus.setImageResource(R.mipmap.icon_plus);   else mMicroBus.setImageResource(R.mipmap.ic_minus);

    }
    public void dropAll(){
        mCheckStandart=false;
        mCheckPremium=false;
        mCheckUniversal=false;
        mCheckMicrobus=false;
    }
}
