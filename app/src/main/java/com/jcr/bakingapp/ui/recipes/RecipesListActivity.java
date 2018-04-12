package com.jcr.bakingapp.ui.recipes;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jcr.bakingapp.Injection;
import com.jcr.bakingapp.R;
import com.jcr.bakingapp.databinding.ActivityRecipesListBinding;
import com.jcr.bakingapp.ui.steps.StepsActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class RecipesListActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "recipe_id";

    private static final String LIST_STATE = "list_state";

    private RecipesListAdapter mAdapter;
    private RecipesListViewModel mViewModel;

    private ActivityRecipesListBinding mBinding;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    private Parcelable listState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipes_list);

        initRecyclerView();
        initViewModel();
    }

    private void initRecyclerView() {
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        RecyclerView.LayoutManager layoutManager;
        if (isTablet) {
            layoutManager = new GridLayoutManager(
                    this, getColumns());
        } else {
            layoutManager = new LinearLayoutManager(
                    this, LinearLayoutManager.VERTICAL, false);
        }
        mBinding.recipesRv.setLayoutManager(layoutManager);
        mAdapter = new RecipesListAdapter(this, this::startStepsActivity);
        mBinding.recipesRv.setAdapter(mAdapter);
    }

    private int getColumns(){
        return getResources().getConfiguration().orientation ==  ORIENTATION_LANDSCAPE ? 3 : 2;
    }

    private void initViewModel() {
        RecipesListViewModelFactory factory = Injection.provideRecipesListViewModel(this);
        mViewModel = ViewModelProviders.of(this, factory).get(RecipesListViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDisposable.add(mViewModel.getRecipesList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recipes -> {
                    mAdapter.swapRecipes(recipes);
                    if (listState != null) {
                        mBinding.recipesRv.getLayoutManager().onRestoreInstanceState(listState);
                    }
                }));
        mBinding.executePendingBindings();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mDisposable.clear();
    }

    public void startStepsActivity(int id) {
        Intent intentStepsActivity = new Intent(this, StepsActivity.class);
        intentStepsActivity.putExtra(EXTRA_RECIPE_ID, id);
        startActivity(intentStepsActivity);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(LIST_STATE, mBinding.recipesRv.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        listState = savedInstanceState.getParcelable(LIST_STATE);
    }


}
