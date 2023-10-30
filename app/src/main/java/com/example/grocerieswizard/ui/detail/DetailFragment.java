package com.example.grocerieswizard.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.grocerieswizard.ui.model.IngredientUi;
import com.example.grocerieswizard.databinding.FragmentDetailBinding;
import com.example.grocerieswizard.ui.model.RecipeUi;

import java.util.List;

public class DetailFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static DetailFragment newInstance(RecipeUi recipeUi) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("MyRecipe", recipeUi);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDetailBinding binding = FragmentDetailBinding.inflate(inflater, container, false);

        Bundle args = getArguments();
        if (args != null) {
            RecipeUi recipeUi = args.getParcelable("MyRecipe");
            if (recipeUi != null) {
                recipeUi.setSwiped(false);

                binding.showTitle.setText(recipeUi.getRecipeName());
                // binding.showRecipeImage.setImageBitmap(recipeUi.getImageBitmap());
                String inst = "\t\t" + recipeUi.getInstructions();
                binding.showHowToPrepare.setText(inst);

                List<IngredientUi> ingredients = recipeUi.getIngredients();
                StringBuilder ingredientsBuilder = new StringBuilder();
                for (IngredientUi ingredient : ingredients) {
                    ingredientsBuilder.append(ingredient.getName()).append(" ").append(ingredient.getQuantity()).append(" ").append(ingredient.getUnit()).append("\n");
                }
                binding.showIngredients.setText(ingredientsBuilder.toString());
            }
        }

        return binding.getRoot();
    }

}