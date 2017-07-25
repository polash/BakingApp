package com.sksanwar.bakingapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sksanwar.bakingapp.Pojo.Recipe;
import com.sksanwar.bakingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecipeAdapter
 */

public class RecipeAdapter extends
        RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    final private ListItemClickListener mOnClickListener;
    private ArrayList<Recipe> recipeList;

    public RecipeAdapter(ListItemClickListener listener, ArrayList<Recipe> recipes) {
        mOnClickListener = listener;
        this.recipeList = recipes;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recipe_items_card, viewGroup, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (null == recipeList) return 0;
        return recipeList.size();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    //Viewholder Class
    class RecipeViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        @BindView(R.id.recipe_image_view)
        ImageView icon;

        @BindView(R.id.recipe_name)
        TextView recipeNameTextView;

        @BindView(R.id.recipe_serving)
        TextView servingsTextview;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        //Binding the view
        public void bind(int position) {
            if (!recipeList.isEmpty()) {
                if (recipeList.get(position).getImage().isEmpty()) {
                    icon.setImageResource(R.drawable.recipe_icon);
                } else {
                    Picasso.with(itemView.getContext()).load(recipeList.get(position).getImage()).into(icon);
                }
                recipeNameTextView.setText(recipeList.get(position).getName());
                servingsTextview.setText(itemView.getContext().getString(R.string.servings) + " " + recipeList.get(position).getServings());
            }
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}