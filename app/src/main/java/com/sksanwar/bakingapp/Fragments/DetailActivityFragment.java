package com.sksanwar.bakingapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sksanwar.bakingapp.Activities.StepsDetailsActivity;
import com.sksanwar.bakingapp.Adapters.IngredientAdapter;
import com.sksanwar.bakingapp.Adapters.StepsAdapter;
import com.sksanwar.bakingapp.Pojo.Ingredients;
import com.sksanwar.bakingapp.Pojo.Recipe;
import com.sksanwar.bakingapp.Pojo.Step;
import com.sksanwar.bakingapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sksanwar.bakingapp.Activities.MainActivity.isTablet;

/**
 * Created by sksho on 22-Jul-17.
 */

public class DetailActivityFragment extends
        Fragment implements StepsAdapter.ListItemClickListener {

    //Binding view with ButterKnife
    @BindView(R.id.ingredient_list)
    RecyclerView ingredientRecyclerView;
    @BindView(R.id.recipe_details_steps)
    RecyclerView stepsRecyclerView;

    //List Creation
    private ArrayList<Step> stepslist;
    private ArrayList<Recipe> recipeList;
    private ArrayList<Ingredients> ingredientsList;
    private int index;

    //default construct for fragment which is must
    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, rootView);

        //setting layoutmanager
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //getting extra data into recipe list with the position
        recipeList = getActivity().getIntent().getParcelableArrayListExtra(RecipeFragment.RECIPE_LIST);
        index = getActivity().getIntent().getExtras().getInt(RecipeFragment.POSITION);

        //getting the steps from the recipelist into stepslist
        stepslist = recipeList.get(index).getSteps();
        ingredientsList = recipeList.get(index).getIngredients();

        //setting the title to the toolbar
        getActivity().setTitle(recipeList.get(index).getName());

        IngredientAdapter ingredientAdapter = new IngredientAdapter(ingredientsList);
        ingredientRecyclerView.setAdapter(ingredientAdapter);

        //step adapter instance to set the adapter with stepsRecyclerView Recyclerview
        StepsAdapter stepsAdapter = new StepsAdapter(this, stepslist);
        stepsRecyclerView.setAdapter(stepsAdapter);


        return rootView;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (!isTablet) {
            Intent intent = new Intent(getActivity(), StepsDetailsActivity.class);
            intent.putParcelableArrayListExtra(RecipeFragment.RECIPE_LIST, stepslist);
            intent.putExtra(RecipeFragment.POSITION, clickedItemIndex);
            startActivity(intent);
        } else {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            StepsDetailActivityFragment stepsDetailActivityFragment = new StepsDetailActivityFragment();
            stepsDetailActivityFragment.index = clickedItemIndex;
            fragmentManager.beginTransaction()
                    .add(R.id.steps_details_frame, stepsDetailActivityFragment)
                    .commit();
        }
    }
}
