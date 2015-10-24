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
public class Cuaca implements Parcelable
{

    private String lokasi;
    private double suhuMax;
    private double suhuMin;
    private double suhu;
    private String tanggal;
    private String cuaca;
    private String kegiatan;
    private double tekanan;
    private double kelembaban;
    private double kecepatanAngin;
    private String arahAngin;

    public Cuaca()
    {
    }

    protected Cuaca(Parcel in)
    {
        lokasi = in.readString();
        suhuMax = in.readDouble();
        suhuMin = in.readDouble();
        suhu = in.readDouble();
        tanggal = in.readString();
        cuaca = in.readString();
        kegiatan = in.readString();
        tekanan = in.readDouble();
        kelembaban = in.readDouble();
        kecepatanAngin = in.readDouble();
        arahAngin = in.readString();
    }

    public static final Creator<Cuaca> CREATOR = new Creator<Cuaca>()
    {
        @Override
        public Cuaca createFromParcel(Parcel in)
        {
            return new Cuaca(in);
        }

        @Override
        public Cuaca[] newArray(int size)
        {
            return new Cuaca[size];
        }
    };

    public String getLokasi()
    {
        return lokasi;
    }

    public void setLokasi(String lokasi)
    {
        this.lokasi = lokasi;
    }

    public double getSuhuMax()
    {
        return suhuMax;
    }

    public void setSuhuMax(double suhuMax)
    {
        this.suhuMax = suhuMax;
    }

    public double getSuhuMin()
    {
        return suhuMin;
    }

    public void setSuhuMin(double suhuMin)
    {
        this.suhuMin = suhuMin;
    }

    public double getSuhu()
    {
        return suhu;
    }

    public void setSuhu(double suhu)
    {
        this.suhu = suhu;
    }

    public String getTanggal()
    {
        return tanggal;
    }

    public void setTanggal(String tanggal)
    {
        this.tanggal = tanggal;
    }

    public String getCuaca()
    {
        return cuaca;
    }

    public void setCuaca(String cuaca)
    {
        this.cuaca = cuaca;
    }

    public String getKegiatan()
    {
        return kegiatan;
    }

    public void setKegiatan(String kegiatan)
    {
        this.kegiatan = kegiatan;
    }

    public double getTekanan()
    {
        return tekanan;
    }

    public void setTekanan(double tekanan)
    {
        this.tekanan = tekanan;
    }

    public double getKelembaban()
    {
        return kelembaban;
    }

    public void setKelembaban(double kelembaban)
    {
        this.kelembaban = kelembaban;
    }

    public double getKecepatanAngin()
    {
        return kecepatanAngin;
    }

    public void setKecepatanAngin(double kecepatanAngin)
    {
        this.kecepatanAngin = kecepatanAngin;
    }

    public String getArahAngin()
    {
        return arahAngin;
    }

    public void setArahAngin(String arahAngin)
    {
        this.arahAngin = arahAngin;
    }

    @Override
    public int describeContents()
    {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(lokasi);
        dest.writeDouble(suhuMax);
        dest.writeDouble(suhuMin);
        dest.writeDouble(suhu);
        dest.writeString(tanggal);
        dest.writeString(cuaca);
        dest.writeString(kegiatan);
        dest.writeDouble(tekanan);
        dest.writeDouble(kelembaban);
        dest.writeDouble(kecepatanAngin);
        dest.writeString(arahAngin);
    }
}
