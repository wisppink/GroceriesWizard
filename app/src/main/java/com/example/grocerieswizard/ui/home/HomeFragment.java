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
    HomeContract.Presenter presenter;
    private RecipeRecyclerViewAdapter adapter;
    private static final String TAG = "HomeFragment";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new RecipeRecyclerViewAdapter();
        adapter.setRecyclerViewInterface(this);
        GroceriesWizardInjector injector = GroceriesWizardInjector.getInstance();
        UiMapper uiMapper = injector.getUiMapper();
        presenter = new HomePresenter(injector.getRecipeRepository(), uiMapper);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.bindView(this);
    }

    @Override
    public void onStop() {
        presenter.unbindView();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadRecipes();
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


    @Override
    public void onItemClick(RecipeUi recipe) {
        presenter.showDetails(recipe);
    }

    @Override
    public void onItemDelete(RecipeUi recipe) {
        presenter.deleteRecipe(recipe);
    }

    @Override
    public void onItemEdit(RecipeUi recipe) {
        showEditRecipe(recipe);
    }

    @Override
    public int updateRecipe(RecipeUi oldRecipeUi) {
        return presenter.updateRecipe(oldRecipeUi);
    }

    @Override
    public void onItemShare(RecipeUi recipe) {
        showRecipeShare(recipe);
    }

    @Override
    public void toggleFavoriteRecipe(RecipeUi recipeUi) {
        presenter.onToggleFavoriteRecipeClick(recipeUi);
    }

    @Override
    public void toggleCartRecipe(RecipeUi recipeUi) {
        presenter.onToggleCartRecipeClick(recipeUi);
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

    @Override
    public void showRecipeDetails(RecipeUi recipe) {
        adapter.itemChanged(adapter.getPositionForRecipe(recipe));
        DetailFragment detailFragment = DetailFragment.newInstance(recipe);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void recipeAddedToFavorites(RecipeUi recipeUi) {
        adapter.itemChanged(adapter.getPositionForRecipe(recipeUi));
        Log.d(TAG, "recipeAddedToFavorites: added to fav: " + recipeUi.isFav() + " id: " + recipeUi.getId());
        Toast.makeText(requireContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void recipeRemovedFromFavorites(RecipeUi recipeUi) {
        adapter.itemChanged(adapter.getPositionForRecipe(recipeUi));
        Log.d(TAG, "recipeAddedToFavorites: removed to fav: " + recipeUi.isFav() + " id: " + recipeUi.getId());
        Toast.makeText(requireContext(), "Removed from Favorites", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecipeDeleted(RecipeUi recipe) {
        adapter.removeRecipe(recipe);
        Toast.makeText(requireContext(), "Recipe deleted: " + recipe.getRecipeName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void recipeAddedToCart(RecipeUi recipeUi) {
        adapter.itemChanged(adapter.getPositionForRecipe(recipeUi));
        Log.d(TAG, "recipeAddedToFavorites: added to CART: " + recipeUi.isFav() + " id: " + recipeUi.getId());
        Toast.makeText(requireContext(), R.string.added_to_cart, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void recipeRemovedFromCart(RecipeUi recipeUi) {
        adapter.itemChanged(adapter.getPositionForRecipe(recipeUi));
        Log.d(TAG, "recipeAddedToFavorites: deleted to CART: " + recipeUi.isFav() + " id: " + recipeUi.getId());
        Toast.makeText(requireContext(), R.string.removed_from_cart, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDeleteConfirmation(RecipeUi recipe) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete " + recipe.getRecipeName() + " recipe?");
        builder.setPositiveButton("Delete", (dialog, which) -> presenter.deleteFromDB(recipe));
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            recipe.setSwiped(false);
            adapter.itemChanged(adapter.getPositionForRecipe(recipe));
            builder.setCancelable(true);
        });
        // Create and show the dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void showEditRecipe(RecipeUi recipe) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        AddRecipeFragment fragment = AddRecipeFragment.newInstance(recipe, adapter.getPositionForRecipe(recipe));
        transaction.replace(R.id.frameLayout, fragment);
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