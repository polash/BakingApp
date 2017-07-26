package com.sksanwar.bakingapp.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.sksanwar.bakingapp.Fragments.DetailActivityFragment;
import com.sksanwar.bakingapp.Fragments.StepsDetailActivityFragment;
import com.sksanwar.bakingapp.R;

import static com.sksanwar.bakingapp.Activities.MainActivity.isTablet;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (isTablet) {
                DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.detail_activity_layout, detailActivityFragment)
                        .commit();

                StepsDetailActivityFragment stepsDetailActivityFragment = new StepsDetailActivityFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.steps_details_frame, stepsDetailActivityFragment)
                        .commit();
            } else {
                DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.detail_activity_layout, detailActivityFragment)
                        .commit();
            }
        }
    }
}
