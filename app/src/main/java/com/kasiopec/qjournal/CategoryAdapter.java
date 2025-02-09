package com.kasiopec.qjournal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kasiopec.qjournal.entities.Goal;

import java.util.List;

/**
 Adapter class for recycleView in the category edit activity
 **/

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context mContext;
    private List<Goal> goals;
    private EditCategoryActivity editCategoryActivity;

    public CategoryAdapter(Context mContext, List<Goal> goals, EditCategoryActivity editCategoryActivity) {
        this.mContext = mContext;
        this.goals = goals;
        this.editCategoryActivity = editCategoryActivity;
    }

    public interface ItemClickListener{
        void onItemLongClick(View v, int position);
        void onItemClick(View v, int position);
    }



    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{

        TextView goalName, detailsCaption, goalDetails;
        ProgressBar goalProgressBar;
        TextView goalPercent;


        ItemClickListener clickListener;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            goalName = (TextView) itemView.findViewById(R.id.textGoalName_editCat);
            goalProgressBar = (ProgressBar) itemView.findViewById(R.id.progressBarGoal_editCat);
            goalPercent = (TextView) itemView.findViewById(R.id.textGoalProg_editCat);
            goalDetails = (TextView) itemView.findViewById(R.id.textGoalDetails_editCat);
            detailsCaption = (TextView) itemView.findViewById(R.id.textDetailsCap_editCat);

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);

        }

        public void setOnItemClickListener(ItemClickListener clickListener){
            this.clickListener = clickListener;
        }

        @Override
        public boolean onLongClick(View v) {
            this.clickListener.onItemLongClick(v, getLayoutPosition());
            return true;
        }

        @Override
        public void onClick(View v) {
            this.clickListener.onItemClick(v, getLayoutPosition());
        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v =inflater.inflate(R.layout.rw_item_edit_goals, viewGroup, false);
        return new CategoryViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, int i) {
        final LayoutInflater inflater  = LayoutInflater.from(mContext);


        int goalPercentage = Math.round((float) goals.get(i).getCurrentTime() / goals.get(i).getGoalTime() * 100);

        int goalHours = goals.get(i).getGoalTime() / 60;
        int curHours = goals.get(i).getCurrentTime() / 60;

        int goalMin = goals.get(i).getGoalTime() % 60;
        int curMin = goals.get(i).getCurrentTime() % 60;

        String detailsText = mContext.getResources().getString(R.string.textViewDetails,
                String.valueOf(curHours), String.valueOf(curMin),
                String.valueOf(goalHours), String.valueOf(goalMin));

        String detailsTextZero = mContext.getResources().getString(R.string.textViewDetailsZero,
                String.valueOf(0), String.valueOf(goalHours), String.valueOf(goalMin));

        holder.goalName.setText(mContext.getResources().getString(R.string.textViewGoalName, goals.get(i).getName()));
        holder.detailsCaption.setText(R.string.textViewCap);

        if(goals.get(i).getCurrentTime() == 0){
            holder.goalDetails.setText(detailsTextZero);
        }else{
            holder.goalDetails.setText(detailsText);
        }

        holder.goalProgressBar.setProgress(goalPercentage);
        holder.goalPercent.setText(goalPercentage+"%");

        holder.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemLongClick(View v, int position) {
                //do something on long click
            }

            @Override
            public void onItemClick(View v, int position) {

                //show bottom bar with goal controls
                editCategoryActivity.ll.setVisibility(View.VISIBLE);
                editCategoryActivity.bottomAppBar.getMenu().findItem(R.id.appBarTitle).setTitle(goals.get(position).getName());
                //editCategoryActivity.bottomText.setText(goals.get(position).getName());

                //Create extra with the goal and push it to the activity
                Intent intent = new Intent("custom-msg");
                String id = String.valueOf(goals.get(position).getId());
                intent.putExtra("goalID", id);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
             }
        });

    }

    @Override
    public int getItemCount() {
        return goals.size();
    }



}
