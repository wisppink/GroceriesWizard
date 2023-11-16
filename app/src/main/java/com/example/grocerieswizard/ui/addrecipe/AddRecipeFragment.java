package com.example.grocerieswizard.ui.addrecipe;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.data.local.model.RecipeItem;
import com.example.grocerieswizard.databinding.DialogAddIngredientBinding;
import com.example.grocerieswizard.databinding.FragmentAddRecipeBinding;
import com.example.grocerieswizard.di.GroceriesWizardInjector;
import com.example.grocerieswizard.ui.model.IngredientUi;
import com.example.grocerieswizard.ui.model.RecipeUi;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class AddRecipeFragment extends Fragment implements AddInterface, AddRecipeContract.View {
    AddRecipeContract.Presenter presenter;
    private IngredientAdapter ingredientAdapter;
    private static final String TAG = "AddRecipeFragment";
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private String imageStr;
    private static final String BUNDLE_KEY_RECIPE_MODEL = "recipeModel";
    private static final String BUNDLE_KEY_POSITION = "position";
    RecipeUi recipe;
    private ScheduledExecutorService scheduledExecutorService;
    private static final int DELAY_SECONDS = 3;
    private boolean isItYes = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GroceriesWizardInjector injector = GroceriesWizardInjector.getInstance();
        presenter = new AddRecipePresenter(injector.getRecipeRepository(), injector.getUiMapper());
        presenter.bindView(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.bindView(this);
    }

    @Override
    public void onStop() {
        presenter.unbindView();
        super.onStop();
    }

    public static AddRecipeFragment newInstance(RecipeUi recipe, int position) {
        AddRecipeFragment addRecipeFragment = new AddRecipeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY_RECIPE_MODEL, recipe);
        bundle.putInt(BUNDLE_KEY_POSITION, position);
        addRecipeFragment.setArguments(bundle);
        return addRecipeFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentAddRecipeBinding binding = FragmentAddRecipeBinding.inflate(inflater, container, false);
        ingredientAdapter = new IngredientAdapter();
        ingredientAdapter.setRecyclerViewInterface(this);
        binding.recyclerViewIngredients.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewIngredients.setAdapter(ingredientAdapter);
        // Check if the fragment is opened for editing
        Bundle arguments = getArguments();
        if (arguments != null) {
            RecipeUi recipeUi = arguments.getParcelable("recipeModel");
            int position = arguments.getInt("position");
            //get old ones to show user
            assert recipeUi != null;
            binding.editRecipeName.setText(recipeUi.getRecipeName());
            binding.editRecipeHowToPrepare.setText(recipeUi.getInstructions());
            ingredientAdapter.setIngredientList(recipeUi.getIngredients());
            ingredientAdapter.changeItem(position);
        } else {
            binding.addImage.setImageResource(R.drawable.recipe_image_default);
            final Handler mHandler = new Handler();
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            binding.editRecipeName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    mHandler.removeCallbacksAndMessages(null);
                    mHandler.postDelayed(() -> {
                        String inputText = binding.editRecipeName.getText().toString().trim();
                        presenter.searchMeal(inputText);
                    }, 1000); // 1 sec
                    scheduledExecutorService.schedule(() -> {
                        if (isItYes && recipe != null) {
                            requireActivity().runOnUiThread(() -> {
                                binding.editRecipeHowToPrepare.setText(recipe.getInstructions());
                                Picasso.get().load(recipe.getImage())
                                        .resize(150, 150)
                                        .centerCrop()
                                        .into(binding.addImage);
                            });
                        }
                    }, DELAY_SECONDS, TimeUnit.SECONDS);
                }
            });
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        }
        binding.addIngredientButton.setOnClickListener(v -> {
            IngredientUi newIngredient = new IngredientUi(null, 0, null);
            showAddIngredientDialog(newIngredient);
        });
        // Create an empty recipe that allows users to choose an image before entering other recipe details.
        RecipeUi recipeUi = new RecipeUi(null, null, null);
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri selectedImageUri = result.getData().getData();
                if (selectedImageUri != null) {
                    recipeUi.setImage(selectedImageUri.toString());
                }
                try {
                    Picasso.get().load(selectedImageUri).resize(150, 150).centerCrop().into(binding.addImage);
                } catch (Exception e) {
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.addImage.setImageResource(R.drawable.recipe_image_default);
                }
            } else {
                binding.addImage.setImageResource(R.drawable.recipe_image_default);
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

            if (recipeName.isEmpty() || ingredientAdapter.getIngredientList().isEmpty() || howToPrepare.isEmpty()) {
                Toast.makeText(requireContext(), R.string.please_fill_all_fields, Toast.LENGTH_SHORT).show();
                return;
            }
            recipeUi.setRecipeName(recipeName);
            recipeUi.setInstructions(howToPrepare);
            recipeUi.setSwiped(false);
            recipeUi.setIngredients(ingredientAdapter.getIngredientList());
            recipeUi.setImage(imageStr);
            Bundle checkEdit = getArguments();
            //if it comes from edit mode, delete original put edited version as new recipe
            if (checkEdit != null) {
                if (arguments != null) {
                    RecipeUi delRecipeUi = arguments.getParcelable("recipeModel");
                    if (delRecipeUi != null) {
                        presenter.deleteRecipe(delRecipeUi);
                    }
                }
                presenter.insertRecipe(recipeUi);
            } else {
                presenter.insertRecipe(recipeUi);
            }
            getParentFragmentManager().popBackStack();
        });
        binding.dischargeRecipe.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle(R.string.confirm_discharge).setMessage(R.string.discharge_recipe_question).setPositiveButton(R.string.yes, (dialog, which) -> getParentFragmentManager().popBackStack()).setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss()).show();
        });
        return binding.getRoot();
    }

    @Override
    public void onItemDelete(IngredientUi ingredientUi) {
        if (ingredientUi != null) {
            presenter.deleteIngredient(ingredientUi);
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
            presenter.insertIngredient(ingredientUi);
            ingredientAdapter.addIngredient(ingredientUi);

        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void showAlertDialogForFoundRecipe(RecipeItem recipeItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.TitleFoundRecipe);
        builder.setMessage(R.string.MessageFoundRecipe);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> presenter.PositiveButton(recipeItem));
        builder.setNegativeButton(R.string.no, (dialog, which) -> {
            Log.d(TAG, "showAlertDialogForFoundRecipe: do nothing.");
            presenter.negativeButton();
        });
        builder.show();
    }

    public void onNegativeButtonCalled() {
        Log.d(TAG, "onNegativeButtonCalled: cancelled");
    }

    public void onPositiveButtonCalled(RecipeUi recipeUi) {
        ingredientAdapter.setIngredientList(recipeUi.getIngredients());
        recipe = recipeUi;
        isItYes = true;
        imageStr = recipeUi.getImage();
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

        builder.setPositiveButton(R.string.save, (dialog, which) -> {
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
            for (int i = 0; i < ingredientAdapter.getIngredientList().size(); i++) {
                if (ingredientAdapter.getIngredientList().get(i) == ingredientUi) {
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