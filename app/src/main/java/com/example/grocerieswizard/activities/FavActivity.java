package com.example.grocerieswizard.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.RecipeDatabaseHelper;
import com.example.grocerieswizard.RecyclerViewInterface;
import com.example.grocerieswizard.adapters.FavRecyclerViewAdapter;
import com.example.grocerieswizard.models.RecipeModel;

import java.util.ArrayList;

public class FavActivity extends AppCompatActivity implements RecyclerViewInterface {

    RecipeDatabaseHelper dbHelper;
    private FavRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        dbHelper = new RecipeDatabaseHelper(this);
        adapter = new FavRecyclerViewAdapter(this);
        RecyclerView favRecyclerView = findViewById(R.id.FavRecyclerView);
        favRecyclerView.setAdapter(adapter);
        adapter.setRecyclerViewInterface(this);
        favRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<RecipeModel> recipes = dbHelper.getRecipesFav();
        adapter.setFavList(recipes);
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

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        super.onBackPressed();
    }

}