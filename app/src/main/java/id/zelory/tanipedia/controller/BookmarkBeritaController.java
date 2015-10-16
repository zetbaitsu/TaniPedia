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

import java.util.List;

import id.zelory.benih.controller.BenihController;
import id.zelory.benih.util.BenihBus;
import id.zelory.benih.util.BenihScheduler;
import id.zelory.tanipedia.controller.event.BookmarkBeritaEvent;
import id.zelory.tanipedia.data.database.DataBaseHelper;
import id.zelory.tanipedia.data.model.Berita;
import timber.log.Timber;

/**
 * Created on : October 16, 2015
 * Author     : zetbaitsu
 * Name       : Zetra
 * Email      : zetra@mail.ugm.ac.id
 * GitHub     : https://github.com/zetbaitsu
 * LinkedIn   : https://id.linkedin.com/in/zetbaitsu
 */
public class BookmarkBeritaController extends BenihController<BookmarkBeritaController.Presenter>
{
    private List<Berita> beritas;
    private Berita berita;

    public BookmarkBeritaController(Presenter presenter)
    {
        super(presenter);
        BenihBus.pluck()
                .receive()
                .subscribe(o -> {
                    if (o instanceof BookmarkBeritaEvent)
                    {
                        onBookmarkEvent((BookmarkBeritaEvent) o);
                    }
                }, throwable -> Timber.e(throwable.getMessage()));
    }

    @Override
    public void saveState(Bundle bundle)
    {

    }

    @Override
    public void loadState(Bundle bundle)
    {

    }

    public void setBerita(Berita berita)
    {
        this.berita = berita;
    }

    private void onBookmarkEvent(BookmarkBeritaEvent bookmarkEvent)
    {
        if (berita != null && berita.getAlamat() == bookmarkEvent.getBerita().getAlamat())
        {
            if (bookmarkEvent.getBerita().isBookmarked() && !berita.isBookmarked())
            {
                berita.setBookmarked(true);
                presenter.onBookmark(berita);
            } else if (!bookmarkEvent.getBerita().isBookmarked() && berita.isBookmarked())
            {
                berita.setBookmarked(false);
                presenter.onUnBookmark(berita);
            }
        }
    }

    public void loadBookmarkedBerita()
    {
        presenter.showLoading();
        DataBaseHelper.pluck()
                .getBookmarkedBerita()
                .compose(BenihScheduler.pluck().applySchedulers(BenihScheduler.Type.IO))
                .subscribe(beritas -> {
                    int size = beritas.size();
                    for (int i = 0; i < size; i++)
                    {
                        beritas.get(i).setBookmarked(DataBaseHelper.pluck().isBookmarked(beritas.get(i)));
                    }
                    this.beritas = beritas;
                    if (presenter != null)
                    {
                        presenter.showListBookmarkedBerita(beritas);
                        presenter.dismissLoading();
                    }
                }, throwable -> {
                    if (presenter != null)
                    {
                        Timber.d(throwable.getMessage());
                        presenter.showError(throwable);
                        presenter.dismissLoading();
                    }
                });
    }

    public void bookmark(Berita berita)
    {
        if (!berita.isBookmarked())
        {
            berita.setBookmarked(true);
            DataBaseHelper.pluck().bookmark(berita);
            presenter.onBookmark(berita);
        } else
        {
            berita.setBookmarked(false);
            DataBaseHelper.pluck().unBookmark(berita);
            presenter.onUnBookmark(berita);
        }

        BenihBus.pluck().send(new BookmarkBeritaEvent(berita));
    }

    public interface Presenter extends BenihController.Presenter
    {
        void showListBookmarkedBerita(List<Berita> listBerita);

        void onBookmark(Berita berita);

        void onUnBookmark(Berita berita);
    }
}
