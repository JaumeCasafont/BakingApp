package com.jcr.bakingapp.ui.recipes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jcr.bakingapp.R;
import com.jcr.bakingapp.data.models.Recipe;
import com.jcr.bakingapp.databinding.RecipeCardItemBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipesListAdapter extends RecyclerView.Adapter<RecipesListAdapter.RecipeViewHolder> {

    private final Context mContext;
    private final OnClickHandler mClickHandler;

    private List<Recipe> mRecipes;

    RecipesListAdapter(@NonNull Context context, OnClickHandler handler) {
        this.mContext = context;
        this.mClickHandler = handler;
    }


    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(mContext);
        RecipeCardItemBinding binding =
                RecipeCardItemBinding.inflate(layoutInflater, parent, false);
        return new RecipeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        holder.bind(recipe);
        holder.mBinding.getRoot().setOnClickListener(view -> {
            if (mClickHandler != null && recipe != null) {
                mClickHandler.onClick(recipe.getId());
            }
        });
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

    interface OnClickHandler {
        void onClick(int id);
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        private final RecipeCardItemBinding mBinding;

        public RecipeViewHolder(RecipeCardItemBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        public void bind(Recipe recipe) {
            if (!recipe.getImage().isEmpty()) {
                Picasso.get().load(recipe.getImage()).into(mBinding.image);
            }
            mBinding.nameTv.setText(recipe.getName());
            mBinding.servingsTv.setText(String.format(
                    mContext.getString(R.string.servings_text),
                    String.valueOf(recipe.getServings())));
            mBinding.executePendingBindings();
        }
    }
}
