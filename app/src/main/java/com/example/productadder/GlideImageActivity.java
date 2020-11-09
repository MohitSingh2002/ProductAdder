package com.example.productadder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.productadder.databinding.ActivityGlideImageBinding;

public class GlideImageActivity extends AppCompatActivity {

    ActivityGlideImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGlideImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Glide.with(GlideImageActivity.this)
                .load("https://www.thurrott.com/wp-content/uploads/sites/2/2020/11/pubg.jpg")
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        binding.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(binding.imageView);

    }

}
