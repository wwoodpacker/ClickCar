package com.taxi.clickcar.Intro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.taxi.clickcar.ActivityDrawer;
import com.taxi.clickcar.GlobalVariables;
import com.taxi.clickcar.MainActivity;
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
 * Created by Назар on 26.03.2016.
 */
public class ThirdFragment extends Fragment {
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LOGIN="LOGIN";
    public static final String APP_PREFERENCES_PASS="PASS";
    public static final String APP_PREFERENCES_CREDIALS="CREDIALS";
    public static final String APP_PREFERENCES_REMEMBER="REMEMBER";
    public View.OnClickListener mOnClickListener;
    public SharedPreferences mSettings;
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    private static boolean isVisible = false;
    private static boolean doneVisible = false;
    ImageView image_intro3;
    ImageView image_intro4;
    Button btn_skip;
    public String hashPass="";
    public String login="";
    public  String str;
    int pageNumber;

    Animation animation1;
    Animation animation2;



    static ThirdFragment newInstance(int page) {
        ThirdFragment thirdFragment = new ThirdFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        thirdFragment.setArguments(arguments);
        Log.d("333", "newInstance");
        return thirdFragment;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        Log.d("333", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intro3, null);
        btn_skip = (Button)view.findViewById(R.id.btn_main);

        Log.d("333", "onCreateView");
        TextView t1=(TextView)view.findViewById(R.id.textView6);
        TextView t2=(TextView)view.findViewById(R.id.textView7);
        t1.setGravity(Gravity.CENTER);
        t2.setGravity(Gravity.CENTER);
        image_intro3=(ImageView)view.findViewById(R.id.imageintro3);
        image_intro4=(ImageView)view.findViewById(R.id.imageintro33);
        Glide.with(getContext()).load(R.drawable.pages_intro3).into(image_intro3);
        Glide.with(getContext()).load(R.drawable.hand_intro3).into(image_intro4);

        image_intro3.setVisibility(View.GONE);
        image_intro4.setVisibility(View.GONE);
        btn_skip.setVisibility(View.GONE);
        animation1 = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.anim_lefthand2);
        animation2 = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.anim_righthand1);
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StaticMethods.CheckConnection(getActivity(), mOnClickListener, getString(R.string.error_connection))) {
                    mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, getActivity().MODE_PRIVATE);
                    if (mSettings.contains(APP_PREFERENCES_REMEMBER)) {
                        if (mSettings.getBoolean(APP_PREFERENCES_REMEMBER, false)) {
                            if (mSettings.contains(APP_PREFERENCES_LOGIN)) {
                                login = mSettings.getString(APP_PREFERENCES_LOGIN, "");
                            }
                            if (mSettings.contains(APP_PREFERENCES_PASS)) {
                                hashPass = mSettings.getString(APP_PREFERENCES_PASS, "");
                            }

                            AuthRequest userAuthRequest = new AuthRequest(login, hashPass);
                            WebOrdersApiInterface apiService = ApiClient.getClient().create(WebOrdersApiInterface.class);
                            Call<AuthResponse> call = apiService.loadAuth(userAuthRequest);
                            call.enqueue(new Callback<AuthResponse>() {
                                @Override
                                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                                    if (response.code() == 200) {
                                        AuthResponse user = response.body();
                                        Intent intent = new Intent(getContext(), ActivityDrawer.class);
                                        GlobalVariables.getInstance().setName(user.getUserFullName());
                                        GlobalVariables.getInstance().setPhone(user.getUserPhone());
                                        intent.putExtra("LOGIN", login);
                                        intent.putExtra("PASSWORD", hashPass);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getContext(), getString(R.string.wrong_phone), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<AuthResponse> call, Throwable t) {

                                }
                            });
                        } else {
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                        }

                    } else {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setViewAnimation(){
        image_intro3.setVisibility(View.VISIBLE);
        image_intro4.setVisibility(View.VISIBLE);
        btn_skip.setVisibility(View.VISIBLE);

        image_intro3.setAnimation(animation1);
        image_intro4.setAnimation(animation2);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            setViewAnimation();

            Log.d("333", "visible to user");

        } else {
            Log.d("333", "visible not to user");
            isVisible = false;
        }
    }


}
