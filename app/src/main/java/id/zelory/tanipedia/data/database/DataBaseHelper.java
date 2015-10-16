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

package id.zelory.tanipedia.data.database;

import android.database.Cursor;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import id.zelory.benih.util.BenihScheduler;
import id.zelory.tanipedia.BuildConfig;
import id.zelory.tanipedia.TaniPediaApp;
import id.zelory.tanipedia.data.model.Berita;
import id.zelory.tanipedia.data.model.Soal;
import rx.Observable;

/**
 * Created on : October 16, 2015
 * Author     : zetbaitsu
 * Name       : Zetra
 * Email      : zetra@mail.ugm.ac.id
 * GitHub     : https://github.com/zetbaitsu
 * LinkedIn   : https://id.linkedin.com/in/zetbaitsu
 */
public enum DataBaseHelper
{
    HARVEST;
    private final BriteDatabase briteDatabase;

    DataBaseHelper()
    {
        DbOpenHelper dbOpenHelper = new DbOpenHelper(TaniPediaApp.pluck().getBaseContext());
        SqlBrite sqlBrite = SqlBrite.create();
        briteDatabase = sqlBrite.wrapDatabaseHelper(dbOpenHelper);
        briteDatabase.setLoggingEnabled(BuildConfig.DEBUG);
    }

    public static DataBaseHelper pluck()
    {
        return HARVEST;
    }

    public Observable<List<Berita>> getBookmarkedBerita()
    {
        return briteDatabase.createQuery(DB.BeritaTable.TABLE_NAME, "SELECT * FROM " + DB.BeritaTable.TABLE_NAME)
                .compose(BenihScheduler.pluck().applySchedulers(BenihScheduler.Type.IO))
                .map(query -> {
                    Cursor cursor = query.run();
                    List<Berita> beritaList = new ArrayList<>();
                    while (cursor.moveToNext())
                    {
                        beritaList.add(DB.BeritaTable.parseCursor(cursor));
                    }
                    return beritaList;
                });
    }

    public Observable<List<Soal>> getBookmarkedSoal()
    {
        return briteDatabase.createQuery(DB.SoalTable.TABLE_NAME, "SELECT * FROM " + DB.SoalTable.TABLE_NAME)
                .compose(BenihScheduler.pluck().applySchedulers(BenihScheduler.Type.IO))
                .map(query -> {
                    Cursor cursor = query.run();
                    List<Soal> soalList = new ArrayList<>();
                    while (cursor.moveToNext())
                    {
                        soalList.add(DB.SoalTable.parseCursor(cursor));
                    }
                    return soalList;
                });
    }

    public boolean isBookmarked(Berita berita)
    {
        Cursor cursor = briteDatabase.query("SELECT * FROM "
                                                    + DB.BeritaTable.TABLE_NAME + " WHERE "
                                                    + DB.BeritaTable.COLUMN_ALAMAT + " = '" + berita.getAlamat() + "'");
        return cursor.getCount() > 0;
    }

    public void bookmark(Berita berita)
    {
        if (!isBookmarked(berita))
        {
            briteDatabase.insert(DB.BeritaTable.TABLE_NAME, DB.BeritaTable.toContentValues(berita));
        }
    }

    public void unBookmark(Berita berita)
    {
        briteDatabase.delete(DB.BeritaTable.TABLE_NAME, DB.BeritaTable.COLUMN_ALAMAT + " = ?", berita.getAlamat() + "");
    }

    public boolean isBookmarked(Soal soal)
    {
        Cursor cursor = briteDatabase.query("SELECT * FROM "
                                                    + DB.SoalTable.TABLE_NAME + " WHERE "
                                                    + DB.SoalTable.COLUMN_ID + " = '" + soal.getId() + "'");
        return cursor.getCount() > 0;
    }

    public void bookmark(Soal soal)
    {
        if (!isBookmarked(soal))
        {
            briteDatabase.insert(DB.SoalTable.TABLE_NAME, DB.SoalTable.toContentValues(soal));
        }
    }

    public void unBookmark(Soal soal)
    {
        briteDatabase.delete(DB.SoalTable.TABLE_NAME, DB.SoalTable.COLUMN_ID + " = ?", soal.getId() + "");
    }
}
