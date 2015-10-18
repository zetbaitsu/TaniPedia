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

package id.zelory.tanipedia.ui.adapter.viewholder;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import id.zelory.benih.adapter.viewholder.BenihItemViewHolder;
import id.zelory.benih.view.BenihImageView;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.TaniPediaApp;
import id.zelory.tanipedia.controller.BookmarkBeritaController;
import id.zelory.tanipedia.data.model.Berita;

import static id.zelory.benih.adapter.BenihRecyclerAdapter.OnItemClickListener;
import static id.zelory.benih.adapter.BenihRecyclerAdapter.OnLongItemClickListener;

/**
 * Created by zetbaitsu on 7/31/15.
 */
public class BeritaViewHolder extends BenihItemViewHolder<Berita> implements
        BookmarkBeritaController.Presenter
{
    @Bind(R.id.gambar) BenihImageView gambar;
    @Bind(R.id.judul) TextView judul;
    @Bind(R.id.tanggal) TextView tanggal;
    @Bind(R.id.bookmark) ImageView ivBookmark;
    private BookmarkBeritaController controller;
    private Animation animation;

    public BeritaViewHolder(View itemView, OnItemClickListener itemClickListener, OnLongItemClickListener longItemClickListener)
    {
        super(itemView, itemClickListener, longItemClickListener);
        controller = new BookmarkBeritaController(this);
        animation = AnimationUtils.loadAnimation(TaniPediaApp.pluck().getApplicationContext(), R.anim.push_right_in);
    }

    @Override
    public void bind(Berita berita)
    {
        gambar.setImageUrl(berita.getGambar());
        judul.setText(berita.getJudul());
        tanggal.setText("TaniPedia - " + berita.getTanggal());
        ivBookmark.setImageResource(berita.isBookmarked() ? R.drawable.ic_bookmark_yes : R.drawable.ic_bookmark_no);
        ivBookmark.setOnClickListener(v -> controller.bookmark(berita));
    }

    @Override
    public void showListBookmarkedBerita(List<Berita> listBerita)
    {

    }

    @Override
    public void onBookmark(Berita berita)
    {
        ivBookmark.startAnimation(animation);
        ivBookmark.setImageResource(R.drawable.ic_bookmark_yes);
    }

    @Override
    public void onUnBookmark(Berita berita)
    {
        ivBookmark.startAnimation(animation);
        ivBookmark.setImageResource(R.drawable.ic_bookmark_no);
    }

    @Override
    public void showError(Throwable throwable)
    {

    }

    @Override
    public void showLoading()
    {

    }

    @Override
    public void dismissLoading()
    {

    }
}
