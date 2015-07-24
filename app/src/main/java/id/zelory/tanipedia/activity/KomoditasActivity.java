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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import id.zelory.benih.BenihActivity;
import id.zelory.benih.utils.BenihScheduler;
import id.zelory.benih.utils.PrefUtils;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.adapter.KomoditasAdapter;
import id.zelory.tanipedia.model.Komoditas;
import id.zelory.tanipedia.network.TaniPediaService;
import mbanje.kurt.fabbutton.FabButton;

public class KomoditasActivity extends BenihActivity
{
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FabButton fabButton;
    private ImageView imageHeader;
    private Animation animation;

    @Override
    protected int getActivityView()
    {
        return R.layout.activity_komoditas;
    }

    @Override
    protected void onViewReady(Bundle bundle)
    {
        toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Harga Komoditas");

        animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);

        drawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        setUpNavDrawer();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(3).setChecked(true);
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
                    intent = new Intent(KomoditasActivity.this, CuacaActivity.class);
                    break;
                case R.id.berita:
                    intent = new Intent(KomoditasActivity.this, BeritaActivity.class);
                    break;
                case R.id.tanya:
                    intent = new Intent(KomoditasActivity.this, TanyaActivity.class);
                    break;
                case R.id.harga:
                    return true;
                case R.id.logout:
                    PrefUtils.putString(KomoditasActivity.this, "nama", null);
                    intent = new Intent(KomoditasActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    return true;
                case R.id.tentang:
                    intent = new Intent(KomoditasActivity.this, TentangActivity.class);
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

        imageHeader = (ImageView) findViewById(R.id.header);
        imageHeader.setVisibility(View.GONE);

        fabButton = (FabButton) findViewById(R.id.determinate);
        fabButton.showProgress(true);
        getKomoditas();
        fabButton.setOnClickListener(v -> {
            fabButton.showProgress(true);
            getKomoditas();
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

    private void getKomoditas()
    {
        TaniPediaService.getApi()
                .getKomoditas()
                .compose(BenihScheduler.applySchedulers(BenihScheduler.Type.IO))
                .subscribe(komoditasArrayList -> {
                    if (komoditasArrayList != null && !komoditasArrayList.isEmpty())
                    {
                        KomoditasAdapter adapter = new KomoditasAdapter(KomoditasActivity.this, komoditasArrayList);
                        adapter.SetOnItemClickListener((view, position) -> {
                            Komoditas komoditas = komoditasArrayList.get(position);
                            Snackbar.make(view, "Harga " + komoditas.getNama().toLowerCase() + " adalah Rp. " + komoditas.getHarga() + ",00 per Kg.", Snackbar.LENGTH_LONG).show();
                        });
                        recyclerView.setAdapter(adapter);
                        recyclerView.startAnimation(animation);
                        imageHeader.setVisibility(View.VISIBLE);
                        imageHeader.startAnimation(animation);
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
}