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
 * Created on : October 23, 2015
 * Author     : zetbaitsu
 * Name       : Zetra
 * Email      : zetra@mail.ugm.ac.id
 * GitHub     : https://github.com/zetbaitsu
 * LinkedIn   : https://id.linkedin.com/in/zetbaitsu
 */
public class Notifikasi implements Parcelable
{
    private String id;
    private Soal soal;
    private String tanggal;
    private String isi;
    private PakTani pakTani;

    public Notifikasi()
    {

    }

    protected Notifikasi(Parcel in)
    {
        id = in.readString();
        soal = in.readParcelable(Soal.class.getClassLoader());
        tanggal = in.readString();
        isi = in.readString();
        pakTani = in.readParcelable(PakTani.class.getClassLoader());
    }

    public static final Creator<Notifikasi> CREATOR = new Creator<Notifikasi>()
    {
        @Override
        public Notifikasi createFromParcel(Parcel in)
        {
            return new Notifikasi(in);
        }

        @Override
        public Notifikasi[] newArray(int size)
        {
            return new Notifikasi[size];
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

    public Soal getSoal()
    {
        return soal;
    }

    public void setSoal(Soal soal)
    {
        this.soal = soal;
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

    @Override
    public int describeContents()
    {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(id);
        dest.writeParcelable(soal, flags);
        dest.writeString(tanggal);
        dest.writeString(isi);
        dest.writeParcelable(pakTani, flags);
    }
}
