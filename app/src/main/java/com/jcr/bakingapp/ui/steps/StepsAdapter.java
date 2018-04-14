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

    private List<Step> mSteps;

    public StepsAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.step_item, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        holder.stepShortDescription.setText(mSteps.get(position).getShortDescription());
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

    class StepViewHolder extends RecyclerView.ViewHolder {

        final TextView stepShortDescription;

        public StepViewHolder(View itemView) {
            super(itemView);
            stepShortDescription = itemView.findViewById(R.id.step_short_description);
        }
    }
}
