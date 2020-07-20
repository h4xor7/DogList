package com.example.doglistjet.view;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.doglistjet.R;
import com.example.doglistjet.databinding.FragmentDetailBinding;
import com.example.doglistjet.model.DogBreed;
import com.example.doglistjet.model.DogPalette;
import com.example.doglistjet.model.SmsInfo;
import com.example.doglistjet.viewmodel.DetailViewModel;


public class DetailFragment extends Fragment {


    int dogUid;
    private DetailViewModel detailViewModel;
    private FragmentDetailBinding binding;
    private boolean sendSmsStarted = false;

    public DetailFragment() {
    }

   /* @BindView(R.id.dog_img)
    ImageView Dog_Img;
    @BindView(R.id.Dog_name)
    TextView Dog_name;
    @BindView(R.id.dogpurpose)
    TextView Dog_pupose;
    @BindView(R.id.dogLifeSpan)
    TextView Dog_life_span;
    @BindView(R.id.dogtemprament)
    TextView dogTemperament;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        this.binding = binding;
        setHasOptionsMenu(true);
        // ButterKnife.bind(this, view);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {

            dogUid = DetailFragmentArgs.fromBundle(getArguments()).getDogUuid();

        }


        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        detailViewModel.fetch(dogUid);
        observerViewModel();

    }

    private void observerViewModel() {
        detailViewModel.dogLiveData.observe(this, dogBreed -> {


            if (dogBreed != null && dogBreed instanceof DogBreed && getContext() != null) {
                 /*

                 Dog_name.setText(dogBreed.dogBreed);
                 Dog_pupose.setText(dogBreed.bredFor);
                 Dog_life_span.setText(dogBreed.lifeSpan);
                 dogTemperament.setText(dogBreed.temperament);

                 if (dogBreed.imageUrl !=null){
                     Util.loadImage(Dog_Img,dogBreed.imageUrl,new CircularProgressDrawable(getContext()));
                 }
*/
                binding.setDog(dogBreed);
                if (dogBreed.imageUrl != null) {
                    setupBackgroundColor(dogBreed.imageUrl);
                }
            }

        });
    }

    private void setupBackgroundColor(String url) {

        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource)
                                .generate(palette -> {
                                    int intColor = palette.getLightMutedSwatch().getRgb();
                                    DogPalette myPalette = new DogPalette(intColor);
                                    //   binding.DogName.setTextColor(intColor);
                                    binding.setPalette(myPalette);
                                });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.detail_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_send_sms: {

                if (!sendSmsStarted){
                    sendSmsStarted =true;
                    ((MainActivity)getActivity()).checkSmsPermission();


                }


                Toast.makeText(getContext(), "Action Send", Toast.LENGTH_SHORT).show();

                break;
            }

            case R.id.action_share: {

                Toast.makeText(getContext(), "Action Share", Toast.LENGTH_SHORT).show();
                break;
            }

        }

        return super.onOptionsItemSelected(item);

    }

    public void onPermissionResult(Boolean permissionGranted) {

        if (isAdded() && sendSmsStarted && permissionGranted){

       //     SmsInfo smsInfo =  new SmsInfo("","",)
        }

    }
}
