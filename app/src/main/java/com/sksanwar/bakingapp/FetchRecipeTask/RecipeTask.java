package com.sksanwar.bakingapp.FetchRecipeTask;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.sksanwar.bakingapp.Pojo.Ingredients;
import com.sksanwar.bakingapp.Pojo.Recipe;
import com.sksanwar.bakingapp.Pojo.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * A class that checks the connection with HTTP and parse the Json in Proper way
 */

public class RecipeTask extends AsyncTask<Void, Void, ArrayList<Recipe>> {

    private static final String LOG_TAG = RecipeTask.class.getSimpleName();

    ArrayList<Recipe> recipeArrayList = new ArrayList<>();
    private AsyncListner listner;

    //default constructor
    public RecipeTask(AsyncListner listner) {
        this.listner = listner;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //getiing data from json mehod
    private ArrayList<Recipe> getRecipeDataFromJson(String baking)
            throws JSONException {

        //CONSTANTS for the JSON Objects
        final String RECIPE_ID = "id";
        final String RECIPE_NAME = "name";
        final String RECIPE_INGREDIENTS = "ingredients";
        final String INGREDIENTS_QUANTIFY = "quantity";
        final String INGREDIENTS_MEASURE = "measure";
        final String INGREDIENTS_INGREDIENT = "ingredient";
        final String RECIPE_STEPS = "steps";
        final String STEPS_ID = "id";
        final String STEPS_SHORT_DESCRP = "shortDescription";
        final String STEPS_DESCRP = "description";
        final String STEPS_VIDEO_PATH = "videoURL";
        final String STEPS_THUMBNAIL = "thumbnailURL";
        final String RECIPE_SERVINGS = "servings";
        final String RECIPE_IMAGE = "image";

        //JSON RecipeArray
        JSONArray recipeArray = new JSONArray(baking);

        //Iterate through the array to get the data
        for (int i = 0; i < recipeArray.length(); i++) {
            JSONObject recipeJsonObject = recipeArray.optJSONObject(i);

            String recipeId = recipeJsonObject.getString(RECIPE_ID);
            String recipeName = recipeJsonObject.getString(RECIPE_NAME);
            Integer recipeServings = recipeJsonObject.getInt(String.valueOf(RECIPE_SERVINGS));
            String recipeImage = recipeJsonObject.getString(RECIPE_IMAGE);
            //Log.v(LOG_TAG, recipeId +" "+ recipeName +" "+ recipeServings +" "+ recipeImage);

            //List Creation
            ArrayList<Ingredients> ingredientList = new ArrayList<>();
            ArrayList<Step> stepsList = new ArrayList<>();

            //Ingredients Json Array
            JSONArray ingredientJsonArray = recipeJsonObject.optJSONArray(RECIPE_INGREDIENTS);

            //Iterate through the Ingredient Array
            for (int j = 0; j < ingredientJsonArray.length(); j++) {
                JSONObject ingredientsObjects = ingredientJsonArray.getJSONObject(j);

                String ingredientQuantity = ingredientsObjects.getString(INGREDIENTS_QUANTIFY);
                String ingredientMeasure = ingredientsObjects.getString(INGREDIENTS_MEASURE);
                String ingredientIngredients = ingredientsObjects.getString(INGREDIENTS_INGREDIENT);
                //Log.v(LOG_TAG, ingredientQuantity+" "+ingredientMeasure+" "+ingredientIngredients);

                //Creating Ingredients Object
                Ingredients ingredients = new Ingredients(ingredientQuantity, ingredientMeasure, ingredientIngredients);

                ingredientList.add(ingredients);
            }

            //Steps Json Array
            JSONArray stepsJsonArray = recipeJsonObject.optJSONArray(RECIPE_STEPS);

            //Iterate through the Step Array
            for (int k = 0; k < stepsJsonArray.length(); k++) {
                JSONObject stepObject = stepsJsonArray.getJSONObject(k);

                String stepsId = stepObject.getString(STEPS_ID);
                String stepsSdescrp = stepObject.getString(STEPS_SHORT_DESCRP);
                String stepsDescrp = stepObject.getString(STEPS_DESCRP);
                String stepsVpath = stepObject.getString(STEPS_VIDEO_PATH);
                String stepsThumnail = stepObject.getString(STEPS_THUMBNAIL);
                //Log.v(LOG_TAG, stepsId+" "+stepsDescrp+" "+stepsSdescrp+" "+stepsVpath+" "+stepsThumnail);

                //Creating Step Object
                Step stepsObj = new Step(stepsId, stepsSdescrp, stepsDescrp, stepsVpath, stepsThumnail);

                stepsList.add(stepsObj);
            }

            //Creating new Recipe Object to hold the data
            Recipe recipeObj = new Recipe(recipeId, recipeName, ingredientList, stepsList, recipeServings, recipeImage);

            recipeArrayList.add(recipeObj);
        }
        return recipeArrayList;
    }

    //Background task
    @Override
    protected ArrayList<Recipe> doInBackground(Void... params) {

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        String recipeJsonString = null;

        try {
            //Recipe Base Url
            final String BASE_URL = "https://go.udacity.com/android-baking-app-json";

            //Creating the Uri
            Uri builtUri = Uri.parse(BASE_URL).buildUpon().build();

            //Creating URL
            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG, "Url Created : " + url.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                recipeJsonString = readFromStream(inputStream);
            } else {
                Log.d(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            // If the code didn't successfully get the Recipe data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (final IOException e) {
                    Log.d(LOG_TAG, "Error closing inputStream", e);
                }
            }
        }

        try {
            return getRecipeDataFromJson(recipeJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Recipe> recipes) {
        super.onPostExecute(recipes);
        listner.returnRecipeList(recipes);
    }
}