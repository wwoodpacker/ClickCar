package com.taxi.clickcar.Intro;

import android.content.Intent;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;
import com.taxi.clickcar.R;

/**
 * Created by Назар on 11.03.2016.
 */
public class ClickIntro extends AppIntro2 {
    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(ClickSlide.newInstance(R.layout.intro));
        addSlide(ClickSlide.newInstance(R.layout.intro2));
        setFadeAnimation();
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
