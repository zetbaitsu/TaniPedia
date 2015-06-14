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
import android.location.Location;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import id.zelory.tanipedia.R;
import id.zelory.tanipedia.adapter.CuacaAdapter;
import id.zelory.tanipedia.model.Cuaca;
import id.zelory.tanipedia.util.PrefUtils;
import mbanje.kurt.fabbutton.FabButton;

public class CuacaActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{

    private CollapsingToolbarLayout collapsingToolbar;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private GoogleApiClient googleApiClient;
    private double latitude;
    private double longitude;
    private ArrayList<Cuaca> cuacaArrayList;
    private FabButton fabButton;
    private TextView cuacaText;
    private TextView suhu;
    private TextView minmax;
    private ImageView iconCuaca;
    private TextView kegiatan;
    private LinearLayout background;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuaca);
        toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);

        drawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        setUpNavDrawer();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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
                        PrefUtils.simpanString(CuacaActivity.this, "nama", null);
                        intent = new Intent(CuacaActivity.this, LoginActivity.class);
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
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.scrollableview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        fabButton = (FabButton) findViewById(R.id.determinate);
        fabButton.showProgress(true);
        fabButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fabButton.showProgress(true);
                new DownloadData().execute();
            }
        });

        cuacaText = (TextView) findViewById(R.id.cuaca);
        suhu = (TextView) findViewById(R.id.suhu);
        minmax = (TextView) findViewById(R.id.minmax);
        iconCuaca = (ImageView) findViewById(R.id.icon_cuaca);
        kegiatan = (TextView) findViewById(R.id.kegiatan);
        background = (LinearLayout) findViewById(R.id.background);

        cuacaText.setVisibility(View.GONE);
        suhu.setVisibility(View.GONE);
        minmax.setVisibility(View.GONE);
        iconCuaca.setVisibility(View.GONE);
        kegiatan.setVisibility(View.GONE);

        if (checkPlayServices())
        {
            buildGoogleApiClient();
        }

    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (googleApiClient != null)
        {
            googleApiClient.connect();
        }
    }

    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else
            {
                Toast.makeText(this,
                        "HP anda belum memenuhi syarat.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        checkPlayServices();
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

    @Override
    public void onConnected(Bundle bundle)
    {
        Location location = LocationServices.FusedLocationApi
                .getLastLocation(googleApiClient);

        if (location != null)
        {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            PrefUtils.simpanDouble(CuacaActivity.this, "lat", latitude);
            PrefUtils.simpanDouble(CuacaActivity.this, "lon", longitude);
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    new DownloadData().execute();
                }
            }, 800);
        } else
        {
            latitude = PrefUtils.ambilDouble(CuacaActivity.this, "lat");
            longitude = PrefUtils.ambilDouble(CuacaActivity.this, "lon");
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    new DownloadData().execute();
                }
            }, 800);
        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        latitude = PrefUtils.ambilDouble(CuacaActivity.this, "lat");
        longitude = PrefUtils.ambilDouble(CuacaActivity.this, "lon");
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                new DownloadData().execute();
            }
        }, 800);
    }

    private class DownloadData extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            ObjectMapper mapper = new ObjectMapper();
            cuacaArrayList = null;
            int i = 0;

            while (cuacaArrayList == null)
            {
                i++;
                try
                {
                    cuacaArrayList = mapper.readValue(new URL(Cuaca.API + "lat=" + latitude + "&lon=" + longitude),
                            mapper.getTypeFactory().constructCollectionType(ArrayList.class, Cuaca.class));
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                if (i >= 5)
                {
                    try
                    {
                        cuacaArrayList = mapper.readValue(PrefUtils.ambilString(CuacaActivity.this, "cuaca"),
                                mapper.getTypeFactory().constructCollectionType(ArrayList.class, Cuaca.class));
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
                PrefUtils.simpanString(CuacaActivity.this, "cuaca", mapper.writeValueAsString(cuacaArrayList));
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
            if (cuacaArrayList != null && !cuacaArrayList.isEmpty())
            {
                Cuaca cuaca = cuacaArrayList.get(0);
                cuacaArrayList.remove(0);
                CuacaAdapter adapter = new CuacaAdapter(CuacaActivity.this, cuacaArrayList);
                adapter.SetOnItemClickListener(new CuacaAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        Cuaca tmp = cuacaArrayList.get(position);
                        Snackbar.make(view, tmp.getLokasi() + " " + tmp.getCuaca().toLowerCase() + " pada " + tmp.getTanggal() + ".", Snackbar.LENGTH_LONG).show();
                    }
                });

                recyclerView.setAdapter(adapter);
                recyclerView.startAnimation(animation);

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
            } else
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
