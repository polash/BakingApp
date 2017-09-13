package com.sksanwar.bakingapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sksanwar.bakingapp.Activities.DetailActivity;
import com.sksanwar.bakingapp.Adapters.RecipeAdapter;
import com.sksanwar.bakingapp.FetchRecipeTask.AsyncListner;
import com.sksanwar.bakingapp.FetchRecipeTask.RecipeTask;
import com.sksanwar.bakingapp.Pojo.Recipe;
import com.sksanwar.bakingapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Recipe Fragment class
 */

public class RecipeFragment extends Fragment implements AsyncListner,
        RecipeAdapter.ListItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String RECIPE_LIST = "recipe_list";
    public static final String POSITION = "position";

    @BindView(R.id.rv_recipe)
    RecyclerView recyclerView;
    @BindView(R.id.no_network)
    TextView no_network;
    @BindView(R.id.swip_to_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<Recipe> recipeList;
    private RecipeAdapter adapter;


    //Default Fragment Constructor
    public RecipeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        no_network.setVisibility(View.GONE);
        swipeRefreshLayout.setOnRefreshListener(this);

        recipeDownloadfromJson();
        networkCheck();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Checks if the savedinsancestate is not null
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(RECIPE_LIST)) {
                recipeList = savedInstanceState.getParcelableArrayList(RECIPE_LIST);
            }
        }

        //this condition checks if the recipeList is null thn run the task
        if (recipeList == null) {
            RecipeTask recipeTask = new RecipeTask(this);
            recipeTask.execute();
        } else {
            loadViews(recipeList);
        }
    }

    //Method for loading the Views
    public void loadViews(ArrayList<Recipe> recipeArrayList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecipeAdapter(this, recipeArrayList);
        swipeRefreshLayout.setRefreshing(false);
        recyclerView.setAdapter(adapter);
    }

    //Network Checks
    private boolean networkCheck() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    //Getting recipeList from the RecipeTask
    @Override
    public void returnRecipeList(ArrayList<Recipe> recipes) {
        recipeList = recipes;
        loadViews(recipes);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_LIST, recipeList);
    }

    //Upon click call the activity and pass the extra data with the intent
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putParcelableArrayListExtra(RECIPE_LIST, recipeList);
        intent.putExtra(POSITION, clickedItemIndex);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        recipeDownloadfromJson();
    }

    private void recipeDownloadfromJson() {
        if (networkCheck()) {
            swipeRefreshLayout.setRefreshing(true);
            new RecipeTask(this).execute();
        } else {
            no_network.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}

