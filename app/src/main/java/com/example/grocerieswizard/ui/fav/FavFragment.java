package com.example.grocerieswizard.ui.fav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.grocerieswizard.data.repo.RecipeRepository;
import com.example.grocerieswizard.databinding.FragmentFavBinding;
import com.example.grocerieswizard.di.GroceriesWizardInjector;
import com.example.grocerieswizard.ui.model.RecipeUi;
import com.example.grocerieswizard.ui.UiMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FavFragment extends Fragment implements FavInterface {
    RecipeRepository recipeRepository;
    private UiMapper uiMapper;
    private FavRecyclerViewAdapter adapter;
    FragmentFavBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GroceriesWizardInjector injector = GroceriesWizardInjector.getInstance();
        recipeRepository = injector.getRecipeRepository();
        uiMapper = injector.getUiMapper();
        adapter = new FavRecyclerViewAdapter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavBinding.inflate(inflater, container, false);
        adapter.setFavInterface(this);

        binding.FavRecyclerView.setAdapter(adapter);
        binding.FavRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<RecipeUi> recipeUis = recipeRepository.getFavoriteRecipes().stream()
                .map(uiMapper::toRecipeUi)
                .collect(Collectors.toList());
        adapter.setFavList(recipeUis);

        return binding.getRoot();
    }

    @Override
    public void onRemoveFromFavorites(RecipeUi recipeUi) {
        recipeRepository.deleteRecipeFromFavorites(recipeUi.getId());
        ArrayList<RecipeUi> tempList = adapter.getFavList();
        for (int i = 0; i < tempList.size(); i++) {
            RecipeUi model = tempList.get(i);
            if (model.getId() == recipeUi.getId()) {
                adapter.removeItem(i);
            }
        }
    }

    @Override
    public boolean isRecipeSelected(int id) {
        return recipeRepository.isRecipeSelected(id);
    }

    @Override
    public void insertSelectedRecipe(int id) {
        recipeRepository.insertSelectedRecipe(id);
    }

    @Override
    public void removeSelectedRecipe(int id) {
        recipeRepository.deleteSelectedRecipe(id);
    }

    @Override
    public void updateIt(int adapterPosition) {
        adapter.updateIt(adapterPosition);
    }
}