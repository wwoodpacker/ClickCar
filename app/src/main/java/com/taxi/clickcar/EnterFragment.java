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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.taxi.clickcar.Tasks.AutorizationTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by Назар on 24.03.2016.
 */
public class EnterFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    public View.OnClickListener mOnClickListener;

    static EnterFragment newInstance(int page) {
        EnterFragment enterFragment = new EnterFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        enterFragment.setArguments(arguments);
        Log.d("enter", "newInstance");
        return enterFragment;

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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("enter", "oncreateview");
        View view = inflater.inflate(R.layout.fragment_enter, null);
        Button btn1 = (Button) view.findViewById(R.id.btn_enter);
        final EditText sign_login = (EditText) view.findViewById(R.id.edit_phone);
        final EditText sign_pass = (EditText) view.findViewById(R.id.edit_pass);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckConnection()) {
                    String login = sign_login.getText().toString();
                    String password = sign_pass.getText().toString();

                    if (login.length() <= 0) {
                        Toast.makeText(getContext(), "Please enter login", Toast.LENGTH_SHORT).show();
                    } else if (password.length() <= 0) {
                        Toast.makeText(getContext(), "Please enter password", Toast.LENGTH_SHORT).show();
                    } else {
                        AutorizationTask task = new AutorizationTask(getContext());
                        String str = "";
                        try {
                            String hashPass = MainActivity.hashText(password);
                            task.execute(login, hashPass);
                            str = task.get();
                            JSONObject obj = new JSONObject(str);
                            Log.e("Auth JSON:", str);
                            Intent intent = new Intent(getContext(), ActivityDrawer.class);
                            intent.putExtra("LOGIN", login);
                            intent.putExtra("PASSWORD", hashPass);
                            startActivity(intent);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
        return view;


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