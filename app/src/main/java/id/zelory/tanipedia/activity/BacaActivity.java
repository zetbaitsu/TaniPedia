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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

import id.zelory.tanipedia.R;
import id.zelory.tanipedia.model.Berita;
import mbanje.kurt.fabbutton.FabButton;

public class BacaActivity extends AppCompatActivity
{
    private Berita tmp;
    private ImageView gambar;
    private TextView judul;
    private TextView tanggal;
    private DocumentView isi;
    private FabButton fabButton;
    private Berita berita;

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
        gambar = (ImageView) findViewById(R.id.gambar);
        judul = (TextView) findViewById(R.id.judul);
        tanggal = (TextView) findViewById(R.id.tanggal);
        isi = (DocumentView) findViewById(R.id.isi);

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

        new DownloadData().execute();
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
        protected void onPreExecute()
        {
            fabButton.showProgress(true);
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            ObjectMapper mapper = new ObjectMapper();
            berita = null;
            while (berita == null)
            {
                try
                {
                    berita = mapper.readValue(new URL(Berita.API + "?url=" + tmp.getAlamat()), Berita.class);
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
            Picasso.with(BacaActivity.this).load(berita.getGambar()).into(gambar);
            judul.setText(berita.getJudul());
            tanggal.setText("TaniPedia - " + berita.getTanggal());
            isi.setText(Html.fromHtml(berita.getIsi()));
            isi.invalidateCache();
        }
    }
}
