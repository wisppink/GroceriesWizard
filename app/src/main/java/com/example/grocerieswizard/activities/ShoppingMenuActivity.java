package com.example.grocerieswizard.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.RecipeDatabaseHelper;
import com.example.grocerieswizard.adapters.ShopAdapter;
import com.example.grocerieswizard.models.RecipeModel;

import java.util.ArrayList;

public class ShoppingMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_menu);
        ArrayList<RecipeModel> recipes;
        ShopAdapter adapter;
        try (RecipeDatabaseHelper dbHelper = new RecipeDatabaseHelper(this)) {

            RecyclerView shoppingRecyclerView = findViewById(R.id.shopping_cart);
            adapter = new ShopAdapter(this);
            shoppingRecyclerView.setAdapter(adapter);
            shoppingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            recipes = dbHelper.getSelectedRecipes();
        }
        adapter.setSelectedRecipeList(recipes);
        adapter.notifyDataSetChanged();
    }

}