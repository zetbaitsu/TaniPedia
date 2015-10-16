/*
 * Copyright (c) 2015 Zetra.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package id.zelory.tanipedia.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import id.zelory.benih.BenihActivity;
import id.zelory.benih.util.BenihPreferenceUtils;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.ui.MainActivity;
import id.zelory.tanipedia.ui.SignInActivity;

/**
 * Created by zetbaitsu on 7/24/15.
 */
public class SplashActivity extends BenihActivity implements Animation.AnimationListener
{
    private Animation animation1;
    private Animation animation2;
    private Animation animation3;
    private View view;

    @Override
    protected int getActivityView()
    {
        return R.layout.activity_splash;
    }

    @Override
    protected void onViewReady(Bundle bundle)
    {
        animation1 = AnimationUtils.loadAnimation(this, R.anim.simple_grow);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.simple_grow);
        animation3 = AnimationUtils.loadAnimation(this, R.anim.simple_grow);


        animation1.setAnimationListener(this);
        animation2.setAnimationListener(this);
        animation3.setAnimationListener(this);

        view = findViewById(R.id.iv_logo);
        view.startAnimation(animation1);
    }

    @Override
    public void onAnimationStart(Animation animation)
    {

    }

    @Override
    public void onAnimationEnd(Animation animation)
    {
        if (animation.equals(animation1))
        {
            view.setVisibility(View.VISIBLE);
            view = findViewById(R.id.tv_logo);
            view.startAnimation(animation2);
        } else if (animation.equals(animation2))
        {
            view.setVisibility(View.VISIBLE);
            view = findViewById(R.id.zelory);
            view.startAnimation(animation3);
        } else
        {
            view.setVisibility(View.VISIBLE);
            Intent intent;
            if (BenihPreferenceUtils.getString(this, "nama") != null)
            {
                intent = new Intent(this, MainActivity.class);

            } else
            {
                intent = new Intent(this, SignInActivity.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation)
    {

    }
}
