package com.kasiopec.qjournal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.PopupMenu;
import android.widget.ProgressBar;

import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.kasiopec.qjournal.entities.Goal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 Adapter class for recycleView in the MainActivity
 **/

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.GoalsViewHolder> {

    private Context mContext;
    private ArrayList<Goal> goals;
    private HashMap<String, List<Goal>> goalMap = new HashMap<>();
    private DatabaseHelper db;

    private String fragName;



        public GoalsAdapter(Context mContext, ArrayList<Goal> goals, String fragName) {
            this.mContext = mContext;
            this.goals = goals;
            this.db = new DatabaseHelper(mContext);
            this.fragName = fragName;
            for (int i = 0; i < goals.size(); i++) {
                String cName = goals.get(i).getCategory();
                if(!goalMap.containsKey(cName)){
                    ArrayList<Goal> tempGoals = new ArrayList<>();
                    for (int j = 0; j < goals.size(); j++) {
                        if(goals.get(j).getCategory().equals(cName)){
                            tempGoals.add(goals.get(j));
                        }
                    }
                    goalMap.put(cName, tempGoals);
                }

            }
        }

    @NonNull
    @Override
    public GoalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            LayoutInflater inflater = LayoutInflater.from(mContext);
            View v =inflater.inflate(R.layout.rw_item_main, parent, false);


        return new GoalsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final GoalsViewHolder holder, final int position) {

            holder.textCategory.setText(goalMap.keySet().toArray()[position].toString());


            List<Goal> g = goalMap.get(goalMap.keySet().toArray()[position]);




            final LayoutInflater inflater  = LayoutInflater.from(mContext);
            holder.listLayout.removeAllViews();

            for (int i = 0; i < g.size(); i++) {
                ConstraintLayout goalsView  = (ConstraintLayout) inflater.inflate(R.layout.card_list_item_main, null);
                ProgressBar progressBar = goalsView.findViewById(R.id.progressBarGoal);
                TextView textGoal = goalsView.findViewById(R.id.textGoalName);
                TextView textGoalProg = goalsView.findViewById(R.id.textGoalProg);
                int goalPercentage = Math.round((float) g.get(i).getCurrentTime() / g.get(i).getGoalTime() * 100);
                textGoalProg.setText(goalPercentage + "%");
                textGoal.setText(g.get(i).getName());
                progressBar.setProgress(goalPercentage);

                holder.listLayout.addView(goalsView);
        }






            //access goals info via for loop over set values





        holder.textViewOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popup = new PopupMenu(mContext, v, Gravity.END);
                popup.inflate(R.menu.activity_main_options_menu);


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final String category = holder.textCategory.getText().toString();

                        switch (item.getItemId()){
                            case R.id.menuEditCat:
                                Intent intent = new Intent(mContext, EditCategoryActivity.class);
                                Bundle extras = new Bundle();
                                extras.putString("category", category);
                                extras.putString("tab_name", fragName);
                                intent.putExtras(extras);
                                mContext.startActivity(intent);
                                return true;
                            case R.id.menuDelCat:


                                final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                                View aDialogView2 = inflater.inflate(R.layout.alert_dialog_layout_delete, null);
                                TextView msg = (TextView) aDialogView2.findViewById(R.id.textDeleteMsg);
                                Button btnYes = (Button) aDialogView2.findViewById(R.id.btnYes);
                                Button btnCloseDialog = (Button) aDialogView2.findViewById(R.id.btnCloseDialog);

                                String delText = mContext.getResources().getString(R.string.delCategory, category);
                                msg.setText(delText);

                                alertDialog.setView(aDialogView2);
                                alertDialog.show();

                                btnYes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        db.removeGoals(category);
                                        goalMap.remove(category);
                                        notifyDataSetChanged();
                                        alertDialog.dismiss();


                                    }
                                });

                                btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                    }
                                });


                                return true;

                                default:
                                  return false;
                        }
                    }
                });
                popup.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return goalMap.size();
    }



    class GoalsViewHolder extends RecyclerView.ViewHolder{
        TextView textCategory;
        ImageView imgEdit;
        LinearLayout listLayout;
        TextView textViewOpt;



        public GoalsViewHolder(@NonNull View itemView) {
            super(itemView);
            textCategory = itemView.findViewById(R.id.text_Category);
           // imgEdit = itemView.findViewById(R.id.imageEditGoal);
            textViewOpt = itemView.findViewById(R.id.textViewOptions);

            listLayout = itemView.findViewById(R.id.goalListLayout);






        }
    }
}



