package com.jcr.bakingapp.ui.steps;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
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
import com.jcr.bakingapp.widget.IngredientsListService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.jcr.bakingapp.ui.recipes.RecipesListActivity.EXTRA_RECIPE_ID;

public class StepsFragment extends Fragment {

    private final static String STEP_ID = "step_id";

    OnStepClickCallback mCallback;

    private FragmentStepsBinding mBinding;
    private StepsListViewModel mViewModel;
    private StepsAdapter mAdapter;
    private int mStepPosition = 0;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        if (savedInstanceState != null) {
            mStepPosition = savedInstanceState.getInt(STEP_ID, 0);
        }
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
        mAdapter = new StepsAdapter(getContext(), this::onStepSelected, mStepPosition);
        mBinding.stepsLayout.stepsRv.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.stepsLayout.stepsRv.setAdapter(mAdapter);
    }

    private void onStepSelected(int stepId, int position) {
        mStepPosition = position;
        mCallback.onStepSelected(stepId);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mViewModel.getRetainedRecipe() == null) {
            mDisposable.add(mViewModel.getRecipe()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::bindRecipe));
            mBinding.executePendingBindings();
        } else {
            bindRecipe(mViewModel.getRetainedRecipe());
        }
    }

    private void bindRecipe(Recipe recipe) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(recipe.getName());
        StringBuilder stringBuilder = new StringBuilder();
        for (Ingredient ingredient : recipe.getIngredients()) {
            stringBuilder.append("· ").append(ingredient.toString()).append("\n");
        }
        mBinding.ingredientsLayout.ingredientsTv.setText(stringBuilder.toString());
        mAdapter.setSteps(recipe.getSteps());
        IngredientsListService.startActionUpdateIngredientsList(getContext());
    }

    @Override
    public void onStop() {
        super.onStop();

        mDisposable.clear();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STEP_ID, mStepPosition);
    }
}
