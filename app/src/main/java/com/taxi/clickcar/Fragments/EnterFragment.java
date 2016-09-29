package com.taxi.clickcar.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.leo.simplearcloader.SimpleArcDialog;
import com.taxi.clickcar.ActivityDrawer;
import com.taxi.clickcar.GlobalVariables;
import com.taxi.clickcar.R;
import com.taxi.clickcar.Requests.AuthRequest;
import com.taxi.clickcar.Responses.AuthResponse;
import com.taxi.clickcar.StaticMethods;
import com.taxi.clickcar.WebOrdersAPI.ApiClient;
import com.taxi.clickcar.WebOrdersAPI.WebOrdersApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Назар on 24.03.2016.
 */
public class EnterFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LOGIN="LOGIN";
    public static final String APP_PREFERENCES_PASS="PASS";
    public SimpleArcDialog mDialog;
    public SharedPreferences mSettings;
    public View.OnClickListener mOnClickListener;
    public String hashPass = "";
    public String login="";
    public static MyFragmentListener myFragmentListener=null;

    public static EnterFragment newInstance(int page, MyFragmentListener _listener) {
        EnterFragment enterFragment = new EnterFragment();
        myFragmentListener=_listener;
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        enterFragment.setArguments(arguments);
        Log.d("enter", "newInstance");
        return enterFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialog= StaticMethods.getArcDialog(getContext(),getContext().getString(R.string.auth));

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("enter", "oncreateview");

        final View view = inflater.inflate(R.layout.fragment_enter, null);
        Button btn1 = (Button) view.findViewById(R.id.btn_enter);
        Button btn2 = (Button) view.findViewById(R.id.btn_order);
        final EditText sign_login = (EditText) view.findViewById(R.id.edit_phone);
        final EditText sign_pass = (EditText) view.findViewById(R.id.edit_pass);
        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, getActivity().MODE_PRIVATE);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (StaticMethods.CheckConnection(getActivity(),mOnClickListener,getString(R.string.error_connection))) {
                    login = sign_login.getText().toString();
                    String password = sign_pass.getText().toString();

                    if (login.length() <= 0) {
                        Toast.makeText(getContext(), getString(R.string.error_login), Toast.LENGTH_SHORT).show();
                    } else if (password.length() <= 0) {
                        Toast.makeText(getContext(), getString(R.string.error_pass), Toast.LENGTH_SHORT).show();
                    }
                    try {
                        hashPass = StaticMethods.hashText(password);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mDialog.show();
                    AuthRequest userAuthRequest = new AuthRequest(login,hashPass);
                    WebOrdersApiInterface apiService= ApiClient.getClient().create(WebOrdersApiInterface.class);
                    Call<AuthResponse> call= apiService.loadAuth(userAuthRequest);
                    call.enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.code()==200) {
                            AuthResponse user = response.body();
                            Intent intent = new Intent(getContext(), ActivityDrawer.class);
                            GlobalVariables.getInstance().setName(user.getUserFullName());
                            GlobalVariables.getInstance().setPhone(user.getUserPhone());
                            SharedPreferences.Editor editor = mSettings.edit();
                            editor.putString(APP_PREFERENCES_LOGIN, login);
                            editor.putString(APP_PREFERENCES_PASS, hashPass);
                            editor.apply();
                            intent.putExtra("LOGIN", login);
                            intent.putExtra("PASSWORD", hashPass);
                            mDialog.dismiss();
                            startActivity(intent);
                        }else{
                            mDialog.dismiss();
                            Toast.makeText(getContext(), getString(R.string.wrong_phone), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        mDialog.dismiss();
                        Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
               }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFragmentListener.onSwitchToNextFragment();
            }
        });
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("enterFragment","OnDestroy");
    }
}