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

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import id.zelory.benih.adapter.viewholder.BenihItemViewHolder;
import id.zelory.benih.view.BenihImageView;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.data.model.Soal;
import id.zelory.tanipedia.ui.ProfileActivity;

import static id.zelory.benih.adapter.BenihRecyclerAdapter.OnItemClickListener;
import static id.zelory.benih.adapter.BenihRecyclerAdapter.OnLongItemClickListener;

/**
 * Created by zetbaitsu on 7/31/15.
 */
public class SoalViewHolder extends BenihItemViewHolder<Soal>
{
    @Bind(R.id.nama) TextView nama;
    @Bind(R.id.isi) TextView isi;
    @Bind(R.id.tanggal) TextView tanggal;
    @Bind(R.id.pak_tani) BenihImageView foto;

    public SoalViewHolder(View itemView, OnItemClickListener itemClickListener, OnLongItemClickListener longItemClickListener)
    {
        super(itemView, itemClickListener, longItemClickListener);
    }

    @Override
    public void bind(Soal soal)
    {
        nama.setText(soal.getPakTani().getNama());
        isi.setText(soal.getIsi());
        tanggal.setText(soal.getTanggal());
        foto.setImageResource(soal.getPakTani().isMale() ? R.drawable.pak_tani : R.drawable.bu_tani);
        if (!"TaniPedia".equals(soal.getPakTani().getNama()))
        {
            foto.setOnClickListener(v -> {
                Intent intent = new Intent(foto.getContext(), ProfileActivity.class);
                intent.putExtra(ProfileActivity.KEY_PAK_TANI, soal.getPakTani());
                foto.getContext().startActivity(intent);
            });
        }
    }
}
