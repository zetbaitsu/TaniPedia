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

package id.zelory.tanipedia;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.benih.BenihActivity;
import id.zelory.tanipedia.controller.PakTaniController;

public class LoginActivity extends BenihActivity implements PakTaniController.Presenter
{
    @Bind(R.id.email) EditText editEmail;
    @Bind(R.id.password) EditText editPass;
    private PakTaniController controller;
    private MaterialDialog dialog;

    @Override
    protected int getActivityView()
    {
        return R.layout.activity_login;
    }

    @Override
    protected void onViewReady(Bundle bundle)
    {
        controller = new PakTaniController(this);
        loadAnimation();
        setUpDialog();
    }

    private void loadAnimation()
    {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);
        ButterKnife.findById(this, R.id.tanipedia).startAnimation(animation);
        ButterKnife.findById(this, R.id.card).startAnimation(animation);
        ButterKnife.findById(this, R.id.login).startAnimation(animation);
        ButterKnife.findById(this, R.id.register).startAnimation(animation);
    }

    @OnClick(R.id.login)
    void loginClick()
    {
        if (editEmail.getText().toString().isEmpty())
        {
            Snackbar.make(editEmail, "Mohon isi email terlebih dahulu!", Snackbar.LENGTH_LONG).show();
        } else if (editPass.getText().toString().isEmpty())
        {
            Snackbar.make(editPass, "Mohon isi password terlebih dahulu!", Snackbar.LENGTH_LONG).show();
        } else
        {
            controller.login(editEmail.getText().toString(), editPass.getText().toString());
        }
    }

    @OnClick(R.id.register)
    void registerClick()
    {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void setUpDialog()
    {
        dialog = new MaterialDialog.Builder(this)
                .title("TaniPedia")
                .content("Mencoba login...")
                .progress(true, 0)
                .build();
    }

    @Override
    public void showLoading()
    {
        dialog.show();
    }

    @Override
    public void dismissLoading()
    {
        dialog.dismiss();
    }

    @Override
    public void onLoginSuccess()
    {
        Intent intent = new Intent(this, CuacaActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onRegisterSuccess()
    {

    }

    @Override
    public void showError(Throwable throwable)
    {
        Snackbar.make(editPass, throwable.getMessage(), Snackbar.LENGTH_LONG).show();
    }
}
