package com.example.grocerieswizard.ui.fav;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.databinding.FragmentFavBinding;
import com.example.grocerieswizard.di.GroceriesWizardInjector;
import com.example.grocerieswizard.ui.model.RecipeUi;

import java.util.List;

public class FavFragment extends Fragment implements FavInterface, FavContract.View {
    FavPresenter presenter;
    private FavRecyclerViewAdapter adapter;
    FragmentFavBinding binding;
    private static final String TAG = "FavFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GroceriesWizardInjector injector = GroceriesWizardInjector.getInstance();
        presenter = new FavPresenter(injector.getRecipeRepository(), injector.getUiMapper());
        adapter = new FavRecyclerViewAdapter(this);
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavBinding.inflate(inflater, container, false);
        adapter.setFavInterface(this);

        binding.FavRecyclerView.setAdapter(adapter);
        binding.FavRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        return binding.getRoot();
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
    public void onRecipeDeleted(RecipeUi recipeUi) {
        adapter.removeRecipe(recipeUi);
        Log.d(TAG, "recipeAddedToFavorites: removed to fav: " + recipeUi.isFav() + " id: " + recipeUi.getId());
        Toast.makeText(requireContext(), "Removed from Favorites", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRecipes(List<RecipeUi> recipeUiList) {
    adapter.setFavList(recipeUiList);
    }

    @Override
    public void toggleFavoriteRecipe(RecipeUi recipeUi) {
        presenter.onToggleFavoriteRecipeClick(recipeUi);
    }

    @Override
    public void toggleCartRecipe(RecipeUi recipeUi) {
        presenter.onToggleCartRecipeClick(recipeUi);
    }
    @Override
    public void recipeAddedToFavorites(RecipeUi recipeUi) {
        adapter.itemChanged(adapter.getPositionForRecipe(recipeUi));
        Log.d(TAG, "recipeAddedToFavorites: added to fav: " + recipeUi.isFav() + " id: " + recipeUi.getId());
        Toast.makeText(requireContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void recipeAddedToCart(RecipeUi recipeUi) {
        adapter.itemChanged(adapter.getPositionForRecipe(recipeUi));
        Log.d(TAG, "recipeAddedToFavorites: added to CART: " + recipeUi.isCart() + " id: " + recipeUi.getId());
        Toast.makeText(requireContext(), R.string.added_to_cart, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void recipeRemovedFromCart(RecipeUi recipeUi) {
        adapter.itemChanged(adapter.getPositionForRecipe(recipeUi));
        Log.d(TAG, "recipeAddedToFavorites: deleted to CART: " + recipeUi.isCart() + " id: " + recipeUi.getId());
        Toast.makeText(requireContext(), R.string.removed_from_cart, Toast.LENGTH_SHORT).show();
    }

}