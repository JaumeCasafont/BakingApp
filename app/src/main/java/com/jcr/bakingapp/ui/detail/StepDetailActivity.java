package com.jcr.bakingapp.ui.detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.jcr.bakingapp.R;

import static com.jcr.bakingapp.ui.steps.StepsActivity.EXTRA_STEP_ID;

public class StepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        if (savedInstanceState == null) {
            showStepDetailFragment();
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

    private void showStepDetailFragment() {
        int stepId = getIntent().getIntExtra(EXTRA_STEP_ID, 0);
        StepDetailFragment stepDetailFragment = (StepDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.step_detail_fragment);
        stepDetailFragment.setStep(stepId);
    }
}
