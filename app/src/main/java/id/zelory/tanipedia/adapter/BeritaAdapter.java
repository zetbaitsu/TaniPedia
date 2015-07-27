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
import android.widget.TextView;

import com.bumptech.glide.Glide;

import id.zelory.benih.adapters.BenihRecyclerAdapter;
import id.zelory.benih.adapters.BenihViewHolder;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.model.Berita;

/**
 * Created by zetbaitsu on 4/26/2015.
 */
public class BeritaAdapter extends BenihRecyclerAdapter<Berita, BeritaAdapter.ViewHolder>
{
    public BeritaAdapter(Context context)
    {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_berita, viewGroup, false);
        return new ViewHolder(view, itemClickListener, longItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i)
    {
        Glide.with(context).load(data.get(i).getGambar()).into(holder.gambar);
        holder.judul.setText(data.get(i).getJudul());
        holder.tanggal.setText("TaniPedia - " + data.get(i).getTanggal());
    }

    class ViewHolder extends BenihViewHolder
    {
        ImageView gambar;
        TextView judul;
        TextView tanggal;

        public ViewHolder(View itemView, OnItemClickListener itemClickListener, OnLongItemClickListener longItemClickListener)
        {
            super(itemView, itemClickListener, longItemClickListener);
            gambar = (ImageView) itemView.findViewById(R.id.gambar);
            judul = (TextView) itemView.findViewById(R.id.judul);
            tanggal = (TextView) itemView.findViewById(R.id.tanggal);
        }
    }
}
