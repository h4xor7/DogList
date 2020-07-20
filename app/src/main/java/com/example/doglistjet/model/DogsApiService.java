package com.example.doglistjet.model;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DogsApiService {
//   ye hai retrofit k liye
    private static final String Base_URL = "https://raw.githubusercontent.com";
    private DogsApi api;

    public DogsApiService() {

        api = new Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(DogsApi.class);

    }

    //ye hai rxjava ka component jo ki ek bar me ek single return krega
    public Single<List<DogBreed>> getDogs() {
        return api.getDogs();
    }


}
