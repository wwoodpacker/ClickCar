package com.taxi.clickcar.Intro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro2;
import com.taxi.clickcar.MainActivity;
import com.taxi.clickcar.R;

/**
 * Created by Назар on 11.03.2016.
 */
public class ClickIntro extends AppIntro2 {
    @Override
    public void init(Bundle savedInstanceState) {
        /*SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if(pref.getBoolean("activity_executed", false)){
            loadMainActivity();
           } else {
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", true);
            ed.commit();

        }*/

        addSlide(ClickSlide.newInstance(R.layout.intro));
        addSlide(ClickSlide.newInstance(R.layout.intro2));
        addSlide(ClickSlide.newInstance(R.layout.intro3));
        setFadeAnimation();
        setIndicatorColor(R.color.process_active, R.color.process_notactive);
    }


    @Override
    public void onNextPressed() {

    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

    @Override
    public void onSlideChanged() {

    }
    private void loadMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
