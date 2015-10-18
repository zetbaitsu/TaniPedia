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
import id.zelory.tanipedia.controller.event.BookmarkSoalEvent;
import id.zelory.tanipedia.data.database.DataBaseHelper;
import id.zelory.tanipedia.data.model.Soal;
import timber.log.Timber;

/**
 * Created on : October 16, 2015
 * Author     : zetbaitsu
 * Name       : Zetra
 * Email      : zetra@mail.ugm.ac.id
 * GitHub     : https://github.com/zetbaitsu
 * LinkedIn   : https://id.linkedin.com/in/zetbaitsu
 */
public class BookmarkSoalController extends BenihController<BookmarkSoalController.Presenter>
{
    private List<Soal> soals;
    private Soal soal;

    public BookmarkSoalController(Presenter presenter)
    {
        super(presenter);
        BenihBus.pluck()
                .receive()
                .subscribe(o -> {
                    if (o instanceof BookmarkSoalEvent)
                    {
                        onBookmarkEvent((BookmarkSoalEvent) o);
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

    public void setSoal(Soal soal)
    {
        this.soal = soal;
    }

    private void onBookmarkEvent(BookmarkSoalEvent bookmarkEvent)
    {
        if (soal != null && soal.getId().equals(bookmarkEvent.getSoal().getId()))
        {
            if (bookmarkEvent.getSoal().isBookmarked() && !soal.isBookmarked())
            {
                soal.setBookmarked(true);
                presenter.onBookmark(soal);
            } else if (!bookmarkEvent.getSoal().isBookmarked() && soal.isBookmarked())
            {
                soal.setBookmarked(false);
                presenter.onUnBookmark(soal);
            }
        }
    }

    public void loadBookmarkedSoal()
    {
        presenter.showLoading();
        DataBaseHelper.pluck()
                .getBookmarkedSoal()
                .compose(BenihScheduler.pluck().applySchedulers(BenihScheduler.Type.IO))
                .subscribe(soals -> {
                    int size = soals.size();
                    for (int i = 0; i < size; i++)
                    {
                        soals.get(i).setBookmarked(DataBaseHelper.pluck().isBookmarked(soals.get(i)));
                    }
                    this.soals = soals;
                    if (presenter != null)
                    {
                        presenter.showListBookmarkedSoal(soals);
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

    public void bookmark(Soal soal)
    {
        if (!soal.isBookmarked())
        {
            soal.setBookmarked(true);
            DataBaseHelper.pluck().bookmark(soal);
            presenter.onBookmark(soal);
        } else
        {
            soal.setBookmarked(false);
            DataBaseHelper.pluck().unBookmark(soal);
            presenter.onUnBookmark(soal);
        }

        BenihBus.pluck().send(new BookmarkSoalEvent(soal));
    }

    public interface Presenter extends BenihController.Presenter
    {
        void showListBookmarkedSoal(List<Soal> listSoal);

        void onBookmark(Soal soal);

        void onUnBookmark(Soal soal);
    }
}
