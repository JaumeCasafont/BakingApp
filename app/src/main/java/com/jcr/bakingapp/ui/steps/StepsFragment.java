package com.jcr.bakingapp.ui.steps;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcr.bakingapp.Injection;
import com.jcr.bakingapp.R;
import com.jcr.bakingapp.data.models.Ingredient;
import com.jcr.bakingapp.data.models.Recipe;
import com.jcr.bakingapp.databinding.FragmentStepsBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.jcr.bakingapp.ui.recipes.RecipesListActivity.EXTRA_RECIPE_ID;

public class StepsFragment extends Fragment {

    OnStepClickCallback mCallback;

    private FragmentStepsBinding mBinding;
    private StepsListViewModel mViewModel;
    private StepsAdapter mAdapter;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public interface OnStepClickCallback {
        void onStepSelected(int stepId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnStepClickCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickCallback");
        }
    }

    public StepsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_steps, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
        initViewModel();
    }

    private void initViewModel() {
        int recipeId = getActivity().getIntent().getIntExtra(EXTRA_RECIPE_ID, 0);
        StepsListViewModelFactory factory = Injection.provideStepsListViewModel(getContext(), recipeId);
        mViewModel = ViewModelProviders.of(this, factory).get(StepsListViewModel.class);
    }

    private void initRecyclerView() {
        mAdapter = new StepsAdapter(getContext(), this::onStepSelected);
        mBinding.stepsLayout.stepsRv.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.stepsLayout.stepsRv.setAdapter(mAdapter);
    }

    private void onStepSelected(int stepId) {
        if (getResources().getBoolean(R.bool.isTablet)) {
            restoreColors();
            setStepItemColor(stepId, R.color.colorPrimaryLight);
        }
        mCallback.onStepSelected(stepId);
    }

    private void restoreColors() {
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            setStepItemColor(i, R.color.colorPrimary);
        }
    }

    private void setStepItemColor(int step, int color) {
        mBinding.stepsLayout.stepsRv.getLayoutManager().findViewByPosition(step)
                .setBackgroundColor(getResources().getColor(color));
    }

    @Override
    public void onStart() {
        super.onStart();
        mDisposable.add(mViewModel.getRecipe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::bindRecipe));
        mBinding.executePendingBindings();
    }

    private void bindRecipe(Recipe recipe) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(recipe.getName());
        mBinding.ingredientsLayout.ingredientsTv.setText("");
        for (Ingredient ingredient : recipe.getIngredients()) {
            mBinding.ingredientsLayout.ingredientsTv.append("· " + ingredient.toString() + "\n");
        }
        mAdapter.setSteps(recipe.getSteps());
    }

    @Override
    public void onStop() {
        super.onStop();

        mDisposable.clear();
    }
}
