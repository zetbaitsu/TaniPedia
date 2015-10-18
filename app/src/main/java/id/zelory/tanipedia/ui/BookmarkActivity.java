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

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.Bind;
import id.zelory.benih.BenihActivity;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.ui.fragment.BookmarkBeritaFragment;
import id.zelory.tanipedia.ui.fragment.BookmarkSoalFragment;

/**
 * Created on : October 18, 2015
 * Author     : zetbaitsu
 * Name       : Zetra
 * Email      : zetra@mail.ugm.ac.id
 * GitHub     : https://github.com/zetbaitsu
 * LinkedIn   : https://id.linkedin.com/in/zetbaitsu
 */
public class BookmarkActivity extends BenihActivity
{
    public static final String KEY_TYPE = "type";
    public static final int TYPE_BERITA = 1;
    public static final int TYPE_SOAL = 2;

    @Bind(R.id.toolbar) Toolbar toolbar;
    private int type;

    @Override
    protected int getActivityView()
    {
        return R.layout.activity_bookmark;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState)
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        type = getIntent().getIntExtra(KEY_TYPE, 0);

        if (type == 0 && savedInstanceState != null)
        {
            type = savedInstanceState.getInt(KEY_TYPE, 1);
        }

        showContent();
    }

    private void showContent()
    {
        switch (type)
        {
            case 1:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new BookmarkBeritaFragment())
                        .commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new BookmarkSoalFragment())
                        .commit();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        onBackPressed();
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState)
    {
        super.onCreate(savedInstanceState, persistentState);
        savedInstanceState.putInt(KEY_TYPE, type);
    }
}
