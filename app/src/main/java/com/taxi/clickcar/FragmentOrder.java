package com.taxi.clickcar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taxi.clickcar.Tasks.CostTask;

/**
 * Created by Назар on 22.04.2016.
 */
public class FragmentOrder extends Fragment {
    private CostTask costTask;

    public FragmentOrder(){}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_order,null);


        Log.e("FragmentOrder", "onCreateView");
        costTask=new CostTask(new MyCallBack() {
            @Override
            public void OnTaskDone(String result) {
                Log.e("CoastJson:",result);
            }
        });
        costTask.setContext(getContext());
        costTask.execute("");
        return view;
    }
}
