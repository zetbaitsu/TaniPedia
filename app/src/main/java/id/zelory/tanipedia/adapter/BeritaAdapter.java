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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import id.zelory.tanipedia.R;
import id.zelory.tanipedia.model.Berita;

/**
 * Created by zetbaitsu on 4/26/2015.
 */
public class BeritaAdapter extends RecyclerView.Adapter
{
    private Context context;
    private List<Berita> beritaArrayList;
    private OnItemClickListener clickListener;

    public BeritaAdapter(Context context, List<Berita> beritaArrayList)
    {
        this.context = context;
        this.beritaArrayList = beritaArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_berita, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        ViewHolder viewHolder = (ViewHolder) holder;
        Berita berita = beritaArrayList.get(position);
        Glide.with(context).load(berita.getGambar()).into(viewHolder.gambar);
        viewHolder.judul.setText(berita.getJudul());
        viewHolder.tanggal.setText("TaniPedia - " + berita.getTanggal());
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemCount()
    {
        return beritaArrayList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView gambar;
        TextView judul;
        TextView tanggal;

        public ViewHolder(View itemView)
        {
            super(itemView);
            gambar = (ImageView) itemView.findViewById(R.id.gambar);
            judul = (TextView) itemView.findViewById(R.id.judul);
            tanggal = (TextView) itemView.findViewById(R.id.tanggal);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            clickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener)
    {
        this.clickListener = itemClickListener;
    }
}
