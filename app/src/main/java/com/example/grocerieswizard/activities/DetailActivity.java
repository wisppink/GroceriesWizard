package com.example.grocerieswizard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.grocerieswizard.databinding.DetailBinding;
import com.example.grocerieswizard.models.IngredientModel;
import com.example.grocerieswizard.models.RecipeModel;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    List<IngredientModel> ingredients;
    DetailBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();

        RecipeModel recipe = intent.getParcelableExtra("MyRecipe");
        assert recipe != null;
        recipe.setSwiped(false);

        binding.showTitle.setText(recipe.getRecipeName());
        binding.showRecipeImage.setImageBitmap(recipe.getImageBitmap());
        binding.showHowToPrepare.setText(recipe.getInstructions());

        ingredients = recipe.getIngredients();
        StringBuilder ingredientsBuilder = new StringBuilder();
        for (IngredientModel ingredient : ingredients) {
            //TODO:show nicely
            ingredientsBuilder.append(ingredient.getName()).append(" ").append(ingredient.getQuantity()).append(" ").append(ingredient.getUnit()).append("\n");
        }
        binding.showIngredients.setText(ingredientsBuilder.toString());

    }
}

