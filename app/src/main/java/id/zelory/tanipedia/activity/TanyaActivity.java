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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import id.zelory.benih.BenihActivity;
import id.zelory.benih.utils.BenihScheduler;
import id.zelory.benih.utils.PrefUtils;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.adapter.SoalAdapter;
import id.zelory.tanipedia.network.TaniPediaService;
import id.zelory.tanipedia.util.MyRecyclerScroll;
import mbanje.kurt.fabbutton.FabButton;

public class TanyaActivity extends BenihActivity
{
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private int fabMargin;
    private FrameLayout fab;
    private ImageButton fabBtn;
    private FabButton fabButton;
    private String pertanyaan;
    private Animation animation;
    private ImageView tanyaGambar;

    @Override
    protected int getActivityView()
    {
        return R.layout.activity_tanya;
    }

    @Override
    protected void onViewReady(Bundle bundle)
    {
        toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Tanya Tani");

        animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);
        fabMargin = getResources().getDimensionPixelSize(R.dimen.fab_margin);

        drawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        setUpNavDrawer();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(2).setChecked(true);
        TextView nama = (TextView) navigationView.findViewById(R.id.nama);
        nama.setText(PrefUtils.getString(this, "nama"));
        TextView email = (TextView) navigationView.findViewById(R.id.email);
        email.setText(PrefUtils.getString(this, "email"));
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
                    PrefUtils.putString(TanyaActivity.this, "nama", null);
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

        recyclerView = (RecyclerView) findViewById(R.id.scrollableview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new MyRecyclerScroll()
        {
            @Override
            public void show()
            {
                fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide()
            {
                fab.animate().translationY(fab.getHeight() + fabMargin).setInterpolator(new AccelerateInterpolator(2)).start();
            }
        });

        tanyaGambar = (ImageView) findViewById(R.id.tanya_gambar);
        tanyaGambar.setVisibility(View.GONE);
        fab = (FrameLayout) findViewById(R.id.myfab_main);
        fab.setVisibility(View.GONE);
        fabBtn = (ImageButton) findViewById(R.id.myfab_main_btn);
        View fabShadow = findViewById(R.id.myfab_shadow);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            fabShadow.setVisibility(View.GONE);
            fabBtn.setBackground(getDrawable(R.drawable.ripple_accent));
        }

        fabBtn.setOnClickListener(v -> new MaterialDialog.Builder(TanyaActivity.this)
                .title("TaniPedia")
                .content("Kirim Pertanyaan")
                .inputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT)
                .input("Ketik pertanyaan anda disini!", null, false, (dialog, input) -> {
                    try
                    {
                        pertanyaan = URLEncoder.encode(input.toString(), "UTF-8");
                        postPertanyaan(PrefUtils.getString(this, "email"), pertanyaan);
                    } catch (UnsupportedEncodingException e)
                    {
                        Snackbar.make(fabBtn, "Terjadi kesalahan, silahkan coba lagi!", Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                })
                .positiveColorRes(R.color.primary_dark)
                .positiveText("Kirim")
                .cancelListener(dialog -> {
                })
                .negativeColorRes(R.color.primary_dark)
                .negativeText("Batal")
                .show());

        fabButton = (FabButton) findViewById(R.id.determinate);
        fabButton.showProgress(true);
        getPertanyaan();
        fabButton.setOnClickListener(v -> {
            fabButton.showProgress(true);
            getPertanyaan();
        });
    }

    private void setUpNavDrawer()
    {
        if (toolbar != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_drawer);
            toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        }
    }

    private void getPertanyaan()
    {
        TaniPediaService.getApi()
                .getPertanyaan()
                .compose(BenihScheduler.applySchedulers(BenihScheduler.Type.IO))
                .subscribe(soalArrayList -> {
                    if (soalArrayList != null && !soalArrayList.isEmpty())
                    {
                        SoalAdapter adapter = new SoalAdapter(TanyaActivity.this, soalArrayList);
                        adapter.SetOnItemClickListener((view, position) -> {
                            Intent intent = new Intent(TanyaActivity.this, JawabActivity.class);
                            intent.putExtra("soal", soalArrayList.get(position));
                            startActivity(intent);
                        });
                        recyclerView.setAdapter(adapter);
                        recyclerView.startAnimation(animation);
                        fab.startAnimation(animation);
                        fab.setVisibility(View.VISIBLE);
                        tanyaGambar.setVisibility(View.VISIBLE);
                        tanyaGambar.startAnimation(animation);
                    } else
                    {
                        Snackbar.make(fabButton, "Mohon periksa koneksi internet anda!", Snackbar.LENGTH_LONG).show();
                    }

                    fabButton.onProgressCompleted();
                    fabButton.showProgress(false);
                    new Handler().postDelayed(fabButton::resetIcon, 2500);
                }, throwable -> {
                    Snackbar.make(fabButton, "Mohon periksa koneksi internet anda!", Snackbar.LENGTH_LONG).show();
                    fabButton.onProgressCompleted();
                    fabButton.showProgress(false);
                    new Handler().postDelayed(fabButton::resetIcon, 2500);
                });
    }

    private void postPertanyaan(String email, String isi)
    {
        MaterialDialog dialog = new MaterialDialog.Builder(TanyaActivity.this)
                .title("TaniPedia")
                .content("Mengirim data...")
                .progress(true, 0)
                .build();
        dialog.show();

        TaniPediaService.getApi()
                .postPertanyaan(email, isi)
                .compose(BenihScheduler.applySchedulers(BenihScheduler.Type.IO))
                .subscribe(status -> {
                    if (status.getStatus())
                    {
                        fabButton.showProgress(true);
                        getPertanyaan();
                        Snackbar.make(fabBtn, "Pertanyaan anda berhasil dikirim.", Snackbar.LENGTH_LONG).show();
                    } else
                    {
                        Snackbar.make(fabBtn, "Terjadi kesalahan, silahkan coba lagi!", Snackbar.LENGTH_LONG).show();
                    }

                    dialog.dismiss();
                }, throwable -> {
                    Snackbar.make(fabBtn, "Terjadi kesalahan, silahkan coba lagi!", Snackbar.LENGTH_LONG).show();
                    dialog.dismiss();
                });
    }
}
