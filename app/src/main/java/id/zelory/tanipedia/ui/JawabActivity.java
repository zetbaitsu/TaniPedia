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

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.benih.BenihActivity;
import id.zelory.benih.util.BenihPreferenceUtils;
import id.zelory.benih.view.BenihRecyclerView;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.controller.BookmarkSoalController;
import id.zelory.tanipedia.controller.JawabController;
import id.zelory.tanipedia.controller.util.MyRecyclerScroll;
import id.zelory.tanipedia.data.model.Jawaban;
import id.zelory.tanipedia.data.model.Soal;
import id.zelory.tanipedia.ui.adapter.JawabanAdapter;
import mbanje.kurt.fabbutton.FabButton;

public class JawabActivity extends BenihActivity implements JawabController.Presenter,
        BookmarkSoalController.Presenter
{
    @Bind(R.id.scrollableview) BenihRecyclerView recyclerView;
    @Bind(R.id.myfab_main) FrameLayout frameFab;
    @Bind(R.id.myfab_main_btn) ImageButton fabAddJawaban;
    @Bind(R.id.determinate) FabButton fabRefreshJawaban;
    private JawabController controller;
    private JawabanAdapter adapter;
    private int fabMargin;
    private Soal soal;
    private MaterialDialog dialog;
    private BookmarkSoalController bookmarkController;
    private MenuItem simpan;

    @Override
    protected int getActivityView()
    {
        return R.layout.activity_jawab;
    }

    @Override
    protected void onViewReady(Bundle bundle)
    {
        controller = new JawabController(this);
        bookmarkController = new BookmarkSoalController(this);
        soal = getIntent().getParcelableExtra("soal");

        setUpToolbar();
        setUpAdapter();
        setUpRecyclerView();
        setUpFab();
        setUpLoadingDialog();

        controller.loadJawaban(soal);
        bookmarkController.setSoal(soal);
    }

    private void setUpLoadingDialog()
    {
        dialog = new MaterialDialog.Builder(this)
                .title("TaniPedia")
                .content("Mengirim jawaban anda...")
                .progress(true, 0)
                .build();
    }

    private void setUpFab()
    {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);
        frameFab.startAnimation(animation);

        fabMargin = getResources().getDimensionPixelSize(R.dimen.fab_margin);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            ButterKnife.findById(this, R.id.myfab_shadow).setVisibility(View.GONE);
            fabAddJawaban.setBackground(getDrawable(R.drawable.ripple_accent));
        }

        fabRefreshJawaban.setOnClickListener(v -> controller.loadJawaban(soal));
    }

    @OnClick(R.id.myfab_main_btn)
    void addJawaban()
    {
        new MaterialDialog.Builder(this)
                .title("TaniPedia")
                .content("Kirim Jawaban")
                .inputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT)
                .input("Ketik jawaban anda disini!", null, false, (dialog, input) -> {
                    controller.sendJawaban(soal, BenihPreferenceUtils.getString(this, "email"), input.toString());
                })
                .positiveColorRes(R.color.primary_dark)
                .positiveText("Kirim")
                .cancelListener(dialog -> {})
                .negativeColorRes(R.color.primary_dark)
                .negativeText("Batal")
                .show();
    }

    private void setUpAdapter()
    {
        adapter = new JawabanAdapter(this);
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

    private void setUpToolbar()
    {
        Toolbar toolbar = ButterKnife.findById(this, R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = ButterKnife.findById(this, R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Tanya Tani");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.baca, menu);
        simpan = menu.getItem(0);
        simpan.setChecked(soal.isBookmarked());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.simpan:
                bookmarkController.bookmark(soal);
                break;
            case R.id.refresh:
                controller.loadJawaban(soal);
                break;
            case android.R.id.home:
                onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoading()
    {
        fabRefreshJawaban.showProgress(true);
    }

    @Override
    public void showLoadingDialog()
    {
        dialog.show();
    }

    @Override
    public void dismissLoading()
    {
        fabRefreshJawaban.onProgressCompleted();
        fabRefreshJawaban.showProgress(false);
        new Handler().postDelayed(fabRefreshJawaban::resetIcon, 2500);
        if (dialog != null)
        {
            dialog.dismiss();
        }
    }

    @Override
    public void showJawaban(List<Jawaban> jawaban)
    {
        adapter.clear();
        adapter.add(jawaban);
    }

    @Override
    public void showError(Throwable throwable)
    {
        Snackbar.make(fabAddJawaban, throwable.getMessage(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showListBookmarkedSoal(List<Soal> listSoal)
    {

    }

    @Override
    public void onBookmark(Soal soal)
    {
        if (simpan != null)
        {
            simpan.setChecked(soal.isBookmarked());
        }
    }

    @Override
    public void onUnBookmark(Soal soal)
    {
        if (simpan != null)
        {
            simpan.setChecked(soal.isBookmarked());
        }
    }
}
