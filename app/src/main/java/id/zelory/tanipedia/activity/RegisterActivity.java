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

import com.afollestad.materialdialogs.MaterialDialog;

import id.zelory.benih.BenihActivity;
import id.zelory.benih.utils.BenihScheduler;
import id.zelory.benih.utils.PrefUtils;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.network.TaniPediaService;

public class RegisterActivity extends BenihActivity
{
    private EditText editNama;
    private EditText editEmail;
    private EditText editPass;

    @Override
    protected int getActivityView()
    {
        return R.layout.activity_register;
    }

    @Override
    protected void onViewReady(Bundle bundle)
    {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);
        findViewById(R.id.tanipedia).startAnimation(animation);
        findViewById(R.id.card).startAnimation(animation);

        editNama = (EditText) findViewById(R.id.nama);
        editEmail = (EditText) findViewById(R.id.email);
        editPass = (EditText) findViewById(R.id.password);

        Button daftar = (Button) findViewById(R.id.daftar);
        daftar.startAnimation(animation);
        daftar.setOnClickListener(v -> {
            if (editNama.getText().toString().isEmpty())
            {
                Snackbar.make(v, "Mohon isi nama terlebih dahulu!", Snackbar.LENGTH_LONG).show();
            } else if (editEmail.getText().toString().isEmpty())
            {
                Snackbar.make(v, "Mohon isi email terlebih dahulu!", Snackbar.LENGTH_LONG).show();
            } else if (editPass.getText().toString().isEmpty())
            {
                Snackbar.make(v, "Mohon isi password terlebih dahulu!", Snackbar.LENGTH_LONG).show();
            } else
            {
                register(editEmail.getText().toString(), editNama.getText().toString(), editPass.getText().toString());
            }
        });
    }

    private void register(String email, String nama, String password)
    {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("TaniPedia")
                .content("Mengirim data...")
                .progress(true, 0)
                .build();
        dialog.show();

        TaniPediaService.getApi()
                .register(email, nama, password)
                .compose(BenihScheduler.applySchedulers(BenihScheduler.Type.IO))
                .subscribe(status -> {
                    if (status.getStatus())
                    {
                        PrefUtils.putString(RegisterActivity.this, "email", email);
                        PrefUtils.putString(RegisterActivity.this, "nama", nama);
                        PrefUtils.putString(RegisterActivity.this, "pass", password);
                        Intent intent = new Intent(RegisterActivity.this, CuacaActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else
                    {
                        Snackbar.make(editPass, "Email tersebut sudah terdaftar!", Snackbar.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                }, throwable -> {
                    Snackbar.make(editPass, "Terjadi kesalahan, silahkan coba lagi!", Snackbar.LENGTH_LONG).show();
                    dialog.dismiss();
                });
    }
}
