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
import com.example.grocerieswizard.data.local.model.CartItem;
import com.example.grocerieswizard.ui.addrecipe.AddRecipeFragment;
import com.example.grocerieswizard.data.repo.RecipeRepository;
import com.example.grocerieswizard.databinding.FragmentHomeBinding;
import com.example.grocerieswizard.ui.detail.DetailFragment;
import com.example.grocerieswizard.di.GroceriesWizardInjector;
import com.example.grocerieswizard.ui.model.IngredientUi;
import com.example.grocerieswizard.ui.model.RecipeUi;
import com.example.grocerieswizard.ui.UiMapper;

import java.util.List;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment implements RecipeInterface {

    private RecipeRecyclerViewAdapter adapter;
    private RecipeRepository recipeRepository;
    private UiMapper uiMapper;
    private static final String TAG = "HomeFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new RecipeRecyclerViewAdapter();
        adapter.setRecyclerViewInterface(this);
        GroceriesWizardInjector injector = GroceriesWizardInjector.getInstance();
        recipeRepository = injector.getRecipeRepository();
        uiMapper = injector.getUiMapper();
    }

    @NonNull
    private List<RecipeUi> getAllRecipes() {
        return recipeRepository.getAllRecipes().stream()
                .map(uiMapper::toRecipeUi)
                .collect(Collectors.toList());
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
        List<RecipeUi> recipeUis = getAllRecipes();
        adapter.setRecipeList(recipeUis);
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
    public void onItemClick(int position) {

        RecipeUi recipeUi = adapter.getItemAtPosition(position);
        if (recipeUi != null) {
            if (!recipeUi.isSwiped()) {
                recipeUi.setSwiped(false);
                adapter.itemChanged(position);
                DetailFragment detailFragment = DetailFragment.newInstance(recipeUi);
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
        RecipeUi recipeUi = adapter.getItemAtPosition(position);
        if (recipeUi != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirm Deletion");
            builder.setMessage("Are you sure you want to delete " + recipeUi.getRecipeName() + " recipe?");
            builder.setPositiveButton("Delete", (dialog, which) -> {
                // User confirmed deletion, remove the recipe and update the RecyclerView
                adapter.removeRecipe(recipeUi);
                Toast.makeText(getContext(), "Recipe deleted: " + recipeUi.getRecipeName(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                recipeUi.setSwiped(false);
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
        RecipeUi recipeUi = adapter.getItemAtPosition(position);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        AddRecipeFragment addRecipeFragment = new AddRecipeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("recipeModel", recipeUi);
        bundle.putInt("position", position);
        addRecipeFragment.setArguments(bundle);
        transaction.replace(R.id.frameLayout, addRecipeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public Boolean isRecipeSelected(int id) {
        return recipeRepository.isRecipeInCart(id);
    }

    @Override
    public int updateRecipe(RecipeUi oldRecipeUi) {
        return recipeRepository.updateRecipe(oldRecipeUi.getId(), uiMapper.toRecipe(oldRecipeUi));
    }

    @Override
    public void insertRecipe(RecipeUi recipeUi) {
        recipeRepository.insertRecipe(uiMapper.toRecipe(recipeUi));
    }

    @Override
    public void deleteRecipe(int id) {
        Log.d(TAG, "deleteRecipe: id: " + id);
        Log.d(TAG, "deleteRecipe: recipes before: " + recipeRepository.getAllRecipes().size());
        recipeRepository.deleteRecipe(id);
        Log.d(TAG, "deleteRecipe: recipes after: " + recipeRepository.getAllRecipes().size());
    }

    @Override
    public void deleteSelectedRecipe(int recipeId) {
        recipeRepository.deleteCartItem(recipeId);
    }

    @Override
    public void insertSelectedRecipe(int recipeId) {
        CartItem cartItem = new CartItem(recipeId);
        recipeRepository.insertCartItem(cartItem);
    }

    @Override
    public boolean isRecipeFavorite(int id) {
        return recipeRepository.isRecipeFavorite(id);
    }

    @Override
    public void insertRecipeFav(int recipeId) {
        recipeRepository.insertRecipeFav(recipeId);
    }

    @Override
    public void deleteRecipeFav(int recipeId) {
        recipeRepository.deleteRecipeFromFavorites(recipeId);
    }

    @Override
    public void onItemShare(int adapterPosition) {
        RecipeUi recipeUi = adapter.getItemAtPosition(adapterPosition);
        String instructions = recipeUi.getInstructions();
        String ingredients = getStringIngredients(recipeUi.getIngredients());

        String shareString = recipeUi.getRecipeName() + "\n\n\n" + instructions + "\n\n\n" + ingredients;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        shareIntent.putExtra(Intent.EXTRA_TEXT, shareString);

        // Start an activity to choose the sharing method
        requireContext().startActivity(Intent.createChooser(shareIntent, "Share Recipe"));
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

}