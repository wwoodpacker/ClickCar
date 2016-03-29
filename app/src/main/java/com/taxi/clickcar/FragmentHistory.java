package com.taxi.clickcar;


import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Назар on 26.03.2016.
 */
public class FragmentHistory extends Fragment {
    public FragmentHistory(){};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_history,null);
        Log.e("FragmentHistory","onCreateView");
        return view;
    }
}
