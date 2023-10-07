package com.example.grocerieswizard.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.grocerieswizard.addRecipe.IngredientModel;
import com.example.grocerieswizard.databinding.FragmentDetailBinding;
import com.example.grocerieswizard.home.RecipeModel;

import java.util.List;

public class DetailFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static DetailFragment newInstance(RecipeModel recipe) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("MyRecipe", recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDetailBinding binding = FragmentDetailBinding.inflate(inflater, container, false);

        Bundle args = getArguments();
        if (args != null) {
            RecipeModel recipe = args.getParcelable("MyRecipe");
            if (recipe != null) {
                recipe.setSwiped(false);

                binding.showTitle.setText(recipe.getRecipeName());
                binding.showRecipeImage.setImageBitmap(recipe.getImageBitmap());
                String inst = "\t\t" + recipe.getInstructions();
                binding.showHowToPrepare.setText(inst);

                List<IngredientModel> ingredients = recipe.getIngredients();
                StringBuilder ingredientsBuilder = new StringBuilder();
                for (IngredientModel ingredient : ingredients) {
                    ingredientsBuilder.append(ingredient.getName()).append(" ").append(ingredient.getQuantity()).append(" ").append(ingredient.getUnit()).append("\n");
                }
                binding.showIngredients.setText(ingredientsBuilder.toString());
            }
        }

        return binding.getRoot();
    }

}