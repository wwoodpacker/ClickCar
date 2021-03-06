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
 * Created by Назар on 12.05.2016.
 */
public class DoplnDialog extends DialogFragment {

    private OnDoplnDialogListener mCallback;
    private Button mDoneButton;
    private Button mCancelButton;
    private ImageView mBaggage,mAnimal,mCondition,mCurier;
    private boolean mCheckBaggage=false,mCheckAnimal=false,mCheckCondition=false,mCheckCurier=false;

    public DoplnDialog(){};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("DoplnDialog","onCreate");
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
        Log.e("DoplnDialog","onCreateView");
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.dopoln_dialog, null);
        mBaggage=(ImageView)view.findViewById(R.id.im11);
        mAnimal=(ImageView)view.findViewById(R.id.im22);
        mCondition=(ImageView)view.findViewById(R.id.im33);
        mCurier=(ImageView)view.findViewById(R.id.im44);
        //******************************************************************************************
        mDoneButton=(Button)view.findViewById(R.id.done_button);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCallback != null) {
                    mCallback.onDoplnSet(mCheckBaggage,mCheckAnimal,mCheckCondition,mCheckCurier);
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
        mBaggage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckBaggage){
                    mCheckBaggage=false;
                } else {
                    mCheckBaggage = true;
                }
                changeImg();
            }
        });
        mAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckAnimal){
                    mCheckAnimal=false;
                } else {
                    mCheckAnimal=true;
                }
                changeImg();
            }
        });
        mCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckCondition){
                    mCheckCondition=false;
                } else {
                    mCheckCondition=true;
                }
                changeImg();
            }
        });
        mCurier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckCurier){
                    mCheckCurier=false;
                } else {
                    mCheckCurier=true;
                }
                changeImg();
            }
        });
        //******************************************************************************************



        return view;
    }
    public static DoplnDialog newInstance(OnDoplnDialogListener callback) {
        DoplnDialog ret = new DoplnDialog();
        ret.initialize(callback);
        return ret;
    }
    public void initialize(OnDoplnDialogListener callback) {
        mCallback = callback;

    }
    public interface OnDoplnDialogListener {
        void onDoplnSet(boolean baggage,boolean animal,boolean condition,boolean curier);
    }
    public void setOnDoplnDialogListener(OnDoplnDialogListener callback) {
        mCallback = callback;
    }

    public void changeImg(){
        //Resources res = getResources();
        if (mCheckBaggage) mBaggage.setImageResource(R.mipmap.icon_plus); else mBaggage.setImageResource(R.mipmap.ic_minus);
        if (mCheckAnimal) mAnimal.setImageResource(R.mipmap.icon_plus);   else mAnimal.setImageResource(R.mipmap.ic_minus);
        if (mCheckCondition) mCondition.setImageResource(R.mipmap.icon_plus); else mCondition.setImageResource(R.mipmap.ic_minus);
        if (mCheckCurier) mCurier.setImageResource(R.mipmap.icon_plus);   else mCurier.setImageResource(R.mipmap.ic_minus);

    }
    public void dropAll(){
        mCheckBaggage=false;
        mCheckAnimal=false;
        mCheckCondition=false;
        mCheckCurier=false;
    }
}
