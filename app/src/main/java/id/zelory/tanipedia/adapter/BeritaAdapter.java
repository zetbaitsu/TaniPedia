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
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import id.zelory.tanipedia.R;
import id.zelory.tanipedia.model.Berita;

/**
 * Created by zetbaitsu on 4/26/2015.
 */
public class BeritaAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<Berita> beritaArrayList;

    public BeritaAdapter(Context context, ArrayList<Berita> beritaArrayList)
    {
        this.context = context;
        this.beritaArrayList = beritaArrayList;
    }

    @Override
    public int getCount()
    {
        return beritaArrayList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return beritaArrayList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        Berita berita = beritaArrayList.get(position);
        String hasil = "";

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_cuaca, parent, false);

            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.teks);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        hasil += berita.getJudul() + "\n";
        hasil += berita.getAlamat() + "\n";
        hasil += berita.getGambar() + "\n";
        hasil += berita.getDeskripsi() + "\n";

        holder.textView.setText(hasil);

        return convertView;
    }

    private class ViewHolder
    {
        TextView textView;
    }
}
