package com.taxi.clickcar.Intro;

import android.os.Bundle;
import android.provider.ContactsContract;
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
public class SecondFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    private static boolean isVisible = false;
    private static boolean doneVisible = false;
    ImageView left_hand;
    ImageView right_hand;
    int pageNumber;

    Animation animation1;
    Animation animation2;


    static SecondFragment newInstance(int page) {
        SecondFragment secondFragment = new SecondFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        secondFragment.setArguments(arguments);
        Log.d("222", "newInstance");
        return secondFragment;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        Log.d("222", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intro2, null);
        Log.d("222", "onCreateView");
        left_hand=(ImageView)view.findViewById(R.id.imageViewLHand2);
        right_hand = (ImageView)view.findViewById(R.id.imageViewRHand2);
        TextView t1=(TextView)view.findViewById(R.id.textView6);
        TextView t2=(TextView)view.findViewById(R.id.textView7);
        t1.setGravity(Gravity.CENTER);
        t2.setGravity(Gravity.CENTER);
        Glide.with(getContext()).load(R.drawable.left_hand_).into(left_hand);
        Glide.with(getContext()).load(R.drawable.right_hand).into(right_hand);
        left_hand.setVisibility(View.GONE);
        right_hand.setVisibility(View.GONE);

        animation1 = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.anim_lefthand2);
        animation2 = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.anim_righthand2);

         return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("222", "view created2");
    }
    private void setViewAnimation(){
        left_hand.setVisibility(View.VISIBLE);
        right_hand.setVisibility(View.VISIBLE);
        left_hand.setAnimation(animation1);
        right_hand.setAnimation(animation2);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            setViewAnimation();
            Log.d("222", "visible to user");

        } else {
            Log.d("222", "visible not to user");
            isVisible = false;
        }
    }


}
