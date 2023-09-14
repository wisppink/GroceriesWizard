package com.example.grocerieswizard.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.interfaces.AddInterface;
import com.example.grocerieswizard.interfaces.IngredientInterface;
import com.example.grocerieswizard.models.IngredientModel;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private final List<IngredientModel> ingredientList;
    private AddInterface addInterface;
    private IngredientInterface ingredientInterface;

    public IngredientAdapter(List<IngredientModel> ingredientList) {
        this.ingredientList = ingredientList;
    }


    @NonNull
    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientViewHolder holder, int position) {
        IngredientModel ingredient = ingredientList.get(position);
        holder.bindIngredient(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public IngredientModel getItemAtPosition(int position) {
        if (position >= 0 && position < ingredientList.size()) {
            return ingredientList.get(position);
        }
        return null;
    }


    public void setRecyclerViewInterface(AddInterface addInterface) {
        this.addInterface = addInterface;
    }

    public void removeIngredient(IngredientModel ingredientModel) {
        ingredientInterface.removeIngredient(ingredientModel);
    }

    public void addIngredient(IngredientModel ingredientModel) {
        ingredientInterface.addIngredient(ingredientModel);
    }

    public void setIngredientInterface(IngredientInterface ingredientInterface) {
        this.ingredientInterface = ingredientInterface;
    }


    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView ingredientName;
        TextView quantity;
        TextView unit;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredientName);
            quantity = itemView.findViewById(R.id.quantity);
            unit = itemView.findViewById(R.id.unit);

            itemView.setOnLongClickListener(v -> {
                showPopUpMenu(v, getItemAtPosition(getAdapterPosition()));
                return false;
            });
        }

        private void showPopUpMenu(View v, IngredientModel ingredientModel) {
            PopupMenu popupMenu = new PopupMenu(itemView.getContext(), v);
            popupMenu.inflate(R.menu.popup_menu_ingredient); // Create a menu resource file

            // Set click listeners for menu items
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_edit) {// Implement the edit action here
                    if (addInterface != null) {
                        addInterface.onItemEdit(ingredientModel);
                        popupMenu.dismiss();
                    }
                    return true;
                } else if (itemId == R.id.menu_delete) {// Implement the delete action here
                    if (addInterface != null) {
                        addInterface.onItemDelete(ingredientModel);
                        popupMenu.dismiss();
                    }
                    return true;
                }
                return false;
            });

            popupMenu.show();
        }

        public void bindIngredient(IngredientModel ingredient) {
            ingredientName.setText(ingredient.getName());
            quantity.setText(String.valueOf(ingredient.getQuantity()));
            unit.setText(ingredient.getUnit());
        }
    }
}
