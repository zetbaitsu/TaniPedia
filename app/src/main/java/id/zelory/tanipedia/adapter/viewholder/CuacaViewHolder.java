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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import id.zelory.benih.adapter.viewholder.BenihViewHolder;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.model.Cuaca;

import static id.zelory.benih.adapter.BenihRecyclerAdapter.OnItemClickListener;
import static id.zelory.benih.adapter.BenihRecyclerAdapter.OnLongItemClickListener;

/**
 * Created by zetbaitsu on 7/31/15.
 */
public class CuacaViewHolder extends BenihViewHolder<Cuaca>
{
    @Bind(R.id.cuaca) TextView cuacaText;
    @Bind(R.id.suhu) TextView suhu;
    @Bind(R.id.minmax) TextView minmax;
    @Bind(R.id.icon_cuaca) ImageView iconCuaca;
    @Bind(R.id.tanggal) TextView tanggal;
    @Bind(R.id.background) LinearLayout background;

    public CuacaViewHolder(View itemView, OnItemClickListener itemClickListener, OnLongItemClickListener longItemClickListener)
    {
        super(itemView, itemClickListener, longItemClickListener);
    }

    @Override
    public void bind(Cuaca cuaca)
    {
        cuacaText.setText(cuaca.getCuaca());
        suhu.setText(cuaca.getSuhu() + (char) 0x2103);
        minmax.setText("Min : " + cuaca.getSuhuMin() + (char) 0x2103 + " Max : " + cuaca.getSuhuMax() + (char) 0x2103);
        tanggal.setText(cuaca.getTanggal());
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
        iconCuaca.setImageResource(gambar);
    }
}
