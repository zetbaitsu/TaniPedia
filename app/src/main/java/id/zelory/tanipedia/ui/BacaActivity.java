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
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import id.zelory.benih.BenihActivity;
import id.zelory.benih.util.BenihUtils;
import id.zelory.benih.view.BenihImageView;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.controller.BeritaController;
import id.zelory.tanipedia.controller.BookmarkBeritaController;
import id.zelory.tanipedia.controller.util.Utils;
import id.zelory.tanipedia.data.model.Berita;
import mbanje.kurt.fabbutton.FabButton;

public class BacaActivity extends BenihActivity implements BeritaController.Presenter,
        BookmarkBeritaController.Presenter
{
    @Bind(R.id.gambar) BenihImageView gambar;
    @Bind(R.id.judul) TextView judul;
    @Bind(R.id.tanggal) TextView tanggal;
    @Bind(R.id.isi) DocumentView isi;
    @Bind(R.id.determinate) FabButton fabShareBerita;
    @Bind(R.id.ll_root_berita) LinearLayout root;
    private BeritaController beritaController;
    private TextView lainnya;
    private CardView berita1;
    private CardView berita2;
    private CardView berita3;
    private CardView berita4;
    private CardView berita5;
    private Animation animation;
    private Berita berita;
    private BookmarkBeritaController bookmarkController;
    private MenuItem simpan;

    @Override
    protected int getActivityView()
    {
        return R.layout.activity_baca;
    }

    @Override
    protected void onViewReady(Bundle bundle)
    {
        animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);
        berita = getIntent().getParcelableExtra("berita");
        gambar.setBackgroundColor(BenihUtils.getRandomColor());

        setUpToolbar();
        setUpFabButton(berita);
        generateBeritaLainnya(berita);
        setUpController(bundle, berita);
    }

    private void setUpFabButton(Berita berita)
    {
        fabShareBerita.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, berita.getJudul());
            intent.putExtra(Intent.EXTRA_TEXT, berita.getJudul() + "\n" + berita.getAlamat());
            startActivity(Intent.createChooser(intent, "Bagikan"));
        });
    }

    private void setUpToolbar()
    {
        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = ButterKnife.findById(this, R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("TaniPedia");
    }

    private void setUpController(Bundle bundle, Berita berita)
    {
        bookmarkController = new BookmarkBeritaController(this);
        if (beritaController == null)
        {
            beritaController = new BeritaController(this);
        }


        if (berita.getIsi() != null)
        {
            beritaController.setBerita(berita);
            showBerita(berita);
        } else if (bundle == null)
        {
            beritaController.loadBerita(berita.getAlamat());
        } else
        {
            beritaController.loadState(bundle);
        }
    }

    private void generateBeritaLainnya(Berita berita)
    {
        ArrayList<Berita> randomBerita = Utils.getRandomBerita(this, berita.getAlamat());

        lainnya = ButterKnife.findById(this, R.id.lainnya);
        lainnya.setVisibility(View.GONE);

        berita1 = ButterKnife.findById(this, R.id.berita1);
        berita1.setVisibility(View.GONE);
        berita1.setOnClickListener(v -> {
            Intent intent = new Intent(this, BacaActivity.class);
            intent.putExtra("berita", randomBerita.get(0));
            startActivity(intent);
        });

        berita2 = ButterKnife.findById(this, R.id.berita2);
        berita2.setVisibility(View.GONE);
        berita2.setOnClickListener(v -> {
            Intent intent = new Intent(this, BacaActivity.class);
            intent.putExtra("berita", randomBerita.get(1));
            startActivity(intent);
        });

        berita3 = ButterKnife.findById(this, R.id.berita3);
        berita3.setVisibility(View.GONE);
        berita3.setOnClickListener(v -> {
            Intent intent = new Intent(this, BacaActivity.class);
            intent.putExtra("berita", randomBerita.get(2));
            startActivity(intent);
        });

        berita4 = ButterKnife.findById(this, R.id.berita4);
        berita4.setVisibility(View.GONE);
        berita4.setOnClickListener(v -> {
            Intent intent = new Intent(this, BacaActivity.class);
            intent.putExtra("berita", randomBerita.get(3));
            startActivity(intent);
        });

        berita5 = ButterKnife.findById(this, R.id.berita5);
        berita5.setVisibility(View.GONE);
        berita5.setOnClickListener(v -> {
            Intent intent = new Intent(this, BacaActivity.class);
            intent.putExtra("berita", randomBerita.get(4));
            startActivity(intent);
        });

        TextView judul = ButterKnife.findById(this, R.id.judul1);
        judul.setText(randomBerita.get(0).getJudul());
        TextView tanggal = ButterKnife.findById(this, R.id.tanggal1);
        tanggal.setText(randomBerita.get(0).getTanggal());
        ImageView gambar = ButterKnife.findById(this, R.id.gambar1);
        gambar.setBackgroundColor(BenihUtils.getRandomColor());
        Glide.with(this).load(randomBerita.get(0).getGambar()).into(gambar);

        judul = ButterKnife.findById(this, R.id.judul2);
        judul.setText(randomBerita.get(1).getJudul());
        tanggal = ButterKnife.findById(this, R.id.tanggal2);
        tanggal.setText(randomBerita.get(1).getTanggal());
        gambar = ButterKnife.findById(this, R.id.gambar2);
        gambar.setBackgroundColor(BenihUtils.getRandomColor());
        Glide.with(this).load(randomBerita.get(1).getGambar()).into(gambar);

        judul = ButterKnife.findById(this, R.id.judul3);
        judul.setText(randomBerita.get(2).getJudul());
        tanggal = ButterKnife.findById(this, R.id.tanggal3);
        tanggal.setText(randomBerita.get(2).getTanggal());
        gambar = ButterKnife.findById(this, R.id.gambar3);
        gambar.setBackgroundColor(BenihUtils.getRandomColor());
        Glide.with(this).load(randomBerita.get(2).getGambar()).into(gambar);

        judul = ButterKnife.findById(this, R.id.judul4);
        judul.setText(randomBerita.get(3).getJudul());
        tanggal = ButterKnife.findById(this, R.id.tanggal4);
        tanggal.setText(randomBerita.get(3).getTanggal());
        gambar = ButterKnife.findById(this, R.id.gambar4);
        gambar.setBackgroundColor(BenihUtils.getRandomColor());
        Glide.with(this).load(randomBerita.get(3).getGambar()).into(gambar);

        judul = ButterKnife.findById(this, R.id.judul5);
        judul.setText(randomBerita.get(4).getJudul());
        tanggal = ButterKnife.findById(this, R.id.tanggal5);
        tanggal.setText(randomBerita.get(4).getTanggal());
        gambar = ButterKnife.findById(this, R.id.gambar5);
        gambar.setBackgroundColor(BenihUtils.getRandomColor());
        Glide.with(this).load(randomBerita.get(4).getGambar()).into(gambar);
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.baca, menu);
        simpan = menu.getItem(0);
        simpan.setChecked(berita.isBookmarked());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.simpan:
                bookmarkController.bookmark(berita);
                break;
            case R.id.refresh:
                beritaController.loadBerita(berita.getAlamat());
                break;
            case android.R.id.home:
                onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoading()
    {
        fabShareBerita.showProgress(true);
    }

    @Override
    public void dismissLoading()
    {
        fabShareBerita.onProgressCompleted();
        fabShareBerita.showProgress(false);
        new Handler().postDelayed(fabShareBerita::resetIcon, 2500);
    }

    @Override
    public void showBerita(Berita berita)
    {
        gambar.setImageUrl(berita.getGambar());
        judul.setText(berita.getJudul());
        tanggal.setText("TaniPedia - " + berita.getTanggal());
        isi.setText(Html.fromHtml(berita.getIsi()));
        isi.invalidateCache();
        showBeritaLainnya();
        root.setVisibility(View.VISIBLE);
        root.startAnimation(animation);
        this.berita.setIsi(berita.getIsi());
        bookmarkController.setBerita(berita);
    }

    @Override
    public void showListBerita(List<Berita> listBerita)
    {

    }

    @Override
    public void showError(Throwable throwable)
    {
        Snackbar.make(fabShareBerita, "Mohon periksa koneksi internet anda!", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState)
    {
        beritaController.saveState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void showListBookmarkedBerita(List<Berita> listBerita)
    {

    }

    @Override
    public void onBookmark(Berita berita)
    {
        if (simpan != null)
        {
            simpan.setChecked(berita.isBookmarked());
        }
    }

    @Override
    public void onUnBookmark(Berita berita)
    {
        if (simpan != null)
        {
            simpan.setChecked(berita.isBookmarked());
        }
    }
}
