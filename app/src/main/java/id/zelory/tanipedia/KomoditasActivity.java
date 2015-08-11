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
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import id.zelory.benih.BenihActivity;
import id.zelory.benih.util.BenihPreferenceUtils;
import id.zelory.benih.view.BenihRecyclerView;
import id.zelory.tanipedia.adapter.KomoditasAdapter;
import id.zelory.tanipedia.controller.KomoditasController;
import id.zelory.tanipedia.model.Komoditas;
import mbanje.kurt.fabbutton.FabButton;

public class KomoditasActivity extends BenihActivity implements KomoditasController.Presenter
{
    @Bind(R.id.nav_drawer) DrawerLayout drawerLayout;
    @Bind(R.id.anim_toolbar) Toolbar toolbar;
    @Bind(R.id.scrollableview) BenihRecyclerView recyclerView;
    @Bind(R.id.determinate) FabButton fabRefreshKomoditas;
    @Bind(R.id.header) ImageView imageHeader;
    private KomoditasController komoditasController;
    private KomoditasAdapter adapter;
    private Animation animation;

    @Override
    protected int getActivityView()
    {
        return R.layout.activity_komoditas;
    }

    @Override
    protected void onViewReady(Bundle bundle)
    {
        animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);
        imageHeader.setVisibility(View.GONE);

        setUpToolbar();
        setUpNavigationDrawer();
        setUpAdapter();
        setUpRecyclerView();
        setUpFabButton();
        setUpController(bundle);
    }

    private void setUpRecyclerView()
    {
        recyclerView.setUpAsList();
        recyclerView.setAdapter(adapter);
    }

    private void setUpAdapter()
    {
        adapter = new KomoditasAdapter(this);
        adapter.setOnItemClickListener((view, position) -> {
            Komoditas komoditas = adapter.getData().get(position);
            Snackbar.make(view, "Harga " + komoditas.getNama().toLowerCase() + " adalah Rp. " + komoditas.getHarga() + ",00 per Kg.", Snackbar.LENGTH_LONG).show();
        });
    }

    private void setUpToolbar()
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = ButterKnife.findById(this, R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Harga Komoditas");
    }

    private void setUpFabButton()
    {
        fabRefreshKomoditas.setOnClickListener(v -> komoditasController.loadListKomoditas());
    }

    private void setUpController(Bundle bundle)
    {
        if (komoditasController == null)
        {
            komoditasController = new KomoditasController(this);
        }

        if (bundle == null)
        {
            komoditasController.loadListKomoditas();
        } else
        {
            komoditasController.loadState(bundle);
        }
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
        navigationView.getMenu().getItem(3).setChecked(true);
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
                    BenihPreferenceUtils.putString(KomoditasActivity.this, "nama", null);
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
    }

    @Override
    public void showLoading()
    {
        fabRefreshKomoditas.showProgress(true);
    }

    @Override
    public void dismissLoading()
    {
        fabRefreshKomoditas.onProgressCompleted();
        fabRefreshKomoditas.showProgress(false);
        new Handler().postDelayed(fabRefreshKomoditas::resetIcon, 2500);
    }

    @Override
    public void showListKomoditas(List<Komoditas> listKomoditas)
    {
        adapter.clear();
        adapter.add(listKomoditas);
        recyclerView.startAnimation(animation);
        imageHeader.setVisibility(View.VISIBLE);
        imageHeader.startAnimation(animation);
    }

    @Override
    public void showError(Throwable throwable)
    {
        Snackbar.make(fabRefreshKomoditas, "Mohon periksa koneksi internet anda!", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState)
    {
        komoditasController.saveState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }
}