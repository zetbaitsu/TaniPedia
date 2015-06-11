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

import android.support.v7.widget.RecyclerView;

public abstract class MyRecyclerScroll extends RecyclerView.OnScrollListener
{
    private static final float HIDE_THRESHOLD = 100;
    private static final float SHOW_THRESHOLD = 50;

    int scrollDist = 0;
    private boolean isVisible = true;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy)
    {
        super.onScrolled(recyclerView, dx, dy);
        if (isVisible && scrollDist > HIDE_THRESHOLD)
        {
            hide();
            scrollDist = 0;
            isVisible = false;
        } else if (!isVisible && scrollDist < -SHOW_THRESHOLD)
        {
            show();
            scrollDist = 0;
            isVisible = true;
        }

        if ((isVisible && dy > 0) || (!isVisible && dy < 0))
        {
            scrollDist += dy;
        }
    }

    public abstract void show();

    public abstract void hide();
}
