package com.taxi.clickcar.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.taxi.clickcar.Adapters.HistoryAdapter;
import com.taxi.clickcar.Order.History;
import com.taxi.clickcar.R;
import com.taxi.clickcar.StaticMethods;
import com.taxi.clickcar.Tasks.HistoryTask;

import java.util.ArrayList;

/**
 * Created by Назар on 26.03.2016.
 */
public class FragmentHistory extends Fragment {


    public FragmentHistory(){};
    public ListView listView;
    public ArrayList<History> historyarray;
    HistoryAdapter dataAdapter = null;
    public static boolean loadingMore = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_history,null);
        listView=(ListView)view.findViewById(R.id.list);
        historyarray = new ArrayList<History>();
        dataAdapter =new HistoryAdapter(getContext(),getFragmentManager());
        setLoading(false);
        listView.setAdapter(dataAdapter);


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                Log.e("FragmentHistory", Boolean.toString(loadingMore));
                Log.e("FragmentHistory", Integer.toString(lastInScreen));
                Log.e("FragmentHistory", Integer.toString(firstVisibleItem));
                Log.e("FragmentHistory", Integer.toString(visibleItemCount));
                Log.e("FragmentHistory", Integer.toString(totalItemCount));
                if (lastInScreen == totalItemCount) {
                    if (!loadingMore) {
                        String url = "?offset=" + Integer.toString(totalItemCount);
                        grabURL(url);
                    }
                }
            }
        });

        Log.e("FragmentHistory","onCreateView");
        return view;
    }
    public static void setLoading(boolean load){
        loadingMore=load;
    }
    public void grabURL(String url) {
        Log.e("FragmentHistory","grabUrl");
        if (StaticMethods.CheckConnection(getActivity(),mOnClickListener,getString(R.string.error_connection))){
        setLoading(true);
         HistoryTask historyTask=new HistoryTask(dataAdapter);
        historyTask.setContext(getContext());
        historyTask.execute(url);}
    }
    public View.OnClickListener mOnClickListener;

}
