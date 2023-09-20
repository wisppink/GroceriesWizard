package com.example.grocerieswizard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.grocerieswizard.RecipeDatabaseHelper;
import com.example.grocerieswizard.adapters.FavRecyclerViewAdapter;
import com.example.grocerieswizard.databinding.ActivityFavBinding;
import com.example.grocerieswizard.interfaces.FavInterface;
import com.example.grocerieswizard.models.RecipeModel;

import java.util.ArrayList;

public class FavActivity extends AppCompatActivity implements FavInterface {

    RecipeDatabaseHelper dbHelper;
    private FavRecyclerViewAdapter adapter;
    ActivityFavBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFavBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        dbHelper = new RecipeDatabaseHelper(this);
        adapter = new FavRecyclerViewAdapter();
        adapter.setFavInterface(this);

        binding.FavRecyclerView.setAdapter(adapter);
        binding.FavRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<RecipeModel> recipes = dbHelper.getRecipesFav();
        adapter.setFavList(recipes);
    }


    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        super.onBackPressed();
    }

    @Override
    public void onRemoveFromFavorites(RecipeModel recipeModel) {
        dbHelper.deleteRecipeFav(recipeModel.getId());
        ArrayList<RecipeModel> tempList = adapter.getFavList();
        for (int i = 0; i < tempList.size(); i++) {
            RecipeModel model = tempList.get(i);
            if (model.getId() == recipeModel.getId()) {
                adapter.removeItem(i);
            }
        }

    }

    @Override
    public boolean isRecipeSelected(int id) {
        return dbHelper.isRecipeSelected(id);
    }

    @Override
    public void insertSelectedRecipe(int id) {
        dbHelper.insertSelectedRecipe(id);
    }

    @Override
    public void removeSelectedRecipe(int id) {
        dbHelper.deleteSelectedRecipe(id);
    }
}