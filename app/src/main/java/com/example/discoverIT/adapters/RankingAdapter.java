package com.example.discoverIT.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discoverIT.databinding.ItemContainerBinding;
import com.example.discoverIT.databinding.ItemRankContainerBinding;
import com.example.discoverIT.listeners.RankingListener;
import com.example.discoverIT.listeners.UserListener;
import com.example.discoverIT.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Base64;
import java.util.List;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingViewHolder> {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://discoverit-b459f-default-rtdb.europe-west1.firebasedatabase.app/");
    private DatabaseReference myRef;
    private Bitmap bitmap;

    private final List<User> users;
    private final RankingListener rankingListener;

    public RankingAdapter(List<User> users, RankingListener rankingListener) {
        this.users = users;
        this.rankingListener = rankingListener;
    }

    @NonNull
    @Override
    public RankingAdapter.RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRankContainerBinding itemRankContainerBinding = ItemRankContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new RankingAdapter.RankingViewHolder(itemRankContainerBinding);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RankingAdapter.RankingViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class RankingViewHolder extends RecyclerView.ViewHolder {

        ItemRankContainerBinding binding;

        RankingViewHolder(ItemRankContainerBinding itemRankContainerBinding) {
            super(itemRankContainerBinding.getRoot());
            binding = itemRankContainerBinding;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void setUserData(User user) {
            binding.textName.setText(user.name);
            binding.textEmail.setText(user.email);
            binding.imageProfile.setImageBitmap(getUserImage(user.image));
            binding.scoreFinal.setText(String.valueOf(user.score));
            // PASSA ALLA CHAT CON L'UTENTE SELEZIONATO
            //binding.getRoot().setOnClickListener(v -> rankingListener.onRankingClicked(user));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.getMimeDecoder().decode(encodedImage);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


}
