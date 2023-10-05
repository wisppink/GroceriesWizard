package com.example.grocerieswizard.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.RecipeDatabaseHelper;
import com.example.grocerieswizard.activities.AddRecipeActivity;
import com.example.grocerieswizard.activities.DetailActivity;
import com.example.grocerieswizard.databinding.FragmentHomeBinding;
import com.example.grocerieswizard.models.RecipeModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements RecipeInterface {

    private FragmentHomeBinding binding;
    private RecipeRecyclerViewAdapter adapter;
    RecipeDatabaseHelper recipeDatabaseHelper;
    Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeDatabaseHelper = new RecipeDatabaseHelper(context);
        adapter = new RecipeRecyclerViewAdapter(context);
    }

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


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        recipeDatabaseHelper = new RecipeDatabaseHelper(context);

        setupRecyclerView();
        setupSwipeGesture();

        // Launch the AddRecipe activity to add a new recipe
        binding.fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddRecipeActivity.class);
            launcher.launch(intent);
        });

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        binding.recipeRecyclerView.setAdapter(adapter);
        binding.recipeRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter.setRecyclerViewInterface(this);
        ArrayList<RecipeModel> recipes = recipeDatabaseHelper.getAllRecipesFromDB();
        adapter.setRecipeList(recipes);
    }

    private void setupSwipeGesture() {
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
                    // Show menu
                    myModel.setSwiped(true);
                    adapter.itemChanged(position);
                } else if (direction == ItemTouchHelper.LEFT && myModel.isSwiped()) {
                    // Swiped, get back to default
                    myModel.setSwiped(false);
                    adapter.itemChanged(position);
                }
            }
        };
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.recipeRecyclerView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Handle item click to show details of a recipe
    @Override
    public void onItemClick(int position) {

        RecipeModel recipeModel = adapter.getItemAtPosition(position);
        if (recipeModel != null) {
            if (!recipeModel.isSwiped()) {
                recipeModel.setSwiped(false);
                adapter.itemChanged(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
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
                Toast.makeText(context, "Recipe deleted: " + recipeModel.getRecipeName(), Toast.LENGTH_SHORT).show();
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
        Intent editIntent = new Intent(getActivity(), AddRecipeActivity.class);
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
