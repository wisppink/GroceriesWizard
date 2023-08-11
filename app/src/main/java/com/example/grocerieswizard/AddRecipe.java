package com.example.grocerieswizard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddRecipe extends AppCompatActivity {

    private static final String KEY_RECIPE_NAME = "recipe_name";
    private static final String KEY_RECIPE_INGREDIENTS = "recipe_ingredients";
    private static final String KEY_RECIPE_HOW_TO_PREPARE = "recipe_how_to_prepare";
    private static final String KEY_SELECTED_IMAGE_URI = "selected_image_uri";

    private Uri selectedImageUri;
    private EditText editRecipeName;
    private EditText editRecipeHowToPrepare;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private List<IngredientModel> ingredientList;
    private IngredientAdapter ingredientAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe);

        ingredientList = new ArrayList<>();
        ingredientAdapter = new IngredientAdapter(ingredientList);

        editRecipeName = findViewById(R.id.edit_recipe_name);
        editRecipeHowToPrepare = findViewById(R.id.edit_recipe_how_to_prepare);

        Button btnSaveRecipe = findViewById(R.id.save_recipe_button);
        ImageButton dischargeRecipe = findViewById(R.id.discharge_recipe);

        RecyclerView ingredientsRecyclerView = findViewById(R.id.recyclerView_ingredients);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientsRecyclerView.setAdapter(ingredientAdapter);

        Button addIngredientButton = findViewById(R.id.addIngredientButton);
        addIngredientButton.setOnClickListener(v -> {showAddIngredientDialog();});

        ImageView addImage = findViewById(R.id.add_image);
        Uri defaultImageUri = Uri.parse("android.resource://com.example.grocerieswizard/" + R.drawable.recipe_image_default);


        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        //TODO: change the resolution
                        addImage.setImageURI(selectedImageUri);
                    } else {
                        // User didn't choose an image, display the default image
                        selectedImageUri = defaultImageUri;
                        addImage.setImageURI(selectedImageUri);
                    }
                });
        addImage.setImageURI(defaultImageUri);
        Log.d("DefaultImageURI", defaultImageUri.toString());

        addImage.setOnClickListener(v -> {
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK);
            pickImageIntent.setType("image/*");
            pickImageLauncher.launch(pickImageIntent);
        });

        btnSaveRecipe.setOnClickListener(v -> {
            String recipeName = editRecipeName.getText().toString();
            List<IngredientModel> ingredientModelList = ingredientList;
            String howToPrepare = editRecipeHowToPrepare.getText().toString();

            if (recipeName.isEmpty() || ingredientModelList.isEmpty() || howToPrepare.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            RecipeModel recipe;
            if (selectedImageUri != null) {
                recipe = new RecipeModel(recipeName, ingredientModelList, howToPrepare, selectedImageUri);
            } else {
                recipe = new RecipeModel(recipeName, ingredientModelList, howToPrepare, defaultImageUri);
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("recipe", recipe);
            setResult(RESULT_OK, resultIntent);
            finish();
            Toast.makeText(this, "saveButton works", Toast.LENGTH_SHORT).show();
        });


        dischargeRecipe.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Discharge")
                    .setMessage("Are you sure you want to discharge this recipe?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        finish();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });

    }

    private void showAddIngredientDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_ingredient, null);
        builder.setView(dialogView);

        EditText ingredientNameEditText = dialogView.findViewById(R.id.ingredientNameEditText);
        EditText ingredientQuantityEditText = dialogView.findViewById(R.id.ingredientQuantityEditText);
        EditText ingredientUnitEditText = dialogView.findViewById(R.id.ingredientUnitEditText);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = ingredientNameEditText.getText().toString();
            String quantityStr = ingredientQuantityEditText.getText().toString();
            double quantity = Double.parseDouble(quantityStr);
            String unit = ingredientUnitEditText.getText().toString();

            IngredientModel newIngredient = new IngredientModel(name, quantity, unit);
            ingredientList.add(newIngredient);
            ingredientAdapter.notifyDataSetChanged();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (unsavedChangesExist()) {
            showUnsavedChangesDialog();
        } else {
            super.onBackPressed();
        }
    }

    private boolean unsavedChangesExist() {
        // Check if there are any unsaved changes (e.g., fields are not empty)
        String recipeName = editRecipeName.getText().toString();
        List<IngredientModel> ingredients = ingredientList;
        String howToPrepare = editRecipeHowToPrepare.getText().toString();

        return !recipeName.isEmpty() || !ingredients.isEmpty() || !howToPrepare.isEmpty();
    }

    private void showUnsavedChangesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unsaved Changes")
                .setMessage("You have unsaved changes. Are you sure you want to discard them?")
                .setPositiveButton("Discard", (dialog, which) -> {
                    finish();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_RECIPE_NAME, editRecipeName.getText().toString());
        outState.putString(KEY_RECIPE_INGREDIENTS, ingredientList.toString());
        outState.putString(KEY_RECIPE_HOW_TO_PREPARE, editRecipeHowToPrepare.getText().toString());
        outState.putParcelable(KEY_SELECTED_IMAGE_URI, selectedImageUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        editRecipeName.setText(savedInstanceState.getString(KEY_RECIPE_NAME));
        ingredientList.addAll(Objects.requireNonNull(savedInstanceState.getParcelableArrayList(KEY_RECIPE_INGREDIENTS)));
        editRecipeHowToPrepare.setText(savedInstanceState.getString(KEY_RECIPE_HOW_TO_PREPARE));
        if (savedInstanceState.containsKey(KEY_SELECTED_IMAGE_URI)) {
            selectedImageUri = savedInstanceState.getParcelable(KEY_SELECTED_IMAGE_URI);
        }
    }
}
