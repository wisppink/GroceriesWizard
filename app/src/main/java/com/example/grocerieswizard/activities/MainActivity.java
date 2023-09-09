package com.example.grocerieswizard.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.RecipeDatabaseHelper;
import com.example.grocerieswizard.RecyclerViewInterface;
import com.example.grocerieswizard.adapters.RecipeRecyclerViewAdapter;
import com.example.grocerieswizard.models.RecipeModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {

    private RecipeRecyclerViewAdapter adapter;
    Context context;
    RecipeDatabaseHelper recipeDatabaseHelper;
    private static final int FAV_ACTIVITY_REQUEST_CODE = 1;

    // List to hold recipes for shopping cart
    private ArrayList<RecipeModel> shopList = new ArrayList<>();

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
                adapter.notifyDataSetChanged();
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
                adapter.notifyDataSetChanged();
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recipeDatabaseHelper = new RecipeDatabaseHelper(this);

        RecyclerView recyclerView = findViewById(R.id.mRecyclerView);
        adapter = new RecipeRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setRecyclerViewInterface(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<RecipeModel> recipes = recipeDatabaseHelper.getAllRecipesFromDB();
        adapter.setRecipeList(recipes);
        adapter.notifyDataSetChanged();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView open_fav = findViewById(R.id.fav_icon);
        open_fav.setOnClickListener(v ->
        {
            Toast.makeText(MainActivity.this, "open_fav icon clicked!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, FavActivity.class);

            // Start FavActivity with the specified request code
            startActivityForResult(intent, FAV_ACTIVITY_REQUEST_CODE);
        });

        ImageView open_menu = findViewById(R.id.open_menu);
        open_menu.setOnClickListener(v -> Toast.makeText(MainActivity.this, "open_menu icon clicked!", Toast.LENGTH_SHORT).show());

        context = this;


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            // Launch the AddRecipe activity to add a new recipe
            Intent intent = new Intent(MainActivity.this, AddRecipeActivity.class);
            launcher.launch(intent);

        });

        // Set up ItemTouchHelper for swipe gestures
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT
        ) {
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
                    adapter.notifyItemChanged(position);
                } else if (direction == ItemTouchHelper.LEFT && myModel.isSwiped()) {
                    //swiped, get back to default
                    myModel.setSwiped(false);
                    adapter.notifyItemChanged(position);
                }

            }
        };
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        // Set up shopping cart button
        Button shoppingCart = findViewById(R.id.shopping_cart);
        shoppingCart.setOnClickListener(v -> {
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
                adapter.notifyDataSetChanged();
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

    // Handle item edit and start AddRecipe activity for editing
    @Override
    public void onItemEdit(int position) {
        RecipeModel recipeModel = adapter.getItemAtPosition(position);
        if (recipeModel != null) {
            recipeModel.setSwiped(false);
            adapter.notifyItemChanged(position);
            Intent editIntent = new Intent(this, AddRecipeActivity.class);
            editIntent.putExtra("editRecipe", true);
            editIntent.putExtra("recipeModel", recipeModel);
            editIntent.putExtra("position", position);
            launcher.launch(editIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FAV_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                adapter.notifyDataSetChanged();
            }
        }
    }


}