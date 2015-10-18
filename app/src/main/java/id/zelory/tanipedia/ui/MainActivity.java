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

package id.zelory.tanipedia.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import id.zelory.benih.BenihActivity;
import id.zelory.benih.util.BenihPreferenceUtils;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.data.LocalDataManager;
import id.zelory.tanipedia.ui.fragment.BaseFragment;
import id.zelory.tanipedia.ui.fragment.BeritaFragment;
import id.zelory.tanipedia.ui.fragment.CuacaFragment;
import id.zelory.tanipedia.ui.fragment.KomoditasFragment;
import id.zelory.tanipedia.ui.fragment.TanyaFragment;

/**
 * Created on : October 15, 2015
 * Author     : zetbaitsu
 * Name       : Zetra
 * Email      : zetra@mail.ugm.ac.id
 * GitHub     : https://github.com/zetbaitsu
 * LinkedIn   : https://id.linkedin.com/in/zetbaitsu
 */
public class MainActivity extends BenihActivity
{
    @Bind(R.id.nav_drawer) DrawerLayout drawerLayout;
    private BaseFragment fragments[] = {
            new CuacaFragment(),
            new BeritaFragment(),
            new TanyaFragment(),
            new KomoditasFragment()
    };

    private Bundle states[] = new Bundle[4];

    private int position = 0;

    @Override
    protected int getActivityView()
    {
        return R.layout.activity_main;
    }

    @Override
    protected void onViewReady(Bundle bundle)
    {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container, fragments[0])
                .commit();
        setUpNavigationDrawer();
    }

    private void setUpNavigationDrawer()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        View header = navigationView.inflateHeaderView(R.layout.header_drawer);
        ImageView foto = (ImageView) header.findViewById(R.id.pak_tani);
        foto.setImageResource(LocalDataManager.getPakTani().isMale() ? R.drawable.pak_tani : R.drawable.bu_tani);
        foto.setOnClickListener(this::onProfileClick);
        TextView nama = (TextView) header.findViewById(R.id.nama);
        nama.setText(BenihPreferenceUtils.getString(this, "nama"));
        TextView email = (TextView) header.findViewById(R.id.email);
        email.setText(BenihPreferenceUtils.getString(this, "email"));
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            if (menuItem.isChecked())
            {
                drawerLayout.closeDrawers();
            } else
            {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                states[position] = fragments[position].getState();
                boolean changeFragment = true;
                switch (menuItem.getItemId())
                {
                    case R.id.cuaca:
                        position = 0;
                        break;
                    case R.id.berita:
                        position = 1;
                        break;
                    case R.id.tanya:
                        position = 2;
                        break;
                    case R.id.harga:
                        position = 3;
                        break;
                    case R.id.logout:
                        changeFragment = false;
                        BenihPreferenceUtils.putString(this, "nama", null);
                        Intent intent = new Intent(this, SignInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                    case R.id.tentang:
                        changeFragment = false;
                        menuItem.setChecked(false);
                        startActivity(new Intent(this, TentangActivity.class));
                        break;
                }
                if (changeFragment)
                {
                    fragments[position].setArguments(states[position]);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container, fragments[position])
                            .commit();
                }
            }
            return true;
        });
    }

    private void onProfileClick(View v)
    {
        drawerLayout.closeDrawers();
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.KEY_PAK_TANI, LocalDataManager.getPakTani());
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawers();
        } else
        {
            super.onBackPressed();
        }
    }
}
