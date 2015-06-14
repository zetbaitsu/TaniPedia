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
import android.os.AsyncTask;
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
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import id.zelory.tanipedia.R;
import id.zelory.tanipedia.adapter.BeritaAdapter;
import id.zelory.tanipedia.model.Berita;
import id.zelory.tanipedia.model.Cuaca;
import id.zelory.tanipedia.util.PrefUtils;
import id.zelory.tanipedia.util.Utils;
import mbanje.kurt.fabbutton.FabButton;


public class BeritaActivity extends AppCompatActivity
{
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ArrayList<Berita> beritaArrayList;
    private ImageView imageHeader;
    private FabButton fabButton;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita);
        toolbar = (Toolbar) findViewById(R.id.anim_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Berita Terbaru");

        animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);

        drawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        setUpNavDrawer();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);
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
                        PrefUtils.simpanString(BeritaActivity.this, "nama", null);
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
            }
        });
        imageHeader = (ImageView) findViewById(R.id.header);
        recyclerView = (RecyclerView) findViewById(R.id.scrollableview);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

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
            beritaArrayList = null;
            int i = 0;

            while (beritaArrayList == null)
            {
                i++;
                try
                {
                    beritaArrayList = mapper.readValue(new URL(Berita.API),
                            mapper.getTypeFactory().constructCollectionType(ArrayList.class, Berita.class));
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                if (i >= 5)
                {
                    try
                    {
                        beritaArrayList = mapper.readValue(PrefUtils.ambilString(BeritaActivity.this, "berita"),
                                mapper.getTypeFactory().constructCollectionType(ArrayList.class, Berita.class));
                    } catch (Exception e)
                    {
                        Snackbar.make(fabButton, "Terjadi kesalahan silahkan coba lagi!", Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    break;
                }
            }

            try
            {
                PrefUtils.simpanString(BeritaActivity.this, "berita", mapper.writeValueAsString(beritaArrayList));
            } catch (JsonProcessingException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            if (beritaArrayList != null && !beritaArrayList.isEmpty())
            {
                BeritaAdapter adapter = new BeritaAdapter(BeritaActivity.this, beritaArrayList);
                adapter.SetOnItemClickListener(new BeritaAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        Intent intent = new Intent(BeritaActivity.this, BacaActivity.class);
                        intent.putExtra("berita", beritaArrayList.get(position));
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(adapter);
                recyclerView.startAnimation(animation);
                String url = beritaArrayList.get(Utils.randInt(0, beritaArrayList.size() - 1)).getGambar();
                Picasso.with(BeritaActivity.this)
                        .load(url)
                        .into(imageHeader);
                imageHeader.startAnimation(animation);
            }else
            {
                Snackbar.make(fabButton, "Mohon periksa koneksi internet anda!", Snackbar.LENGTH_LONG).show();
            }

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
}
