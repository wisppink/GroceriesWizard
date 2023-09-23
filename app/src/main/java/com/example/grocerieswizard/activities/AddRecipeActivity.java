package com.example.grocerieswizard.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.MealResponse;
import com.example.grocerieswizard.R;
import com.example.grocerieswizard.RecipeDatabaseHelper;
import com.example.grocerieswizard.adapters.IngredientAdapter;
import com.example.grocerieswizard.adapters.RecipeRecyclerViewAdapter;
import com.example.grocerieswizard.databinding.ActivityAddRecipeBinding;
import com.example.grocerieswizard.databinding.DialogAddIngredientBinding;
import com.example.grocerieswizard.interfaces.AddInterface;
import com.example.grocerieswizard.interfaces.MealService;
import com.example.grocerieswizard.models.IngredientModel;
import com.example.grocerieswizard.models.Meal;
import com.example.grocerieswizard.models.RecipeModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddRecipeActivity extends AppCompatActivity implements AddInterface {

    ActivityAddRecipeBinding binding;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private IngredientAdapter ingredientAdapter;
    private final List<IngredientModel> ingredientList = new ArrayList<>();
    private RecipeModel recipe;
    private boolean editMode = false;
    private Bitmap defaultImageBitmap;
    private RecipeDatabaseHelper recipeDatabaseHelper;
    private static final String TAG = "AddRecipeActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddRecipeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        recipeDatabaseHelper = new RecipeDatabaseHelper(this);

        ingredientAdapter = new IngredientAdapter(ingredientList);
        ingredientAdapter.setRecyclerViewInterface(this);

        RecyclerView recyclerViewIngredients = binding.recyclerViewIngredients;
        recyclerViewIngredients.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewIngredients.setAdapter(ingredientAdapter);

        RecipeRecyclerViewAdapter recipeAdapter = new RecipeRecyclerViewAdapter(this);

        TextView editRecipeName = binding.editRecipeName;

        TextView editRecipeHowToPrepare = binding.editRecipeHowToPrepare;

        Intent intent = getIntent();
        editMode = intent.getBooleanExtra("editRecipe", false);
        int position = intent.getIntExtra("position", -1);

        ImageView addImage = binding.addImage;

        Button addIngredient = binding.addIngredientButton;

        Uri defaultImageUri = Uri.parse("android.resource://com.example.grocerieswizard/" + R.drawable.recipe_image_default);

        Button saveRecipe = binding.saveRecipeButton;

        ImageView discharge = binding.dischargeRecipe;

        try {
            defaultImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), defaultImageUri);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        //create an empty recipe
        recipe = new RecipeModel(null, null, null, null);

        if (!editMode) {
            final Handler mHandler = new Handler();
            editRecipeName.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    mHandler.removeCallbacksAndMessages(null);
                    mHandler.postDelayed(() -> {
                        String inputText = editRecipeName.getText().toString().trim();
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        MealService mealApi = retrofit.create(MealService.class);
                        Call<MealResponse> call = mealApi.searchMeals(inputText);
                        Log.d(TAG, "afterTextChanged: input text " + inputText);

                        call.enqueue(new Callback<MealResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                                if (response.isSuccessful()) {

                                    MealResponse mealResponse = response.body();
                                    if (mealResponse != null && mealResponse.getMeals() != null) {
                                        showAlertDialogForFoundRecipe(mealResponse.getMeal(0), editRecipeHowToPrepare, addImage);
                                    } else {
                                        System.out.println("No meals found.");
                                    }
                                } else {
                                    System.out.println("API request failed.");
                                }

                            }

                            @Override
                            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                                Log.e(TAG, "onFailure: ", t);
                            }
                        });

                    }, 1000); // 0.5 saniye
                }
            });
        }


        addIngredient.setOnClickListener(v -> {
            IngredientModel newIngredient = new IngredientModel(null, 0, null);
            showAddIngredientDialog(newIngredient);
        });


        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri selectedImageUri = result.getData().getData();
                try {
                    Picasso.get().load(selectedImageUri).resize(150, 150).centerCrop().into(new Target() {
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

        saveRecipe.setOnClickListener(v -> {
            String recipeName = editRecipeName.getText().toString();
            String howToPrepare = editRecipeHowToPrepare.getText().toString();
            if (recipeName.isEmpty() || ingredientList.isEmpty() || howToPrepare.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (recipe.getImageBitmap() == null) recipe.setImageBitmap(defaultImageBitmap);

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

        discharge.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Discharge").setMessage("Are you sure you want to discharge this recipe?")
                    .setPositiveButton(R.string.yes, (dialog, which) -> finish()).setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss()).show();
        });

    }

    private void showAlertDialogForFoundRecipe(Meal meal, TextView howToPrepare, ImageView addImage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.TitleFoundRecipe);
        builder.setMessage(R.string.MessageFoundRecipe);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            System.out.println("Meal ID: " + meal.getIdMeal());
            System.out.println("Meal Name: " + meal.getStrMeal());
            howToPrepare.setText(meal.getStrInstructions());
            setIngredients(meal);
            bitmapFromMeal(meal.getStrMealThumb(), bitmap -> {
                if (bitmap != null) {
                    Log.d(TAG, "showAlertDialogForFoundRecipe: its not null" + bitmap);
                    recipe.setImageBitmap(bitmap);
                    addImage.setImageBitmap(bitmap);
                    Log.d(TAG, "showAlertDialogForFoundRecipe: recipe set image: " + recipe.getImageBitmap());
                }
            });

        });
        builder.setNegativeButton(R.string.no, (dialog, which) -> Log.d(TAG, "showAlertDialogForFoundRecipe: do nothing."));
        builder.show();

    }

    private void bitmapFromMeal(String strImageSource, BitmapCallback callback) {
        Log.d(TAG, "bitmapFromMeal: strImage " + strImageSource);
        try {
            Picasso.get().load(strImageSource).resize(150, 150).centerCrop().into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Log.d(TAG, "onBitmapLoaded: strImageSource " + strImageSource);
                    Log.d(TAG, "onBitmapLoaded: bitmap: " + bitmap);
                    callback.onBitmapLoaded(bitmap);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    callback.onBitmapLoaded(null); // Callback with null in case of failure
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void setIngredients(Meal meal) {
        String[] ingredientNames = {
                meal.getStrIngredient1(),
                meal.getStrIngredient2(),
                meal.getStrIngredient3(),
                meal.getStrIngredient4(),
                meal.getStrIngredient5(),
                meal.getStrIngredient6(),
                meal.getStrIngredient7(),
                meal.getStrIngredient8(),
                meal.getStrIngredient9(),
                meal.getStrIngredient10(),
                meal.getStrIngredient11(),
                meal.getStrIngredient12(),
                meal.getStrIngredient13(),
                meal.getStrIngredient14(),
                meal.getStrIngredient15(),
                meal.getStrIngredient16(),
                meal.getStrIngredient17(),
                meal.getStrIngredient18(),
                meal.getStrIngredient19(),
                meal.getStrIngredient20()
        };

        String[] ingredientMeasures = {
                meal.getStrMeasure1(),
                meal.getStrMeasure2(),
                meal.getStrMeasure3(),
                meal.getStrMeasure4(),
                meal.getStrMeasure5(),
                meal.getStrMeasure6(),
                meal.getStrMeasure7(),
                meal.getStrMeasure8(),
                meal.getStrMeasure9(),
                meal.getStrMeasure10(),
                meal.getStrMeasure11(),
                meal.getStrMeasure12(),
                meal.getStrMeasure13(),
                meal.getStrMeasure14(),
                meal.getStrMeasure15(),
                meal.getStrMeasure16(),
                meal.getStrMeasure17(),
                meal.getStrMeasure18(),
                meal.getStrMeasure19(),
                meal.getStrMeasure20()
        };

        for (int i = 0; i < ingredientNames.length; i++) {
            String name = ingredientNames[i];
            String measure = ingredientMeasures[i];

            if (name != null && !name.isEmpty() && measure != null && !measure.isEmpty()) {
                IngredientModel ingredient = new IngredientModel(name, parseQuantity(measure), parseUnit(measure));
                addIngredientAutomatically(ingredient);
            }
        }
    }

    private double parseQuantity(String measure) {
        StringBuilder quantityStr = new StringBuilder();
        boolean foundDigit = false;

        for (char c : measure.toCharArray()) {
            if (Character.isDigit(c) || c == '.') {
                quantityStr.append(c);
                foundDigit = true;
            } else if (foundDigit) {
                break;
            }
        }

        try {
            return Double.parseDouble(quantityStr.toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private String parseUnit(String measure) {
        StringBuilder unitStr = new StringBuilder();
        boolean foundDigit = false;

        for (char c : measure.toCharArray()) {
            if (Character.isDigit(c) || c == '.') {
                foundDigit = true;
            } else if (foundDigit) {
                unitStr.append(c);
            }
        }

        String unit = unitStr.toString().trim();

        if (unit.isEmpty()) {
            unit = "piece";
        }

        return unit;
    }


    private void addIngredientAutomatically(IngredientModel ingredientModel) {
        ingredientAdapter.addIngredient(ingredientModel);
        recipeDatabaseHelper.insertIngredient(ingredientModel, ingredientModel.getRecipeId());
    }


    private void showAddIngredientDialog(IngredientModel ingredientModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogAddIngredientBinding dialogBinding = DialogAddIngredientBinding.inflate(getLayoutInflater());
        View dialogView = dialogBinding.getRoot();
        builder.setView(dialogView);

        EditText ingredientNameEditText = dialogBinding.ingredientNameEditText;
        EditText ingredientQuantityEditText = dialogBinding.ingredientQuantityEditText;
        EditText ingredientUnitEditText = dialogBinding.ingredientUnitEditText;

        builder.setPositiveButton(R.string.add, (dialog, which) -> {
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
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss());
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
        String recipeName = binding.editRecipeName.getText().toString();
        String howToPrepare = binding.editRecipeHowToPrepare.getText().toString();

        return !recipeName.isEmpty() || !ingredientList.isEmpty() || !howToPrepare.isEmpty();
    }

    private void showUnsavedChangesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unsaved Changes").setMessage("You have unsaved changes. Are you sure you want to discard them?").setPositiveButton("Discard", (dialog, which) -> finish()).setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss()).show();
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
        DialogAddIngredientBinding dialogBinding = DialogAddIngredientBinding.inflate(getLayoutInflater());
        View dialogView = dialogBinding.getRoot();
        builder.setView(dialogView);


        EditText ingredientNameEditText = dialogBinding.ingredientNameEditText;
        EditText ingredientQuantityEditText = dialogBinding.ingredientQuantityEditText;
        EditText ingredientUnitEditText = dialogBinding.ingredientUnitEditText;

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


        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    interface BitmapCallback {
        void onBitmapLoaded(Bitmap bitmap);
    }

}
