package com.example.grocerieswizard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.RecipeViewHolder> {

    private ArrayList<RecipeModel> recipeList = new ArrayList<>();
    private  RecyclerViewInterface recyclerViewInterface;

    @NonNull
    @Override
    public RecipeRecyclerViewAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false), recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeRecyclerViewAdapter.RecipeViewHolder holder, int position) {
        RecipeModel recipeModel = recipeList.get(holder.getAdapterPosition());
        holder.bind(recipeModel);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void addRecipe(RecipeModel recipe) {
        recipeList.add(recipe);
        notifyItemInserted(recipeList.size()-1);
    }

    public RecipeModel getItemAtPosition(int position) {
        if (position >= 0 && position < recipeList.size()) {
            return recipeList.get(position);
        }
        return null;
    }
    public void setRecyclerViewInterface(RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
    }


    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        public RecipeViewHolder(View itemView, RecyclerViewInterface  recyclerViewInterface) {
            super(itemView);
            title = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(v -> {
                if (recyclerViewInterface != null) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemClick(pos);
                    }

                }
            });
        }

        public void bind(RecipeModel recipeModel) {
            title.setText(recipeModel.getRecipeName());
        }
    }
}
