package com.example.grocerieswizard.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.RecipeDatabaseHelper;
import com.example.grocerieswizard.RecyclerViewInterface;
import com.example.grocerieswizard.models.RecipeModel;

import java.util.ArrayList;

public class FavRecyclerViewAdapter extends RecyclerView.Adapter<FavRecyclerViewAdapter.FavViewHolder> {

    private final RecipeDatabaseHelper dbHelper;
    private ArrayList<RecipeModel> favList = new ArrayList<>();
    private final Context context;
    private RecyclerViewInterface recyclerViewInterface;
    private static final String TAG = "FavRecyclerViewAdapter";

    public FavRecyclerViewAdapter(Context context) {
        this.context = context;
        dbHelper = new RecipeDatabaseHelper(context);
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_item_row, parent, false);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        RecipeModel recipeModel = favList.get(position);
        holder.bind(recipeModel);

    }


    @Override
    public int getItemCount() {
        return favList.size();
    }

    public void setRecyclerViewInterface(RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
    }


    public void setFavList(ArrayList<RecipeModel> recipes) {
        this.favList = recipes;
    }

    public class FavViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView favButton;
        private CardView background;

        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.fav_recipe_title);
            favButton = itemView.findViewById(R.id.unFav);
            background = itemView.findViewById(R.id.cardView);

            favButton.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setMessage(R.string.unfav_recipe_dialog_ask)
                        .setPositiveButton(R.string.yes, (dialog, which) -> {
                            int adapterPosition = getAdapterPosition();
                            if (adapterPosition != RecyclerView.NO_POSITION) {
                                RecipeModel recipeModel = favList.get(adapterPosition);
                                dbHelper.deleteRecipeFav(recipeModel.getId());
                                favList.remove(adapterPosition);
                                notifyItemRemoved(adapterPosition);
                                notifyItemRangeChanged(adapterPosition, favList.size());
                                recipeModel.setFavorite(false);
                            }
                        })
                        .setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
            });

        }

        public void bind(RecipeModel recipeModel) {
            title.setText(recipeModel.getRecipeName());
            recipeModel.setFavorite(true);

            if(dbHelper.isRecipeSelected(recipeModel.getId())){
                recipeModel.setSelected(true);
                background.setBackgroundColor(itemView.getResources().getColor(R.color.gray));
            }
            else {
                background.setBackgroundColor(Color.TRANSPARENT);
            }


            itemView.setOnClickListener(v -> {
                Log.d(TAG, "bind: clicked!");
                if (!recipeModel.isSelected()) {
                    recipeModel.setSelected(true);
                    background.setBackgroundColor(itemView.getResources().getColor(R.color.gray));
                    dbHelper.insertSelectedRecipe(recipeModel.getId());
                } else {
                    itemView.setBackgroundColor(Color.TRANSPARENT);
                }
                notifyDataSetChanged();
            });

        }
    }
}
