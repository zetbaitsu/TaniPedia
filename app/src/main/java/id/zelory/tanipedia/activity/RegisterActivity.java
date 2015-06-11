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

import com.afollestad.materialdialogs.MaterialDialog;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

import id.zelory.tanipedia.R;
import id.zelory.tanipedia.model.PakTani;
import id.zelory.tanipedia.util.PrefUtils;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener
{
    private String nama;
    private String email;
    private String password;
    private EditText editNama;
    private EditText editEmail;
    private EditText editPass;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);
        findViewById(R.id.tanipedia).startAnimation(animation);
        findViewById(R.id.card).startAnimation(animation);
        Button daftar = (Button) findViewById(R.id.daftar);
        daftar.startAnimation(animation);
        daftar.setOnClickListener(this);
        editNama = (EditText) findViewById(R.id.nama);
        editEmail = (EditText) findViewById(R.id.email);
        editPass = (EditText) findViewById(R.id.password);
    }

    @Override
    public void onClick(View v)
    {
        nama = editNama.getText().toString();
        email = editEmail.getText().toString();
        password = editPass.getText().toString();
        if (nama.isEmpty())
        {
            Snackbar.make(v, "Mohon isi nama terlebih dahulu!", Snackbar.LENGTH_LONG).show();
        } else if (email.isEmpty())
        {
            Snackbar.make(v, "Mohon isi email terlebih dahulu!", Snackbar.LENGTH_LONG).show();
        } else if (password.isEmpty())
        {
            Snackbar.make(v, "Mohon isi password terlebih dahulu!", Snackbar.LENGTH_LONG).show();
        } else
        {
            new Register().execute();
        }
    }

    private class Register extends AsyncTask<Void, Void, Void>
    {
        MaterialDialog dialog;
        String status;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new MaterialDialog.Builder(RegisterActivity.this)
                    .title("TaniPedia")
                    .content("Mengirim data...")
                    .progress(true, 0)
                    .build();
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = null;
            String url = PakTani.REGISTER_API + "email=" + email + "&nama=" + nama + "&pass=" + password;
            url = url.replace(" ", "%20");
            while (root == null)
            {
                try
                {
                    root = mapper.readTree(new URL(url));
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            status = root.findValue("status").asText();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            if (status.equals("Berhasil"))
            {
                PrefUtils.simpanString(RegisterActivity.this, "email", email);
                PrefUtils.simpanString(RegisterActivity.this, "nama", nama);
                PrefUtils.simpanString(RegisterActivity.this, "pass", password);
                Intent intent = new Intent(RegisterActivity.this, CuacaActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else
            {
                Snackbar.make(editPass, "Terjadi kesalahan, silahkan coba lagi!", Snackbar.LENGTH_LONG).show();
            }

            dialog.dismiss();
        }
    }
}
