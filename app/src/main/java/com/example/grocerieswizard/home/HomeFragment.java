package com.example.grocerieswizard.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.RecipeDatabaseHelper;
import com.example.grocerieswizard.addRecipe.AddRecipeFragment;
import com.example.grocerieswizard.addRecipe.IngredientModel;
import com.example.grocerieswizard.databinding.FragmentHomeBinding;
import com.example.grocerieswizard.detail.DetailFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements RecipeInterface {

    private RecipeRecyclerViewAdapter adapter;
    RecipeDatabaseHelper recipeDatabaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeDatabaseHelper = new RecipeDatabaseHelper(getContext());
        adapter = new RecipeRecyclerViewAdapter();
        adapter.setRecyclerViewInterface(this);
        ArrayList<RecipeModel> recipes = recipeDatabaseHelper.getAllRecipesFromDB();
        adapter.setRecipeList(recipes);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.recipeRecyclerView.setAdapter(adapter);
        binding.recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Launch the AddRecipe activity to add a new recipe
        binding.fab.setOnClickListener(v -> {
            AddRecipeFragment addRecipeFragment = new AddRecipeFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, addRecipeFragment)
                    .addToBackStack(null)
                    .commit();
        });
        setupSwipeGesture(binding);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<RecipeModel> recipes = recipeDatabaseHelper.getAllRecipesFromDB();
        adapter.updateList();
        adapter.setRecipeList(recipes);
    }


    private void setupSwipeGesture(FragmentHomeBinding binding) {
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

    // Handle item click to show details of a recipe
    @Override
    public void onItemClick(int position) {

        RecipeModel recipeModel = adapter.getItemAtPosition(position);
        if (recipeModel != null) {
            if (!recipeModel.isSwiped()) {
                recipeModel.setSwiped(false);
                adapter.itemChanged(position);
                DetailFragment detailFragment = DetailFragment.newInstance(recipeModel);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, detailFragment)
                        .addToBackStack(null)
                        .commit();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirm Deletion");
            builder.setMessage("Are you sure you want to delete " + recipeModel.getRecipeName() + " recipe?");
            builder.setPositiveButton("Delete", (dialog, which) -> {
                // User confirmed deletion, remove the recipe and update the RecyclerView
                adapter.removeRecipe(recipeModel);
                Toast.makeText(getContext(), "Recipe deleted: " + recipeModel.getRecipeName(), Toast.LENGTH_SHORT).show();
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
    public void onItemEdit(int position) {
        RecipeModel recipeModel = adapter.getItemAtPosition(position);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        AddRecipeFragment addRecipeFragment = new AddRecipeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("recipeModel", recipeModel);
        bundle.putInt("position", position);
        addRecipeFragment.setArguments(bundle);
        transaction.replace(R.id.frameLayout, addRecipeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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

    @Override
    public void onItemShare(int adapterPosition) {
        RecipeModel recipeModel = adapter.getItemAtPosition(adapterPosition);
        String instructions = recipeModel.getInstructions();
        String ingredients = getStringIngredients(recipeModel.getIngredients());

        String shareString = recipeModel.getRecipeName() + "\n\n\n" + instructions + "\n\n\n" + ingredients;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        shareIntent.putExtra(Intent.EXTRA_TEXT, shareString);

        // Start an activity to choose the sharing method
        requireContext().startActivity(Intent.createChooser(shareIntent, "Share Recipe"));
    }

    private String getStringIngredients(List<IngredientModel> ingredients) {
        StringBuilder stringBuilder = new StringBuilder();

        for (IngredientModel ingredient : ingredients) {
            stringBuilder.append(ingredient.getName())
                    .append(" ")
                    .append(ingredient.getQuantity())
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append("\n");
        }

        return stringBuilder.toString();
    }

}
