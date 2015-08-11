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

package id.zelory.tanipedia.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import id.zelory.benih.adapter.viewholder.BenihViewHolder;
import id.zelory.benih.view.BenihImageView;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.model.Berita;

import static id.zelory.benih.adapter.BenihRecyclerAdapter.OnItemClickListener;
import static id.zelory.benih.adapter.BenihRecyclerAdapter.OnLongItemClickListener;

/**
 * Created by zetbaitsu on 7/31/15.
 */
public class BeritaViewHolder extends BenihViewHolder<Berita>
{
    @Bind(R.id.gambar) BenihImageView gambar;
    @Bind(R.id.judul) TextView judul;
    @Bind(R.id.tanggal) TextView tanggal;

    public BeritaViewHolder(View itemView, OnItemClickListener itemClickListener, OnLongItemClickListener longItemClickListener)
    {
        super(itemView, itemClickListener, longItemClickListener);
    }

    @Override
    public void bind(Berita berita)
    {
        gambar.setImageUrl(berita.getGambar());
        judul.setText(berita.getJudul());
        tanggal.setText("TaniPedia - " + berita.getTanggal());
    }
}
