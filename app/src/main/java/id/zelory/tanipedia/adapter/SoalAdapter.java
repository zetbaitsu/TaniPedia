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
import android.view.ViewGroup;

import id.zelory.benih.adapter.BenihRecyclerAdapter;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.adapter.viewholder.SoalViewHolder;
import id.zelory.tanipedia.model.Soal;

/**
 * Created by zetbaitsu on 5/6/15.
 */
public class SoalAdapter extends BenihRecyclerAdapter<Soal, SoalViewHolder>
{
    public SoalAdapter(Context context)
    {
        super(context);
    }

    @Override
    protected int getItemView(int i)
    {
        return R.layout.item_soal;
    }

    @Override
    public SoalViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new SoalViewHolder(getView(parent, viewType), itemClickListener, longItemClickListener);
    }
}
