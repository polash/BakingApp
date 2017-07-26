package com.sksanwar.bakingapp.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.sksanwar.bakingapp.Fragments.RecipeFragment;
import com.sksanwar.bakingapp.R;

public class MainActivity extends AppCompatActivity {
    public static boolean isTablet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            if (findViewById(R.id.tablet_view) != null) {
                isTablet = true;
                FragmentManager fragmentManager = getSupportFragmentManager();
                RecipeFragment recipeFragment = new RecipeFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.tablet_view, recipeFragment)
                        .commit();
            } else {
                FragmentManager fragmentManager = getSupportFragmentManager();
                RecipeFragment recipeFragment = new RecipeFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.phone_view, recipeFragment)
                        .commit();
            }
        }
    }

}