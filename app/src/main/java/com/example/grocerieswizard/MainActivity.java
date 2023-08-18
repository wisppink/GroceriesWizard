package com.example.grocerieswizard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {

    private static final int VIEW_TYPE_SWIPED = 1;
    private static final int VIEW_TYPE_NORMAL = 0;

    private static final int ADD_RECIPE_REQUEST_CODE = 1;

    //TODO:ekranı döndürünce recipeler gidiyor lol
    private RecipeRecyclerViewAdapter adapter;
    Context context;

    // Launcher for starting AddRecipe activity and receiving results
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        Intent data = result.getData();
        if (data != null && data.hasExtra("recipe")) {
            RecipeModel recipe = data.getParcelableExtra("recipe");
            // Add the new recipe to the RecyclerView
            if (recipe != null) {
                recipe.setSwiped(false);
                adapter.addRecipe(recipe);
                adapter.notifyDataSetChanged();
            }
        }
        if (data != null && data.hasExtra("new_recipe")) {
            RecipeModel recipe = data.getParcelableExtra("new_recipe");
            int position = data.getIntExtra("position", -1);
            Log.d("main recive: ", String.valueOf(recipe.isSwiped()));
            Log.d("main recive position: ", String.valueOf(position));
            // Add the new recipe to the RecyclerView
            if (recipe != null) {
                recipe.setSwiped(false);
                adapter.addRecipe(recipe);
                adapter.removeRecipeAtPosition(position);
                adapter.notifyDataSetChanged();
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

        /*
         *
         * default recipe
         *
         * */

        List<IngredientModel> mlist = new ArrayList<>();
        IngredientModel ingredientModel = new IngredientModel("malzeme", 1.0, "kg");
        IngredientModel ingredient2Model = new IngredientModel("malzeme", 1.0, "kg");
        mlist.add(ingredientModel);
        mlist.add(ingredient2Model);
        RecipeModel recipeModel = new RecipeModel("isim", mlist, "lalala", null);
        RecipeModel recipe2Model = new RecipeModel("isim2", mlist, "lalala", null);
        adapter.addRecipe(recipeModel);
        adapter.addRecipe(recipe2Model);

        /*
         *
         * default recipe
         *
         * */

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            // Launch the AddRecipe activity to add a new recipe
            Intent intent = new Intent(MainActivity.this, AddRecipe.class);
            launcher.launch(intent);

        });
        context = this;

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT
        ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false; // We're not interested in moving items in this case
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                RecipeModel myModel = adapter.getItemAtPosition(position);

                if (direction == ItemTouchHelper.LEFT && !myModel.isSwiped()) {
                    //show menu
                    myModel.setSwiped(true);
                    adapter.notifyItemChanged(position);
                } else if (direction == ItemTouchHelper.LEFT && myModel.isSwiped()) {
                    //swiped, get back to default
                    myModel.setSwiped(false);
                    adapter.notifyItemChanged(position);
                }

            }
        };

        // Attach the ItemTouchHelper to the RecyclerView
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

    }

    @Override
    public void onItemClick(int position) {
        // Handle item click to show details of a recipe
        RecipeModel recipeModel = adapter.getItemAtPosition(position);
        if (!recipeModel.isSwiped()) {
            if (recipeModel != null) {
                recipeModel.setSwiped(false);
                adapter.notifyDataSetChanged();
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra("MyRecipe", recipeModel);
                startActivity(intent);
            } else {
                Log.d("MainActivity", "recipe model null");
            }
        }

    }

    @Override
    public void onItemDelete(int position) {
        // Handle delete action for a recipe with an alert
        RecipeModel recipeModel = adapter.getItemAtPosition(position);
        if (recipeModel != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Confirm Deletion");
            builder.setMessage("Are you sure you want to delete " + recipeModel.getRecipeName() + " recipe?");
            builder.setPositiveButton("Delete", (dialog, which) -> {
                // User confirmed deletion, remove the recipe and update the RecyclerView
                adapter.removeRecipe(recipeModel);
                recipeModel.setSwiped(false);
                adapter.notifyItemChanged(position);
                Toast.makeText(this, "Recipe deleted: " + recipeModel.getRecipeName(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                recipeModel.setSwiped(false);
                adapter.notifyItemChanged(position);
                dialog.dismiss();
            });

            // Create and show the dialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onItemEdit(int position) {
        RecipeModel recipeModel = adapter.getItemAtPosition(position);
        if (recipeModel != null) {
            recipeModel.setSwiped(false);
            adapter.notifyItemChanged(position);
            Intent editIntent = new Intent(this, AddRecipe.class);
            editIntent.putExtra("editRecipe", true);
            editIntent.putExtra("recipeModel", recipeModel);
            editIntent.putExtra("position", position);
            Log.d("mainden edite position: ", String.valueOf(position));
            Log.d("mainden edite ", String.valueOf(recipeModel.isSwiped()));
            launcher.launch(editIntent);
        }
    }

}