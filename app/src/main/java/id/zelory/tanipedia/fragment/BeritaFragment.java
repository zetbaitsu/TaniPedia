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

package id.zelory.tanipedia.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;
import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.JazzyListView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import id.zelory.tanipedia.R;
import id.zelory.tanipedia.adapter.BeritaAdapter;
import id.zelory.tanipedia.model.Berita;
import id.zelory.tanipedia.util.Utils;
import it.carlom.stikkyheader.core.StikkyHeaderBuilder;
import it.carlom.stikkyheader.core.StikkyHeaderListView;
import it.carlom.stikkyheader.core.animator.AnimatorBuilder;
import it.carlom.stikkyheader.core.animator.HeaderStikkyAnimator;

/**
 * Created by zetbaitsu on 4/23/2015.
 */
public class BeritaFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, SwipeRefreshLayout.OnRefreshListener
{
    private ArrayList<Berita> beritaArrayList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private JazzyListView listView;
    private BeritaAdapter adapter;
    private ImageView imageView;

    public BeritaFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_berita, container, false);

        imageView = (ImageView) view.findViewById(R.id.header_image);

        listView = (JazzyListView) view.findViewById(R.id.list_item);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        listView.setTransitionEffect(JazzyHelper.GROW);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }

    private class ParallaxStikkyAnimator extends HeaderStikkyAnimator
    {
        @Override
        public AnimatorBuilder getAnimatorBuilder()
        {
            View mHeader_image = getHeader().findViewById(R.id.header_image);
            return AnimatorBuilder.create().applyVerticalParallax(mHeader_image);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        StikkyHeaderListView stikkyHeaderListView = (StikkyHeaderListView) StikkyHeaderBuilder.stickTo(listView)
                .setHeader(R.id.header, (ViewGroup) getView())
                .minHeightHeaderRes(R.dimen.min_height_header)
                .animator(new ParallaxStikkyAnimator())
                .build();
        stikkyHeaderListView.setSwipeRefreshLayout(swipeRefreshLayout);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                onRefresh();
            }
        }, 800);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        return false;
    }

    @Override
    public void onRefresh()
    {
        swipeRefreshLayout.setRefreshing(true);
        new DownloadData().execute();
    }

    private class DownloadData extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            ObjectMapper mapper = new ObjectMapper();
            while (beritaArrayList == null)
            {
                try
                {
                    beritaArrayList = mapper.readValue(new URL(Berita.API),
                            mapper.getTypeFactory().constructCollectionType(ArrayList.class, Berita.class));
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            adapter = new BeritaAdapter(getActivity(), beritaArrayList);
            listView.setAdapter(adapter);
            Picasso.with(getActivity())
                    .load(((Berita) adapter.getItem(Utils.randInt(0, beritaArrayList.size() - 1))).getGambar())
                    .into(imageView);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
