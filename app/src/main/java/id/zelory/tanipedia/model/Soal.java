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
public class Soal implements Parcelable
{
    public static final String API_AMBIL = "http://apitanipedia.appspot.com/tanya/ambil-soal";
    public static final String API_KIRIM = "http://apitanipedia.appspot.com/tanya/kirim-soal";

    public String id;
    private String nama;
    private String tanggal;
    private String isi;

    public Soal()
    {

    }

    private Soal(Parcel in)
    {
        id = in.readString();
        nama = in.readString();
        tanggal = in.readString();
        isi = in.readString();
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getNama()
    {
        return nama;
    }

    public void setNama(String nama)
    {
        this.nama = nama;
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

    @Override
    public int describeContents()
    {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(id);
        parcel.writeString(nama);
        parcel.writeString(tanggal);
        parcel.writeString(isi);
    }

    public static final Creator<Soal> CREATOR
            = new Creator<Soal>()
    {
        public Soal createFromParcel(Parcel in)
        {
            return new Soal(in);
        }

        public Soal[] newArray(int size)
        {
            return new Soal[size];
        }
    };
}
