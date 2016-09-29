package com.taxi.clickcar.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taxi.clickcar.R;

/**
 * Created by Назар on 29.09.2016.
 */
public class RootFragmentReg extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.root_fragment, container, false);

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
//ergister fragment
        transaction.replace(R.id.root_frame, new RegFragment());
        transaction.commit();
        return view;
    }
}
