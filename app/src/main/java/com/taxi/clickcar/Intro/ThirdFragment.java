package com.taxi.clickcar.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.taxi.clickcar.MainActivity;
import com.taxi.clickcar.R;

/**
 * Created by Назар on 26.03.2016.
 */
public class ThirdFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    private static boolean isVisible = false;
    private static boolean doneVisible = false;
    ImageView image_intro3;
    ImageView image_intro4;
    Button btn_skip;

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


        image_intro3.setVisibility(View.GONE);
        image_intro4.setVisibility(View.GONE);
        btn_skip.setVisibility(View.GONE);
        animation1 = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.anim_lefthand2);
        animation2 = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.anim_righthand1);
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
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
