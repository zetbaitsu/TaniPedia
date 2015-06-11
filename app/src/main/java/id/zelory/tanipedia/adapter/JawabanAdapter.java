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
import id.zelory.tanipedia.model.Jawaban;

/**
 * Created by zetbaitsu on 6/11/15.
 */
public class JawabanAdapter extends RecyclerView.Adapter
{
    private Context context;
    private ArrayList<Jawaban> jawabanArrayList;
    private OnItemClickListener clickListener;

    public JawabanAdapter(Context context, ArrayList<Jawaban> jawabanArrayList)
    {
        this.context = context;
        this.jawabanArrayList = jawabanArrayList;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view;
        if (viewType == 0)
        {
            view = LayoutInflater.from(context).inflate(R.layout.item_soal_besar, parent, false);
        } else
        {
            view = LayoutInflater.from(context).inflate(R.layout.item_soal, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        ViewHolder viewHolder = (ViewHolder) holder;
        Jawaban jawaban = jawabanArrayList.get(position);
        viewHolder.nama.setText(jawaban.getNama());
        viewHolder.isi.setText(jawaban.getIsi());
        viewHolder.tanggal.setText(jawaban.getTanggal());
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemCount()
    {
        return jawabanArrayList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView nama;
        TextView isi;
        TextView tanggal;

        public ViewHolder(View itemView)
        {
            super(itemView);
            nama = (TextView) itemView.findViewById(R.id.nama);
            isi = (TextView) itemView.findViewById(R.id.isi);
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