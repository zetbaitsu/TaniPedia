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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import id.zelory.benih.BenihActivity;
import id.zelory.benih.util.BenihPreferenceUtils;
import id.zelory.benih.view.BenihRecyclerView;
import id.zelory.tanipedia.adapter.CuacaAdapter;
import id.zelory.tanipedia.controller.CuacaController;
import id.zelory.tanipedia.model.Cuaca;
import mbanje.kurt.fabbutton.FabButton;

public class CuacaActivity extends BenihActivity implements CuacaController.Presenter
{
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.nav_drawer) DrawerLayout drawerLayout;
    @Bind(R.id.anim_toolbar) Toolbar toolbar;
    @Bind(R.id.scrollableview) BenihRecyclerView recyclerView;
    @Bind(R.id.determinate) FabButton fabButton;
    @Bind(R.id.cuaca) TextView cuacaText;
    @Bind(R.id.suhu) TextView suhu;
    @Bind(R.id.minmax) TextView minmax;
    @Bind(R.id.icon_cuaca) ImageView iconCuaca;
    @Bind(R.id.kegiatan) TextView kegiatan;
    @Bind(R.id.background) LinearLayout background;
    private CuacaController cuacaController;
    private CuacaAdapter adapter;
    private Animation animation;

    @Override
    protected int getActivityView()
    {
        return R.layout.activity_cuaca;
    }

    @Override
    protected void onViewReady(Bundle bundle)
    {
        animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);

        setUpToolbar();
        setUpNavigationDrawer();
        setUpHeaderContent();
        setUpAdapter();
        setUpRecyclerView();
        setUpController();
        setUpFabButton();
    }

    private void setUpController()
    {
        if (cuacaController == null)
        {
            cuacaController = new CuacaController(this);
        }
    }

    private void setUpHeaderContent()
    {
        cuacaText.setVisibility(View.GONE);
        suhu.setVisibility(View.GONE);
        minmax.setVisibility(View.GONE);
        iconCuaca.setVisibility(View.GONE);
        kegiatan.setVisibility(View.GONE);
    }

    private void setUpFabButton()
    {
        fabButton.setOnClickListener(v -> cuacaController.loadListCuaca());
    }

    private void setUpRecyclerView()
    {
        recyclerView.setUpAsList();
        recyclerView.setAdapter(adapter);
    }

    private void setUpAdapter()
    {
        adapter = new CuacaAdapter(this);
        adapter.setOnItemClickListener((view, position) -> {
            Cuaca tmp = adapter.getData().get(position);
            Snackbar.make(view, tmp.getLokasi() + " " + tmp.getCuaca().toLowerCase() + " pada " + tmp.getTanggal() + ".", Snackbar.LENGTH_LONG).show();
        });
    }

    private void setUpToolbar()
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        cuacaController.connect();
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
                    return true;
                case R.id.berita:
                    intent = new Intent(CuacaActivity.this, BeritaActivity.class);
                    break;
                case R.id.tanya:
                    intent = new Intent(CuacaActivity.this, TanyaActivity.class);
                    break;
                case R.id.harga:
                    intent = new Intent(CuacaActivity.this, KomoditasActivity.class);
                    break;
                case R.id.logout:
                    BenihPreferenceUtils.putString(CuacaActivity.this, "nama", null);
                    intent = new Intent(CuacaActivity.this, SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    return true;
                case R.id.tentang:
                    intent = new Intent(CuacaActivity.this, TentangActivity.class);
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
        fabButton.showProgress(true);
    }

    @Override
    public void dismissLoading()
    {
        fabButton.onProgressCompleted();
        fabButton.showProgress(false);
        new Handler().postDelayed(fabButton::resetIcon, 2500);
    }

    @Override
    public void showListCuaca(List<Cuaca> listCuaca)
    {
        showHeaderContent(listCuaca.get(0));
        listCuaca.remove(0);
        adapter.clear();
        adapter.add(listCuaca);
        recyclerView.startAnimation(animation);
    }

    @Override
    public void showError(Throwable throwable)
    {
        Snackbar.make(fabButton, "Mohon periksa koneksi internet anda!", Snackbar.LENGTH_LONG).show();
    }

    private void showHeaderContent(Cuaca cuaca)
    {
        collapsingToolbar.setTitle(cuaca.getLokasi());
        collapsingToolbar.invalidate();
        cuacaText.setVisibility(View.VISIBLE);
        cuacaText.setText(cuaca.getCuaca());
        cuacaText.startAnimation(animation);
        suhu.setVisibility(View.VISIBLE);
        suhu.setText(cuaca.getSuhu() + (char) 0x2103);
        suhu.startAnimation(animation);
        minmax.setVisibility(View.VISIBLE);
        minmax.setText("Min : " + cuaca.getSuhuMin() + (char) 0x2103 + " Max : " + cuaca.getSuhuMax() + (char) 0x2103);
        minmax.startAnimation(animation);
        kegiatan.setVisibility(View.VISIBLE);
        kegiatan.setText(cuaca.getKegiatan());
        kegiatan.startAnimation(animation);
        int gambar;
        switch (cuaca.getCuaca())
        {
            case "Cerah":
                gambar = R.drawable.cerah;
                background.setBackgroundResource(R.color.cerah);
                break;
            case "Berawan":
                gambar = R.drawable.berawan;
                background.setBackgroundResource(R.color.berawan);
                break;
            case "Hujan":
                gambar = R.drawable.hujan;
                background.setBackgroundResource(R.color.hujan);
                break;
            default:
                gambar = R.drawable.berawan;
        }
        iconCuaca.setVisibility(View.VISIBLE);
        iconCuaca.setImageResource(gambar);
        iconCuaca.startAnimation(animation);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState)
    {
        cuacaController.saveState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
