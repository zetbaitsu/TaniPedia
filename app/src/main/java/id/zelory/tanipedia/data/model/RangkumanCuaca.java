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
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on : October 24, 2015
 * Author     : zetbaitsu
 * Name       : Zetra
 * Email      : zetra@mail.ugm.ac.id
 * GitHub     : https://github.com/zetbaitsu
 * LinkedIn   : https://id.linkedin.com/in/zetbaitsu
 */
public class RangkumanCuaca implements Parcelable
{
    private List<Cuaca> cuacaList;
    private double rataSuhu;
    private double suhuMin;
    private double suhuMax;
    private double rataTekanan;
    private double rataKelembaban;
    private double rataKecepatanAngin;
    private String rataArahAngin;
    private String rangeTanggal;
    private String rataCuaca;
    private String lokasi;
    private String kegiatan;
    private int size;

    public RangkumanCuaca(List<Cuaca> cuacaList)
    {
        this.cuacaList = cuacaList;
        size = cuacaList.size();
        if (size > 0)
        {
            String awal = cuacaList.get(0).getTanggal();
            awal = awal.substring(awal.indexOf(","));
            awal = awal.substring(2);
            String akhir = cuacaList.get(size - 1).getTanggal();
            akhir = akhir.substring(akhir.indexOf(","));
            akhir = akhir.substring(2);
            rangeTanggal = awal + " - " + akhir;
            lokasi = cuacaList.get(0).getLokasi();
            hitungRataSuhu();
            cariSuhuMin();
            cariSuhuMax();
            hitungRataTekanan();
            hitungRataKelembaban();
            hitungRataKecepatanAngin();
            hitungArahAngin();
            hitungRataCuaca();
            cariKegiatan();
        }
    }

    private void cariKegiatan()
    {
        for (int i = 0; i < size; i++)
        {
            if (cuacaList.get(i).getCuaca().equals(rataCuaca))
            {
                kegiatan = cuacaList.get(i).getKegiatan();
                break;
            }
        }
    }

    protected RangkumanCuaca(Parcel in)
    {
        cuacaList = in.createTypedArrayList(Cuaca.CREATOR);
        rataSuhu = in.readDouble();
        suhuMin = in.readDouble();
        suhuMax = in.readDouble();
        rataTekanan = in.readDouble();
        rataKelembaban = in.readDouble();
        rataKecepatanAngin = in.readDouble();
        rataArahAngin = in.readString();
        rangeTanggal = in.readString();
        rataCuaca = in.readString();
        lokasi = in.readString();
        kegiatan = in.readString();
        size = in.readInt();
    }

    public static final Creator<RangkumanCuaca> CREATOR = new Creator<RangkumanCuaca>()
    {
        @Override
        public RangkumanCuaca createFromParcel(Parcel in)
        {
            return new RangkumanCuaca(in);
        }

        @Override
        public RangkumanCuaca[] newArray(int size)
        {
            return new RangkumanCuaca[size];
        }
    };

    private void hitungRataCuaca()
    {
        HashMap<String, Integer> cuaca = new HashMap<>();
        cuaca.put("Cerah", 0);
        cuaca.put("Berawan", 0);
        cuaca.put("Hujan", 0);

        for (int i = 0; i < size; i++)
        {
            cuaca.put(cuacaList.get(i).getCuaca(), cuaca.get(cuacaList.get(i).getCuaca()) + 1);
        }

        Map.Entry<String, Integer> maxEntry = null;

        for (Map.Entry<String, Integer> entry : cuaca.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
            Log.d("zet", entry.getKey() + " : " + entry.getValue());
        }

        rataCuaca = maxEntry.getKey();
    }

