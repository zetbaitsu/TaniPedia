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
import id.zelory.tanipedia.data.LocalDataManager;
import id.zelory.tanipedia.data.api.TaniPediaApi;
import id.zelory.tanipedia.data.model.Notifikasi;

/**
 * Created on : October 23, 2015
 * Author     : zetbaitsu
 * Name       : Zetra
 * Email      : zetra@mail.ugm.ac.id
 * GitHub     : https://github.com/zetbaitsu
 * LinkedIn   : https://id.linkedin.com/in/zetbaitsu
 */
public class NotifikasiController extends BenihController<NotifikasiController.Presenter>
{
    private List<Notifikasi> notifikasi;

    public NotifikasiController(Presenter presenter)
    {
        super(presenter);
    }

    public void loadListNotif()
    {
        presenter.showLoading();
        TaniPediaApi.pluck()
                .getApi()
                .getNotif(LocalDataManager.getPakTani().getEmail())
                .compose(BenihScheduler.pluck().applySchedulers(BenihScheduler.Type.IO))
                .subscribe(notifikasi -> {
                    this.notifikasi = notifikasi;
                    if (presenter != null)
                    {
                        presenter.showNotif(notifikasi);
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
        bundle.putParcelableArrayList("notifikasi", (ArrayList<Notifikasi>) notifikasi);
    }

    @Override
    public void loadState(Bundle bundle)
    {
        notifikasi = bundle.getParcelableArrayList("notifikasi");
        if (notifikasi != null)
        {
            new Handler().postDelayed(() -> presenter.showNotif(notifikasi), 500);
        } else
        {
            presenter.showError(new Throwable("List notifikasi is null"));
        }
    }

    public interface Presenter extends BenihController.Presenter
    {
        void showNotif(List<Notifikasi> notifikasi);
    }
}
