package com.example.grocerieswizard.Fav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.grocerieswizard.RecipeDatabaseHelper;
import com.example.grocerieswizard.databinding.FragmentFavBinding;
import com.example.grocerieswizard.home.RecipeModel;

import java.util.ArrayList;

public class FavFragment extends Fragment implements FavInterface {
    RecipeDatabaseHelper dbHelper;
    private FavRecyclerViewAdapter adapter;
    FragmentFavBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new RecipeDatabaseHelper(getContext());
        adapter = new FavRecyclerViewAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavBinding.inflate(inflater, container, false);
        adapter.setFavInterface(this);

        binding.FavRecyclerView.setAdapter(adapter);
        binding.FavRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<RecipeModel> recipes = dbHelper.getRecipesFav();
        adapter.setFavList(recipes);

        return binding.getRoot();
    }

    @Override
    public void onRemoveFromFavorites(RecipeModel recipeModel) {
        dbHelper.deleteRecipeFav(recipeModel.getId());
        ArrayList<RecipeModel> tempList = adapter.getFavList();
        for (int i = 0; i < tempList.size(); i++) {
            RecipeModel model = tempList.get(i);
            if (model.getId() == recipeModel.getId()) {
                adapter.removeItem(i);
            }
        }

    }

    @Override
    public boolean isRecipeSelected(int id) {
        return dbHelper.isRecipeSelected(id);
    }

    @Override
    public void insertSelectedRecipe(int id) {
        dbHelper.insertSelectedRecipe(id);
    }

    @Override
    public void removeSelectedRecipe(int id) {
        dbHelper.deleteSelectedRecipe(id);
    }
}
