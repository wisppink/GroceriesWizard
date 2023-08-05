package com.example.grocerieswizard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddRecipe extends AppCompatActivity {

    private static final String KEY_RECIPE_NAME = "recipe_name";
    private static final String KEY_RECIPE_INGREDIENTS = "recipe_ingredients";
    private static final String KEY_RECIPE_HOW_TO_PREPARE = "recipe_how_to_prepare";
    private static final String KEY_SELECTED_IMAGE_URI = "selected_image_uri";

    private Uri selectedImageUri;
    private EditText editRecipeName;
    private EditText editRecipeIngredients;
    private EditText editRecipeHowToPrepare;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe);

        ImageButton addImage = findViewById(R.id.add_image);

        editRecipeName = findViewById(R.id.edit_recipe_name);
        editRecipeIngredients = findViewById(R.id.edit_recipe_ingredients);
        editRecipeHowToPrepare = findViewById(R.id.edit_recipe_how_to_prepare);

        Button btnSaveRecipe = findViewById(R.id.save_recipe_button);

        ImageButton deleteRecipeButton = findViewById(R.id.delete_recipe_button);
        ImageButton dischargeRecipe = findViewById(R.id.discharge_recipe);

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        //TODO: change the resolution

                        addImage.setImageURI(selectedImageUri);
                    }
                });

        addImage.setOnClickListener(v -> {
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK);
            pickImageIntent.setType("image/*");
            pickImageLauncher.launch(pickImageIntent);
        });

        btnSaveRecipe.setOnClickListener(v -> {
            String recipeName = editRecipeName.getText().toString();
            String ingredients = editRecipeIngredients.getText().toString();
            String howToPrepare = editRecipeHowToPrepare.getText().toString();

            if (recipeName.isEmpty() || ingredients.isEmpty() || howToPrepare.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedImageUri == null) {
                Toast.makeText(this, "Please select an image!", Toast.LENGTH_SHORT).show();
                return;
            }
            RecipeModel recipe = new RecipeModel(recipeName, ingredients, howToPrepare, selectedImageUri);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("recipe", recipe);
            setResult(RESULT_OK, resultIntent);
            finish();
            Toast.makeText(this, "saveButton works", Toast.LENGTH_SHORT).show();
        });

        deleteRecipeButton.setOnClickListener(v -> {
            //delete the recipe
            // show a confirmation dialog before deleting
        });

        dischargeRecipe.setOnClickListener(v -> {
            // show a confirmation dialog before discharge
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_RECIPE_NAME, editRecipeName.getText().toString());
        outState.putString(KEY_RECIPE_INGREDIENTS, editRecipeIngredients.getText().toString());
        outState.putString(KEY_RECIPE_HOW_TO_PREPARE, editRecipeHowToPrepare.getText().toString());
        outState.putParcelable(KEY_SELECTED_IMAGE_URI, selectedImageUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        editRecipeName.setText(savedInstanceState.getString(KEY_RECIPE_NAME));
        editRecipeIngredients.setText(savedInstanceState.getString(KEY_RECIPE_INGREDIENTS));
        editRecipeHowToPrepare.setText(savedInstanceState.getString(KEY_RECIPE_HOW_TO_PREPARE));
        selectedImageUri = savedInstanceState.getParcelable(KEY_SELECTED_IMAGE_URI);
    }
}
