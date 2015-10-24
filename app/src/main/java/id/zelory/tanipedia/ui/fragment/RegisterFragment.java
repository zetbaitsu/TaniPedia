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
import android.util.Patterns;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RadioButton;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.benih.fragment.BenihFragment;
import id.zelory.tanipedia.ui.MainActivity;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.controller.PakTaniController;
import id.zelory.tanipedia.data.model.PakTani;

/**
 * Created by zetbaitsu on 8/11/15.
 */
public class RegisterFragment extends BenihFragment implements PakTaniController.Presenter
{
    @Bind(R.id.nama) EditText editNama;
    @Bind(R.id.email) EditText editEmail;
    @Bind(R.id.password) EditText editPass;
    @Bind(R.id.rb_laki) RadioButton rbLaki;
    private PakTaniController controller;
    private MaterialDialog dialog;

    @Override
    protected int getFragmentView()
    {
        return R.layout.fragment_register;
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
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.simple_grow);
        ButterKnife.findById(getView(), R.id.tanipedia).startAnimation(animation);
        ButterKnife.findById(getView(), R.id.card).startAnimation(animation);
        ButterKnife.findById(getView(), R.id.daftar).startAnimation(animation);
    }

    @OnClick(R.id.daftar)
    void daftarClick()
    {
        if (isValid())
        {
            controller.register(editEmail.getText().toString(), editNama.getText().toString(),
                                editPass.getText().toString(), rbLaki.isChecked());
        }
    }

    private boolean isValid()
    {
        boolean valid = true;
        if ("".equals(editNama.getText().toString()))
        {
            valid = false;
            editNama.setError("Mohon isi dengan nama lengkap anda!");
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(editEmail.getText().toString()).matches())
        {
            valid = false;
            editEmail.setError("Mohon isi dengan email yang valid!");
        }

        if (editPass.getText().toString().length() < 6)
        {
            valid = false;
            editPass.setError("Mohon isi password minimal 6 karakter!");
        }

        return valid;
    }

    private void setUpDialog()
    {
        dialog = new MaterialDialog.Builder(getActivity())
                .title("TaniPedia")
                .content("Mengirim data...")
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

    }

    @Override
    public void onRegisterSuccess()
    {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
}
