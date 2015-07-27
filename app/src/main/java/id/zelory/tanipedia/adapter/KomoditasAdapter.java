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
import id.zelory.tanipedia.model.Komoditas;

/**
 * Created by zetbaitsu on 5/6/15.
 */
public class KomoditasAdapter extends BenihRecyclerAdapter<Komoditas, KomoditasAdapter.ViewHolder>
{
    public KomoditasAdapter(Context context)
    {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_komoditas, parent, false);
        return new ViewHolder(view, itemClickListener, longItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.nomor.setText((position + 1) + ".");
        holder.nama.setText(data.get(position).getNama());
        holder.harga.setText(data.get(position).getHarga() + ",00");
    }

    class ViewHolder extends BenihViewHolder
    {
        TextView nomor;
        TextView nama;
        TextView harga;

        public ViewHolder(View itemView, OnItemClickListener itemClickListener, OnLongItemClickListener longItemClickListener)
        {
            super(itemView, itemClickListener, longItemClickListener);
            nomor = (TextView) itemView.findViewById(R.id.nomor);
            nama = (TextView) itemView.findViewById(R.id.nama);
            harga = (TextView) itemView.findViewById(R.id.harga);
        }

    }

}
