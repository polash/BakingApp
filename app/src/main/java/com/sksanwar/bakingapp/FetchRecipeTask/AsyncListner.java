package com.sksanwar.bakingapp.FetchRecipeTask;

import com.sksanwar.bakingapp.Pojo.Recipe;

import java.util.ArrayList;

/**
 * An Interface to pass the recipelist data from Json Parsing class
 */

public interface AsyncListner {
    void returnRecipeList(ArrayList<Recipe> recipeList);
}
