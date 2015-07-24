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

package id.zelory.tanipedia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import id.zelory.benih.BenihActivity;
import id.zelory.benih.utils.BenihScheduler;
import id.zelory.benih.utils.PrefUtils;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.network.TaniPediaService;

public class LoginActivity extends BenihActivity
{
    private EditText editEmail;
    private EditText editPass;

    @Override
    protected int getActivityView()
    {
        return R.layout.activity_login;
    }

    @Override
    protected void onViewReady(Bundle bundle)
    {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);
        findViewById(R.id.tanipedia).startAnimation(animation);
        findViewById(R.id.card).startAnimation(animation);

        editEmail = (EditText) findViewById(R.id.email);
        editPass = (EditText) findViewById(R.id.password);

        Button login = (Button) findViewById(R.id.login);
        login.startAnimation(animation);
        login.setOnClickListener(v -> {
            if (editEmail.getText().toString().isEmpty())
            {
                Snackbar.make(v, "Mohon isi email terlebih dahulu!", Snackbar.LENGTH_LONG).show();
            } else if (editPass.getText().toString().isEmpty())
            {
                Snackbar.make(v, "Mohon isi password terlebih dahulu!", Snackbar.LENGTH_LONG).show();
            } else
            {
                login(editEmail.getText().toString(), editPass.getText().toString());
            }
        });

        TextView daftar = (TextView) findViewById(R.id.register);
        daftar.startAnimation(animation);
        daftar.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void login(String email, String password)
    {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("TaniPedia")
                .content("Mencoba login...")
                .progress(true, 0)
                .build();
        dialog.show();

        TaniPediaService.getApi()
                .login(email, password)
                .compose(BenihScheduler.applySchedulers(BenihScheduler.Type.IO))
                .subscribe(pakTani -> {
                    if (pakTani.getEmail() != null)
                    {
                        PrefUtils.putString(this, "email", pakTani.getEmail());
                        PrefUtils.putString(this, "nama", pakTani.getNama());
                        PrefUtils.putString(this, "pass", pakTani.getPassword());
                        Intent intent = new Intent(this, CuacaActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else
                    {
                        Snackbar.make(editPass, "E-Mail dan Password tidak cocok.", Snackbar.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                }, throwable -> {
                    Snackbar.make(editPass, "Mohon periksa koneksi internet anda!", Snackbar.LENGTH_LONG).show();
                    dialog.dismiss();
                });
    }
}
