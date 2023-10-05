package com.example.grocerieswizard.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.databinding.RecyclerViewMenuBinding;
import com.example.grocerieswizard.databinding.RecyclerViewRowBinding;
import com.example.grocerieswizard.models.RecipeModel;

import java.util.ArrayList;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SWIPE = 1;
    private static final int VIEW_TYPE_ROW = 0;
    private final ArrayList<RecipeModel> recipeList = new ArrayList<>();
    private RecipeInterface recipeInterface;
    private final Context context;


    public RecipeRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        RecipeModel recipeModel = recipeList.get(position);
        return recipeModel.isSwiped() ? VIEW_TYPE_SWIPE : VIEW_TYPE_ROW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;

        if (viewType == VIEW_TYPE_ROW) {
            RecyclerViewRowBinding rowBinding = RecyclerViewRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            viewHolder = new RowViewHolder(rowBinding, recipeInterface);
        } else {
            RecyclerViewMenuBinding menuBinding = RecyclerViewMenuBinding.inflate(inflater, parent, false);
            viewHolder = new MenuViewHolder(menuBinding, recipeInterface);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecipeModel item = recipeList.get(position);

        if (holder instanceof RowViewHolder) {
            RowViewHolder rowViewHolder = (RowViewHolder) holder;
            rowViewHolder.bind(item);
        } else if (holder instanceof MenuViewHolder) {
            MenuViewHolder menuViewHolder = (MenuViewHolder) holder;
            menuViewHolder.bind(item);
        }
    }


    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public RecipeModel editRecipe(int position, RecipeModel editedRecipe) {
        if (position >= 0 && position < recipeList.size()) {
            RecipeModel oldRecipe = getItemAtPosition(position);
            oldRecipe.setRecipeName(editedRecipe.getRecipeName());
            oldRecipe.setImageBitmap(editedRecipe.getImageBitmap());
            String TAG = "RecipeAdapter";
            Log.d(TAG, "editRecipe: editedRecipe" + editedRecipe.getImageBitmap());
            Log.d(TAG, "editRecipe: oldRecipe" + oldRecipe.getImageBitmap());

            oldRecipe.setInstructions(editedRecipe.getInstructions());
            oldRecipe.setIngredients(editedRecipe.getIngredients());
            editedRecipe.setSwiped(false);
            int updatedRows = recipeInterface.updateRecipe(oldRecipe);
            if (updatedRows > 0) {
                notifyItemChanged(position);
            } else
                Toast.makeText(context,
                        "couldn't edited:" + oldRecipe.getRecipeName(),
                        Toast.LENGTH_SHORT).show();
        }

        return editedRecipe;
    }


    // Add a new recipe to the list and notify the adapter
    public void addRecipe(RecipeModel recipe) {
        recipeList.add(recipe);
        notifyItemInserted(recipeList.size() - 1);
        recipeInterface.insertRecipe(recipe);

    }

    public RecipeModel getItemAtPosition(int position) {
        if (position >= 0 && position < recipeList.size()) {
            return recipeList.get(position);
        }
        return null;
    }

    // Remove a recipe from the list and notify the adapter
    public void removeRecipe(RecipeModel recipeModel) {
        // Find the position of the recipe in the list
        recipeModel.setSwiped(false);
        int pos = recipeList.indexOf(recipeModel);
        notifyItemChanged(pos);
        recipeList.remove(pos);
        notifyItemRemoved(pos);
        recipeInterface.deleteRecipe(recipeModel.getId());
    }

    public void removeRecipeAtPosition(int position) {
        if (position >= 0 && position < recipeList.size()) {

            RecipeModel removedRecipe = getItemAtPosition(position);
            if (removedRecipe.isSelected()) {
                removedRecipe.setSelected(false);
                notifyItemChanged(position);
            }
            recipeInterface.deleteRecipe(removedRecipe.getId());
            recipeList.remove(position);
            notifyItemRemoved(position);
        }
    }


    public void setRecyclerViewInterface(RecipeInterface recipeInterface) {
        this.recipeInterface = recipeInterface;
    }

    public void setRecipeList(ArrayList<RecipeModel> rList) {
        for (RecipeModel recipeModel : rList) {
            recipeList.add(recipeModel);
            notifyItemInserted(recipeList.size() - 1);
        }

    }

    public void itemChanged(int position) {
        notifyItemChanged(position);
    }


    public static class RowViewHolder extends RecyclerView.ViewHolder {
        RecipeInterface recipeInterface;
        TextView nameTV;
        ImageButton favIcon;
        ImageView recipeImage;

        public RowViewHolder(RecyclerViewRowBinding binding, RecipeInterface recipeInterface) {
            super(binding.getRoot());
            nameTV = binding.textView;
            favIcon = binding.favIcon;
            recipeImage = binding.defaultCardRecipeImage;
            this.recipeInterface = recipeInterface;

            binding.getRoot().setOnClickListener(
                    v -> recipeInterface.onItemClick(getAdapterPosition())
            );

            itemView.setOnLongClickListener(v -> true);
        }

        public void bind(RecipeModel recipeModel) {
            nameTV.setText(recipeModel.getRecipeName());
            recipeImage.setImageBitmap(recipeModel.getImageBitmap());
            if (recipeModel.getImageBitmap() != null) {
                recipeImage.setImageBitmap(recipeModel.getImageBitmap());
            } else {
                recipeImage.setImageResource(R.drawable.recipe_image_default);
            }

            boolean isFavorite = recipeInterface.isRecipeFavorite(recipeModel.getId());
            if (isFavorite) {
                // Set the favorite icon if it is favorite
                favIcon.setImageResource(R.drawable.baseline_favorite_24);
            } else {
                // Set the non-favorite icon if it is not favorite
                favIcon.setImageResource(R.drawable.baseline_unfavorite_border_24);
            }
            if (!recipeModel.isSelected()) {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.transparent));
            }
        }
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView deleteIcon, editIcon, shareIcon;
        RecipeInterface recipeInterface;

        public MenuViewHolder(RecyclerViewMenuBinding binding, RecipeInterface recipeInterface) {
            super(binding.getRoot());
            deleteIcon = binding.deleteIcon;
            editIcon = binding.editIcon;
            shareIcon = binding.shareIcon;
            this.recipeInterface = recipeInterface;
        }

        public void bind(RecipeModel recipeModel) {

            if (!recipeModel.isSelected()) {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.transparent));
            }
            editIcon.setOnClickListener(v -> recipeInterface.onItemEdit(recipeModel, getAdapterPosition()));

            shareIcon.setOnClickListener(v -> { // TODO: Handle share icon click
                Toast.makeText(itemView.getContext(), "Share icon clicked", Toast.LENGTH_SHORT).show();
            });

            deleteIcon.setOnClickListener(v -> recipeInterface.onItemDelete(getAdapterPosition()));

        }
    }
}
