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
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import id.zelory.benih.fragment.BenihFragment;
import id.zelory.benih.view.BenihRecyclerView;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.controller.BookmarkBeritaController;
import id.zelory.tanipedia.data.model.Berita;
import id.zelory.tanipedia.ui.BacaActivity;
import id.zelory.tanipedia.ui.adapter.BeritaAdapter;

/**
 * Created on : October 18, 2015
 * Author     : zetbaitsu
 * Name       : Zetra
 * Email      : zetra@mail.ugm.ac.id
 * GitHub     : https://github.com/zetbaitsu
 * LinkedIn   : https://id.linkedin.com/in/zetbaitsu
 */
public class BookmarkBeritaFragment extends BenihFragment implements
        BookmarkBeritaController.Presenter
{
    @Bind(R.id.recycler_view) BenihRecyclerView recyclerView;
    @Bind(R.id.empty) TextView empty;
    private BookmarkBeritaController controller;
    private BeritaAdapter adapter;

    @Override
    protected int getFragmentView()
    {
        return R.layout.fragment_list;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState)
    {
        getSupportActionBar().setTitle("Berita Tersimpan");
        adapter = new BeritaAdapter(getActivity());
        adapter.setOnItemClickListener(this::onItemClick);
        recyclerView.setAdapter(adapter);
        recyclerView.setUpAsList();

        controller = new BookmarkBeritaController(this);
        controller.loadBookmarkedBerita();
    }

    private void onItemClick(View view, int position)
    {
        Intent intent = new Intent(getActivity(), BacaActivity.class);
        intent.putExtra("berita", adapter.getData().get(position));
        startActivity(intent);
    }

    @Override
    public void showListBookmarkedBerita(List<Berita> listBerita)
    {
        adapter.clear();
        adapter.add(listBerita);
        empty.setText("Anda belum menyimpan berita satupun.");
        empty.setVisibility(listBerita.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBookmark(Berita berita)
    {

    }

    @Override
    public void onUnBookmark(Berita berita)
    {

    }

    @Override
    public void showError(Throwable throwable)
    {

    }

    @Override
    public void showLoading()
    {

    }

    @Override
    public void dismissLoading()
    {

    }
}
