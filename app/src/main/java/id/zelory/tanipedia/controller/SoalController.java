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
import id.zelory.benih.util.BenihScheduler;
import id.zelory.tanipedia.data.database.DataBaseHelper;
import id.zelory.tanipedia.data.model.Soal;
import id.zelory.tanipedia.data.api.TaniPediaApi;

/**
 * Created by zetbaitsu on 8/10/15.
 */
public class SoalController extends BenihController<SoalController.Presenter>
{
    private List<Soal> listSoal;

    public SoalController(Presenter presenter)
    {
        super(presenter);
    }

    public void loadListSoal()
    {
        presenter.showLoading();
        TaniPediaApi.pluck()
                .getApi()
                .getPertanyaan()
                .compose(BenihScheduler.pluck().applySchedulers(BenihScheduler.Type.IO))
                .subscribe(listSoal -> {

                    int size = listSoal.size();
                    for (int i = 0; i < size; i++)
                    {
                        listSoal.get(i).setBookmarked(DataBaseHelper.pluck().isBookmarked(listSoal.get(i)));
                    }

                    this.listSoal = listSoal;
                    if (presenter != null)
                    {
                        presenter.showListSoal(listSoal);
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

    public void loadListSoal(String email)
    {
        presenter.showLoading();
        TaniPediaApi.pluck()
                .getApi()
                .getPertanyaan(email)
                .compose(BenihScheduler.pluck().applySchedulers(BenihScheduler.Type.IO))
                .subscribe(listSoal -> {

                    int size = listSoal.size();
                    for (int i = 0; i < size; i++)
                    {
                        listSoal.get(i).setBookmarked(DataBaseHelper.pluck().isBookmarked(listSoal.get(i)));
                    }

                    this.listSoal = listSoal;
                    if (presenter != null)
                    {
                        presenter.showListSoal(listSoal);
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

    public void sendSoal(String email, String isi)
    {
        presenter.showLoadingDialog();
        TaniPediaApi.pluck()
                .getApi()
                .postPertanyaan(email, isi)
                .compose(BenihScheduler.pluck().applySchedulers(BenihScheduler.Type.IO))
                .subscribe(status -> {
                    if (status.getStatus())
                    {
                        loadListSoal();
                    } else
                    {
                        if (presenter != null)
                        {
                            presenter.showError(new Throwable("Terjadi kesalahan, silahkan coba lagi!"));
                            presenter.dismissLoading();
                        }
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
        bundle.putParcelableArrayList("listSoal", (ArrayList<Soal>) listSoal);
    }

    @Override
    public void loadState(Bundle bundle)
    {
        listSoal = bundle.getParcelableArrayList("listSoal");
        if (listSoal != null)
        {
            new Handler().postDelayed(() -> presenter.showListSoal(listSoal), 500);
        } else
        {
            presenter.showError(new Throwable("Terjadi kesalahan!"));
        }
    }

    public interface Presenter extends BenihController.Presenter
    {
        void showLoading();

        void showLoadingDialog();

        void dismissLoading();

        void showListSoal(List<Soal> listSoal);
    }
}
