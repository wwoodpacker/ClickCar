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
public class LanguageDialog extends DialogFragment {

    private OnLanguageDialogListener mCallback;
    private Button mDoneButton;
    private Button mCancelButton;
    private ImageView mRus,mUk,mEng;
    private static boolean mCheckRus=false;
    private static boolean mCheckUk=false;
    private static boolean mCheckEng=false;

    public LanguageDialog(){};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("LanguageDialog","onCreate");
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
        Log.e("LanguageDialog","onCreateView");
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.language_dialog, null);
        mRus=(ImageView)view.findViewById(R.id.im11);
        mUk=(ImageView)view.findViewById(R.id.im22);
        mEng=(ImageView)view.findViewById(R.id.im33);
        changeImg();
        //******************************************************************************************
        mDoneButton=(Button)view.findViewById(R.id.done_button);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCallback != null) {
                    mCallback.onLanguageSet(mCheckRus,mCheckUk,mCheckEng);
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
        mRus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckRus){
                    dropAll();
                } else {
                    mCheckRus = true;
                    mCheckUk=false;
                    mCheckEng=false;

                }
                changeImg();
            }
        });
        mUk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckUk){
                    dropAll();
                } else {
                    mCheckRus = false;
                    mCheckUk=true;
                    mCheckEng=false;

                }
                changeImg();
            }
        });
        mEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckEng){
                    dropAll();
                } else {
                    mCheckRus = false;
                    mCheckUk=false;
                    mCheckEng=true;

                }
                changeImg();
            }
        });

        //******************************************************************************************



        return view;
    }
    public static LanguageDialog newInstance(OnLanguageDialogListener callback,boolean rus,boolean uk,boolean eng) {
        LanguageDialog ret = new LanguageDialog();
        mCheckRus=rus;
        mCheckUk=uk;
        mCheckEng=eng;
        ret.initialize(callback);
        return ret;
    }
    public void initialize(OnLanguageDialogListener callback) {
        mCallback = callback;

    }
    public interface OnLanguageDialogListener {
        void onLanguageSet(boolean rus,boolean uk,boolean eng);
    }
    public void setOnLanguageDialogListener(OnLanguageDialogListener callback) {
        mCallback = callback;
    }

    public void changeImg(){
        //Resources res = getResources();
        if (mCheckRus) mRus.setImageResource(R.mipmap.icon_plus); else mRus.setImageResource(R.mipmap.ic_minus);
        if (mCheckUk) mUk.setImageResource(R.mipmap.icon_plus);   else mUk.setImageResource(R.mipmap.ic_minus);
        if (mCheckEng) mEng.setImageResource(R.mipmap.icon_plus); else mEng.setImageResource(R.mipmap.ic_minus);


    }
    public void dropAll(){
        mCheckRus=false;
        mCheckUk=false;
        mCheckEng=false;

    }
}

