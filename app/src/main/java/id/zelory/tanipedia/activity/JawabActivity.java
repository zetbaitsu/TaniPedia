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

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.afollestad.materialdialogs.MaterialDialog;

import id.zelory.benih.BenihActivity;
import id.zelory.benih.utils.BenihScheduler;
import id.zelory.benih.utils.PrefUtils;
import id.zelory.benih.views.BenihRecyclerView;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.adapter.JawabanAdapter;
import id.zelory.tanipedia.model.Jawaban;
import id.zelory.tanipedia.model.Soal;
import id.zelory.tanipedia.network.TaniPediaService;
import id.zelory.tanipedia.util.MyRecyclerScroll;
import mbanje.kurt.fabbutton.FabButton;

public class JawabActivity extends BenihActivity
{

    private BenihRecyclerView recyclerView;
    private JawabanAdapter adapter;
    private int fabMargin;
    private FrameLayout fab;
    private ImageButton fabBtn;
    private FabButton fabButton;
    private String jawaban;
    private Soal soal;

    @Override
    protected int getActivityView()
    {
        return R.layout.activity_jawab;
    }

    @Override
    protected void onViewReady(Bundle bundle)
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Tanya Tani");

        soal = getIntent().getParcelableExtra("soal");

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);
        fabMargin = getResources().getDimensionPixelSize(R.dimen.fab_margin);

        recyclerView = (BenihRecyclerView) findViewById(R.id.scrollableview);
        recyclerView.setUpAsList();
        adapter = new JawabanAdapter(this);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((view, position) -> {
        });

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

        fab = (FrameLayout) findViewById(R.id.myfab_main);
        fabBtn = (ImageButton) findViewById(R.id.myfab_main_btn);
        View fabShadow = findViewById(R.id.myfab_shadow);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            fabShadow.setVisibility(View.GONE);
            fabBtn.setBackground(getDrawable(R.drawable.ripple_accent));
        }

        fab.startAnimation(animation);

        fabBtn.setOnClickListener(v -> new MaterialDialog.Builder(JawabActivity.this)
                .title("TaniPedia")
                .content("Kirim Jawaban")
                .inputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT)
                .input("Ketik jawaban anda disini!", null, false, (dialog, input) -> {
                    jawaban = input.toString();
                    postJawaban(soal.getId(), PrefUtils.getString(this, "email"), jawaban);
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
        getJawaban(soal.getId());
        fabButton.setOnClickListener(v -> {
            fabButton.showProgress(true);
            getJawaban(soal.getId());
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void getJawaban(String idSoal)
    {
        TaniPediaService.getApi()
                .getJawaban(idSoal)
                .compose(BenihScheduler.applySchedulers(BenihScheduler.Type.IO))
                .subscribe(jawabanArrayList -> {

                    Jawaban jawaban = new Jawaban();
                    jawaban.setIdSoal(soal.getId());
                    jawaban.setNama(soal.getNama());
                    jawaban.setIsi(soal.getIsi());
                    jawaban.setTanggal(soal.getTanggal());
                    jawabanArrayList.add(0, jawaban);

                    if (jawabanArrayList.size() == 1)
                    {
                        jawaban = new Jawaban();
                        jawaban.setIdSoal(soal.getId());
                        jawaban.setNama("TaniPedia");
                        jawaban.setIsi("Pertanyaan ini belum mempunyai jawaban satupun, jadilah orang pertama yang bisa membantu " + soal.getNama() + "!");
                        jawaban.setTanggal(soal.getTanggal());
                        jawabanArrayList.add(jawaban);
                    }
                    adapter.clear();
                    adapter.add(jawabanArrayList);
                    fabButton.onProgressCompleted();
                    fabButton.showProgress(false);
                    new Handler().postDelayed(fabButton::resetIcon, 2500);
                }, throwable -> {
                    fabButton.onProgressCompleted();
                    fabButton.showProgress(false);
                    new Handler().postDelayed(fabButton::resetIcon, 2500);
                });
    }

    private void postJawaban(String idSoal, String email, String isi)
    {
        MaterialDialog dialog = new MaterialDialog.Builder(JawabActivity.this)
                .title("TaniPedia")
                .content("Mengirim data...")
                .progress(true, 0)
                .build();
        dialog.show();

        TaniPediaService.getApi()
                .postJawaban(idSoal, email, isi)
                .compose(BenihScheduler.applySchedulers(BenihScheduler.Type.IO))
                .subscribe(status -> {
                    if (status.getStatus())
                    {
                        fabButton.showProgress(true);
                        getJawaban(idSoal);
                        Snackbar.make(fabBtn, "Jawaban anda berhasil dikirim.", Snackbar.LENGTH_LONG).show();
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
