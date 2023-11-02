package com.example.grocerieswizard.ui.fav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.grocerieswizard.databinding.FragmentFavBinding;
import com.example.grocerieswizard.di.GroceriesWizardInjector;
import com.example.grocerieswizard.ui.model.RecipeUi;

import java.util.ArrayList;
import java.util.List;

public class FavFragment extends Fragment implements FavInterface, FavContract.View {
    FavPresenter presenter;
    private FavRecyclerViewAdapter adapter;
    FragmentFavBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GroceriesWizardInjector injector = GroceriesWizardInjector.getInstance();
        presenter = new FavPresenter(injector.getRecipeRepository(), injector.getUiMapper());
        presenter.bindView(this);
        adapter = new FavRecyclerViewAdapter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavBinding.inflate(inflater, container, false);
        adapter.setFavInterface(this);

        binding.FavRecyclerView.setAdapter(adapter);
        binding.FavRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        presenter.setFavList();

        return binding.getRoot();
    }

    @Override
    public void showFavList(List<RecipeUi> recipeUis) {
        adapter.setFavList(recipeUis);
    }

    @Override
    public void onRemoveFromFavorites(RecipeUi recipeUi) {
        presenter.removeFromFavorites(recipeUi);
    }

    @Override
    public void removeFromFavorites(RecipeUi recipeUi) {
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
        return presenter.isRecipeInCart(id);
    }

    @Override
    public void insertSelectedRecipe(int id) {
        presenter.insertCartItem(id);
    }

    @Override
    public void removeSelectedRecipe(int id) {
        presenter.removeFromCart(id);
    }

    @Override
    public void updateIt(int adapterPosition) {
        adapter.updateIt(adapterPosition);
    }
}