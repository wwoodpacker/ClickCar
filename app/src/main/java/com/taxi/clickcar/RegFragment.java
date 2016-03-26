package com.taxi.clickcar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Назар on 24.03.2016.
 */
public class RegFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    public View.OnClickListener mOnClickListener;

    static RegFragment newInstance(int page) {
        RegFragment regFragment = new RegFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        regFragment.setArguments(arguments);
        Log.d("reg", "newInstance");
        return regFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        Log.d("reg", "oncreateview");
        CheckConnection();

        View v = inflater.inflate(R.layout.fragment_reg,null);
        Button btn_reg= (Button)v.findViewById(R.id.btn_reg);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("reg", "click");
                MainActivity.setFl(true);
                Intent intent= new Intent(getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        return v;

    }
    public boolean CheckConnection() {

        ConnectivityManager conMan = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING || wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            return true;
        } else {
            Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "No connection", Snackbar.LENGTH_LONG)
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
}