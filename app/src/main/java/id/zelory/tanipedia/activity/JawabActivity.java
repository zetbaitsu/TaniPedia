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

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import id.zelory.tanipedia.R;
import id.zelory.tanipedia.adapter.JawabanAdapter;
import id.zelory.tanipedia.model.Jawaban;
import id.zelory.tanipedia.model.Soal;
import id.zelory.tanipedia.util.MyRecyclerScroll;
import id.zelory.tanipedia.util.PrefUtils;
import mbanje.kurt.fabbutton.FabButton;

public class JawabActivity extends AppCompatActivity
{

    private RecyclerView recyclerView;
    private ArrayList<Jawaban> jawabanArrayList;
    private int fabMargin;
    private FrameLayout fab;
    private ImageButton fabBtn;
    private FabButton fabButton;
    private String jawaban;
    private Soal soal;
    private String idSoal;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jawab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Tanya Tani");

        soal = getIntent().getParcelableExtra("soal");
        try
        {
            idSoal = URLEncoder.encode(soal.getId(), "UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);
        fabMargin = getResources().getDimensionPixelSize(R.dimen.fab_margin);

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

        fab = (FrameLayout) findViewById(R.id.myfab_main);
        fabBtn = (ImageButton) findViewById(R.id.myfab_main_btn);
        View fabShadow = findViewById(R.id.myfab_shadow);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            fabShadow.setVisibility(View.GONE);
            fabBtn.setBackground(getDrawable(R.drawable.ripple_accent));
        }

        fab.startAnimation(animation);

        fabBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new MaterialDialog.Builder(JawabActivity.this)
                        .title("TaniPedia")
                        .content("Kirim Jawaban")
                        .inputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT)
                        .input("Ketik jawaban anda disini!", null, false, new MaterialDialog.InputCallback()
                        {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input)
                            {
                                try
                                {
                                    jawaban = URLEncoder.encode(input.toString(), "UTF-8");
                                    new KirimJawaban().execute();
                                } catch (UnsupportedEncodingException e)
                                {
                                    Snackbar.make(fabBtn, "Terjadi kesalahan, silahkan coba lagi!", Snackbar.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        })
                        .positiveColorRes(R.color.primary_dark)
                        .positiveText("Kirim")
                        .cancelListener(new DialogInterface.OnCancelListener()
                        {
                            @Override
                            public void onCancel(DialogInterface dialog)
                            {

                            }
                        })
                        .negativeColorRes(R.color.primary_dark)
                        .negativeText("Batal")
                        .show();
            }
        });

        fabButton = (FabButton) findViewById(R.id.determinate);
        fabButton.showProgress(true);
        new DownloadData().execute();
        fabButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fabButton.showProgress(true);
                new DownloadData().execute();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private class DownloadData extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            ObjectMapper mapper = new ObjectMapper();
            jawabanArrayList = null;
            int i = 0;
            while (jawabanArrayList == null)
            {
                i++;
                try
                {
                    jawabanArrayList = mapper.readValue(new URL(Jawaban.API_AMBIL + idSoal),
                            mapper.getTypeFactory().constructCollectionType(ArrayList.class, Jawaban.class));
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                if (i >= 5)
                {
                    jawabanArrayList = new ArrayList<>();
                    break;
                }
            }

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

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            JawabanAdapter adapter = new JawabanAdapter(JawabActivity.this, jawabanArrayList);
            adapter.SetOnItemClickListener(new JawabanAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                }
            });
            recyclerView.setAdapter(adapter);
            fabButton.onProgressCompleted();
            fabButton.showProgress(false);
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    fabButton.resetIcon();
                }
            }, 2500);
        }
    }

    private class KirimJawaban extends AsyncTask<Void, Void, Void>
    {
        MaterialDialog dialog;
        String status;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new MaterialDialog.Builder(JawabActivity.this)
                    .title("TaniPedia")
                    .content("Mengirim data...")
                    .progress(true, 0)
                    .build();
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = null;
            int i = 0;
            while (root == null)
            {
                i++;
                try
                {
                    root = mapper.readTree(new URL(Jawaban.API_KIRIM + idSoal + "&email=" + PrefUtils.ambilString(JawabActivity.this, "email") + "&isi=" + jawaban));
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                if (i >= 5)
                    break;
            }
            if (i < 5)
                status = root.findValue("status").asText();
            else
                status = "Gagal";
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            if (status.equals("Berhasil"))
            {
                fabButton.showProgress(true);
                new DownloadData().execute();
                Snackbar.make(fabBtn, "Jawaban anda berhasil dikirim.", Snackbar.LENGTH_LONG).show();
            } else
            {
                Snackbar.make(fabBtn, "Terjadi kesalahan, silahkan coba lagi!", Snackbar.LENGTH_LONG).show();
            }

            dialog.dismiss();
        }
    }
}
