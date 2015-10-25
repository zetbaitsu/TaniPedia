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

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.ui.adapter.KomoditasAdapter;
import id.zelory.tanipedia.controller.KomoditasController;
import id.zelory.tanipedia.data.model.Komoditas;
import timber.log.Timber;

/**
 * Created by zetbaitsu on 8/11/15.
 */
public class KomoditasFragment extends
        BaseFragment<KomoditasController, KomoditasAdapter> implements
        KomoditasController.Presenter
{
    @Bind(R.id.header) ImageView imageHeader;
    @Bind(R.id.sumber) TextView sumber;

    @Override
    protected int getFragmentView()
    {
        return R.layout.fragment_komoditas;
    }

    @Override
    protected void onViewReady(Bundle bundle)
    {
        imageHeader.setVisibility(View.GONE);
        sumber.setVisibility(View.GONE);
        super.onViewReady(bundle);
        collapsingToolbar.setTitle("Harga Komoditas");
    }

    @Override
    protected void onFabClick(View view)
    {
        controller.loadListKomoditas();
    }

    @Override
    protected void onItemClick(View view, int position)
    {
        Komoditas komoditas = adapter.getData().get(position);
        Snackbar.make(view, "Harga " + komoditas.getNama().toLowerCase() + " adalah Rp. "
                + komoditas.getHarga() + ",00 per Kg.", Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected KomoditasAdapter createAdapter()
    {
        return new KomoditasAdapter(getActivity());
    }

    @Override
    protected void setUpController(Bundle savedInstanceState)
    {
        if (controller == null)
        {
            controller = new KomoditasController(this);
        }

        if (savedInstanceState != null)
        {
            Timber.d("bundle is there");
            controller.loadState(savedInstanceState);
        } else if (getArguments() != null)
        {
            Timber.d("getArguments");
            controller.loadState(getArguments());
        } else
        {
            controller.loadListKomoditas();
        }
    }

    @Override
    public void showListKomoditas(List<Komoditas> listKomoditas)
    {
        adapter.clear();
        adapter.add(listKomoditas);
        recyclerView.startAnimation(animation);
        imageHeader.setVisibility(View.VISIBLE);
        imageHeader.startAnimation(animation);
        sumber.setVisibility(View.VISIBLE);
        sumber.startAnimation(animation);
    }
}
