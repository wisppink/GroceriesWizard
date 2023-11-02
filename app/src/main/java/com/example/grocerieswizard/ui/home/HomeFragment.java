package com.example.grocerieswizard.ui.home;

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
import com.example.grocerieswizard.databinding.FragmentHomeBinding;
import com.example.grocerieswizard.di.GroceriesWizardInjector;
import com.example.grocerieswizard.ui.UiMapper;
import com.example.grocerieswizard.ui.addrecipe.AddRecipeFragment;
import com.example.grocerieswizard.ui.detail.DetailFragment;
import com.example.grocerieswizard.ui.model.IngredientUi;
import com.example.grocerieswizard.ui.model.RecipeUi;

import java.util.List;

public class HomeFragment extends Fragment implements RecipeInterface, HomeContract.View {
    HomePresenter presenter;
    private RecipeRecyclerViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new RecipeRecyclerViewAdapter();
        adapter.setRecyclerViewInterface(this);
        GroceriesWizardInjector injector = GroceriesWizardInjector.getInstance();
        UiMapper uiMapper = injector.getUiMapper();
        presenter = new HomePresenter(injector.getRecipeRepository(), uiMapper);
        presenter.bindView(this);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.recipeRecyclerView.setAdapter(adapter);
        binding.recipeRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
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
        presenter.loadRecipes();
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
                RecipeUi myModel = adapter.getItemAtPosition(position);

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
    public void onItemClick(RecipeUi recipe) {
        showRecipeDetails(recipe);
    }

    // Handle item delete with confirmation dialog
    @Override
    public void onItemDelete(RecipeUi recipe) {
        deleteRecipe(recipe);
    }

    @Override
    public void onItemEdit(RecipeUi recipe) {
        showEditRecipe(recipe);
    }

    @Override
    public Boolean isRecipeSelected(int id) {
        return presenter.isRecipeSelected(id);
    }

    @Override
    public int updateRecipe(RecipeUi oldRecipeUi) {
        return presenter.updateRecipe(oldRecipeUi);
    }

    @Override
    public void insertRecipe(RecipeUi recipeUi) {
        presenter.insertRecipe(recipeUi);
    }

    @Override
    public void deleteRecipe(RecipeUi recipe) {
        presenter.deleteRecipe(recipe);
    }

    @Override
    public void deleteSelectedRecipe(int recipeId) {
        presenter.deleteSelectedRecipe(recipeId);
    }

    @Override
    public void insertSelectedRecipe(int recipeId) {
        presenter.insertSelectedRecipe(recipeId);
    }

    @Override
    public boolean isRecipeFavorite(int id) {
        return presenter.isRecipeFavorite(id);
    }

    @Override
    public void insertRecipeFav(int recipeId) {
        presenter.insertRecipeFav(recipeId);
    }

    @Override
    public void deleteRecipeFav(int recipeId) {
        presenter.deleteRecipeFav(recipeId);
    }

    @Override
    public void onItemShare(RecipeUi recipe) {
        showRecipeShare(recipe);
    }

    private String getStringIngredients(List<IngredientUi> ingredients) {
        StringBuilder stringBuilder = new StringBuilder();

        for (IngredientUi ingredient : ingredients) {
            stringBuilder.append(ingredient.getName())
                    .append(" ")
                    .append(ingredient.getQuantity())
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append("\n");
        }

        return stringBuilder.toString();
    }

    @Override
    public void showRecipes(List<RecipeUi> recipes) {
        adapter.setRecipeList(recipes);
    }

    private void showRecipeDetails(RecipeUi recipe) {
        if (recipe != null) {
            if (!recipe.isSwiped()) {
                recipe.setSwiped(false);
                adapter.itemChanged(adapter.getPositionForRecipe(recipe));
                DetailFragment detailFragment = DetailFragment.newInstance(recipe);
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

    @Override
    public void showDeleteConfirmation(RecipeUi recipe) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirm Deletion");
            builder.setMessage("Are you sure you want to delete " + recipe.getRecipeName() + " recipe?");
            builder.setPositiveButton("Delete", (dialog, which) -> {
                // User confirmed deletion, remove the recipe and update the RecyclerView
                adapter.removeRecipe(recipe);
                Toast.makeText(requireContext(), "Recipe deleted: " + recipe.getRecipeName(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                recipe.setSwiped(false);
                adapter.itemChanged(adapter.getPositionForRecipe(recipe));
                dialog.dismiss();
            });
            // Create and show the dialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
    }

    @Override
    public void showEditRecipe(RecipeUi recipe) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        AddRecipeFragment addRecipeFragment = new AddRecipeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("recipeModel", recipe);
        bundle.putInt("position", adapter.getPositionForRecipe(recipe));
        addRecipeFragment.setArguments(bundle);
        transaction.replace(R.id.frameLayout, addRecipeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void showRecipeShare(RecipeUi recipe) {
        String instructions = recipe.getInstructions();
        String ingredients = getStringIngredients(recipe.getIngredients());
        String shareString = recipe.getRecipeName() + "\n\n\n" + instructions + "\n\n\n" + ingredients;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        shareIntent.putExtra(Intent.EXTRA_TEXT, shareString);

        // Start an activity to choose the sharing method
        startActivity(Intent.createChooser(shareIntent, "Share Recipe"));
    }
}