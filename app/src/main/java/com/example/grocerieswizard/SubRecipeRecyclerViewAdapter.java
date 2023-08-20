package com.example.grocerieswizard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

public class SubRecipeRecyclerViewAdapter extends RecyclerView.Adapter<SubRecipeRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<String> values;

    private List<Boolean> checkBoxes;
    private OnSubItemCheckListener onSubItemCheckListener;

    public SubRecipeRecyclerViewAdapter(Context context, List<String> values) {
        this.context = context;
        this.values = values;

        // Initialize checkbox states for each item
        checkBoxes = new ArrayList<>(values.size());
        for (int i = 0; i < values.size(); i++) {
            checkBoxes.add(false);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_recipe, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String value = values.get(position);
        String[] parts = value.split(" ");

        // Set the text values for each view holder element
        holder.ingredientTitle.setText(parts[0]); // Recipe Name
        holder.ingredientQuantity.setText(parts[1]); // Quantity
        holder.ingredientUnit.setText(parts[2]); // Unit

        // Handle checkbox state changes
        holder.ingredientCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update the checkbox state for the current item
            checkBoxes.set(position, isChecked);

            // Check if all checkboxes are checked
            boolean isAllChecked = true;
            for (boolean checked : checkBoxes) {
                if (!checked) {
                    isAllChecked = false;
                    break;
                }
            }
            // Notify the listener about checkbox changes
            if (onSubItemCheckListener != null) {
                onSubItemCheckListener.onSubItemChecked(isAllChecked);
            }
        });

    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    // Check if an item's checkbox is checked
    public boolean isItemChecked(int position) {
        if (checkBoxes != null && position >= 0 && position < checkBoxes.size()) {
            return checkBoxes.get(position);
        }
        return false;
    }

    public String getValueAtPosition(int i) {
        return values.get(i);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientTitle;
        TextView ingredientQuantity;
        TextView ingredientUnit;
        CheckBox ingredientCB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientTitle = itemView.findViewById(R.id.sub_ingredient_name);
            ingredientQuantity = itemView.findViewById(R.id.sub_ingredient_quantity);
            ingredientUnit = itemView.findViewById(R.id.sub_ingredient_unit);
            ingredientCB = itemView.findViewById(R.id.sub_ingredient_checkbox);
        }
    }

    // Set a listener for checkbox changes
    public void setOnSubItemCheckListener(OnSubItemCheckListener listener) {
        this.onSubItemCheckListener = listener;
    }

    // Interface for notifying checkbox changes
    public interface OnSubItemCheckListener {
        void onSubItemChecked(boolean isAllChecked);
    }

}
