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

package id.zelory.tanipedia.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import butterknife.Bind;
import id.zelory.benih.util.BenihUtils;
import id.zelory.benih.view.BenihImageView;
import id.zelory.tanipedia.BacaActivity;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.adapter.BeritaAdapter;
import id.zelory.tanipedia.controller.BeritaController;
import id.zelory.tanipedia.model.Berita;
import timber.log.Timber;

/**
 * Created on : October 15, 2015
 * Author     : zetbaitsu
 * Name       : Zetra
 * Email      : zetra@mail.ugm.ac.id
 * GitHub     : https://github.com/zetbaitsu
 * LinkedIn   : https://id.linkedin.com/in/zetbaitsu
 */
public class BeritaFragment extends BaseFragment<BeritaController, BeritaAdapter> implements
        BeritaController.Presenter
{
    @Bind(R.id.header) BenihImageView imageHeader;

    @Override
    protected int getFragmentView()
    {
        return R.layout.fragment_berita;
    }

    @Override
    protected void onViewReady(Bundle bundle)
    {
        super.onViewReady(bundle);
        collapsingToolbar.setTitle("Berita Terbaru");
    }

    @Override
    protected void onFabClick(View view)
    {
        controller.loadListBerita();
    }

    @Override
    protected void onItemClick(View view, int position)
    {
        Intent intent = new Intent(getActivity(), BacaActivity.class);
        intent.putExtra("berita", adapter.getData().get(position));
        startActivity(intent);
    }

    @Override
    protected BeritaAdapter createAdapter()
    {
        return new BeritaAdapter(getActivity());
    }

    protected void setUpController(Bundle bundle)
    {
        if (controller == null)
        {
            controller = new BeritaController(this);
        }

        if (bundle != null)
        {
            Timber.d("bundle is there");
            controller.loadState(bundle);
        } else if (getArguments() != null)
        {
            Timber.d("getArguments");
            controller.loadState(getArguments());
        } else
        {
            controller.loadListBerita();
        }
    }

    @Override
    public void showBerita(Berita berita)
    {

    }

    @Override
    public void showError(Throwable throwable)
    {
        if (throwable.getMessage().equals("List is null"))
        {
            super.showError(throwable);
        }
    }

    @Override
    public void showListBerita(List<Berita> listBerita)
    {
        adapter.clear();
        adapter.add(listBerita);
        recyclerView.startAnimation(animation);
        String url = listBerita.get(BenihUtils.randInt(0, listBerita.size() - 1)).getGambar();
        imageHeader.setImageUrl(url);
        imageHeader.startAnimation(animation);
    }
}
