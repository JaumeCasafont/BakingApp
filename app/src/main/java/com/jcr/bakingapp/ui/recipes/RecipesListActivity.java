package com.jcr.bakingapp.ui.recipes;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jcr.bakingapp.Injection;
import com.jcr.bakingapp.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RecipesListActivity extends AppCompatActivity {

    private RecipesListAdapter mAdapter;
    private RecipesListViewModel mViewModel;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        mAdapter = new RecipesListAdapter(this);

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
    }
}
