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

package id.zelory.tanipedia.network;

import java.util.List;

import id.zelory.benih.network.BenihServiceGenerator;
import id.zelory.tanipedia.model.Berita;
import id.zelory.tanipedia.model.Cuaca;
import id.zelory.tanipedia.model.Jawaban;
import id.zelory.tanipedia.model.Komoditas;
import id.zelory.tanipedia.model.PakTani;
import id.zelory.tanipedia.model.Soal;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by zetbaitsu on 7/24/15.
 */
public enum TaniPediaService
{
    HARVEST;
    private final Api api;

    TaniPediaService()
    {
        api = BenihServiceGenerator.createService(Api.class, Api.ENDPOINT);
    }

    public static TaniPediaService pluck()
    {
        return HARVEST;
    }

    public Api getApi()
    {
        return api;
    }

    public interface Api
    {
        String ENDPOINT = "http://apitanipedia.appspot.com";

        @POST("/login")
        Observable<PakTani> login(@Query("email") String email, @Query("pass") String password);

        @POST("/register")
        Observable<Status> register(@Query("email") String email, @Query("nama") String nama, @Query("pass") String password, @Query("male") boolean males);

        @GET("/cuaca")
        Observable<List<Cuaca>> getCuaca(@Query("lat") double lat, @Query("lon") double lon);

        @GET("/berita")
        Observable<List<Berita>> getAllBerita();

        @GET("/berita")
        Observable<Berita> getBerita(@Query("url") String url);

        @GET("/tanya/ambil-soal")
        Observable<List<Soal>> getPertanyaan();

        @GET("/tanya/ambil-soal")
        Observable<List<Soal>> getPertanyaan(@Query("email") String email);

        @POST("/tanya/kirim-soal")
        Observable<Status> postPertanyaan(@Query("email") String email, @Query("isi") String isi);

        @GET("/tanya/ambil-jawaban")
        Observable<List<Jawaban>> getJawaban(@Query("idSoal") String idSoal);

        @POST("/tanya/kirim-jawaban")
        Observable<Status> postJawaban(@Query("idSoal") String idSoal, @Query("email") String email, @Query("isi") String isi);

        @GET("/komoditas")
        Observable<List<Komoditas>> getKomoditas();

        @GET("/pak-tani")
        Observable<PakTani> getPakTani(@Query("email") String email);

        @POST("/pak-tani")
        Observable<PakTani> updatePakTani(@Query("email") String email, @Query("nama") String nama, @Query("pass") String password, @Query("male") boolean male);
    }
}
