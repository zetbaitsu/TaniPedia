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

import id.zelory.benih.controller.BenihController;
import id.zelory.benih.util.BenihPreferenceUtils;
import id.zelory.benih.util.BenihScheduler;
import id.zelory.tanipedia.TaniPediaApp;
import id.zelory.tanipedia.model.PakTani;
import id.zelory.tanipedia.network.TaniPediaService;

/**
 * Created by zetbaitsu on 8/10/15.
 */
public class PakTaniController extends BenihController<PakTaniController.Presenter>
{
    public PakTaniController(Presenter presenter)
    {
        super(presenter);
    }

    public void login(String email, String password)
    {
        presenter.showLoading();
        TaniPediaService.pluck()
                .getApi()
                .login(email, password)
                .compose(BenihScheduler.pluck().applySchedulers(BenihScheduler.Type.IO))
                .subscribe(pakTani -> {
                    if (pakTani.getEmail() != null)
                    {
                        BenihPreferenceUtils.putString(TaniPediaApp.pluck().getApplicationContext(), "email", pakTani.getEmail());
                        BenihPreferenceUtils.putString(TaniPediaApp.pluck().getApplicationContext(), "nama", pakTani.getNama());
                        BenihPreferenceUtils.putString(TaniPediaApp.pluck().getApplicationContext(), "pass", pakTani.getPassword());
                        BenihPreferenceUtils.putBoolean(TaniPediaApp.pluck().getApplicationContext(), "male", pakTani.isMale());
                        if (presenter != null)
                        {
                            presenter.onLoginSuccess();
                        }
                    } else
                    {
                        if (presenter != null)
                        {
                            presenter.showError(new Throwable("E-Mail dan Password tidak cocok."));
                        }
                    }

                    if (presenter != null)
                    {
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

    public void register(String email, String nama, String password, boolean male)
    {
        presenter.showLoading();
        TaniPediaService.pluck()
                .getApi()
                .register(email, nama, password, male)
                .compose(BenihScheduler.pluck().applySchedulers(BenihScheduler.Type.IO))
                .subscribe(status -> {
                    if (status.getStatus())
                    {
                        BenihPreferenceUtils.putString(TaniPediaApp.pluck().getApplicationContext(), "email", email);
                        BenihPreferenceUtils.putString(TaniPediaApp.pluck().getApplicationContext(), "nama", nama);
                        BenihPreferenceUtils.putString(TaniPediaApp.pluck().getApplicationContext(), "pass", password);
                        BenihPreferenceUtils.putBoolean(TaniPediaApp.pluck().getApplicationContext(), "male", male);
                        if (presenter != null)
                        {
                            presenter.onRegisterSuccess();
                        }
                    } else
                    {
                        if (presenter != null)
                        {
                            presenter.showError(new Throwable("Email tersebut sudah terdaftar!"));
                        }
                    }
                    if (presenter != null)
                    {
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

    public void loadPakTani(String email)
    {
        presenter.showLoading();
        TaniPediaService.pluck()
                .getApi()
                .getPakTani(email)
                .compose(BenihScheduler.pluck().applySchedulers(BenihScheduler.Type.IO))
                .subscribe(pakTani -> {
                    if (presenter != null)
                    {
                        presenter.showPakTani(pakTani);
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

    public void updatePakTani(PakTani pakTani)
    {
        presenter.showLoading();
        TaniPediaService.pluck()
                .getApi()
                .updatePakTani(pakTani.getEmail(), pakTani.getNama(), pakTani.getPassword(), pakTani.isMale())
                .compose(BenihScheduler.pluck().applySchedulers(BenihScheduler.Type.IO))
                .subscribe(tani -> {
                    if (presenter != null)
                    {
                        presenter.showPakTani(tani);
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

    }

    @Override
    public void loadState(Bundle bundle)
    {

    }

    public interface Presenter extends BenihController.Presenter
    {
        void showLoading();

        void dismissLoading();

        void onLoginSuccess();

        void onRegisterSuccess();

        void showPakTani(PakTani pakTani);
    }
}
