package com.example.grocerieswizard.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.RecipeDatabaseHelper;
import com.example.grocerieswizard.adapters.RecipeRecyclerViewAdapter;
import com.example.grocerieswizard.databinding.ActivityMainBinding;
import com.example.grocerieswizard.interfaces.RecipeInterface;
import com.example.grocerieswizard.models.RecipeModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecipeInterface {

    private RecipeRecyclerViewAdapter adapter;
    Context context;
    RecipeDatabaseHelper recipeDatabaseHelper;
    ActivityMainBinding binding;

    // List to hold recipes for shopping cart
    private final ArrayList<RecipeModel> shopList = new ArrayList<>();

    // Launcher for starting AddRecipe activity and receiving results
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        // Check if a new recipe was added
        Intent data = result.getData();
        if (data != null && data.hasExtra("recipe")) {
            RecipeModel recipe = data.getParcelableExtra("recipe");
            // Add the new recipe to the RecyclerView
            if (recipe != null) {
                recipe.setSwiped(false);
                adapter.addRecipe(recipe);
            }
        }
        // Check if a new recipe was added
        if (data != null && data.hasExtra("new_recipe")) {
            RecipeModel recipe = data.getParcelableExtra("new_recipe");
            int position = data.getIntExtra("position", -1);
            // Add the new recipe to the RecyclerView
            if (recipe != null) {
                recipe.setSwiped(false);
                adapter.addRecipe(recipe);
                adapter.removeRecipeAtPosition(position);
            }
        }
    });

    ActivityResultLauncher<Intent> favLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> adapter.updateList());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        recipeDatabaseHelper = new RecipeDatabaseHelper(this);

        adapter = new RecipeRecyclerViewAdapter(this);
        binding.mRecyclerView.setAdapter(adapter);
        binding.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setRecyclerViewInterface(this);

        ArrayList<RecipeModel> recipes = recipeDatabaseHelper.getAllRecipesFromDB();
        adapter.setRecipeList(recipes);

        setSupportActionBar(binding.toolbar);


        binding.favIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavActivity.class);
            favLauncher.launch(intent);
        });

        binding.openMenu.setOnClickListener(v -> Toast.makeText(MainActivity.this, "open_menu icon clicked!", Toast.LENGTH_SHORT).show());

        context = this;

        binding.fab.setOnClickListener(v -> {
            // Launch the AddRecipe activity to add a new recipe
            Intent intent = new Intent(MainActivity.this, AddRecipeActivity.class);
            launcher.launch(intent);

        });

        // Set up ItemTouchHelper for swipe gestures
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // We're not interested in moving items in this case
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                RecipeModel myModel = adapter.getItemAtPosition(position);

                if (direction == ItemTouchHelper.LEFT && !myModel.isSwiped()) {
                    //show menu
                    myModel.setSwiped(true);
                    adapter.itemChanged(position);
                } else if (direction == ItemTouchHelper.LEFT && myModel.isSwiped()) {
                    //swiped, get back to default
                    myModel.setSwiped(false);
                    adapter.itemChanged(position);
                }

            }
        };
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.mRecyclerView);

        // Set up shopping cart button
        binding.shoppingCart.setOnClickListener(v -> {
            shopList.clear();
            shopList.addAll(recipeDatabaseHelper.getSelectedRecipes());
            Log.d("Main get selected list: ", shopList.toString());
            //TODO: ask these are your recipes, wanna cont?
            Intent shopping = new Intent(this, ShoppingMenuActivity.class);
            shopping.putParcelableArrayListExtra("list", shopList);
            startActivity(shopping);
        });

    }

    // Handle item click to show details of a recipe
    @Override
    public void onItemClick(int position) {

        RecipeModel recipeModel = adapter.getItemAtPosition(position);
        if (recipeModel != null) {
            if (!recipeModel.isSwiped()) {
                recipeModel.setSwiped(false);
                adapter.itemChanged(position);
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra("MyRecipe", recipeModel);
                startActivity(intent);
            } else {
                Log.d("MainActivity", "recipe model null");
            }
        }

    }

    // Handle item delete with confirmation dialog
    @Override
    public void onItemDelete(int position) {
        RecipeModel recipeModel = adapter.getItemAtPosition(position);
        if (recipeModel != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Confirm Deletion");
            builder.setMessage("Are you sure you want to delete " + recipeModel.getRecipeName() + " recipe?");
            builder.setPositiveButton("Delete", (dialog, which) -> {
                // User confirmed deletion, remove the recipe and update the RecyclerView
                adapter.removeRecipe(recipeModel);
                Toast.makeText(this, "Recipe deleted: " + recipeModel.getRecipeName(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                recipeModel.setSwiped(false);
                adapter.itemChanged(position);
                dialog.dismiss();
            });
            // Create and show the dialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    // Handle item edit and start AddRecipe activity for editing
    @Override
    public void onItemEdit(RecipeModel recipeModel, int position) {
        Intent editIntent = new Intent(this, AddRecipeActivity.class);
        editIntent.putExtra("editRecipe", true);
        editIntent.putExtra("recipeModel", recipeModel);
        editIntent.putExtra("position", position);
        launcher.launch(editIntent);

    }

    @Override
    public Boolean isRecipeSelected(int id) {
        return recipeDatabaseHelper.isRecipeSelected(id);
    }

    @Override
    public int updateRecipe(RecipeModel oldRecipe) {
        return recipeDatabaseHelper.updateRecipe(oldRecipe.getId(), oldRecipe);
    }

    @Override
    public void insertRecipe(RecipeModel recipe) {
        recipeDatabaseHelper.insertRecipe(recipe);
    }

    @Override
    public void deleteRecipe(int id) {
        recipeDatabaseHelper.deleteRecipe(id);
    }

    @Override
    public void deleteSelectedRecipe(int recipeId) {
        recipeDatabaseHelper.deleteSelectedRecipe(recipeId);
    }

    @Override
    public void insertSelectedRecipe(int recipeId) {
        recipeDatabaseHelper.insertSelectedRecipe(recipeId);
    }

    @Override
    public boolean isRecipeFavorite(int id) {
        return recipeDatabaseHelper.isRecipeFavorite(id);
    }

    @Override
    public void insertRecipeFav(int recipeId) {
        recipeDatabaseHelper.insertRecipeFav(recipeId);
    }

    @Override
    public void deleteRecipeFav(int recipeId) {
        recipeDatabaseHelper.deleteRecipeFav(recipeId);

    }
}