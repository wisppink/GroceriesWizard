package com.example.grocerieswizard.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.grocerieswizard.RecipeDatabaseHelper;
import com.example.grocerieswizard.databinding.FragmentShopBinding;
import com.example.grocerieswizard.home.RecipeModel;

import java.util.ArrayList;

public class ShopFragment extends Fragment {

    private FragmentShopBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShopBinding.inflate(inflater, container, false);

        // Initialize the adapter and set it to the RecyclerView
        ShopAdapter adapter = new ShopAdapter(requireContext(), new ShopHelperImpl());
        // Retrieve selected recipes from the database
        ArrayList<RecipeModel> recipes;
        try (RecipeDatabaseHelper dbHelper = new RecipeDatabaseHelper(requireContext())) {
            recipes = dbHelper.getSelectedRecipes();
            adapter.setSelectedRecipeList(recipes);
        }
        binding.shoppingCart.setAdapter(adapter);
        binding.shoppingCart.setLayoutManager(new LinearLayoutManager(requireContext()));
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
