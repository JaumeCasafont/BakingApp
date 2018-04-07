package com.jcr.bakingapp.ui.recipes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.jcr.bakingapp.data.models.Recipe;

import java.util.List;

public class RecipesListAdapter extends RecyclerView.Adapter {

    private final Context mContext;

    private List<Recipe> mRecipes;

    RecipesListAdapter(@NonNull Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (null == mRecipes) return 0;
        return mRecipes.size();
    }

    void swapRecipes(final List<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }
}
