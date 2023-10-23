package com.example.grocerieswizard;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.grocerieswizard.ui.fav.FavFragment;
import com.example.grocerieswizard.databinding.ActivityMainBinding;
import com.example.grocerieswizard.ui.home.HomeFragment;
import com.example.grocerieswizard.ui.shop.ShopFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        replaceFragment(new HomeFragment());

        binding.openMenu.setOnClickListener(v -> Toast.makeText(this, "clicked open menu!", Toast.LENGTH_SHORT).show());

        binding.getHome.setOnClickListener(v -> replaceFragment(new HomeFragment()));
        binding.getFav.setOnClickListener(v -> replaceFragment(new FavFragment()));
        binding.getShop.setOnClickListener(v -> replaceFragment(new ShopFragment()));

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}