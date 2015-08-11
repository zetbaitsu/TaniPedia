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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.benih.BenihActivity;
import id.zelory.benih.util.BenihPreferenceUtils;
import id.zelory.benih.view.BenihRecyclerView;
import id.zelory.tanipedia.adapter.SoalAdapter;
import id.zelory.tanipedia.controller.SoalController;
import id.zelory.tanipedia.model.Soal;
import id.zelory.tanipedia.util.MyRecyclerScroll;
import mbanje.kurt.fabbutton.FabButton;

public class TanyaActivity extends BenihActivity implements SoalController.Presenter
{
    @Bind(R.id.nav_drawer) DrawerLayout drawerLayout;
    @Bind(R.id.anim_toolbar) Toolbar toolbar;
    @Bind(R.id.scrollableview) BenihRecyclerView recyclerView;
    @Bind(R.id.myfab_main) FrameLayout frameFab;
    @Bind(R.id.myfab_main_btn) ImageButton fabAddPertanyaan;
    @Bind(R.id.determinate) FabButton fabRefreshPertanyaan;
    @Bind(R.id.tanya_gambar) ImageView tanyaGambar;
    private SoalController controller;
    private SoalAdapter adapter;
    private int fabMargin;
    private Animation animation;
    private MaterialDialog dialog;

    @Override
    protected int getActivityView()
    {
        return R.layout.activity_tanya;
    }

    @Override
    protected void onViewReady(Bundle bundle)
    {
        controller = new SoalController(this);
        animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);

        setUpToolbar();
        setUpNavigationDrawer();
        setUpAdapter();
        setUpRecyclerView();
        setUpFab();
        setUpLoadingDialog();

        tanyaGambar.setVisibility(View.GONE);
        controller.loadListSoal();
    }

    private void setUpFab()
    {
        fabMargin = getResources().getDimensionPixelSize(R.dimen.fab_margin);
        frameFab.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            ButterKnife.findById(this, R.id.myfab_shadow).setVisibility(View.GONE);
            fabAddPertanyaan.setBackground(getDrawable(R.drawable.ripple_accent));
        }

        fabRefreshPertanyaan.setOnClickListener(v -> controller.loadListSoal());
    }

    @OnClick(R.id.myfab_main_btn)
    void addPertanyaan()
    {
        new MaterialDialog.Builder(this)
                .title("TaniPedia")
                .content("Kirim Pertanyaan")
                .inputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT)
                .input("Ketik pertanyaan anda disini!", null, false, (dialog, input) -> {
                    controller.sendSoal(BenihPreferenceUtils.getString(this, "email"), input.toString());
                })
                .positiveColorRes(R.color.primary_dark)
                .positiveText("Kirim")
                .cancelListener(dialog -> {})
                .negativeColorRes(R.color.primary_dark)
                .negativeText("Batal")
                .show();
    }

    private void setUpRecyclerView()
    {
        recyclerView.setUpAsList();
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new MyRecyclerScroll()
        {
            @Override
            public void show()
            {
                frameFab.animate()
                        .translationY(0)
                        .setInterpolator(new DecelerateInterpolator(2))
                        .start();
            }

            @Override
            public void hide()
            {
                frameFab.animate()
                        .translationY(frameFab.getHeight() + fabMargin)
                        .setInterpolator(new AccelerateInterpolator(2))
                        .start();
            }
        });
    }

    private void setUpAdapter()
    {
        adapter = new SoalAdapter(this);
        adapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent(this, JawabActivity.class);
            intent.putExtra("soal", adapter.getData().get(position));
            startActivity(intent);
        });
    }

    private void setUpNavigationDrawer()
    {
        if (toolbar != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_drawer);
            toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(2).setChecked(true);
        TextView nama = (TextView) navigationView.findViewById(R.id.nama);
        nama.setText(BenihPreferenceUtils.getString(this, "nama"));
        TextView email = (TextView) navigationView.findViewById(R.id.email);
        email.setText(BenihPreferenceUtils.getString(this, "email"));
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            drawerLayout.closeDrawers();
            Intent intent;
            switch (menuItem.getItemId())
            {
                case R.id.cuaca:
                    intent = new Intent(TanyaActivity.this, CuacaActivity.class);
                    break;
                case R.id.berita:
                    intent = new Intent(TanyaActivity.this, BeritaActivity.class);
                    break;
                case R.id.tanya:
                    return true;
                case R.id.harga:
                    intent = new Intent(TanyaActivity.this, KomoditasActivity.class);
                    break;
                case R.id.logout:
                    BenihPreferenceUtils.putString(TanyaActivity.this, "nama", null);
                    intent = new Intent(TanyaActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    return true;
                case R.id.tentang:
                    intent = new Intent(TanyaActivity.this, TentangActivity.class);
                    startActivity(intent);
                    return true;
                default:
                    return true;
            }
            startActivity(intent);
            finish();
            return true;
        });
    }

    private void setUpToolbar()
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = ButterKnife.findById(this, R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Tanya Tani");
    }

    private void setUpLoadingDialog()
    {
        dialog = new MaterialDialog.Builder(this)
                .title("TaniPedia")
                .content("Mengirim pertanyaan anda...")
                .progress(true, 0)
                .build();
    }

    @Override
    public void showLoading()
    {
        fabRefreshPertanyaan.showProgress(true);
    }

    @Override
    public void showLoadingDialog()
    {
        dialog.show();
    }

    @Override
    public void dismissLoading()
    {
        fabRefreshPertanyaan.onProgressCompleted();
        fabRefreshPertanyaan.showProgress(false);
        new Handler().postDelayed(fabRefreshPertanyaan::resetIcon, 2500);
        if (dialog != null)
        {
            dialog.dismiss();
        }
    }

    @Override
    public void showListSoal(List<Soal> listSoal)
    {
        adapter.clear();
        adapter.add(listSoal);
        recyclerView.startAnimation(animation);
        frameFab.startAnimation(animation);
        frameFab.setVisibility(View.VISIBLE);
        tanyaGambar.setVisibility(View.VISIBLE);
        tanyaGambar.startAnimation(animation);
    }

    @Override
    public void showError(Throwable throwable)
    {
        Snackbar.make(fabAddPertanyaan, throwable.getMessage(), Snackbar.LENGTH_LONG).show();
    }
}
