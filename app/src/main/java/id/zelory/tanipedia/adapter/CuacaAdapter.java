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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import id.zelory.tanipedia.R;
import id.zelory.tanipedia.model.Cuaca;

/**
 * Created by zetbaitsu on 4/26/2015.
 */
public class CuacaAdapter extends RecyclerView.Adapter
{
    private Context context;
    private ArrayList<Cuaca> cuacaArrayList;
    private OnItemClickListener clickListener;

    public CuacaAdapter(Context context, ArrayList<Cuaca> cuacaArrayList)
    {
        this.context = context;
        this.cuacaArrayList = cuacaArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cuaca, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        ViewHolder viewHolder = (ViewHolder) holder;
        Cuaca cuaca = cuacaArrayList.get(position);

        viewHolder.cuacaText.setText(cuaca.getCuaca());
        viewHolder.suhu.setText(cuaca.getSuhu() + (char) 0x2103);
        viewHolder.minmax.setText("Min : " + cuaca.getSuhuMin() + (char) 0x2103 + " Max : " + cuaca.getSuhuMax() + (char) 0x2103);
        viewHolder.tanggal.setText(cuaca.getTanggal());
        int gambar;
        switch (cuaca.getCuaca())
        {
            case "Cerah":
                gambar = R.drawable.ceraah;
                viewHolder.background.setBackgroundResource(R.color.cerah);
                break;
            case "Berawan":
                gambar = R.drawable.berawan;
                viewHolder.background.setBackgroundResource(R.color.berawan);
                break;
            case "Hujan":
                gambar = R.drawable.hujan;
                viewHolder.background.setBackgroundResource(R.color.hujan);
                break;
            default:
                gambar = R.drawable.berawan;
        }
        viewHolder.iconCuaca.setImageResource(gambar);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemCount()
    {
        return cuacaArrayList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView cuacaText;
        TextView suhu;
        TextView minmax;
        ImageView iconCuaca;
        TextView tanggal;
        LinearLayout background;
        public ViewHolder(View itemView)
        {
            super(itemView);
            cuacaText = (TextView) itemView.findViewById(R.id.cuaca);
            suhu = (TextView) itemView.findViewById(R.id.suhu);
            minmax = (TextView) itemView.findViewById(R.id.minmax);
            iconCuaca = (ImageView) itemView.findViewById(R.id.icon_cuaca);
            tanggal = (TextView) itemView.findViewById(R.id.tanggal);
            background = (LinearLayout) itemView.findViewById(R.id.background);
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
