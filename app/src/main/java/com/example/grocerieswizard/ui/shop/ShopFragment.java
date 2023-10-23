package com.example.grocerieswizard.ui.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.grocerieswizard.data.repo.RecipeRepository;
import com.example.grocerieswizard.databinding.FragmentShopBinding;
import com.example.grocerieswizard.di.GroceriesWizardInjector;
import com.example.grocerieswizard.ui.model.RecipeUi;
import com.example.grocerieswizard.ui.UiMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ShopFragment extends Fragment {

    private FragmentShopBinding binding;
    private RecipeRepository recipeRepository;
    private UiMapper uiMapper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GroceriesWizardInjector injector = GroceriesWizardInjector.getInstance();
        recipeRepository = injector.getRecipeRepository();
        uiMapper = injector.getUiMapper();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShopBinding.inflate(inflater, container, false);

        // Initialize the adapter and set it to the RecyclerView
        ShopAdapter adapter = new ShopAdapter(requireContext(), new ShopHelperImpl());
        // Retrieve selected recipes from the database
        List<RecipeUi> recipeUis = recipeRepository.getSelectedRecipes().stream()
                .map(uiMapper::toRecipeUi)
                .collect(Collectors.toList());
        adapter.setSelectedRecipeList(recipeUis);
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