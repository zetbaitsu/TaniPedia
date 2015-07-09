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
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import id.zelory.tanipedia.R;
import id.zelory.tanipedia.model.Berita;
import id.zelory.tanipedia.util.Utils;
import mbanje.kurt.fabbutton.FabButton;

public class BacaActivity extends AppCompatActivity implements View.OnClickListener
{
    private Berita tmp;
    private ImageView gambar;
    private TextView judul;
    private TextView tanggal;
    private DocumentView isi;
    private FabButton fabButton;
    private Berita berita;
    private ArrayList<Berita> beritaArrayList;
    private TextView lainnya;
    private CardView berita1;
    private CardView berita2;
    private CardView berita3;
    private CardView berita4;
    private CardView berita5;
    private Animation animation;
    private LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baca);

        tmp = getIntent().getParcelableExtra("berita");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("TaniPedia");

        animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);
        gambar = (ImageView) findViewById(R.id.gambar);
        judul = (TextView) findViewById(R.id.judul);
        tanggal = (TextView) findViewById(R.id.tanggal);
        isi = (DocumentView) findViewById(R.id.isi);
        root = (LinearLayout) findViewById(R.id.ll_root_berita);

        fabButton = (FabButton) findViewById(R.id.determinate);
        fabButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, berita.getJudul());
                intent.putExtra(Intent.EXTRA_TEXT, berita.getJudul() + "\n" + berita.getAlamat());
                startActivity(Intent.createChooser(intent, "Bagikan"));
            }
        });

        generateBeritaLainnya();

        new DownloadData().execute();
    }

    private void generateBeritaLainnya()
    {
        beritaArrayList = Utils.getRandomBerita(this, tmp.getAlamat());
        lainnya = (TextView) findViewById(R.id.lainnya);
        lainnya.setVisibility(View.GONE);
        berita1 = (CardView) findViewById(R.id.berita1);
        berita1.setVisibility(View.GONE);
        berita1.setOnClickListener(this);
        berita2 = (CardView) findViewById(R.id.berita2);
        berita2.setVisibility(View.GONE);
        berita2.setOnClickListener(this);
        berita3 = (CardView) findViewById(R.id.berita3);
        berita3.setVisibility(View.GONE);
        berita3.setOnClickListener(this);
        berita4 = (CardView) findViewById(R.id.berita4);
        berita4.setVisibility(View.GONE);
        berita4.setOnClickListener(this);
        berita5 = (CardView) findViewById(R.id.berita5);
        berita5.setVisibility(View.GONE);
        berita5.setOnClickListener(this);

        TextView judul = (TextView) findViewById(R.id.judul1);
        judul.setText(beritaArrayList.get(0).getJudul());
        TextView tanggal = (TextView) findViewById(R.id.tanggal1);
        tanggal.setText(beritaArrayList.get(0).getTanggal());
        ImageView gambar = (ImageView) findViewById(R.id.gambar1);
        Picasso.with(this).load(beritaArrayList.get(0).getGambar()).into(gambar);

        judul = (TextView) findViewById(R.id.judul2);
        judul.setText(beritaArrayList.get(1).getJudul());
        tanggal = (TextView) findViewById(R.id.tanggal2);
        tanggal.setText(beritaArrayList.get(1).getTanggal());
        gambar = (ImageView) findViewById(R.id.gambar2);
        Picasso.with(this).load(beritaArrayList.get(1).getGambar()).into(gambar);

        judul = (TextView) findViewById(R.id.judul3);
        judul.setText(beritaArrayList.get(2).getJudul());
        tanggal = (TextView) findViewById(R.id.tanggal3);
        tanggal.setText(beritaArrayList.get(2).getTanggal());
        gambar = (ImageView) findViewById(R.id.gambar3);
        Picasso.with(this).load(beritaArrayList.get(2).getGambar()).into(gambar);

        judul = (TextView) findViewById(R.id.judul4);
        judul.setText(beritaArrayList.get(3).getJudul());
        tanggal = (TextView) findViewById(R.id.tanggal4);
        tanggal.setText(beritaArrayList.get(3).getTanggal());
        gambar = (ImageView) findViewById(R.id.gambar4);
        Picasso.with(this).load(beritaArrayList.get(3).getGambar()).into(gambar);

        judul = (TextView) findViewById(R.id.judul5);
        judul.setText(beritaArrayList.get(4).getJudul());
        tanggal = (TextView) findViewById(R.id.tanggal5);
        tanggal.setText(beritaArrayList.get(4).getTanggal());
        gambar = (ImageView) findViewById(R.id.gambar5);
        Picasso.with(this).load(beritaArrayList.get(4).getGambar()).into(gambar);
    }

    private void showBeritaLainnya()
    {
        lainnya.setVisibility(View.VISIBLE);
        lainnya.startAnimation(animation);
        berita1.setVisibility(View.VISIBLE);
        berita1.startAnimation(animation);
        berita2.setVisibility(View.VISIBLE);
        berita2.startAnimation(animation);
        berita3.setVisibility(View.VISIBLE);
        berita3.startAnimation(animation);
        berita4.setVisibility(View.VISIBLE);
        berita4.startAnimation(animation);
        berita5.setVisibility(View.VISIBLE);
        berita5.startAnimation(animation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent = new Intent(this, BacaActivity.class);
        switch (v.getId())
        {
            case R.id.berita1:
                intent.putExtra("berita", beritaArrayList.get(0));
                break;
            case R.id.berita2:
                intent.putExtra("berita", beritaArrayList.get(1));
                break;
            case R.id.berita3:
                intent.putExtra("berita", beritaArrayList.get(2));
                break;
            case R.id.berita4:
                intent.putExtra("berita", beritaArrayList.get(3));
                break;
            case R.id.berita5:
                intent.putExtra("berita", beritaArrayList.get(4));
                break;
        }
        startActivity(intent);
    }

    private class DownloadData extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            fabButton.showProgress(true);
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            ObjectMapper mapper = new ObjectMapper();
            berita = null;
            int i = 0;
            while (berita == null)
            {
                i++;
                try
                {
                    berita = mapper.readValue(new URL(Berita.API + "?url=" + tmp.getAlamat()), Berita.class);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                if (i >= 5)
                {
                    berita = tmp;
                    berita.setIsi("");
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            Picasso.with(BacaActivity.this).load(berita.getGambar()).into(gambar);
            judul.setText(berita.getJudul());
            tanggal.setText("TaniPedia - " + berita.getTanggal());
            isi.setText(Html.fromHtml(berita.getIsi()));
            isi.invalidateCache();
            showBeritaLainnya();
            fabButton.onProgressCompleted();
            fabButton.showProgress(false);
            root.setVisibility(View.VISIBLE);
            if (berita.getIsi().equals(""))
            {
                Snackbar.make(fabButton, "Mohon periksa koneksi internet anda!", Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
