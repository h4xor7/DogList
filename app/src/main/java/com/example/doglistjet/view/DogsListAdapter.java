package com.example.doglistjet.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.doglistjet.R;
import com.example.doglistjet.databinding.ItemDogGridBinding;
import com.example.doglistjet.model.DogBreed;
import com.example.doglistjet.model.DogPalette;

import java.util.ArrayList;
import java.util.List;

public class DogsListAdapter extends RecyclerView.Adapter<DogsListAdapter.DogViewHolder> implements DogClickListner {

    private ArrayList<DogBreed> dogList;
    private  Context context ;

    public DogsListAdapter(ArrayList<DogBreed> DogList, Context context) {

        this.dogList = DogList;
        this.context = context;
    }

    public void updateDogsList(List<DogBreed> newDogList) {
        dogList.clear();
        dogList.addAll(newDogList);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public DogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemDogGridBinding view = DataBindingUtil.inflate(inflater, R.layout.item_dog_grid, parent, false);

/*
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dog, parent, false);
*/
        return new DogViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {

        holder.itemView.setDog(dogList.get(position));
        holder.itemView.setListner(this);







        /*       ImageView imageView = holder.itemView.findViewById(R.id.imageView);
        TextView name = holder.itemView.findViewById(R.id.name);
        TextView lifeSpan = holder.itemView.findViewById(R.id.lifespan);
        LinearLayout layout = holder.itemView.findViewById(R.id.dog_layout);

        name.setText(dogList.get(position).dogBreed);
        lifeSpan.setText(dogList.get(position).lifeSpan);
        Util.loadImage(imageView, dogList.get(position).imageUrl,Util.getProgressDrawable(imageView.getContext()) );

        layout.setOnClickListener(v -> {
            ListFragmentDirections.ActionDetail action = ListFragmentDirections.actionDetail();
            action.setDogUuid(dogList.get(position).uuid);

            Navigation.findNavController(layout).navigate(action);
        });*/

    }
    @Override
    public void onDogClicked(View v) {
    String uuidString =((TextView) v.findViewById(R.id.dogGridId)).getText().toString();
    int uuid = Integer.valueOf(uuidString);

        ListFragmentDirections.ActionDetail action = ListFragmentDirections.actionDetail();
        action.setDogUuid(uuid);

        Navigation.findNavController(v).navigate(action);


    }
    @Override
    public int getItemCount() {
        return dogList.size();
    }



    class DogViewHolder extends RecyclerView.ViewHolder {
        public ItemDogGridBinding itemView;

        public DogViewHolder(@NonNull ItemDogGridBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }
    }

}
