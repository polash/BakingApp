package com.sksanwar.bakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sksanwar.bakingapp.Pojo.Ingredients;
import com.sksanwar.bakingapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for Ingredient
 */

public class IngredientAdapter extends
        RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private ArrayList<Ingredients> ingredientsList;


    public IngredientAdapter(ArrayList<Ingredients> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.ingredient_list_item_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToparentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToparentImmediately);
        IngredientViewHolder viewHolder = new IngredientViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredient)
        TextView ingredient;
        @BindView(R.id.quantity)
        TextView quantity;
        @BindView(R.id.measure)
        TextView measure;


        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void onBind(int position) {
            ingredient.setText(ingredientsList.get(position).getIngredient());
            quantity.setText(String.valueOf(ingredientsList.get(position).getQuantity()));
            measure.setText(ingredientsList.get(position).getMeasure());
        }
    }
}
