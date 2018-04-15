package com.jcr.bakingapp.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcr.bakingapp.Injection;
import com.jcr.bakingapp.R;
import com.jcr.bakingapp.data.models.Step;
import com.jcr.bakingapp.databinding.FragmentStepDetailBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.jcr.bakingapp.ui.recipes.RecipesListActivity.EXTRA_RECIPE_ID;

public class StepDetailFragment extends Fragment {

    private StepDetailViewModel mViewModel;
    private int mStepId = 0;
    private FragmentStepDetailBinding mBinding;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_step_detail, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewModel();
    }

    private void initViewModel() {
        int recipeId = getActivity().getIntent().getIntExtra(EXTRA_RECIPE_ID, 0);
        StepDetailViewModelFactory factory = Injection.provideStepDetailViewModel(getContext(), recipeId, mStepId);
        mViewModel = ViewModelProviders.of(this, factory).get(StepDetailViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        mDisposable.add(mViewModel.getStep()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::bindStep));
        mBinding.executePendingBindings();
    }

    @Override
    public void onStop() {
        super.onStop();

        mDisposable.clear();
    }

    private void bindStep(Step step) {
        if (!getResources().getBoolean(R.bool.isTablet)) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(String.format(
                    getString(R.string.steps_detail_toolbar), String.valueOf(step.getId())));
        }
        mBinding.stepId.setText(step.getShortDescription());
    }

    public void setStep(int stepId) {
        mStepId = stepId;
    }

}
