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

package id.zelory.tanipedia.controller;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import id.zelory.benih.controller.BenihController;
import id.zelory.benih.util.BenihPreferenceUtils;
import id.zelory.benih.util.BenihScheduler;
import id.zelory.benih.util.BenihWorker;
import id.zelory.tanipedia.TaniPediaApp;
import id.zelory.tanipedia.data.api.TaniPediaApi;
import id.zelory.tanipedia.data.model.Cuaca;
import id.zelory.tanipedia.data.model.RangkumanCuaca;
import timber.log.Timber;

/**
 * Created on : October 15, 2015
 * Author     : zetbaitsu
 * Name       : Zetra
 * Email      : zetra@mail.ugm.ac.id
 * GitHub     : https://github.com/zetbaitsu
 * LinkedIn   : https://id.linkedin.com/in/zetbaitsu
 */
public class CuacaController extends BenihController<CuacaController.Presenter> implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    private List<Cuaca> listCuaca;
    private RangkumanCuaca rangkumanCuaca;
    private GoogleApiClient googleApiClient;
    private double latitude;
    private double longitude;

    public CuacaController(Presenter presenter)
    {
        super(presenter);
        if (checkPlayServices())
        {
            buildGoogleApiClient();
        } else
        {
            presenter.showError(new Throwable("Google Play Services Not Found"));
        }
    }

    public void connect()
    {
        if (googleApiClient != null)
        {
            googleApiClient.connect();
        } else
        {
            presenter.showError(new Throwable("Lokasi tidak ditemukan"));
        }
    }

    public void loadListCuaca()
    {
        presenter.showLoading();
        TaniPediaApi.pluck()
                .getApi()
                .getCuaca(latitude, longitude)
                .compose(BenihScheduler.pluck().applySchedulers(BenihScheduler.Type.IO))
                .subscribe(listCuaca -> {

                    int size = listCuaca.size();

                    for (int i = 0; i < size; i++)
                    {
                        listCuaca.get(i).setTanggal(listCuaca.get(i).getTanggal().replace("Oct", "Okt"));
                    }

                    this.listCuaca = listCuaca;
                    if (presenter != null)
                    {
                        presenter.showListCuaca(listCuaca);
                        presenter.dismissLoading();
                    }
                }, throwable -> {
                    if (presenter != null)
                    {
                        presenter.showError(throwable);
                        presenter.dismissLoading();
                    }
                });
    }

    public void loadRangkumanCuaca(List<Cuaca> cuacaList)
    {
        presenter.showLoading();
        BenihWorker.pluck()
                .doInComputation(() -> {
                    rangkumanCuaca = new RangkumanCuaca(cuacaList);
                }).subscribe(o -> {
            if (presenter != null)
            {
                presenter.showRangkumanCuaca(rangkumanCuaca);
                presenter.dismissLoading();
            }
        }, throwable -> {
            if (presenter != null)
            {
                presenter.showError(throwable);
                presenter.dismissLoading();
            }
        });
    }

    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(TaniPediaApp.pluck().getApplicationContext());
        return resultCode == ConnectionResult.SUCCESS;
    }

    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(TaniPediaApp.pluck().getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void saveState(Bundle bundle)
    {
        Timber.d("saveState");
        bundle.putParcelableArrayList("listCuaca", (ArrayList<Cuaca>) listCuaca);
        bundle.putParcelable("rangkuman", rangkumanCuaca);
        saveLocation();
    }

    @Override
    public void loadState(Bundle bundle)
    {
        Timber.d("loadState");
        listCuaca = bundle.getParcelableArrayList("listCuaca");
        if (listCuaca != null)
        {
            new Handler().postDelayed(() -> presenter.showListCuaca(listCuaca), 500);
        } else
        {
            presenter.showError(new Throwable("Error"));
        }

        rangkumanCuaca = bundle.getParcelable("rangkuman");
        if (rangkumanCuaca != null)
        {
            new Handler().postDelayed(() -> presenter.showRangkumanCuaca(rangkumanCuaca), 500);
        } else
        {
            presenter.showError(new Throwable("Error"));
        }
        restoreLocation();
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null)
        {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            saveLocation();
            new Handler().postDelayed(this::loadListCuaca, 800);
        } else
        {
            restoreLocation();
            new Handler().postDelayed(this::loadListCuaca, 800);
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
        restoreLocation();
        new Handler().postDelayed(this::loadListCuaca, 800);
    }

    private void saveLocation()
    {
        BenihPreferenceUtils.putDouble(TaniPediaApp.pluck().getApplicationContext(), "lat", latitude);
        BenihPreferenceUtils.putDouble(TaniPediaApp.pluck().getApplicationContext(), "lon", longitude);
    }

    private void restoreLocation()
    {
        latitude = BenihPreferenceUtils.getDouble(TaniPediaApp.pluck().getApplicationContext(), "lat");
        longitude = BenihPreferenceUtils.getDouble(TaniPediaApp.pluck().getApplicationContext(), "lon");
    }

    public interface Presenter extends BenihController.Presenter
    {
        void showLoading();

        void dismissLoading();

        void showListCuaca(List<Cuaca> listCuaca);

        void showRangkumanCuaca(RangkumanCuaca rangkumanCuaca);
    }
}
