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

package id.zelory.tanipedia.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import id.zelory.benih.utils.PrefUtils;
import id.zelory.tanipedia.model.Berita;

/**
 * Created by zetbaitsu on 4/26/2015.
 */
public class Utils extends id.zelory.benih.utils.Utils
{
    public static ArrayList<Berita> getRandomBerita(Context context, String alamat)
    {
        ArrayList<Berita> beritaArrayList = new Gson().fromJson(PrefUtils.getString(context, "berita"),
                                                                new TypeToken<ArrayList<Berita>>() {}.getType());

        for (int i = 0; i < 5; i++)
        {
            int x = randInt(0, beritaArrayList.size() - 1);
            if (beritaArrayList.get(x).getAlamat().equals(alamat))
            {
                beritaArrayList.remove(x);
                i--;
            } else
            {
                beritaArrayList.set(i, beritaArrayList.get(x));
                beritaArrayList.remove(x);
            }
        }

        for (int i = 5; i < beritaArrayList.size(); i++)
        {
            beritaArrayList.remove(i);
        }

        return beritaArrayList;
    }
}
