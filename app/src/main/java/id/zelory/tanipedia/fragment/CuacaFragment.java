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

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.JazzyListView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import id.zelory.tanipedia.R;
import id.zelory.tanipedia.adapter.CuacaAdapter;
import id.zelory.tanipedia.model.Cuaca;
import it.carlom.stikkyheader.core.StikkyHeaderBuilder;
import it.carlom.stikkyheader.core.StikkyHeaderListView;
import it.carlom.stikkyheader.core.animator.AnimatorBuilder;
import it.carlom.stikkyheader.core.animator.HeaderStikkyAnimator;

/**
 * Created by zetbaitsu on 4/23/2015.
 */
public class CuacaFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener
{
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private GoogleApiClient googleApiClient;
    private double latitude;
    private double longitude;
    private ArrayList<Cuaca> cuacaArrayList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private JazzyListView listView;
    private CuacaAdapter adapter;

    public CuacaFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_cuaca, container, false);

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
    public void onStart()
    {
        super.onStart();
        if (googleApiClient != null)
        {
            googleApiClient.connect();
        }
    }

    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else
            {
                Toast.makeText(getActivity(),
                        "HP anda belum memenuhi syarat.", Toast.LENGTH_LONG)
                        .show();
                getActivity().finish();
            }
            return false;
        }
        return true;
    }

    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
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

        if (checkPlayServices())
        {
            buildGoogleApiClient();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        checkPlayServices();
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        Location location = LocationServices.FusedLocationApi
                .getLastLocation(googleApiClient);

        if (location != null)
        {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    onRefresh();
                }
            }, 800);
        } else
        {

        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        Log.d("zet", "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    @Override
    public void onRefresh()
    {
        swipeRefreshLayout.setRefreshing(true);
        new DownloadCuaca().execute();
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

    private class DownloadCuaca extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            ObjectMapper mapper = new ObjectMapper();
            while (cuacaArrayList == null)
            {
                try
                {
                    cuacaArrayList = mapper.readValue(new URL(Cuaca.API + "lat=" + latitude + "&lon=" + longitude),
                            mapper.getTypeFactory().constructCollectionType(ArrayList.class, Cuaca.class));
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
            adapter = new CuacaAdapter(getActivity(), cuacaArrayList);
            listView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
