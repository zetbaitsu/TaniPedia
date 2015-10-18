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

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import id.zelory.benih.BenihActivity;
import id.zelory.benih.view.BenihRecyclerView;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.controller.SoalController;
import id.zelory.tanipedia.data.model.PakTani;
import id.zelory.tanipedia.data.model.Soal;
import id.zelory.tanipedia.ui.adapter.SoalAdapter;
import mbanje.kurt.fabbutton.FabButton;

/**
 * Created on : October 16, 2015
 * Author     : zetbaitsu
 * Name       : Zetra
 * Email      : zetra@mail.ugm.ac.id
 * GitHub     : https://github.com/zetbaitsu
 * LinkedIn   : https://id.linkedin.com/in/zetbaitsu
 */
public class ProfileActivity extends BenihActivity implements SoalController.Presenter
{
    public static final String KEY_PAK_TANI = "pak_tani";

    @Bind(R.id.email) TextView email;
    @Bind(R.id.pak_tani) FabButton foto;
    @Bind(R.id.progress) ProgressBar progressBar;
    @Bind(R.id.background) View background;
    @Bind(R.id.scrollableview) BenihRecyclerView recyclerView;
    private SoalController soalController;
    private PakTani pakTani;
    private SoalAdapter adapter;

    @Override
    protected int getActivityView()
    {
        return R.layout.activity_profile;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState)
    {

        pakTani = getIntent().getParcelableExtra(KEY_PAK_TANI);
        if (pakTani == null && savedInstanceState != null)
        {
            pakTani = savedInstanceState.getParcelable(KEY_PAK_TANI);
        }
        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = ButterKnife.findById(this, R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(pakTani.getNama());
        email.setText(pakTani.getEmail());
        adapter = new SoalAdapter(this);
        adapter.setOnItemClickListener(this::onItemClick);
        recyclerView.setAdapter(adapter);
        recyclerView.setUpAsList();
        setUpController(savedInstanceState);
    }

    private void onItemClick(View view, int position)
    {
        Intent intent = new Intent(this, JawabActivity.class);
        intent.putExtra("soal", adapter.getData().get(position));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.edit_profile:
                break;
            case R.id.refresh:
                soalController.loadListSoal(pakTani.getEmail());
                break;
            case android.R.id.home:
                onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpController(Bundle savedInstanceState)
    {
        if (soalController == null)
        {
            soalController = new SoalController(this);
        }

        if (savedInstanceState != null)
        {
            soalController.loadState(savedInstanceState);
        } else
        {
            soalController.loadListSoal(pakTani.getEmail());
        }
    }

    @Override
    public void showError(Throwable throwable)
    {

    }

    @Override
    public void showLoading()
    {
        background.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingDialog()
    {

    }

    @Override
    public void dismissLoading()
    {
        progressBar.setVisibility(View.GONE);
        background.setVisibility(View.GONE);
    }

    @Override
    public void showListSoal(List<Soal> listSoal)
    {
        adapter.clear();
        adapter.add(listSoal);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState)
    {
        soalController.saveState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable(KEY_PAK_TANI, pakTani);
    }
}
