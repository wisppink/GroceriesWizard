package com.example.grocerieswizard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {

    //TODO:ekranı döndürünce recipeler gidiyor lol
    private RecipeRecyclerViewAdapter adapter;

    // Launcher for starting AddRecipe activity and receiving results
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        Intent data = result.getData();
        if (data != null && data.hasExtra("recipe")) {
            RecipeModel recipe = data.getParcelableExtra("recipe");
            // Add the new recipe to the RecyclerView
            if (recipe != null) {
                adapter.addRecipe(recipe);
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.mRecyclerView);
        adapter = new RecipeRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setRecyclerViewInterface(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView starIcon = findViewById(R.id.star_icon);
        starIcon.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Star icon clicked!", Toast.LENGTH_SHORT).show());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            // Launch the AddRecipe activity to add a new recipe
            Intent intent = new Intent(MainActivity.this, AddRecipe.class);
            launcher.launch(intent);

        });
    }

    @Override
    public void onItemClick(int position) {
        // Handle item click to show details of a recipe
        RecipeModel recipeModel = adapter.getItemAtPosition(position);
        if (recipeModel != null) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("recipe", recipeModel);
            startActivity(intent);
        }
        else{
            Log.d("MainActivity","recipe model null");
        }

    }

    @Override
    public void onDeleteRecipe(int position) {
        // Handle delete action for a recipe
        RecipeModel recipeModel = adapter.getItemAtPosition(position);
        if (recipeModel != null) {
            // Remove the recipe from the RecyclerView
            adapter.removeRecipe(recipeModel);
            Toast.makeText(this, "Recipe deleted: " + recipeModel.getRecipeName(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEditRecipe(int position) {
        // TODO: Handle edit action for a recipe
        RecipeModel recipeModel = adapter.getItemAtPosition(position);
        // TODO: Implement editing functionality
    }

}