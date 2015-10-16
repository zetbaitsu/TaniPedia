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

package id.zelory.tanipedia.controller;

import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import id.zelory.benih.controller.BenihController;
import id.zelory.benih.util.BenihPreferenceUtils;
import id.zelory.benih.util.BenihScheduler;
import id.zelory.benih.util.Bson;
import id.zelory.tanipedia.TaniPediaApp;
import id.zelory.tanipedia.data.model.Berita;
import id.zelory.tanipedia.data.api.TaniPediaApi;
import timber.log.Timber;

/**
 * Created by zetbaitsu on 7/31/15.
 */
public class BeritaController extends BenihController<BeritaController.Presenter>
{
    private Berita berita;
    private List<Berita> listBerita;

    public BeritaController(Presenter presenter)
    {
        super(presenter);
    }

    public void loadBerita(String url)
    {
        presenter.showLoading();
        TaniPediaApi.pluck()
                .getApi()
                .getBerita(url)
                .compose(BenihScheduler.pluck().applySchedulers(BenihScheduler.Type.IO))
                .subscribe(berita -> {
                    this.berita = berita;
                    if (presenter != null)
                    {
                        presenter.showBerita(berita);
                        presenter.dismissLoading();
                    }
                }, throwable -> {
                    if (presenter != null)
                    {
                        presenter.showError(throwable);
                        presenter.dismissLoading();
                    }
                });
    }

    public void loadListBerita()
    {
        presenter.showLoading();
        TaniPediaApi.pluck()
                .getApi()
                .getAllBerita()
                .compose(BenihScheduler.pluck().applySchedulers(BenihScheduler.Type.IO))
                .subscribe(listBerita -> {
                    this.listBerita = listBerita;
                    BenihPreferenceUtils.putString(TaniPediaApp.pluck().getApplicationContext(), "berita", Bson.pluck().getParser().toJson(listBerita));
                    if (presenter != null)
                    {
                        presenter.showListBerita(listBerita);
                        presenter.dismissLoading();
                    }
                }, throwable -> {
                    if (presenter != null)
                    {
                        presenter.showError(throwable);
                        presenter.dismissLoading();
                    }
                });
    }

    @Override
    public void saveState(Bundle bundle)
    {
        Timber.d("saveState");
        bundle.putParcelable("berita", berita);
        bundle.putParcelableArrayList("listBerita", (ArrayList<Berita>) listBerita);
    }

    @Override
    public void loadState(Bundle bundle)
    {
        Timber.d("loadState");
        berita = bundle.getParcelable("berita");
        if (berita != null)
        {
            presenter.showBerita(berita);
        } else
        {
            presenter.showError(new Throwable("Berita is null"));
        }

        listBerita = bundle.getParcelableArrayList("listBerita");
        if (listBerita != null)
        {
            Timber.d("show list berita");
            new Handler().postDelayed(() -> presenter.showListBerita(listBerita), 500);
        } else
        {
            presenter.showError(new Throwable("List is null"));
        }
    }

    public interface Presenter extends BenihController.Presenter
    {
        void showLoading();

        void dismissLoading();

        void showBerita(Berita berita);

        void showListBerita(List<Berita> listBerita);
    }
}
