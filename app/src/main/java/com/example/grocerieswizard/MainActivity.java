package com.example.grocerieswizard;

import android.content.Intent;
import android.os.Bundle;
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

    private RecipeRecyclerViewAdapter adapter;


    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        Intent data = result.getData();
        if (data != null && data.hasExtra("recipe")) {
            RecipeModel recipe = data.getParcelableExtra("recipe");

            if (recipe != null) {
                adapter.addRecipe(recipe);
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.mRecyclerView);

        adapter = new RecipeRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setRecyclerViewInterface(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView starIcon = findViewById(R.id.star_icon);
        starIcon.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Star icon clicked!", Toast.LENGTH_SHORT).show());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "fab clicked!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, AddRecipe.class);
            launcher.launch(intent);

        });
    }

    @Override
    public void onItemClick(int position) {
        RecipeModel recipeModel = adapter.getItemAtPosition(position);
        if (recipeModel != null) {
            Toast.makeText(this, "Clicked on recipe: " + recipeModel.getRecipeName(), Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("recipe", recipeModel);
        startActivity(intent);
    }

}