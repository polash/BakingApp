package com.sksanwar.bakingapp.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.sksanwar.bakingapp.Fragments.StepsDetailActivityFragment;
import com.sksanwar.bakingapp.R;

import static com.sksanwar.bakingapp.Activities.MainActivity.isTablet;

public class StepsDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (isTablet) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepsDetailActivityFragment stepsDetailActivityFragment = new StepsDetailActivityFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.steps_details_frame, stepsDetailActivityFragment)
                    .commit();
        }
    }
}
