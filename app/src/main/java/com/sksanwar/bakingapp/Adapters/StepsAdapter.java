package com.sksanwar.bakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sksanwar.bakingapp.Pojo.Step;
import com.sksanwar.bakingapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for Steps
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {

    final private ListItemClickListener mOnClickListener;
    final private ArrayList<Step> stepArrayList;

    public StepsAdapter(ListItemClickListener mOnClickListener, ArrayList<Step> steps) {
        this.mOnClickListener = mOnClickListener;
        this.stepArrayList = steps;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.steps_item_view;

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        StepViewHolder viewHolder = new StepViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return stepArrayList.size();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    //ViewHolder Class
    class StepViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.list_step_description)
        TextView short_description;
        @BindView(R.id.list_step_id)
        TextView steps_id;

        //Constructor
        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        //binding the view
        void onBind(int position) {
            if (!stepArrayList.isEmpty()) {
                steps_id.setText(stepArrayList.get(position).getId() + ".");
                short_description.setText(stepArrayList.get(position).getShortDescription());
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