    private void hitungArahAngin()
    {
        HashMap<String, Integer> angin = new HashMap<>();
        angin.put("Utara", 0);
        angin.put("Timur Laut", 0);
        angin.put("Timur", 0);
        angin.put("Tenggara", 0);
        angin.put("Selatan", 0);
        angin.put("Barat Daya", 0);
        angin.put("Barat", 0);
        angin.put("Barat Laut", 0);

        for (int i = 0; i < size; i++)
        {
            angin.put(cuacaList.get(i).getArahAngin(), angin.get(cuacaList.get(i).getArahAngin()) + 1);
        }

        Map.Entry<String, Integer> maxEntry = null;

        for (Map.Entry<String, Integer> entry : angin.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
            Log.d("zet", entry.getKey() + " : " + entry.getValue());
        }

        rataArahAngin = maxEntry.getKey();
    }

    private void hitungRataKecepatanAngin()
    {
        double sum = 0;
        int count = 0;
        for (int i = 0; i < size; i++)
        {
            if (cuacaList.get(i).getKecepatanAngin() > 0)
            {
                sum += cuacaList.get(i).getKecepatanAngin();
                count++;
            }
        }

        rataKecepatanAngin = sum / count;
    }

    private void hitungRataKelembaban()
    {
        double sum = 0;
        int count = 0;
        for (int i = 0; i < size; i++)
        {
            if (cuacaList.get(i).getKelembaban() > 0)
            {
                sum += cuacaList.get(i).getKelembaban();
                count++;
            }
        }

        rataKelembaban = sum / count;
    }

    private void hitungRataTekanan()
    {
        double sum = 0;
        int count = 0;
        for (int i = 0; i < size; i++)
        {
            if (cuacaList.get(i).getTekanan() > 0)
            {
                sum += cuacaList.get(i).getTekanan();
                count++;
            }

            Log.d("Zet", cuacaList.get(i).getTekanan() + " hpa");
        }

        rataTekanan = sum / count;
    }

    private void cariSuhuMax()
    {
        suhuMax = cuacaList.get(0).getSuhuMax();
        for (int i = 1; i < size; i++)
        {
            if (cuacaList.get(i).getSuhuMax() > suhuMax)
            {
                suhuMax = cuacaList.get(i).getSuhuMax();
            }
        }
    }

    private void cariSuhuMin()
    {
        suhuMin = cuacaList.get(0).getSuhuMin();
        for (int i = 1; i < size; i++)
        {
            if (cuacaList.get(i).getSuhuMin() < suhuMin)
            {
                suhuMin = cuacaList.get(i).getSuhuMin();
            }
        }
    }

    private void hitungRataSuhu()
    {
        double sum = 0;
        for (int i = 0; i < size; i++)
        {
            sum += cuacaList.get(i).getSuhu();
        }
        rataSuhu = sum / size;
    }

    public String getRataSuhu()
    {
        return String.format("%.2f", rataSuhu);
    }

    public double getSuhuMin()
    {
        return suhuMin;
    }

    public double getSuhuMax()
    {
        return suhuMax;
    }

    public String getRataTekanan()
    {
        return String.format("%.2f", rataTekanan);
    }

    public String getRataKelembaban()
    {
        return String.format("%.2f", rataKelembaban);
    }

    public String getRataKecepatanAngin()
    {
        return String.format("%.2f", rataKecepatanAngin);
    }

    public String getRataArahAngin()
    {
        return rataArahAngin;
    }

    public String getRangeTanggal()
    {
        return rangeTanggal;
    }

    public String getRataCuaca()
    {
        return rataCuaca;
    }

    public String getLokasi()
    {
        return lokasi;
    }

    public String getKegiatan()
    {
        return kegiatan;
    }

    @Override
    public int describeContents()
    {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeTypedList(cuacaList);
        dest.writeDouble(rataSuhu);
        dest.writeDouble(suhuMin);
        dest.writeDouble(suhuMax);
        dest.writeDouble(rataTekanan);
        dest.writeDouble(rataKelembaban);
        dest.writeDouble(rataKecepatanAngin);
        dest.writeString(rataArahAngin);
        dest.writeString(rangeTanggal);
        dest.writeString(rataCuaca);
        dest.writeString(lokasi);
        dest.writeString(kegiatan);
        dest.writeInt(size);
    }
}
