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
 * Created by Назар on 15.05.2016.
 */
public class OplataDialog  extends DialogFragment {

    private OnOplataDialogListener mCallback;
    private Button mDoneButton;
    private Button mCancelButton;
    private ImageView mCash,mNoCash;
    private boolean mCheckCash=false,mCheckNoCash=false;

    public OplataDialog(){};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("OplataDialog","onCreate");
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
        Log.e("OplataDialog","onCreateView");
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.oplata_dialog, null);
        mCash=(ImageView)view.findViewById(R.id.im11);
        mNoCash=(ImageView)view.findViewById(R.id.im22);
        //******************************************************************************************
        mDoneButton=(Button)view.findViewById(R.id.done_button);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCallback != null) {
                    mCallback.onOplataSet(mCheckCash,mCheckNoCash);
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
        mCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckCash){
                    mCheckCash=false;
                    mCheckNoCash=true;
                } else {
                    mCheckCash = true;
                    mCheckNoCash=false;
                }
                changeImg();
            }
        });
        mNoCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckNoCash){
                    mCheckCash = true;
                    mCheckNoCash=false;
                } else {
                    mCheckCash=false;
                    mCheckNoCash=true;
                }
                changeImg();
            }
        });
        //******************************************************************************************



        return view;
    }
    public static OplataDialog newInstance(OnOplataDialogListener callback) {
        OplataDialog ret = new OplataDialog();
        ret.initialize(callback);
        return ret;
    }
    public void initialize(OnOplataDialogListener callback) {
        mCallback = callback;

    }
    public interface OnOplataDialogListener {
        void onOplataSet(boolean cash,boolean nocash);
    }
    public void setOnOplataDialogListener(OnOplataDialogListener callback) {
        mCallback = callback;
    }

    public void changeImg(){
        //Resources res = getResources();
        if (mCheckCash) mCash.setImageResource(R.mipmap.icon_plus); else mCash.setImageResource(R.mipmap.ic_minus);
        if (mCheckNoCash) mNoCash.setImageResource(R.mipmap.icon_plus);   else mNoCash.setImageResource(R.mipmap.ic_minus);

    }
    public void dropAll(){
        mCheckCash=false;
        mCheckNoCash=false;

    }
}

