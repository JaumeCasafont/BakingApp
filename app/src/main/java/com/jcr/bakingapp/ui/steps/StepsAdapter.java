package com.jcr.bakingapp.ui.steps;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcr.bakingapp.R;
import com.jcr.bakingapp.data.models.Step;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {

    private final Context mContext;
    private final OnStepClickHandler mClickHandler;
    private int mSelectedItemPosition = 0;

    private List<Step> mSteps;

    public StepsAdapter(Context context, OnStepClickHandler handler, int selectedPosition) {
        mContext = context;
        mClickHandler = handler;
        mSelectedItemPosition = selectedPosition;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.step_item, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        if (mSelectedItemPosition == position && mContext.getResources().getBoolean(R.bool.isTablet)) {
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryLight));
        } else {
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
        Step step = mSteps.get(position);
        holder.stepShortDescription.setText(step.getShortDescription());
        holder.itemView.setOnClickListener(view -> {
            if (mClickHandler != null) {
                mClickHandler.onClick(step.getId(), position);
                if (position != mSelectedItemPosition && mContext.getResources().getBoolean(R.bool.isTablet)) {
                    mSelectedItemPosition = position;
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mSteps == null) return 0;
        return mSteps.size();
    }

    public void setSteps(List<Step> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }

    interface OnStepClickHandler {
        void onClick(int stepId, int position);
    }

    class StepViewHolder extends RecyclerView.ViewHolder {

        final TextView stepShortDescription;

        public StepViewHolder(View itemView) {
            super(itemView);
            stepShortDescription = itemView.findViewById(R.id.step_short_description);
        }
    }
}
