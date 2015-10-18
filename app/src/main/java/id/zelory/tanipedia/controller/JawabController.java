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

import java.util.ArrayList;
import java.util.List;

import id.zelory.benih.controller.BenihController;
import id.zelory.benih.util.BenihScheduler;
import id.zelory.tanipedia.data.LocalDataManager;
import id.zelory.tanipedia.data.model.Jawaban;
import id.zelory.tanipedia.data.model.PakTani;
import id.zelory.tanipedia.data.model.Soal;
import id.zelory.tanipedia.data.api.TaniPediaApi;

/**
 * Created by zetbaitsu on 8/10/15.
 */
public class JawabController extends BenihController<JawabController.Presenter>
{
    private List<Jawaban> jawaban;

    public JawabController(Presenter presenter)
    {
        super(presenter);
    }


    public void loadJawaban(Soal soal)
    {
        presenter.showLoading();
        TaniPediaApi.pluck()
                .getApi()
                .getJawaban(soal.getId())
                .compose(BenihScheduler.pluck().applySchedulers(BenihScheduler.Type.IO))
                .subscribe(jawabanArrayList -> {

                    Jawaban jawaban = new Jawaban();
                    jawaban.setIdSoal(soal.getId());
                    jawaban.setPakTani(soal.getPakTani());
                    jawaban.setIsi(soal.getIsi());
                    jawaban.setTanggal(soal.getTanggal());
                    jawabanArrayList.add(0, jawaban);

                    if (jawabanArrayList.size() == 1)
                    {
                        jawaban = new Jawaban();
                        jawaban.setIdSoal(soal.getId());
                        PakTani pakTani = new PakTani();
                        pakTani.setNama("TaniPedia");
                        pakTani.setMale(!LocalDataManager.getPakTani().isMale());
                        jawaban.setPakTani(pakTani);
                        jawaban.setIsi("Pertanyaan ini belum mempunyai jawaban satupun, jadilah orang pertama yang bisa membantu " + soal.getPakTani().getNama() + "!");
                        jawaban.setTanggal(soal.getTanggal());
                        jawabanArrayList.add(jawaban);
                    }

                    this.jawaban = jawabanArrayList;

                    if (presenter != null)
                    {
                        presenter.showJawaban(jawabanArrayList);
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

    public void sendJawaban(Soal soal, String email, String isi)
    {
        presenter.showLoadingDialog();
        TaniPediaApi.pluck()
                .getApi()
                .postJawaban(soal.getId(), email, isi)
                .compose(BenihScheduler.pluck().applySchedulers(BenihScheduler.Type.IO))
                .subscribe(status -> {
                    if (status.getStatus())
                    {
                        loadJawaban(soal);
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
        bundle.putParcelableArrayList("jawaban", (ArrayList<Jawaban>) jawaban);
    }

    @Override
    public void loadState(Bundle bundle)
    {
        jawaban = bundle.getParcelableArrayList("jawaban");
        if (jawaban != null)
        {
            presenter.showJawaban(jawaban);
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

        void showJawaban(List<Jawaban> jawaban);
    }
}
