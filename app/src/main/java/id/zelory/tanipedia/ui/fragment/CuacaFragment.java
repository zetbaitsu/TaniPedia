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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.ui.adapter.CuacaAdapter;
import id.zelory.tanipedia.controller.CuacaController;
import id.zelory.tanipedia.data.model.Cuaca;
import timber.log.Timber;

/**
 * Created on : October 15, 2015
 * Author     : zetbaitsu
 * Name       : Zetra
 * Email      : zetra@mail.ugm.ac.id
 * GitHub     : https://github.com/zetbaitsu
 * LinkedIn   : https://id.linkedin.com/in/zetbaitsu
 */

public class CuacaFragment extends BaseFragment<CuacaController, CuacaAdapter> implements
        CuacaController.Presenter
{
    @Bind(R.id.cuaca) TextView cuacaText;
    @Bind(R.id.suhu) TextView suhu;
    @Bind(R.id.minmax) TextView minmax;
    @Bind(R.id.icon_cuaca) ImageView iconCuaca;
    @Bind(R.id.kegiatan) TextView kegiatan;
    @Bind(R.id.background) LinearLayout background;

    @Override
    protected int getFragmentView()
    {
        return R.layout.fragment_cuaca;
    }

    @Override
    protected void onViewReady(Bundle bundle)
    {
        super.onViewReady(bundle);
        setUpHeaderContent();
    }

    @Override
    protected void onFabClick(View view)
    {
        controller.loadListCuaca();
    }

    @Override
    protected void onItemClick(View view, int position)
    {
        Cuaca tmp = adapter.getData().get(position);
        Snackbar.make(view, tmp.getLokasi() + " " + tmp.getCuaca().toLowerCase() + " pada "
                + tmp.getTanggal() + ".", Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected CuacaAdapter createAdapter()
    {
        return new CuacaAdapter(getActivity());
    }

    protected void setUpController(Bundle bundle)
    {
        if (controller == null)
        {
            controller = new CuacaController(this);
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
            controller.connect();
        }
    }

    private void setUpHeaderContent()
    {
        cuacaText.setVisibility(View.GONE);
        suhu.setVisibility(View.GONE);
        minmax.setVisibility(View.GONE);
        iconCuaca.setVisibility(View.GONE);
        kegiatan.setVisibility(View.GONE);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        controller.connect();
    }

    @Override
    public void showListCuaca(List<Cuaca> listCuaca)
    {
        Timber.d("show " + listCuaca.size() + " cuaca");
        showHeaderContent(listCuaca.get(0));
        listCuaca.remove(0);
        adapter.clear();
        adapter.add(listCuaca);
        recyclerView.startAnimation(animation);
    }

    private void showHeaderContent(Cuaca cuaca)
    {
        collapsingToolbar.setTitle(cuaca.getLokasi());
        collapsingToolbar.invalidate();
        cuacaText.setVisibility(View.VISIBLE);
        cuacaText.setText(cuaca.getCuaca());
        cuacaText.startAnimation(animation);
        suhu.setVisibility(View.VISIBLE);
        suhu.setText(String.format("%s%s", cuaca.getSuhu(), (char) 0x2103));
        suhu.startAnimation(animation);
        minmax.setVisibility(View.VISIBLE);
        minmax.setText(String.format("Min : %s%s Max : %s%s", cuaca.getSuhuMin(), (char) 0x2103, cuaca.getSuhuMax(), (char) 0x2103));
        minmax.startAnimation(animation);
        kegiatan.setVisibility(View.VISIBLE);
        kegiatan.setText(cuaca.getKegiatan());
        kegiatan.startAnimation(animation);
        int gambar;
        switch (cuaca.getCuaca())
        {
            case "Cerah":
                gambar = R.drawable.cerah;
                background.setBackgroundResource(R.color.cerah);
                break;
            case "Berawan":
                gambar = R.drawable.berawan;
                background.setBackgroundResource(R.color.berawan);
                break;
            case "Hujan":
                gambar = R.drawable.hujan;
                background.setBackgroundResource(R.color.hujan);
                break;
            default:
                gambar = R.drawable.berawan;
        }
        iconCuaca.setVisibility(View.VISIBLE);
        iconCuaca.setImageResource(gambar);
        iconCuaca.startAnimation(animation);
    }
}
