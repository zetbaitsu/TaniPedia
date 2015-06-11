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
public class Komoditas implements Parcelable
{
    public static final String API = "http://apitanipedia.appspot.com/komoditas";
    public static final Creator<Komoditas> CREATOR
            = new Creator<Komoditas>()
    {
        public Komoditas createFromParcel(Parcel in)
        {
            return new Komoditas(in);
        }

        public Komoditas[] newArray(int size)
        {
            return new Komoditas[size];
        }
    };
    private String nama;
    private String harga;

    public Komoditas()
    {

    }

    public Komoditas(Parcel in)
    {
        nama = in.readString();
        harga = in.readString();
    }

    public String getNama()
    {
        return nama;
    }

    public void setNama(String nama)
    {
        this.nama = nama;
    }

    public String getHarga()
    {
        return harga;
    }

    public void setHarga(String harga)
    {
        harga = harga.replace(",",".");
        harga = harga+",00";
        this.harga = harga;
    }

    @Override
    public int describeContents()
    {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(nama);
        parcel.writeString(harga);
    }
}
