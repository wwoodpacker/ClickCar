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
import android.widget.EditText;
import android.widget.ImageView;

import com.taxi.clickcar.R;

/**
 * Created by Назар on 05.06.2016.
 */
public class SendDialog extends DialogFragment {

    private OnSendDialogListener mCallback;
    private Button mDoneButton;
    private Button mCancelButton;
    private EditText mMailText;
    private static String mText="";

    public SendDialog(){};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("SendDialog","onCreate");
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
        Log.e("SendDialog","onCreateView");
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.send_dialog, null);
        mMailText=(EditText) view.findViewById(R.id.txt_massage);

        //******************************************************************************************
        mDoneButton=(Button)view.findViewById(R.id.done_button);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mText=mMailText.getText().toString();

                if (mCallback != null) {
                    mCallback.onEnterSet(mText);
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


        //******************************************************************************************



        return view;
    }
    public static SendDialog newInstance(OnSendDialogListener callback) {
        SendDialog ret = new SendDialog();

        ret.initialize(callback);
        return ret;
    }
    public void initialize(OnSendDialogListener callback) {
        mCallback = callback;

    }
    public interface OnSendDialogListener {
        void onEnterSet(String text);
    }
    public void setOnSendDialogListener(OnSendDialogListener callback) {
        mCallback = callback;
    }

}
