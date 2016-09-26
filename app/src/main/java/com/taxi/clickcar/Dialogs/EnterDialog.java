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
public class EnterDialog extends DialogFragment {

    private OnEnterDialogListener mCallback;
    private Button mDoneButton;
    private Button mCancelButton;
    private ImageView mRemember;
    private static boolean mCheckRemember=false;

    public EnterDialog(){};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("EnterDialog","onCreate");
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
        Log.e("EnterDialog","onCreateView");
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.entersettings_dialog, null);
        mRemember=(ImageView)view.findViewById(R.id.im11);
        changeImg();
        //******************************************************************************************
        mDoneButton=(Button)view.findViewById(R.id.done_button);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCallback != null) {
                    mCallback.onEnterSet(mCheckRemember);
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
        mRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckRemember){
                    mCheckRemember=false;
                } else {
                    mCheckRemember = true;

                }
                changeImg();
            }
        });

        //******************************************************************************************



        return view;
    }
    public static EnterDialog newInstance(OnEnterDialogListener callback,boolean remember) {
        EnterDialog ret = new EnterDialog();
        mCheckRemember=remember;
        ret.initialize(callback);
        return ret;
    }
    public void initialize(OnEnterDialogListener callback) {
        mCallback = callback;

    }
    public interface OnEnterDialogListener {
        void onEnterSet(boolean remember);
    }
    public void setOnEnterDialogListener(OnEnterDialogListener callback) {
        mCallback = callback;
    }

    public void changeImg(){
        //Resources res = getResources();
        if (mCheckRemember) mRemember.setImageResource(R.mipmap.icon_plus); else mRemember.setImageResource(R.mipmap.ic_minus);

    }

}

