package com.example.grocerieswizard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    List<IngredientModel> ingredients;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        Intent intent = getIntent();

        RecipeModel recipe = intent.getParcelableExtra("MyRecipe");
        TextView titleTextView = findViewById(R.id.show_title);
        TextView ingredientsTextView = findViewById(R.id.show_ingredients);
        ImageView imageRecipe = findViewById(R.id.showRecipeImage);
        TextView howtoTextView = findViewById(R.id.show_how_to_prepare);

        if (recipe != null) {
            titleTextView.setText(recipe.getRecipeName());
            imageRecipe.setImageURI(recipe.getRecipeImageUri());
            howtoTextView.setText(recipe.getHowToPrepare());
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

}

