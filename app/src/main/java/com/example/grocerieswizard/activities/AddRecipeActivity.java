package com.example.grocerieswizard.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.RecipeDatabaseHelper;
import com.example.grocerieswizard.adapters.IngredientAdapter;
import com.example.grocerieswizard.adapters.RecipeRecyclerViewAdapter;
import com.example.grocerieswizard.interfaces.AddInterface;
import com.example.grocerieswizard.models.IngredientModel;
import com.example.grocerieswizard.models.RecipeModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity implements AddInterface {

    private EditText editRecipeName;
    private EditText editRecipeHowToPrepare;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private IngredientAdapter ingredientAdapter;
    private final List<IngredientModel> ingredientList = new ArrayList<>();
    private RecipeModel recipe;
    private boolean editMode = false;
    private Bitmap defaultImageBitmap;
    private RecipeDatabaseHelper recipeDatabaseHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe);

        recipeDatabaseHelper = new RecipeDatabaseHelper(this);

        editRecipeName = findViewById(R.id.edit_recipe_name);
        editRecipeHowToPrepare = findViewById(R.id.edit_recipe_how_to_prepare);

        Button btnSaveRecipe = findViewById(R.id.save_recipe_button);
        ImageButton dischargeRecipe = findViewById(R.id.discharge_recipe);

        ingredientAdapter = new IngredientAdapter(ingredientList);
        ingredientAdapter.setRecyclerViewInterface(this);
        RecyclerView ingredientsRecyclerView = findViewById(R.id.recyclerView_ingredients);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientsRecyclerView.setAdapter(ingredientAdapter);

        RecipeRecyclerViewAdapter recipeAdapter = new RecipeRecyclerViewAdapter(this);

        Button addIngredientButton = findViewById(R.id.addIngredientButton);
        addIngredientButton.setOnClickListener(v -> {
            IngredientModel newIngredient = new IngredientModel(null, 0, null);
            showAddIngredientDialog(newIngredient);
        });


        ImageView addImage = findViewById(R.id.add_image);
        Uri defaultImageUri = Uri.parse("android.resource://com.example.grocerieswizard/" + R.drawable.recipe_image_default);
        try {
            defaultImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), defaultImageUri);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //create an empty recipe
        recipe = new RecipeModel(null, null, null, null);

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        try {
                            Picasso.get()
                                    .load(selectedImageUri)
                                    .resize(150, 150)
                                    .centerCrop()
                                    .into(new Target() {
                                        @Override
                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                            addImage.setImageBitmap(bitmap);
                                            recipe.setImageBitmap(bitmap);
                                        }

                                        @Override
                                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                            Toast.makeText(AddRecipeActivity.this, "image error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                                        }
                                    });

                        } catch (Exception e) {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            addImage.setImageBitmap(defaultImageBitmap);
                            recipe.setImageBitmap(defaultImageBitmap);
                        }
                    } else {
                        addImage.setImageBitmap(defaultImageBitmap);
                        recipe.setImageBitmap(defaultImageBitmap);
                    }
                });

        addImage.setOnClickListener(v -> {
            Intent pickImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
            pickImageIntent.setType("image/*");
            pickImageLauncher.launch(pickImageIntent);
        });

        Intent intent = getIntent();
        editMode = intent.getBooleanExtra("editRecipe", false);
        int position = intent.getIntExtra("position", -1);

        // Check if the activity is opened for editing
        if (editMode) {
            RecipeModel recipeModel = intent.getParcelableExtra("recipeModel");
            //get old ones to show user
            assert recipeModel != null;
            editRecipeName.setText(recipeModel.getRecipeName());
            editRecipeHowToPrepare.setText(recipeModel.getInstructions());
            ingredientList.addAll(recipeModel.getIngredients());
            Bitmap selectedImageBitmap = recipeModel.getImageBitmap();
            //get new ones to update
            String recipeName = editRecipeName.getText().toString();
            String howToPrepare = editRecipeHowToPrepare.getText().toString();

            RecipeModel editedRecipe = new RecipeModel(recipeName, ingredientList, howToPrepare, selectedImageBitmap);
            recipe = recipeAdapter.editRecipe(position, editedRecipe);
            ingredientAdapter.changeItem(position);
            addImage.setImageBitmap(selectedImageBitmap);
        } else {
            addImage.setImageURI(defaultImageUri);
        }

        btnSaveRecipe.setOnClickListener(v -> {
            String recipeName = editRecipeName.getText().toString();
            String howToPrepare = editRecipeHowToPrepare.getText().toString();
            if (recipeName.isEmpty() || ingredientList.isEmpty() || howToPrepare.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (recipe.getImageBitmap() == null)
                recipe.setImageBitmap(defaultImageBitmap);

            recipe.setRecipeName(recipeName);
            recipe.setInstructions(howToPrepare);
            recipe.setSwiped(false);
            recipe.setIngredients(ingredientList);


            if (editMode) {
                Intent editintent = new Intent();
                editintent.putExtra("edited", true);
                editintent.putExtra("new_recipe", recipe);
                editintent.putExtra("position", position);
                setResult(RESULT_OK, editintent);
            } else {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("recipe", recipe);
                setResult(RESULT_OK, resultIntent);
            }

            finish();

        });


        dischargeRecipe.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Discharge")
                    .setMessage("Are you sure you want to discharge this recipe?")
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        });

    }

    private void showAddIngredientDialog(IngredientModel ingredientModel) {
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

            ingredientModel.setName(name);
            ingredientModel.setUnit(unit);
            ingredientModel.setQuantity(quantity);
            ingredientAdapter.addIngredient(ingredientModel);
            recipeDatabaseHelper.insertIngredient(ingredientModel, ingredientModel.getRecipeId());

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
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
        String recipeName = editRecipeName.getText().toString();
        String howToPrepare = editRecipeHowToPrepare.getText().toString();

        return !recipeName.isEmpty() || !ingredientList.isEmpty() || !howToPrepare.isEmpty();
    }

    private void showUnsavedChangesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unsaved Changes")
                .setMessage("You have unsaved changes. Are you sure you want to discard them?")
                .setPositiveButton("Discard", (dialog, which) -> finish())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void onItemDelete(IngredientModel ingredientModel) {
        if (ingredientModel != null) {
            recipeDatabaseHelper.deleteIngredient(ingredientModel.getId());
            ingredientAdapter.removeIngredient(ingredientModel, this);
        }

    }

    @Override
    public void onItemEdit(IngredientModel ingredientModel) {
        if (ingredientModel != null) {
            showEditIngredientDialog(ingredientModel);
        }
    }

    public void showEditIngredientDialog(IngredientModel ingredientModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_ingredient, null);
        builder.setView(dialogView);

        EditText ingredientNameEditText = dialogView.findViewById(R.id.ingredientNameEditText);
        EditText ingredientQuantityEditText = dialogView.findViewById(R.id.ingredientQuantityEditText);
        EditText ingredientUnitEditText = dialogView.findViewById(R.id.ingredientUnitEditText);

        // Pre-fill the dialog's fields with the existing ingredient's information
        ingredientNameEditText.setText(ingredientModel.getName());
        ingredientQuantityEditText.setText(String.valueOf(ingredientModel.getQuantity()));
        ingredientUnitEditText.setText(ingredientModel.getUnit());

        builder.setPositiveButton("Save", (dialog, which) -> {
            // Get the updated values from the dialog's fields
            String name = ingredientNameEditText.getText().toString();
            String quantityStr = ingredientQuantityEditText.getText().toString();
            double quantity = Double.parseDouble(quantityStr);
            String unit = ingredientUnitEditText.getText().toString();

            // Update the ingredient model with the new values
            ingredientModel.setName(name);
            ingredientModel.setQuantity(quantity);
            ingredientModel.setUnit(unit);

            // Find the position of the ingredient model in the list
            int position = -1;
            for (int i = 0; i < ingredientList.size(); i++) {
                if (ingredientList.get(i) == ingredientModel) {
                    position = i;
                    break;
                }
            }

            // Check if the position was found and update the adapter if needed
            if (position != -1) {
                ingredientAdapter.changeItem(position);
            }

            dialog.dismiss(); // Dismiss the dialog after saving
        });


        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
