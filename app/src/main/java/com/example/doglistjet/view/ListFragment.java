package com.example.doglistjet.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.doglistjet.R;
import com.example.doglistjet.model.DogBreed;
import com.example.doglistjet.viewmodel.ListViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment {

    private ListViewModel listViewModel;
    private DogsListAdapter dogsListAdapter = new DogsListAdapter(new ArrayList<>(),getContext());

    @BindView(R.id.dog_list)
    RecyclerView doglist;
    @BindView(R.id.listError)
    TextView listError;
    @BindView(R.id.loadingView)
    ProgressBar loadingView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    public ListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);


        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

      /*  ListFragmentDirections.ActionDetail action = ListFragmentDirections.actionDetail();
        Navigation.findNavController(view).navigate(action);*/

        listViewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        listViewModel.refresh();

        doglist.setLayoutManager(new GridLayoutManager(getContext(),2));
        doglist.setAdapter(dogsListAdapter);

        refreshLayout.setOnRefreshListener(() -> {
            doglist.setVisibility(View.GONE);
            listError.setVisibility(View.GONE);
            loadingView.setVisibility(View.VISIBLE);
            listViewModel.refreshBypassCache();
            refreshLayout.setRefreshing(false);
        });

        observeViewModel();

    }

    private void observeViewModel() {

        listViewModel.dogs.observe(this, dogBreeds -> {

            if (dogBreeds != null && dogBreeds instanceof List) {


                doglist.setVisibility(View.VISIBLE);
                dogsListAdapter.updateDogsList(dogBreeds);
            }
        });








        listViewModel.dogLoadError.observe(this, isError -> {
            if (isError != null && isError instanceof Boolean) {
                listError.setVisibility(isError ? View.VISIBLE : View.GONE);
            }
        });

        listViewModel.loading.observe(this, isLoading -> {

            if (isLoading != null && isLoading instanceof Boolean) {

                loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isLoading) {
                    listError.setVisibility(View.GONE);
                    doglist.setVisibility(View.GONE);
                }
            }

        });

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
  switch (item.getItemId()){
      case R.id.actionSettings:{
          if (isAdded()){
              Navigation.findNavController(getView()).navigate(ListFragmentDirections.actionSettings());
          }
          break;
      }
  }
        return super.onOptionsItemSelected(item);
    }
}
