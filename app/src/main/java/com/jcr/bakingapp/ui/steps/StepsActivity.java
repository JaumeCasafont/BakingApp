package com.jcr.bakingapp.ui.steps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.jcr.bakingapp.R;
import com.jcr.bakingapp.ui.detail.StepDetailActivity;
import com.jcr.bakingapp.ui.detail.StepDetailFragment;

import static com.jcr.bakingapp.ui.recipes.RecipesListActivity.EXTRA_RECIPE_ID;

public class StepsActivity extends AppCompatActivity implements StepsFragment.OnStepClickCallback {

    public static final String EXTRA_STEP_ID = "step_id";

    private int mRecipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        mRecipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, 0);

        if (getResources().getBoolean(R.bool.isTablet)) {
            showStepDetailFragment(0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStepSelected(int stepId) {
        if (getResources().getBoolean(R.bool.isTablet)) {
            replaceStepDetailFragment(stepId);
        } else {
            launchStepDetailActivity(stepId);
        }
    }

    private void showStepDetailFragment(int stepId) {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setStep(stepId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.detail_container, stepDetailFragment)
                .commit();
    }

    private void replaceStepDetailFragment(int stepId) {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setStep(stepId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_container, stepDetailFragment)
                .commit();
    }

    private void launchStepDetailActivity(int stepId) {
        Intent intentStartStepDetail = new Intent(this, StepDetailActivity.class);
        intentStartStepDetail.putExtra(EXTRA_RECIPE_ID, mRecipeId);
        intentStartStepDetail.putExtra(EXTRA_STEP_ID, stepId);
        startActivity(intentStartStepDetail);
    }
}
