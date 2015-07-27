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

package id.zelory.tanipedia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.zelory.benih.adapters.BenihRecyclerAdapter;
import id.zelory.benih.adapters.BenihViewHolder;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.model.Cuaca;

/**
 * Created by zetbaitsu on 4/26/2015.
 */
public class CuacaAdapter extends BenihRecyclerAdapter<Cuaca, CuacaAdapter.ViewHolder>
{
    public CuacaAdapter(Context context)
    {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cuaca, viewGroup, false);
        return new ViewHolder(view, itemClickListener, longItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i)
    {
        Cuaca cuaca = data.get(i);

        holder.cuacaText.setText(cuaca.getCuaca());
        holder.suhu.setText(cuaca.getSuhu() + (char) 0x2103);
        holder.minmax.setText("Min : " + cuaca.getSuhuMin() + (char) 0x2103 + " Max : " + cuaca.getSuhuMax() + (char) 0x2103);
        holder.tanggal.setText(cuaca.getTanggal());
        int gambar;
        switch (cuaca.getCuaca())
        {
            case "Cerah":
                gambar = R.drawable.cerah;
                holder.background.setBackgroundResource(R.color.cerah);
                break;
            case "Berawan":
                gambar = R.drawable.berawan;
                holder.background.setBackgroundResource(R.color.berawan);
                break;
            case "Hujan":
                gambar = R.drawable.hujan;
                holder.background.setBackgroundResource(R.color.hujan);
                break;
            default:
                gambar = R.drawable.berawan;
        }
        holder.iconCuaca.setImageResource(gambar);
    }

    class ViewHolder extends BenihViewHolder
    {
        TextView cuacaText;
        TextView suhu;
        TextView minmax;
        ImageView iconCuaca;
        TextView tanggal;
        LinearLayout background;

        public ViewHolder(View itemView, OnItemClickListener itemClickListener, OnLongItemClickListener longItemClickListener)
        {
            super(itemView, itemClickListener, longItemClickListener);
            cuacaText = (TextView) itemView.findViewById(R.id.cuaca);
            suhu = (TextView) itemView.findViewById(R.id.suhu);
            minmax = (TextView) itemView.findViewById(R.id.minmax);
            iconCuaca = (ImageView) itemView.findViewById(R.id.icon_cuaca);
            tanggal = (TextView) itemView.findViewById(R.id.tanggal);
            background = (LinearLayout) itemView.findViewById(R.id.background);
        }
    }
}
