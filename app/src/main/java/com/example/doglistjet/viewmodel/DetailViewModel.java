package com.example.doglistjet.viewmodel;


import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.doglistjet.model.DogBreed;
import com.example.doglistjet.model.DogDatabase;

public class DetailViewModel extends AndroidViewModel {

    public MutableLiveData<DogBreed> dogLiveData = new MutableLiveData<DogBreed>();
    private RetriveDogTask task;

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }


    public void fetch(int uuid) {

        task = new RetriveDogTask();
        task.execute(uuid);

    }


    private class RetriveDogTask extends AsyncTask<Integer, Void, DogBreed> {
        @Override
        protected DogBreed doInBackground(Integer... integers) {
            int uuid = integers[0];
            return DogDatabase.getInstance(getApplication()).dogDao().getDog(uuid);
        }


        @Override
        protected void onPostExecute(DogBreed dogBreed) {

            dogLiveData.setValue(dogBreed);
        }
    }


    @Override
    protected void onCleared() {
        super.onCleared();

        if (task != null) {

            task.cancel(true);
            task = null;
        }
    }
}
