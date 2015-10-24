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

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.benih.BenihActivity;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.controller.PakTaniController;
import id.zelory.tanipedia.data.LocalDataManager;
import id.zelory.tanipedia.data.model.PakTani;
import mbanje.kurt.fabbutton.FabButton;

/**
 * Created on : October 21, 2015
 * Author     : zetbaitsu
 * Name       : Zetra
 * Email      : zetra@mail.ugm.ac.id
 * GitHub     : https://github.com/zetbaitsu
 * LinkedIn   : https://id.linkedin.com/in/zetbaitsu
 */
public class EditProfileActivity extends BenihActivity implements PakTaniController.Presenter
{
    @Bind(R.id.pak_tani) ImageView ivPakTani;
    @Bind(R.id.nama) EditText editNama;
    @Bind(R.id.email) EditText editEmail;
    @Bind(R.id.password) EditText editPass;
    @Bind(R.id.rb_laki) RadioButton rbLaki;
    @Bind(R.id.rb_perempuan) RadioButton rbPerempuan;
    @Bind(R.id.determinate) FabButton fabUpload;
    private PakTaniController controller;
    private PakTani pakTani;

    @Override
    protected int getActivityView()
    {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState)
    {
        pakTani = LocalDataManager.getPakTani();
        setContent();
        setUpToolbar();
        setUpFab();
        controller = new PakTaniController(this);
    }

    @OnClick(R.id.edit_pass)
    public void showPasswordDialog()
    {
        new MaterialDialog.Builder(this)
                .customView(R.layout.dialog_password, true)
                .title("TaniPedia")
                .positiveColorRes(R.color.primary_dark)
                .positiveText("Simpan")
                .onPositive((materialDialog, dialogAction) -> {
                    View view = materialDialog.getCustomView();
                    TextView pass = ButterKnife.findById(view, R.id.password);
                    TextView passBaru = ButterKnife.findById(view, R.id.password_baru);
                    TextView passBaruLagi = ButterKnife.findById(view, R.id.password_baru_lagi);

                    if (pakTani.getPassword().equals(pass.getText().toString()))
                    {
                        if (passBaru.getText().toString().length() < 6)
                        {
                            passBaru.setError("Mohon isi password minimal 6 karakter!");
                        } else if (!passBaruLagi.getText().toString().equals(passBaru.getText().toString()))
                        {
                            passBaruLagi.setError("Password tidak cocok!");
                        } else
                        {
                            editPass.setText(passBaru.getText().toString());
                            materialDialog.dismiss();
                        }
                    } else
                    {
                        pass.setError("Password lama anda salah!");
                    }
                })
                .autoDismiss(false)
                .onNegative((materialDialog, dialogAction) -> materialDialog.dismiss())
                .negativeColorRes(R.color.primary_dark)
                .negativeText("Batal")
                .show();
    }

    private void setUpFab()
    {
        fabUpload.setOnClickListener(v -> {
            if (isValid())
            {
                pakTani.setNama(editNama.getText().toString());
                pakTani.setEmail(editEmail.getText().toString());
                pakTani.setPassword(editPass.getText().toString());
                pakTani.setMale(rbLaki.isChecked());
                controller.updatePakTani(pakTani);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        onBackPressed();
        return true;
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

    private void setContent()
    {
        ivPakTani.setImageResource(pakTani.isMale() ? R.drawable.pak_tani : R.drawable.bu_tani);
        editNama.setText(pakTani.getNama());
        editEmail.setText(pakTani.getEmail());
        editPass.setText(pakTani.getPassword());
        rbLaki.setChecked(pakTani.isMale());
        rbPerempuan.setChecked(!pakTani.isMale());
    }

    private void setUpToolbar()
    {
        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = ButterKnife.findById(this, R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Edit Profil");
    }

    @Override
    public void showError(Throwable throwable)
    {
        Snackbar.make(fabUpload, "Gagal menyimpan data.", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading()
    {
        fabUpload.showProgress(true);
    }

    @Override
    public void dismissLoading()
    {
        fabUpload.onProgressCompleted();
        fabUpload.showProgress(false);
        new Handler().postDelayed(fabUpload::resetIcon, 2500);
    }

    @Override
    public void onLoginSuccess()
    {

    }

    @Override
    public void onRegisterSuccess()
    {

    }

    @Override
    public void showPakTani(PakTani pakTani)
    {
        Snackbar.make(fabUpload, "Data berhasil disimpan.", Snackbar.LENGTH_SHORT).show();
        setContent();
    }
}
