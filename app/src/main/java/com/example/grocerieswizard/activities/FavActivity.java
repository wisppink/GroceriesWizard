package com.example.grocerieswizard.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.RecipeDatabaseHelper;
import com.example.grocerieswizard.adapters.FavRecyclerViewAdapter;
import com.example.grocerieswizard.interfaces.FavInterface;
import com.example.grocerieswizard.models.RecipeModel;

import java.util.ArrayList;

public class FavActivity extends AppCompatActivity implements FavInterface {

    RecipeDatabaseHelper dbHelper;
    private FavRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        dbHelper = new RecipeDatabaseHelper(this);
        adapter = new FavRecyclerViewAdapter();
        adapter.setFavInterface(this);
        RecyclerView favRecyclerView = findViewById(R.id.FavRecyclerView);
        favRecyclerView.setAdapter(adapter);
        favRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<RecipeModel> recipes = dbHelper.getRecipesFav();
        adapter.setFavList(recipes);
        adapter.notifyDataSetChanged();
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
        adapter.favList.remove(recipeModel);
        adapter.notifyDataSetChanged();
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