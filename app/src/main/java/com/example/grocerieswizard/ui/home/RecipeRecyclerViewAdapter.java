package com.example.grocerieswizard.ui.home;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.databinding.RecyclerViewMenuBinding;
import com.example.grocerieswizard.databinding.RecyclerViewRowBinding;
import com.example.grocerieswizard.ui.model.RecipeUi;

import java.util.ArrayList;
import java.util.List;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SWIPE = 1;
    private static final int VIEW_TYPE_ROW = 0;
    private final ArrayList<RecipeUi> recipeUiList = new ArrayList<>();
    private RecipeInterface recipeInterface;


    public RecipeRecyclerViewAdapter() {
    }

    @Override
    public int getItemViewType(int position) {
        RecipeUi recipeUi = recipeUiList.get(position);
        return recipeUi.isSwiped() ? VIEW_TYPE_SWIPE : VIEW_TYPE_ROW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ROW) {
            RecyclerViewRowBinding rowBinding = RecyclerViewRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            RecyclerView.ViewHolder rowHolder = new RowViewHolder(rowBinding, recipeInterface);

            rowBinding.getRoot().setOnClickListener(
                    v -> recipeInterface.onItemClick(recipeUiList.get(rowHolder.getAdapterPosition()))
            );
            rowBinding.favIcon.setOnClickListener(v -> {
                int position = rowHolder.getAdapterPosition();
                RecipeUi recipeUi = recipeUiList.get(position);
                if (position != RecyclerView.NO_POSITION) {
                    recipeInterface.toggleFavoriteRecipe(recipeUi);
                }
            });

            rowBinding.addCart.setOnClickListener(v -> {
                int position = rowHolder.getAdapterPosition();
                RecipeUi recipeUi = recipeUiList.get(position);
                if (position != RecyclerView.NO_POSITION) {
                   recipeInterface.toggleCartRecipe(recipeUi);
                }
            });

            return rowHolder;

        } else {
            RecyclerViewMenuBinding menuBinding = RecyclerViewMenuBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            RecyclerView.ViewHolder menuHolder = new MenuViewHolder(menuBinding);

            menuBinding.editIcon.setOnClickListener(v -> recipeInterface.onItemEdit(recipeUiList.get(menuHolder.getAdapterPosition())));
            menuBinding.shareIcon.setOnClickListener(v -> {
                recipeInterface.onItemShare(recipeUiList.get((menuHolder.getAdapterPosition())));
                Toast.makeText(menuBinding.getRoot().getContext(), "Share icon clicked", Toast.LENGTH_SHORT).show();
            });
            menuBinding.deleteIcon.setOnClickListener(v -> recipeInterface.onItemDelete(recipeUiList.get(menuHolder.getAdapterPosition())));

            return menuHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecipeUi item = recipeUiList.get(position);
        if (holder instanceof RowViewHolder) {
            RowViewHolder rowViewHolder = (RowViewHolder) holder;
            rowViewHolder.bind(item);
        }
    }


    @Override
    public int getItemCount() {
        return recipeUiList.size();
    }

    public RecipeUi getItemAtPosition(int position) {
        if (position >= 0 && position < recipeUiList.size()) {
            return recipeUiList.get(position);
        }
        return null;
    }

    // Remove a recipe from the list and notify the adapter
    public void removeRecipe(RecipeUi recipeUi) {
        int pos = recipeUiList.indexOf(recipeUi);
        notifyItemChanged(pos);
        recipeUiList.remove(pos);
        notifyItemRemoved(pos);
    }

    public void setRecyclerViewInterface(RecipeInterface recipeInterface) {
        this.recipeInterface = recipeInterface;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setRecipeList(List<RecipeUi> rList) {
        recipeUiList.clear();
        recipeUiList.addAll(rList);
        notifyDataSetChanged();
    }

    public void itemChanged(int position) {
        notifyItemChanged(position);
    }

    public int getPositionForRecipe(RecipeUi recipe) {
        for (int i = 0; i < recipeUiList.size(); i++) {
            if (recipeUiList.get(i).getId() == recipe.getId()) {
                return i;
            }
        }
        return -1;
    }

    public static class RowViewHolder extends RecyclerView.ViewHolder {
        RecipeInterface recipeInterface;
        RecyclerViewRowBinding binding;

        public void setBinding(RecyclerViewRowBinding binding) {
            this.binding = binding;
        }

        public RowViewHolder(RecyclerViewRowBinding binding, RecipeInterface recipeInterface) {
            super(binding.getRoot());
            setBinding(binding);
            this.recipeInterface = recipeInterface;

        }

        public void bind(RecipeUi recipeUi) {
            binding.textView.setText(recipeUi.getRecipeName());
            if (recipeUi.isFav()) {
                binding.favIcon.setImageResource(R.drawable.baseline_favorite_24);
            } else {
                binding.favIcon.setImageResource(R.drawable.baseline_unfavorite_border_24);
            }
            /*if (recipeUi.getImageBitmap() != null) {
                binding.defaultCardRecipeImage.setImageBitmap(recipeUi.getImageBitmap());
            } else {
                binding.defaultCardRecipeImage.setImageResource(R.drawable.recipe_image_default);
            }*/

            if (recipeUi.isCart()) {
                binding.addCart.setImageResource(R.drawable.baseline_remove_shopping_cart_24);

            } else {
                binding.addCart.setImageResource(R.drawable.add_cart);

            }
        }
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        public MenuViewHolder(RecyclerViewMenuBinding binding) {
            super(binding.getRoot());
        }
    }
}