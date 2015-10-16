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

package id.zelory.tanipedia.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zetbaitsu on 4/23/2015.
 */
public class Jawaban implements Parcelable
{
    private String idSoal;
    private String tanggal;
    private String isi;
    private PakTani pakTani;

    public Jawaban()
    {

    }

    protected Jawaban(Parcel in)
    {
        idSoal = in.readString();
        tanggal = in.readString();
        isi = in.readString();
        pakTani = in.readParcelable(PakTani.class.getClassLoader());
    }

    public static final Creator<Jawaban> CREATOR = new Creator<Jawaban>()
    {
        @Override
        public Jawaban createFromParcel(Parcel in)
        {
            return new Jawaban(in);
        }

        @Override
        public Jawaban[] newArray(int size)
        {
            return new Jawaban[size];
        }
    };

    public String getIdSoal()
    {
        return idSoal;
    }

    public void setIdSoal(String idSoal)
    {
        this.idSoal = idSoal;
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
        dest.writeString(idSoal);
        dest.writeString(tanggal);
        dest.writeString(isi);
        dest.writeParcelable(pakTani, flags);
    }
}
