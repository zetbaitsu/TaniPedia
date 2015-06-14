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

import android.app.ActivityManager;
import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import id.zelory.tanipedia.model.Berita;

/**
 * Created by zetbaitsu on 4/26/2015.
 */
public class Utils
{
    public static int randInt(int min, int max)
    {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public static ArrayList<Berita> getRandomBerita(Context context, String alamat)
    {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Berita> beritaArrayList = null;
        try
        {
            beritaArrayList = mapper.readValue(PrefUtils.ambilString(context, "berita"),
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class, Berita.class));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        for (int i = 0; i < 5; i++)
        {
            int x = randInt(0, beritaArrayList.size()-1);
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
            beritaArrayList.remove(i);

        return beritaArrayList;
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context)
    {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if (serviceClass.getName().equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean isMyAppRunning(Context context)
    {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        boolean run = false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses)
        {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
            {
                if (appProcess.processName.equals("id.zelory.tanipedia"))
                {
                    run = true;
                    break;
                }
            }
        }

        return run;
    }
}
