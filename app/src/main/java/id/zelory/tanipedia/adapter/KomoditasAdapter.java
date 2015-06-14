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
import android.widget.TextView;

import java.util.ArrayList;

import id.zelory.tanipedia.R;
import id.zelory.tanipedia.model.Komoditas;

/**
 * Created by zetbaitsu on 5/6/15.
 */
public class KomoditasAdapter extends RecyclerView.Adapter
{
    private Context context;
    private ArrayList<Komoditas> komoditasArrayList;
    private OnItemClickListener clickListener;

    public KomoditasAdapter(Context context, ArrayList<Komoditas> komoditasArrayList)
    {
        this.context = context;
        this.komoditasArrayList = komoditasArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_komoditas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        ViewHolder viewHolder = (ViewHolder) holder;
        Komoditas komoditas = komoditasArrayList.get(position);
        viewHolder.nomor.setText((position + 1) + ".");
        viewHolder.nama.setText(komoditas.getNama());
        viewHolder.harga.setText(komoditas.getHarga() + ",00");
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemCount()
    {
        return komoditasArrayList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView nomor;
        TextView nama;
        TextView harga;

        public ViewHolder(View itemView)
        {
            super(itemView);
            nomor = (TextView) itemView.findViewById(R.id.nomor);
            nama = (TextView) itemView.findViewById(R.id.nama);
            harga = (TextView) itemView.findViewById(R.id.harga);
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
