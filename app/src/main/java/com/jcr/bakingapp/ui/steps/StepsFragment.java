package com.jcr.bakingapp.ui.steps;

import android.arch.lifecycle.ViewModelProviders;
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

    private FragmentStepsBinding mBinding;
    private StepsListViewModel mViewModel;
    private StepsAdapter mAdapter;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

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
        mAdapter = new StepsAdapter(getContext(), null);
        mBinding.stepsLayout.stepsRv.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.stepsLayout.stepsRv.setAdapter(mAdapter);
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
