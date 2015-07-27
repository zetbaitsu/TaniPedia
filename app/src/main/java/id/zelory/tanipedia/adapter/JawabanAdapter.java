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
import android.widget.TextView;

import id.zelory.benih.adapters.BenihRecyclerAdapter;
import id.zelory.benih.adapters.BenihViewHolder;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.model.Jawaban;

/**
 * Created by zetbaitsu on 6/11/15.
 */
public class JawabanAdapter extends BenihRecyclerAdapter<Jawaban, JawabanAdapter.ViewHolder>
{
    public JawabanAdapter(Context context)
    {
        super(context);
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view;
        view = viewType == 0 ? LayoutInflater.from(context).inflate(R.layout.item_soal_besar, parent, false) :
                LayoutInflater.from(context).inflate(R.layout.item_soal, parent, false);
        return new ViewHolder(view, itemClickListener, longItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i)
    {
        holder.nama.setText(data.get(i).getNama());
        holder.isi.setText(data.get(i).getIsi());
        holder.tanggal.setText(data.get(i).getTanggal());
    }

    class ViewHolder extends BenihViewHolder
    {
        TextView nama;
        TextView isi;
        TextView tanggal;

        public ViewHolder(View itemView, OnItemClickListener itemClickListener, OnLongItemClickListener longItemClickListener)
        {
            super(itemView, itemClickListener, longItemClickListener);
            nama = (TextView) itemView.findViewById(R.id.nama);
            isi = (TextView) itemView.findViewById(R.id.isi);
            tanggal = (TextView) itemView.findViewById(R.id.tanggal);
        }
    }
}