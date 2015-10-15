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

package id.zelory.tanipedia.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import id.zelory.benih.adapter.BenihRecyclerAdapter;
import id.zelory.benih.controller.BenihController;
import id.zelory.benih.fragment.BenihFragment;
import id.zelory.benih.view.BenihRecyclerView;
import id.zelory.tanipedia.R;
import mbanje.kurt.fabbutton.FabButton;

/**
 * Created on : October 15, 2015
 * Author     : zetbaitsu
 * Name       : Zetra
 * Email      : zetra@mail.ugm.ac.id
 * GitHub     : https://github.com/zetbaitsu
 * LinkedIn   : https://id.linkedin.com/in/zetbaitsu
 */
public abstract class BaseFragment<Controller extends BenihController, Adapter extends BenihRecyclerAdapter> extends
        BenihFragment
{
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.determinate) FabButton fabButton;
    @Bind(R.id.anim_toolbar) Toolbar toolbar;
    @Bind(R.id.scrollableview) BenihRecyclerView recyclerView;
    protected Controller controller;
    protected Adapter adapter;
    protected DrawerLayout drawerLayout;
    protected Animation animation;

    @Override
    protected void onViewReady(Bundle savedInstanceState)
    {
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.simple_grow);
        setUpToolbar();
        adapter = createAdapter();
        adapter.setOnItemClickListener(this::onItemClick);
        setUpRecyclerView();
        fabButton.setOnClickListener(this::onFabClick);
        setUpController(savedInstanceState);
    }

    protected abstract void onFabClick(View view);

    protected abstract void onItemClick(View view, int position);

    protected abstract Adapter createAdapter();

    protected abstract void setUpController(Bundle savedInstanceState);

    protected void setUpToolbar()
    {
        drawerLayout = ButterKnife.findById(getActivity(), R.id.nav_drawer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
    }

    protected void setUpRecyclerView()
    {
        recyclerView.setUpAsList();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        controller.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    public Bundle getState()
    {
        Bundle bundle = new Bundle();
        if (controller != null)
        {
            controller.saveState(bundle);
        }
        return bundle;
    }

    public void showLoading()
    {
        fabButton.showProgress(true);
    }

    public void dismissLoading()
    {
        fabButton.onProgressCompleted();
        fabButton.showProgress(false);
        new Handler().postDelayed(fabButton::resetIcon, 2500);
    }

    public void showError(Throwable throwable)
    {
        Snackbar.make(fabButton, "Gagal memuat data!", Snackbar.LENGTH_LONG).show();
    }
}
