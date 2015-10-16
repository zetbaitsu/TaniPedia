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

package id.zelory.tanipedia.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zetbaitsu on 23/04/2015.
 */
public class Berita implements Parcelable
{
    private String judul;
    private String alamat;
    private String gambar;
    private String tanggal;
    private String deskripsi;
    private String isi;
    private boolean bookmarked;

    public Berita()
    {
    }

    private Berita(Parcel in)
    {
        judul = in.readString();
        alamat = in.readString();
        gambar = in.readString();
        tanggal = in.readString();
        deskripsi = in.readString();
        isi = in.readString();
        bookmarked = in.readByte() != 0;
    }

    public String getJudul()
    {
        return judul;
    }

    public void setJudul(String judul)
    {
        this.judul = judul;
    }

    public String getAlamat()
    {
        return alamat;
    }

    public void setAlamat(String alamat)
    {
        this.alamat = alamat;
    }

    public String getGambar()
    {
        return gambar;
    }

    public void setGambar(String gambar)
    {
        if (gambar.contains("http://i2.wp.com/"))
        {
            this.gambar = gambar;
        } else
        {
            gambar = gambar.replace("-340x160", "");
            gambar = gambar.replace("http://", "");
            this.gambar = "http://i2.wp.com/" + gambar + "?resize=400%2C242";
        }
    }

    public String getTanggal()
    {
        return tanggal;
    }

    public void setTanggal(String tanggal)
    {
        this.tanggal = tanggal;
    }

    public String getDeskripsi()
    {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi)
    {
        this.deskripsi = deskripsi;
    }

    public String getIsi()
    {
        return isi;
    }

    public void setIsi(String isi)
    {
        try
        {
            isi = isi.substring(0, isi.indexOf("Bagikan Artikel"));
        } catch (Exception e)
        {

        }

        this.isi = isi;
    }

    public boolean isBookmarked()
    {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked)
    {
        this.bookmarked = bookmarked;
    }

    @Override
    public int describeContents()
    {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(judul);
        dest.writeString(alamat);
        dest.writeString(gambar);
        dest.writeString(tanggal);
        dest.writeString(deskripsi);
        dest.writeString(isi);
        dest.writeByte((byte) (bookmarked ? 1 : 0));
    }

    public static final Creator<Berita> CREATOR
            = new Creator<Berita>()
    {
        public Berita createFromParcel(Parcel in)
        {
            return new Berita(in);
        }

        public Berita[] newArray(int size)
        {
            return new Berita[size];
        }
    };
}
