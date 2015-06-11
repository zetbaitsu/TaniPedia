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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import id.zelory.tanipedia.R;
import id.zelory.tanipedia.adapter.SoalAdapter;
import id.zelory.tanipedia.model.Soal;
import id.zelory.tanipedia.util.MyRecyclerScroll;
import id.zelory.tanipedia.util.PrefUtils;
import mbanje.kurt.fabbutton.FabButton;

public class TanyaActivity extends AppCompatActivity
{
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ArrayList<Soal> soalArrayList;
    private int fabMargin;
    private FrameLayout fab;
    private ImageButton fabBtn;
    private FabButton fabButton;
    private String pertanyaan;
    private Animation animation;
    private ImageView tanyaGambar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tanya);
        toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Tanya Tani");

        animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);
        fabMargin = getResources().getDimensionPixelSize(R.dimen.fab_margin);

        drawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        setUpNavDrawer();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(2).setChecked(true);
        TextView nama = (TextView) navigationView.findViewById(R.id.nama);
        nama.setText(PrefUtils.ambilString(this, "nama"));
        TextView email = (TextView) navigationView.findViewById(R.id.email);
        email.setText(PrefUtils.ambilString(this, "email"));
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem)
            {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                Intent intent;
                switch (menuItem.getItemId())
                {
                    case R.id.cuaca:
                        intent = new Intent(TanyaActivity.this, CuacaActivity.class);
                        break;
                    case R.id.berita:
                        intent = new Intent(TanyaActivity.this, BeritaActivity.class);
                        break;
                    case R.id.tanya:
                        return true;
                    case R.id.harga:
                        intent = new Intent(TanyaActivity.this, KomoditasActivity.class);
                        break;
                    case R.id.logout:
                        PrefUtils.simpanString(TanyaActivity.this, "nama", null);
                        intent = new Intent(TanyaActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        return true;
                    case R.id.tentang:
                        intent = new Intent(TanyaActivity.this, TentangActivity.class);
                        startActivity(intent);
                        return true;
                    default:
                        return true;
                }
                startActivity(intent);
                finish();
                return true;
            }
        });

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

        tanyaGambar = (ImageView) findViewById(R.id.tanya_gambar);
        tanyaGambar.setVisibility(View.GONE);
        fab = (FrameLayout) findViewById(R.id.myfab_main);
        fab.setVisibility(View.GONE);
        fabBtn = (ImageButton) findViewById(R.id.myfab_main_btn);
        View fabShadow = findViewById(R.id.myfab_shadow);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            fabShadow.setVisibility(View.GONE);
            fabBtn.setBackground(getDrawable(R.drawable.ripple_accent));
        }

        fabBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new MaterialDialog.Builder(TanyaActivity.this)
                        .title("TaniPedia")
                        .content("Kirim Pertanyaan")
                        .inputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT)
                        .input("Ketik pertanyaan anda disini!", null, false, new MaterialDialog.InputCallback()
                        {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input)
                            {
                                try
                                {
                                    pertanyaan = URLEncoder.encode(input.toString(), "UTF-8");
                                    new KirimSoal().execute();
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

    private void setUpNavDrawer()
    {
        if (toolbar != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_drawer);
            toolbar.setNavigationOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }
    }

    private class DownloadData extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            ObjectMapper mapper = new ObjectMapper();
            soalArrayList = null;
            while (soalArrayList == null)
            {
                try
                {
                    soalArrayList = mapper.readValue(new URL(Soal.API_AMBIL),
                            mapper.getTypeFactory().constructCollectionType(ArrayList.class, Soal.class));
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            fabButton.onProgressCompleted();

            SoalAdapter adapter = new SoalAdapter(TanyaActivity.this, soalArrayList);
            adapter.SetOnItemClickListener(new SoalAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    Intent intent = new Intent(TanyaActivity.this, JawabActivity.class);
                    intent.putExtra("soal", soalArrayList.get(position));
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapter);
            recyclerView.startAnimation(animation);
            fab.startAnimation(animation);
            fab.setVisibility(View.VISIBLE);
            tanyaGambar.setVisibility(View.VISIBLE);
            tanyaGambar.startAnimation(animation);
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

    private class KirimSoal extends AsyncTask<Void, Void, Void>
    {
        MaterialDialog dialog;
        String status;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new MaterialDialog.Builder(TanyaActivity.this)
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

            while (root == null)
            {
                try
                {
                    root = mapper.readTree(new URL(Soal.API_KIRIM + "?email=" + PrefUtils.ambilString(TanyaActivity.this, "email") + "&isi=" + pertanyaan));
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            status = root.findValue("status").asText();
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
                Snackbar.make(fabBtn, "Pertanyaan anda berhasil dikirim.", Snackbar.LENGTH_LONG).show();
            } else
            {
                Snackbar.make(fabBtn, "Terjadi kesalahan, silahkan coba lagi!", Snackbar.LENGTH_LONG).show();
            }

            dialog.dismiss();
        }
    }
}
