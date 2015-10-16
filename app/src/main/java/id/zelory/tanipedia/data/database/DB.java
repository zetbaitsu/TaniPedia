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

import android.content.ContentValues;
import android.database.Cursor;

import id.zelory.tanipedia.data.model.Berita;
import id.zelory.tanipedia.data.model.PakTani;
import id.zelory.tanipedia.data.model.Soal;

/**
 * Created on : October 16, 2015
 * Author     : zetbaitsu
 * Name       : Zetra
 * Email      : zetra@mail.ugm.ac.id
 * GitHub     : https://github.com/zetbaitsu
 * LinkedIn   : https://id.linkedin.com/in/zetbaitsu
 */
public class DB
{
    public static abstract class BeritaTable
    {
        public static final String TABLE_NAME = "berita";
        public static final String COLUMN_ALAMAT = "alamat";
        public static final String COLUMN_JUDUL = "judul";
        public static final String COLUMN_GAMBAR = "gambar";
        public static final String COLUMN_TANGGAL = "tanggal";
        public static final String COLUMN_DESKRIPSI = "deskripsi";
        public static final String COLUMN_ISI = "isi";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ALAMAT + " TEXT PRIMARY KEY," +
                        COLUMN_JUDUL + " TEXT NOT NULL," +
                        COLUMN_GAMBAR + " TEXT NOT NULL," +
                        COLUMN_TANGGAL + " TEXT NOT NULL," +
                        COLUMN_DESKRIPSI + " TEXT NOT NULL," +
                        COLUMN_ISI + " TEXT NOT NULL" +
                        " ); ";

        public static ContentValues toContentValues(Berita berita)
        {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ALAMAT, berita.getAlamat());
            values.put(COLUMN_JUDUL, berita.getJudul());
            values.put(COLUMN_GAMBAR, berita.getGambar());
            values.put(COLUMN_TANGGAL, berita.getTanggal());
            values.put(COLUMN_DESKRIPSI, berita.getDeskripsi());
            values.put(COLUMN_ISI, berita.getIsi());
            return values;
        }

        public static Berita parseCursor(Cursor cursor)
        {
            Berita berita = new Berita();
            berita.setAlamat(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALAMAT)));
            berita.setJudul(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JUDUL)));
            berita.setGambar(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GAMBAR)));
            berita.setTanggal(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TANGGAL)));
            berita.setDeskripsi(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESKRIPSI)));
            berita.setIsi(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ISI)));
            return berita;
        }
    }

    public static abstract class SoalTable
    {
        public static final String TABLE_NAME = "soal";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TANGGAL = "tanggal";
        public static final String COLUMN_ISI = "isi";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_NAMA = "nama";
        public static final String COLUMN_MALE = "male";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " TEXT PRIMARY KEY," +
                        COLUMN_TANGGAL + " TEXT NOT NULL," +
                        COLUMN_ISI + " TEXT NOT NULL," +
                        COLUMN_EMAIL + " TEXT NOT NULL," +
                        COLUMN_NAMA + " TEXT NOT NULL," +
                        COLUMN_MALE + " INTEGER NOT NULL" +
                        " ); ";

        public static ContentValues toContentValues(Soal soal)
        {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, soal.getId());
            values.put(COLUMN_TANGGAL, soal.getTanggal());
            values.put(COLUMN_ISI, soal.getIsi());
            values.put(COLUMN_EMAIL, soal.getPakTani().getEmail());
            values.put(COLUMN_NAMA, soal.getPakTani().getNama());
            values.put(COLUMN_MALE, soal.getPakTani().isMale() ? 1 : 0);
            return values;
        }

        public static Soal parseCursor(Cursor cursor)
        {
            Soal soal = new Soal();
            soal.setId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            soal.setTanggal(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TANGGAL)));
            soal.setIsi(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ISI)));
            PakTani pakTani = new PakTani();
            pakTani.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
            pakTani.setNama(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA)));
            pakTani.setMale(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MALE)) == 1);
            soal.setPakTani(pakTani);
            return soal;
        }
    }
}
