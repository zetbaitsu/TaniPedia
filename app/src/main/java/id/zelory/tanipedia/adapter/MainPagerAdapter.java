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

package id.zelory.tanipedia.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import id.zelory.tanipedia.fragment.BeritaFragment;
import id.zelory.tanipedia.fragment.CuacaFragment;
import id.zelory.tanipedia.fragment.HargaFragment;
import id.zelory.tanipedia.fragment.TanyaTaniFragment;

/**
 * Created by zetbaitsu on 4/23/2015.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter
{
    public MainPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int i)
    {
        switch (i)
        {
            case 0:
                return new CuacaFragment();
            case 1:
                return new BeritaFragment();
            case 2:
                return new HargaFragment();
            case 3:
                return new TanyaTaniFragment();
        }

        return null;
    }

    @Override
    public int getCount()
    {
        return 4;
    }
}
