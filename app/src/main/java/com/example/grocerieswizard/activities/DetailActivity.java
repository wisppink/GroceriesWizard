package com.example.grocerieswizard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.models.IngredientModel;
import com.example.grocerieswizard.models.RecipeModel;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    List<IngredientModel> ingredients;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        Intent intent = getIntent();

        RecipeModel recipe = intent.getParcelableExtra("MyRecipe");
        assert recipe != null;
        recipe.setSwiped(false);
        TextView titleTextView = findViewById(R.id.show_title);
        TextView ingredientsTextView = findViewById(R.id.show_ingredients);
        ImageView imageRecipe = findViewById(R.id.showRecipeImage);
        TextView howtoTextView = findViewById(R.id.show_how_to_prepare);

        titleTextView.setText(recipe.getRecipeName());
        imageRecipe.setImageBitmap(recipe.getImageBitmap());
        howtoTextView.setText(recipe.getInstructions());
        ingredients = recipe.getIngredients();
        System.out.println(ingredients);
        StringBuilder ingredientsBuilder = new StringBuilder();
        for (IngredientModel ingredient : ingredients) {
            //TODO:show nicely, like max 30 char for name, then quantity, then unit
            ingredientsBuilder.append(ingredient.getName()).append(" ").append(ingredient.getQuantity()).append(" ").append(ingredient.getUnit()).append("\n");
        }
        ingredientsTextView.setText(ingredientsBuilder.toString());


    }
}

