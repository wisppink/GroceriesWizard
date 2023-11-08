package com.example.grocerieswizard.ui.addrecipe;

import static android.app.Activity.RESULT_OK;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.data.local.model.IngredientItem;
import com.example.grocerieswizard.data.local.model.RecipeItem;
import com.example.grocerieswizard.data.repo.RecipeRepository;
import com.example.grocerieswizard.data.repo.RepositoryCallback;
import com.example.grocerieswizard.databinding.DialogAddIngredientBinding;
import com.example.grocerieswizard.databinding.FragmentAddRecipeBinding;
import com.example.grocerieswizard.di.GroceriesWizardInjector;
import com.example.grocerieswizard.ui.UiMapper;
import com.example.grocerieswizard.ui.model.IngredientUi;
import com.example.grocerieswizard.ui.model.RecipeUi;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeFragment extends Fragment implements AddInterface {

    private RecipeRepository recipeRepository;
    private UiMapper uiMapper;
    private IngredientAdapter ingredientAdapter;
    private List<IngredientUi> ingredientList;
    private Bitmap defaultImageBitmap;
    private RecipeUi recipeUi;
    private static final String TAG = "AddRecipeFragment";
    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GroceriesWizardInjector injector = GroceriesWizardInjector.getInstance();
        recipeRepository = injector.getRecipeRepository();
        uiMapper = injector.getUiMapper();
        ingredientList = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentAddRecipeBinding binding = FragmentAddRecipeBinding.inflate(inflater, container, false);
        ingredientAdapter = new IngredientAdapter(ingredientList);
        ingredientAdapter.setRecyclerViewInterface(this);
        binding.recyclerViewIngredients.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewIngredients.setAdapter(ingredientAdapter);

        Uri defaultImageUri = Uri.parse("android.resource://com.example.grocerieswizard/" + R.drawable.recipe_image_default);


        try {
            defaultImageBitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), defaultImageUri);
        } catch (Exception e) {
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        //create an empty recipe
        recipeUi = new RecipeUi(null, null, null);

        // Check if the fragment is opened for editing
        Bundle arguments = getArguments();
        if (arguments != null) {
            RecipeUi recipeUi = arguments.getParcelable("recipeModel");
            int position = arguments.getInt("position");
            //get old ones to show user
            assert recipeUi != null;
            binding.editRecipeName.setText(recipeUi.getRecipeName());
            binding.editRecipeHowToPrepare.setText(recipeUi.getInstructions());
            ingredientList.addAll(recipeUi.getIngredients());
            //Bitmap selectedImageBitmap = recipeUi.getImageBitmap();
            //, selectedImageBitmap
            ingredientAdapter.changeItem(position);
            //binding.addImage.setImageBitmap(selectedImageBitmap);
        } else {
            binding.addImage.setImageURI(defaultImageUri);
            final Handler mHandler = new Handler();
            binding.editRecipeName.addTextChangedListener(new TextWatcher() {
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
                        String inputText = binding.editRecipeName.getText().toString().trim();
                        Log.d(TAG, "afterTextChanged: input text " + inputText);

                        recipeRepository.searchMeals(inputText, new RepositoryCallback<List<RecipeItem>>() {
                            @Override
                            public void onSuccess(List<RecipeItem> data) {
                                if (data.isEmpty())
                                    return;
                                //TODO:recipe image
                                /*
                                Picasso.get().load(data.get(0).getImageUrl()).resize(150, 150).centerCrop().into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        data.get(0).setImageBitmap(bitmap);
                                        Log.d(TAG, "onBitmapLoaded: success");
                                        Log.d(TAG, "onBitmapLoaded: recipe image bitmap: " + data.get(0).getImageBitmap());
                                        showAlertDialogForFoundRecipe(uiMapper.toRecipeUi(data.get(0)), binding.editRecipeHowToPrepare, binding.addImage,bitmap);
                                        recipeUi.setImageBitmap(bitmap);
                                    }

                                    @Override
                                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                        Log.e(TAG, "onBitmapFailed: ", e);
                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    }
                                });

                                */
                                showAlertDialogForFoundRecipe(uiMapper.toRecipeUi(data.get(0)), binding.editRecipeHowToPrepare, binding.addImage);
                                //,bitmap
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e(TAG, "onError", e);
                            }
                        });
                    }, 1000); // 0.5 saniye
                }
            });

        }

        binding.addIngredientButton.setOnClickListener(v -> {
            IngredientUi newIngredient = new IngredientUi(null, 0, null);
            showAddIngredientDialog(newIngredient);
        });


        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri selectedImageUri = result.getData().getData();
                try {
                    Picasso.get().load(selectedImageUri).resize(150, 150).centerCrop().into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            binding.addImage.setImageBitmap(bitmap);
                            //recipeUi.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            Toast.makeText(requireContext(), "image error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.addImage.setImageBitmap(defaultImageBitmap);
                    //  recipeUi.setImageBitmap(defaultImageBitmap);
                }
            } else {
                binding.addImage.setImageBitmap(defaultImageBitmap);
                //  recipeUi.setImageBitmap(defaultImageBitmap);
            }
        });

        binding.addImage.setOnClickListener(v -> {
            Intent pickImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
            pickImageIntent.setType("image/*");
            pickImageLauncher.launch(pickImageIntent);
        });

        binding.saveRecipeButton.setOnClickListener(v -> {
            String recipeName = binding.editRecipeName.getText().toString();
            String howToPrepare = binding.editRecipeHowToPrepare.getText().toString();

            if (recipeName.isEmpty() || ingredientList.isEmpty() || howToPrepare.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }
            /*if (recipeUi.getImageBitmap() == null) {
                recipeUi.setImageBitmap(defaultImageBitmap);
                Log.d(TAG, "onCreateView: recipe ui image bitmap null");
            }*/

            recipeUi.setRecipeName(recipeName);
            recipeUi.setInstructions(howToPrepare);
            recipeUi.setSwiped(false);
            recipeUi.setIngredients(ingredientList);

            Bundle checkEdit = getArguments();
            //if it comes from edit mode, delete original put edited version as new recipe
            if (checkEdit != null) {
                if (arguments != null) {
                    RecipeUi recipeUi = arguments.getParcelable("recipeModel");
                    if (recipeUi != null) {
                        recipeRepository.deleteRecipe(recipeUi.getId());
                    }
                }
                recipeRepository.insertRecipe(uiMapper.toRecipe(recipeUi));
            } else {
                recipeRepository.insertRecipe(uiMapper.toRecipe(recipeUi));
            }
            getParentFragmentManager().popBackStack();
        });

        binding.dischargeRecipe.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirm Discharge").setMessage("Are you sure you want to discharge this recipe?")
                    .setPositiveButton(R.string.yes, (dialog, which) -> getParentFragmentManager().popBackStack())
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss())
                    .show();
        });
        return binding.getRoot();
    }

    @Override
    public void onItemDelete(IngredientUi ingredientUi) {
        if (ingredientUi != null) {
            recipeRepository.deleteIngredient(uiMapper.toIngredient(ingredientUi));
            ingredientAdapter.removeIngredient(ingredientUi, requireContext());
        }

    }

    @Override
    public void onItemEdit(IngredientUi ingredientUi) {
        if (ingredientUi != null) {
            showEditIngredientDialog(ingredientUi);
        }
    }

    private void showAddIngredientDialog(IngredientUi ingredientUi) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
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

            ingredientUi.setName(name);
            ingredientUi.setUnit(unit);
            ingredientUi.setQuantity(quantity);
            ingredientAdapter.addIngredient(ingredientUi);
            IngredientItem ingredient = uiMapper.toIngredient(ingredientUi);
            recipeRepository.insertIngredient(ingredient);

        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlertDialogForFoundRecipe(RecipeUi recipeUi, TextView howToPrepare, ImageView addImage) {
        //, Bitmap bitmap
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.TitleFoundRecipe);
        builder.setMessage(R.string.MessageFoundRecipe);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            //Log.d(TAG, "showAlert: recipeUi image bitmap: " + recipeUi.getImageBitmap());
            howToPrepare.setText(recipeUi.getInstructions());
            ingredientList.addAll(recipeUi.getIngredients());
            // addImage.setImageBitmap(bitmap);
        });
        builder.setNegativeButton(R.string.no, (dialog, which) -> Log.d(TAG, "showAlertDialogForFoundRecipe: do nothing."));
        builder.show();

    }

    public void showEditIngredientDialog(IngredientUi ingredientUi) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        DialogAddIngredientBinding dialogBinding = DialogAddIngredientBinding.inflate(getLayoutInflater());
        View dialogView = dialogBinding.getRoot();
        builder.setView(dialogView);


        EditText ingredientNameEditText = dialogBinding.ingredientNameEditText;
        EditText ingredientQuantityEditText = dialogBinding.ingredientQuantityEditText;
        EditText ingredientUnitEditText = dialogBinding.ingredientUnitEditText;

        // Pre-fill the dialog's fields with the existing ingredient's information
        ingredientNameEditText.setText(ingredientUi.getName());
        ingredientQuantityEditText.setText(String.valueOf(ingredientUi.getQuantity()));
        ingredientUnitEditText.setText(ingredientUi.getUnit());

        builder.setPositiveButton("Save", (dialog, which) -> {
            // Get the updated values from the dialog's fields
            String name = ingredientNameEditText.getText().toString();
            String quantityStr = ingredientQuantityEditText.getText().toString();
            double quantity = Double.parseDouble(quantityStr);
            String unit = ingredientUnitEditText.getText().toString();

            // Update the ingredient model with the new values
            ingredientUi.setName(name);
            ingredientUi.setQuantity(quantity);
            ingredientUi.setUnit(unit);

            // Find the position of the ingredient model in the list
            int position = -1;
            for (int i = 0; i < ingredientList.size(); i++) {
                if (ingredientList.get(i) == ingredientUi) {
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


}