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

package id.zelory.tanipedia.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import id.zelory.benih.adapter.BenihRecyclerAdapter;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.data.model.Notifikasi;
import id.zelory.tanipedia.ui.adapter.viewholder.NotifikasiViewHolder;

/**
 * Created on : October 23, 2015
 * Author     : zetbaitsu
 * Name       : Zetra
 * Email      : zetra@mail.ugm.ac.id
 * GitHub     : https://github.com/zetbaitsu
 * LinkedIn   : https://id.linkedin.com/in/zetbaitsu
 */
public class NotifikasiAdapter extends BenihRecyclerAdapter<Notifikasi, NotifikasiViewHolder>
{
    public NotifikasiAdapter(Context context)
    {
        super(context);
    }

    @Override
    protected int getItemView(int viewType)
    {
        return R.layout.item_notifikasi;
    }

    @Override
    public NotifikasiViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new NotifikasiViewHolder(getView(parent, viewType), itemClickListener, longItemClickListener);
    }
}
