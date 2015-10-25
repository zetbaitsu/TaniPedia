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
import id.zelory.tanipedia.controller.CuacaController;
import id.zelory.tanipedia.data.model.Cuaca;
import id.zelory.tanipedia.data.model.RangkumanCuaca;
import id.zelory.tanipedia.ui.adapter.CuacaAdapter;
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
    @Bind(R.id.background) LinearLayout background;
    @Bind(R.id.range) TextView range;
    @Bind(R.id.arah_angin) TextView arahAngin;
    @Bind(R.id.kecepatan_angin) TextView kecepatanAngin;
    @Bind(R.id.tekanan) TextView tekanan;
    @Bind(R.id.kelembaban) TextView kelembaban;
    @Bind(R.id.divider) View divider;
    @Bind(R.id.iv_tekanan) ImageView ivTekanan;
    @Bind(R.id.iv_kelembaban) ImageView ivKelembaban;
    @Bind(R.id.sumber) TextView sumber;

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
        divider.setVisibility(View.GONE);
        range.setVisibility(View.GONE);
        arahAngin.setVisibility(View.GONE);
        kecepatanAngin.setVisibility(View.GONE);
        tekanan.setVisibility(View.GONE);
        kelembaban.setVisibility(View.GONE);
        ivKelembaban.setVisibility(View.GONE);
        ivTekanan.setVisibility(View.GONE);
        sumber.setVisibility(View.GONE);
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
        controller.loadRangkumanCuaca(listCuaca);
        adapter.clear();
        adapter.add(listCuaca);
        recyclerView.startAnimation(animation);
    }

    @Override
    public void showRangkumanCuaca(RangkumanCuaca rangkumanCuaca)
    {
        collapsingToolbar.setTitle(rangkumanCuaca.getLokasi());
        collapsingToolbar.invalidate();
        cuacaText.setVisibility(View.VISIBLE);
        cuacaText.setText(rangkumanCuaca.getRataCuaca());
        cuacaText.startAnimation(animation);
        suhu.setVisibility(View.VISIBLE);
        suhu.setText(String.format("%s%s", rangkumanCuaca.getRataSuhu(), (char) 0x2103));
        suhu.startAnimation(animation);
        minmax.setVisibility(View.VISIBLE);
        minmax.setText(String.format("Min : %s%s Maks : %s%s", rangkumanCuaca.getSuhuMin(),
                                     (char) 0x2103, rangkumanCuaca.getSuhuMax(), (char) 0x2103));
        minmax.startAnimation(animation);
        int gambar;
        switch (rangkumanCuaca.getRataCuaca())
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
        divider.setVisibility(View.VISIBLE);
        divider.startAnimation(animation);
        iconCuaca.setVisibility(View.VISIBLE);
        iconCuaca.setImageResource(gambar);
        iconCuaca.startAnimation(animation);
        range.setVisibility(View.VISIBLE);
        range.setText("Rata-rata " + rangkumanCuaca.getRangeTanggal());
        range.startAnimation(animation);
        arahAngin.setVisibility(View.VISIBLE);
        arahAngin.setText(rangkumanCuaca.getRataArahAngin());
        arahAngin.startAnimation(animation);
        kecepatanAngin.setVisibility(View.VISIBLE);
        kecepatanAngin.setText("Kecepatan angin : " + rangkumanCuaca.getRataKecepatanAngin() + " m/s");
        kecepatanAngin.startAnimation(animation);
        tekanan.setVisibility(View.VISIBLE);
        tekanan.setText(rangkumanCuaca.getRataTekanan() + " hpa");
        tekanan.startAnimation(animation);
        kelembaban.setVisibility(View.VISIBLE);
        kelembaban.setText(rangkumanCuaca.getRataKelembaban() + " %");
        kelembaban.startAnimation(animation);
        ivTekanan.setVisibility(View.VISIBLE);
        ivTekanan.startAnimation(animation);
        ivKelembaban.setVisibility(View.VISIBLE);
        ivKelembaban.startAnimation(animation);
        sumber.setVisibility(View.VISIBLE);
        sumber.startAnimation(animation);
    }
}
