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

package id.zelory.tanipedia.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import butterknife.Bind;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.controller.NotifikasiController;
import id.zelory.tanipedia.data.model.Notifikasi;
import id.zelory.tanipedia.ui.JawabActivity;
import id.zelory.tanipedia.ui.adapter.NotifikasiAdapter;

/**
 * Created on : October 23, 2015
 * Author     : zetbaitsu
 * Name       : Zetra
 * Email      : zetra@mail.ugm.ac.id
 * GitHub     : https://github.com/zetbaitsu
 * LinkedIn   : https://id.linkedin.com/in/zetbaitsu
 */
public class NotifikasiFragment extends BaseFragment<NotifikasiController, NotifikasiAdapter> implements
        NotifikasiController.Presenter
{
    @Bind(R.id.header) ImageView imageHeader;

    @Override
    protected void onFabClick(View view)
    {
        controller.loadListNotif();
    }

    @Override
    protected void onItemClick(View view, int position)
    {
        Intent intent = new Intent(getActivity(), JawabActivity.class);
        intent.putExtra("soal", adapter.getData().get(position).getSoal());
        startActivity(intent);
    }

    @Override
    protected NotifikasiAdapter createAdapter()
    {
        return new NotifikasiAdapter(getActivity());
    }

    @Override
    protected void setUpController(Bundle savedInstanceState)
    {
        if (controller == null)
        {
            controller = new NotifikasiController(this);
        }

        if (savedInstanceState != null)
        {
            controller.loadState(savedInstanceState);
        } else if (getArguments() != null)
        {
            controller.loadState(getArguments());
        } else
        {
            controller.loadListNotif();
        }
    }

    @Override
    protected int getFragmentView()
    {
        return R.layout.fragment_notifikasi;
    }

    @Override
    protected void onViewReady(Bundle bundle)
    {
        imageHeader.setVisibility(View.GONE);
        super.onViewReady(bundle);
        collapsingToolbar.setTitle("Notifikasi");
    }

    @Override
    public void showNotif(List<Notifikasi> notifikasi)
    {
        adapter.clear();
        adapter.add(notifikasi);
        recyclerView.startAnimation(animation);
        imageHeader.setVisibility(View.VISIBLE);
        imageHeader.startAnimation(animation);
    }
}
