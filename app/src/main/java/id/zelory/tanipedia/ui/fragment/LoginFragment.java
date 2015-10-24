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

package id.zelory.tanipedia.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.util.Patterns;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.benih.fragment.BenihFragment;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.controller.PakTaniController;
import id.zelory.tanipedia.controller.PasswordController;
import id.zelory.tanipedia.data.model.PakTani;
import id.zelory.tanipedia.ui.MainActivity;

/**
 * Created by zetbaitsu on 8/11/15.
 */
public class LoginFragment extends BenihFragment implements PakTaniController.Presenter,
        PasswordController.Presenter
{
    @Bind(R.id.email) EditText editEmail;
    @Bind(R.id.password) EditText editPass;
    private PakTaniController controller;
    private MaterialDialog dialog;
    private PasswordController passwordController;

    @Override
    protected int getFragmentView()
    {
        return R.layout.fragment_login;
    }

    @Override
    protected void onViewReady(Bundle bundle)
    {
        controller = new PakTaniController(this);
        passwordController = new PasswordController(this);
        loadAnimation();
        setUpDialog();
    }

    private void loadAnimation()
    {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.simple_grow);
        ButterKnife.findById(getView(), R.id.tanipedia).startAnimation(animation);
        ButterKnife.findById(getView(), R.id.card).startAnimation(animation);
        ButterKnife.findById(getView(), R.id.login).startAnimation(animation);
        ButterKnife.findById(getView(), R.id.register).startAnimation(animation);
    }

    @OnClick(R.id.login)
    void loginClick()
    {
        if (isValid())
        {
            controller.login(editEmail.getText().toString(), editPass.getText().toString());
        }
    }

    @OnClick(R.id.lupa_password)
    void addJawaban()
    {
        new MaterialDialog.Builder(getActivity())
                .title("TaniPedia")
                .content("Lupa Password")
                .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_CLASS_TEXT)
                .input("Masukan alamat email anda!", null, false, (dialog, input) -> {
                    passwordController.lupaPassword(input.toString());
                })
                .positiveColorRes(R.color.primary_dark)
                .positiveText("Kirim")
                .cancelListener(dialog -> {})
                .negativeColorRes(R.color.primary_dark)
                .negativeText("Batal")
                .show();
    }

    private boolean isValid()
    {
        boolean valid = true;

        if (!Patterns.EMAIL_ADDRESS.matcher(editEmail.getText().toString()).matches())
        {
            valid = false;
            editEmail.setError("Mohon isi dengan email yang valid!");
        }

        if (editPass.getText().toString().length() < 6)
        {
            valid = false;
            editPass.setError("Password yang anda masukan tidak valid!");
        }

        return valid;
    }

    @OnClick(R.id.register)
    void registerClick()
    {
        replace(R.id.sign_in_container, new RegisterFragment(), true);
    }

    private void setUpDialog()
    {
        dialog = new MaterialDialog.Builder(getActivity())
                .title("TaniPedia")
                .content("Mengirimkan permintaan...")
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
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onRegisterSuccess()
    {

    }

    @Override
    public void showPakTani(PakTani pakTani)
    {

    }

    @Override
    public void showError(Throwable throwable)
    {
        Snackbar.make(editPass, throwable.getMessage(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSuccessLupaPassword()
    {
        Toast.makeText(getActivity(), "Password anda telah kami kirimkan ke email tersebut.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailedLupaPassword()
    {
        Toast.makeText(getActivity(), "Maaf, email tersebut tidak terdaftar.", Toast.LENGTH_SHORT).show();
    }
}
