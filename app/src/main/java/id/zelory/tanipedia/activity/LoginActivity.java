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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

import id.zelory.tanipedia.R;
import id.zelory.tanipedia.model.PakTani;
import id.zelory.tanipedia.util.PrefUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    private String email;
    private String password;
    private EditText editEmail;
    private EditText editPass;
    private PakTani pakTani;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (PrefUtils.ambilString(this,"nama") != null)
        {
            Intent intent = new Intent(LoginActivity.this, CuacaActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);
        findViewById(R.id.tanipedia).startAnimation(animation);
        findViewById(R.id.card).startAnimation(animation);
        Button login = (Button) findViewById(R.id.login);
        login.startAnimation(animation);
        login.setOnClickListener(this);
        TextView daftar = (TextView) findViewById(R.id.register);
        daftar.startAnimation(animation);
        daftar.setOnClickListener(this);
        editEmail = (EditText) findViewById(R.id.email);
        editPass = (EditText) findViewById(R.id.password);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.login:
                email = editEmail.getText().toString();
                password = editPass.getText().toString();
                if (email.isEmpty())
                {
                    Snackbar.make(v, "Mohon isi email terlebih dahulu!", Snackbar.LENGTH_LONG).show();
                } else if (password.isEmpty())
                {
                    Snackbar.make(v, "Mohon isi password terlebih dahulu!", Snackbar.LENGTH_LONG).show();
                } else
                {
                    new Login().execute();
                }
                break;
            case R.id.register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    private class Login extends AsyncTask<Void, Void, Void>
    {
        MaterialDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new MaterialDialog.Builder(LoginActivity.this)
                    .title("TaniPedia")
                    .content("Mencoba login...")
                    .progress(true, 0)
                    .build();
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            ObjectMapper mapper = new ObjectMapper();
            pakTani = null;
            while (pakTani == null)
            {
                try
                {
                    pakTani = mapper.readValue(new URL(PakTani.LOGIN_API + "email=" + email + "&pass=" + password), PakTani.class);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            if (pakTani.getEmail() != null)
            {
                PrefUtils.simpanString(LoginActivity.this, "email", email);
                PrefUtils.simpanString(LoginActivity.this, "nama", pakTani.getNama());
                PrefUtils.simpanString(LoginActivity.this, "pass", password);
                Intent intent = new Intent(LoginActivity.this, CuacaActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else
            {
                Snackbar.make(editPass, "E-Mail dan Password tidak cocok.", Snackbar.LENGTH_LONG).show();
            }

            dialog.dismiss();
        }
    }
}
