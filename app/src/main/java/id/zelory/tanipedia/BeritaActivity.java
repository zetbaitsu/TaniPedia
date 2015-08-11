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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import id.zelory.benih.BenihActivity;
import id.zelory.benih.util.BenihPreferenceUtils;
import id.zelory.benih.util.BenihUtils;
import id.zelory.benih.view.BenihImageView;
import id.zelory.benih.view.BenihRecyclerView;
import id.zelory.tanipedia.adapter.BeritaAdapter;
import id.zelory.tanipedia.controller.BeritaController;
import id.zelory.tanipedia.model.Berita;
import mbanje.kurt.fabbutton.FabButton;


public class BeritaActivity extends BenihActivity implements BeritaController.Presenter
{
    @Bind(R.id.nav_drawer) DrawerLayout drawerLayout;
    @Bind(R.id.anim_toolbar) Toolbar toolbar;
    @Bind(R.id.scrollableview) BenihRecyclerView recyclerView;
    @Bind(R.id.header) BenihImageView imageHeader;
    @Bind(R.id.determinate) FabButton fabRefreshBerita;
    private BeritaController beritaController;
    private BeritaAdapter adapter;
    private Animation animation;

    @Override
    protected int getActivityView()
    {
        return R.layout.activity_berita;
    }

    @Override
    protected void onViewReady(Bundle bundle)
    {
        animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);

        setUpToolbar();
        setUpNavigationDrawer();
        setUpAdapter();
        setUpRecyclerView();
        setUpController(bundle);
        setUpFabButton();
    }

    private void setUpFabButton()
    {
        fabRefreshBerita.setOnClickListener(v -> beritaController.loadListBerita());
    }

    private void setUpAdapter()
    {
        adapter = new BeritaAdapter(this);
        adapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent(this, BacaActivity.class);
            intent.putExtra("berita", adapter.getData().get(position));
            startActivity(intent);
        });
    }

    private void setUpRecyclerView()
    {
        recyclerView.setUpAsList();
        recyclerView.setAdapter(adapter);
    }

    private void setUpToolbar()
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = ButterKnife.findById(this, R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Berita Terbaru");
    }

    private void setUpController(Bundle bundle)
    {
        if (beritaController == null)
        {
            beritaController = new BeritaController(this);
        }

        if (bundle == null)
        {
            beritaController.loadListBerita();
        } else
        {
            beritaController.loadState(bundle);
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
        navigationView.getMenu().getItem(1).setChecked(true);
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
                    BenihPreferenceUtils.putString(BeritaActivity.this, "nama", null);
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
    }

    @Override
    public void showLoading()
    {
        fabRefreshBerita.showProgress(true);
    }

    @Override
    public void dismissLoading()
    {
        fabRefreshBerita.onProgressCompleted();
        fabRefreshBerita.showProgress(false);
        new Handler().postDelayed(fabRefreshBerita::resetIcon, 2500);
    }

    @Override
    public void showBerita(Berita berita)
    {

    }

    @Override
    public void showListBerita(List<Berita> listBerita)
    {
        adapter.clear();
        adapter.add(listBerita);
        recyclerView.startAnimation(animation);
        String url = listBerita.get(BenihUtils.randInt(0, listBerita.size() - 1)).getGambar();
        imageHeader.setImageUrl(url);
        imageHeader.startAnimation(animation);
    }

    @Override
    public void showError(Throwable throwable)
    {
        Snackbar.make(fabRefreshBerita, "Terjadi kesalahan silahkan coba lagi!", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState)
    {
        beritaController.saveState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
