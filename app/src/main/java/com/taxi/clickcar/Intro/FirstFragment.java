package com.taxi.clickcar.Intro;


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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.taxi.clickcar.R;

/**
 * Created by Назар on 23.03.2016.
 */
public class FirstFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    private static boolean isVisible = false;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    int pageNumber;

    Animation animation2;
    Animation animation3;
    Animation animation4;


    static FirstFragment newInstance(int page) {
        FirstFragment firstFragment = new FirstFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        firstFragment.setArguments(arguments);
        Log.d("111", "newInstance");
        return firstFragment;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        Log.d("111", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.intro, null);
        Log.d("111", "onCreateView");
        TextView t1=(TextView)view.findViewById(R.id.textView6);
        TextView t2=(TextView)view.findViewById(R.id.textView7);
        t1.setGravity(Gravity.CENTER);
        t2.setGravity(Gravity.CENTER);
        if(isVisible) {

            Log.d("111", "anim");
            imageView2 = (ImageView) view.findViewById(R.id.imageViewLHand);
            imageView3 = (ImageView) view.findViewById(R.id.imageViewRHand);
            imageView4 = (ImageView) view.findViewById(R.id.imageViewMetka);

            Glide.with(getContext()).load(R.drawable.left__hand).animate(R.anim.anim_lefthand1).into(imageView2);
            Glide.with(getContext()).load(R.drawable.right_hand_2).animate(R.anim.anim_righthand1).into(imageView3);
            Glide.with(getContext()).load(R.drawable.metka).animate(R.anim.anim_metka).into(imageView4);

            //animation2 = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.anim_lefthand1);
          //  animation3 = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.anim_righthand1);
          //  animation4 = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.anim_metka);

          //  imageView2.setAnimation(animation2);
           // imageView3.setAnimation(animation3);
           // imageView4.setAnimation(animation4);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.d("111", "visible to user");
            isVisible = true;
        } else {
            Log.d("111", "visible not to user");
            isVisible = false;
        }
    }


}
