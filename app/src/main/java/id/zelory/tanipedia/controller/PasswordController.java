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
import id.zelory.benih.util.BenihScheduler;
import id.zelory.tanipedia.data.api.TaniPediaApi;

/**
 * Created on : October 21, 2015
 * Author     : zetbaitsu
 * Name       : Zetra
 * Email      : zetra@mail.ugm.ac.id
 * GitHub     : https://github.com/zetbaitsu
 * LinkedIn   : https://id.linkedin.com/in/zetbaitsu
 */
public class PasswordController extends BenihController<PasswordController.Presenter>
{
    public PasswordController(Presenter presenter)
    {
        super(presenter);
    }

    public void lupaPassword(String email)
    {
        presenter.showLoading();
        TaniPediaApi.pluck()
                .getApi()
                .lupaPassword(email)
                .compose(BenihScheduler.pluck().applySchedulers(BenihScheduler.Type.IO))
                .subscribe(statusResponse -> {
                    if (presenter != null)
                    {
                        if (statusResponse.getStatus())
                        {
                            presenter.onSuccessLupaPassword();
                        } else
                        {
                            presenter.onFailedLupaPassword();
                        }
                        presenter.dismissLoading();
                    }
                }, throwable -> {
                    if (presenter != null)
                    {
                        presenter.onFailedLupaPassword();
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
        void onSuccessLupaPassword();

        void onFailedLupaPassword();
    }
}
