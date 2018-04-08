package com.jcr.bakingapp.ui.recipes;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.jcr.bakingapp.Injection;
import com.jcr.bakingapp.R;
import com.jcr.bakingapp.databinding.ActivityRecipesListBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RecipesListActivity extends AppCompatActivity {

    private RecipesListAdapter mAdapter;
    private RecipesListViewModel mViewModel;

    private ActivityRecipesListBinding mBinding;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipes_list);

        initRecyclerView();
        initViewModel();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mBinding.recipesRv.setLayoutManager(layoutManager);
        mAdapter = new RecipesListAdapter(this);
        mBinding.recipesRv.setAdapter(mAdapter);
    }

    private void initViewModel() {
        RecipesListViewModelFactory factory = Injection.provideRecipesListViewModel();
        mViewModel = ViewModelProviders.of(this, factory).get(RecipesListViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDisposable.add(mViewModel.getRecipes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recipes -> mAdapter.swapRecipes(recipes)));
        mBinding.executePendingBindings();
    }
}
