package com.example.grocerieswizard.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.RecipeDatabaseHelper;
import com.example.grocerieswizard.models.RecipeModel;
import com.example.grocerieswizard.RecyclerViewInterface;
import com.example.grocerieswizard.adapters.ShoppingRecyclerViewAdapter;

import java.util.ArrayList;

public class ShoppingMenu extends AppCompatActivity implements RecyclerViewInterface {

    private ShoppingRecyclerViewAdapter adapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_menu);
        RecipeDatabaseHelper dbHelper = new RecipeDatabaseHelper(this); // Initialize the helper with the appropriate context

        RecyclerView shoppingRecyclerView = findViewById(R.id.shopping_cart);
        adapter = new ShoppingRecyclerViewAdapter(this);
        shoppingRecyclerView.setAdapter(adapter);
        adapter.setRecyclerViewInterface(this);
        shoppingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<RecipeModel> recipes = dbHelper.getSelectedRecipes();
        adapter.setSelectedRecipeList(recipes);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemDelete(int position) {

    }

    @Override
    public void onItemEdit(int position) {

    }


}