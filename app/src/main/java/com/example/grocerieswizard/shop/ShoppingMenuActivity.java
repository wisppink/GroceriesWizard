package com.example.grocerieswizard.shop;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.grocerieswizard.RecipeDatabaseHelper;
import com.example.grocerieswizard.shop.ShopAdapter;
import com.example.grocerieswizard.databinding.ActivityShoppingMenuBinding;
import com.example.grocerieswizard.shop.ShopHelperImpl;
import com.example.grocerieswizard.models.RecipeModel;

import java.util.ArrayList;

public class ShoppingMenuActivity extends AppCompatActivity {

    private static final String TAG = "ShoppingMenuActivity";
    ActivityShoppingMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShoppingMenuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ArrayList<RecipeModel> recipes;
        ShopAdapter adapter;
        try (RecipeDatabaseHelper dbHelper = new RecipeDatabaseHelper(this)) {

            adapter = new ShopAdapter(this, new ShopHelperImpl());
            binding.shoppingCart.setAdapter(adapter);
            binding.shoppingCart.setLayoutManager(new LinearLayoutManager(this));

            recipes = dbHelper.getSelectedRecipes();
        }
        adapter.setSelectedRecipeList(recipes);
    }
}