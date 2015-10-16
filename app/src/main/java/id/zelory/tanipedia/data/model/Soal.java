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
 * Created by zetbaitsu on 4/23/2015.
 */
public class Soal implements Parcelable
{
    public String id;
    private String tanggal;
    private String isi;
    private PakTani pakTani;
    private boolean bookmarked;

    public Soal()
    {

    }

    protected Soal(Parcel in)
    {
        id = in.readString();
        tanggal = in.readString();
        isi = in.readString();
        pakTani = in.readParcelable(PakTani.class.getClassLoader());
        bookmarked = in.readByte() != 0;
    }

    public static final Creator<Soal> CREATOR = new Creator<Soal>()
    {
        @Override
        public Soal createFromParcel(Parcel in)
        {
            return new Soal(in);
        }

        @Override
        public Soal[] newArray(int size)
        {
            return new Soal[size];
        }
    };

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTanggal()
    {
        return tanggal;
    }

    public void setTanggal(String tanggal)
    {
        this.tanggal = tanggal;
    }

    public String getIsi()
    {
        return isi;
    }

    public void setIsi(String isi)
    {
        this.isi = isi;
    }

    public PakTani getPakTani()
    {
        return pakTani;
    }

    public void setPakTani(PakTani pakTani)
    {
        this.pakTani = pakTani;
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
        dest.writeString(id);
        dest.writeString(tanggal);
        dest.writeString(isi);
        dest.writeParcelable(pakTani, flags);
        dest.writeByte((byte) (bookmarked ? 1 : 0));
    }
}
