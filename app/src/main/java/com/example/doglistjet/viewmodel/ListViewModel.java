package com.example.doglistjet.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.doglistjet.model.DogBreed;
import com.example.doglistjet.model.DogDao;
import com.example.doglistjet.model.DogDatabase;
import com.example.doglistjet.model.DogsApiService;
import com.example.doglistjet.util.NotificationsHelper;
import com.example.doglistjet.util.SharedPrefrencesHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListViewModel extends AndroidViewModel {

    public MutableLiveData<List<DogBreed>> dogs = new MutableLiveData<List<DogBreed>>();
    public MutableLiveData<Boolean> dogLoadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>();
    private DogsApiService dogsService = new DogsApiService();
    private CompositeDisposable disposable = new CompositeDisposable();
    private AsyncTask<List<DogBreed>, Void, List<DogBreed>> insertTask;
    private AsyncTask<Void, Void, List<DogBreed>> retriveTask;

    private SharedPrefrencesHelper prefrencesHelper = SharedPrefrencesHelper.getInstance(getApplication());
    private long refreshTime = 5 * 60 * 1000 * 1000 * 1000L;


    public ListViewModel(@NonNull Application application) {
        super(application);


    }

    public void refresh() {

        checkCacheDuration();
        long updateTime = prefrencesHelper.getUpdateTime();
        long currentTime = System.nanoTime();

        if (updateTime != 0 && currentTime - updateTime < refreshTime) {
            fetchFromDatabse();
        } else {
            fetchFromRemote();

        }

    }


    public  void refreshBypassCache(){
      fetchFromRemote();


    }


    private  void checkCacheDuration(){
       String cachePrefs =prefrencesHelper.getCacheDuration();
        if (!cachePrefs.equals("")){

            try{
                int cachePrefsInt = Integer.parseInt(cachePrefs);
                refreshTime = cachePrefsInt *1000 *1000 *1000L;
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }



    public void fetchFromRemote() {
        // getDogs ye method hai jo  interface  hai  ye ek single return krega jo ki observer hai
        //subscribeOn is of Rx java as we dont know how much time it will take to get network result
        // subscribeOn  will block ui from user interaction  since android does not allow to do network
        //call in  main thread

        //
        loading.setValue(true);
        disposable.add(

                dogsService.getDogs() // interface method
                        .subscribeOn(Schedulers.newThread()) // new call be take place in new thread
                        .observeOn(AndroidSchedulers.mainThread()) // we will recieve result in main thread
                        .subscribeWith(new DisposableSingleObserver<List<DogBreed>>() {
                            @Override
                            public void onSuccess(List<DogBreed> dogBreeds) {
                                insertTask = new InsertDogsTask();
                                insertTask.execute(dogBreeds);
                                NotificationsHelper.getInstance(getApplication()).createNotification();
                            }


                            @Override
                            public void onError(Throwable e) {

                                dogLoadError.setValue(true);
                                loading.setValue(false);
                                e.printStackTrace();

                            }
                        }));

    }


    public void fetchFromDatabse() {

        loading.setValue(true);
        retriveTask = new RetriveDogsTask();
        retriveTask.execute();


    }


    private void dogRetrived(List<DogBreed> dogList) {
        dogs.setValue(dogList);
        dogLoadError.setValue(false);
        loading.setValue(false);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();

        if (insertTask != null) {
            insertTask.cancel(true);
            insertTask = null;
        }

        if (retriveTask != null) {
            retriveTask.cancel(true);
            retriveTask = null;
        }


    }


    private class InsertDogsTask extends AsyncTask<List<DogBreed>, Void, List<DogBreed>> {
        @Override
        protected List<DogBreed> doInBackground(List<DogBreed>... lists) {

            List<DogBreed> list = lists[0];
            DogDao dao = DogDatabase.getInstance(getApplication()).dogDao();
            dao.deleteAllDogs();

            ArrayList<DogBreed> newList = new ArrayList<>(list);
            List<Long> result = dao.insertAll(newList.toArray(new DogBreed[0]));


            int i = 0;

            while (i < list.size()) {
                list.get(i).uuid = result.get(i).intValue();
                ++i;
            }

            return list;
        }


        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {

            dogRetrived(dogBreeds);
            prefrencesHelper.saveUpdateTime(System.nanoTime());

        }
    }


    private class RetriveDogsTask extends AsyncTask<Void, Void, List<DogBreed>> {
        @Override
        protected List<DogBreed> doInBackground(Void... voids) {
            return DogDatabase.getInstance(getApplication()).dogDao().getAllDogs();
        }


        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {

            dogRetrived(dogBreeds);


        //    Toast.makeText(getApplication(), "Dogs retrived From Database ", Toast.LENGTH_SHORT).show();
        }
    }


}
