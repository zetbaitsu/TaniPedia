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
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import id.zelory.benih.BenihActivity;
import id.zelory.benih.utils.BenihScheduler;
import id.zelory.benih.utils.PrefUtils;
import id.zelory.benih.views.BenihRecyclerView;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.adapter.BeritaAdapter;
import id.zelory.tanipedia.network.TaniPediaService;
import id.zelory.tanipedia.util.Utils;
import mbanje.kurt.fabbutton.FabButton;


public class BeritaActivity extends BenihActivity
{
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private BenihRecyclerView recyclerView;
    private BeritaAdapter adapter;
    private ImageView imageHeader;
    private FabButton fabButton;
    private Animation animation;

    @Override
    protected int getActivityView()
    {
        return R.layout.activity_berita;
    }

    @Override
    protected void onViewReady(Bundle bundle)
    {
        toolbar = (Toolbar) findViewById(R.id.anim_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Berita Terbaru");

        animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);

        drawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        setUpNavDrawer();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);
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
                    intent = new Intent(BeritaActivity.this, CuacaActivity.class);
                    break;
                case R.id.berita:
                    return true;
                case R.id.tanya:
                    intent = new Intent(BeritaActivity.this, TanyaActivity.class);
                    break;
                case R.id.harga:
                    intent = new Intent(BeritaActivity.this, KomoditasActivity.class);
                    break;
                case R.id.logout:
                    PrefUtils.putString(BeritaActivity.this, "nama", null);
                    intent = new Intent(BeritaActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    return true;
                case R.id.tentang:
                    intent = new Intent(BeritaActivity.this, TentangActivity.class);
                    startActivity(intent);
                    return true;
                default:
                    return true;
            }
            startActivity(intent);
            finish();
            return true;
        });
        imageHeader = (ImageView) findViewById(R.id.header);

        recyclerView = (BenihRecyclerView) findViewById(R.id.scrollableview);
        recyclerView.setUpAsList();
        adapter = new BeritaAdapter(this);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent(this, BacaActivity.class);
            intent.putExtra("berita", adapter.getData().get(position));
            startActivity(intent);
        });

        fabButton = (FabButton) findViewById(R.id.determinate);
        fabButton.showProgress(true);
        getAllBerita();
        fabButton.setOnClickListener(v -> {
            fabButton.showProgress(true);
            getAllBerita();
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

    private void getAllBerita()
    {
        TaniPediaService.getApi()
                .getAllBerita()
                .compose(BenihScheduler.applySchedulers(BenihScheduler.Type.IO))
                .subscribe(beritaArrayList -> {
                    if (beritaArrayList != null && !beritaArrayList.isEmpty())
                    {
                        adapter.add(beritaArrayList);
                        recyclerView.startAnimation(animation);

                        String url = beritaArrayList.get(Utils.randInt(0, beritaArrayList.size() - 1)).getGambar();
                        Glide.with(BeritaActivity.this)
                                .load(url)
                                .into(imageHeader);
                        imageHeader.startAnimation(animation);
                        PrefUtils.putString(this, "berita", new Gson().toJson(beritaArrayList));
                    } else
                    {
                        Snackbar.make(fabButton, "Mohon periksa koneksi internet anda!", Snackbar.LENGTH_LONG).show();
                    }

                    fabButton.onProgressCompleted();
                    fabButton.showProgress(false);
                    new Handler().postDelayed(fabButton::resetIcon, 2500);
                }, throwable -> {
                    Snackbar.make(fabButton, "Terjadi kesalahan silahkan coba lagi!", Snackbar.LENGTH_LONG).show();
                    fabButton.onProgressCompleted();
                    fabButton.showProgress(false);
                    new Handler().postDelayed(fabButton::resetIcon, 2500);
                });
    }
}
