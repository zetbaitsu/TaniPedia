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
import id.zelory.tanipedia.data.model.Notifikasi;
import id.zelory.tanipedia.ui.ProfileActivity;

import static id.zelory.benih.adapter.BenihRecyclerAdapter.OnItemClickListener;
import static id.zelory.benih.adapter.BenihRecyclerAdapter.OnLongItemClickListener;

/**
 * Created on : October 23, 2015
 * Author     : zetbaitsu
 * Name       : Zetra
 * Email      : zetra@mail.ugm.ac.id
 * GitHub     : https://github.com/zetbaitsu
 * LinkedIn   : https://id.linkedin.com/in/zetbaitsu
 */
public class NotifikasiViewHolder extends BenihItemViewHolder<Notifikasi>
{
    @Bind(R.id.nama) TextView nama;
    @Bind(R.id.isi) TextView isi;
    @Bind(R.id.tanggal) TextView tanggal;
    @Bind(R.id.pak_tani) BenihImageView foto;

    public NotifikasiViewHolder(View itemView, OnItemClickListener itemClickListener, OnLongItemClickListener longItemClickListener)
    {
        super(itemView, itemClickListener, longItemClickListener);
    }

    @Override
    public void bind(Notifikasi notifikasi)
    {
        int x = notifikasi.getPakTani().getNama().indexOf(" ");
        if (x != -1)
        {
            nama.setText(notifikasi.getPakTani().getNama().substring(0, x));
        } else
        {
            nama.setText(notifikasi.getPakTani().getNama());
        }
        if (!nama.getText().equals("TaniPedia"))
        {
            nama.setText(nama.getText() + " menjawab pertanyaan anda.");
        }
        isi.setText(notifikasi.getIsi());
        tanggal.setText(notifikasi.getTanggal());
        foto.setImageResource(notifikasi.getPakTani().isMale() ? R.drawable.pak_tani : R.drawable.bu_tani);
        if (!"TaniPedia".equals(notifikasi.getPakTani().getNama()))
        {
            foto.setOnClickListener(v -> {
                Intent intent = new Intent(foto.getContext(), ProfileActivity.class);
                intent.putExtra(ProfileActivity.KEY_PAK_TANI, notifikasi.getPakTani());
                foto.getContext().startActivity(intent);
            });
        }
    }
}
