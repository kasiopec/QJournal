package com.example.sean.qjournalv11;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.PopupMenu;
import android.widget.ProgressBar;

import android.widget.TextView;


import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.GoalsViewHolder> {

    private Context mContext;
    private ArrayList<String> goals;


        public GoalsAdapter(Context mContext, ArrayList<String> goals) {
        this.mContext = mContext;
        this.goals = goals;
    }

    @NonNull
    @Override
    public GoalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v =inflater.inflate(R.layout.rw_item_main, null);

        return new GoalsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final GoalsViewHolder holder, final int position) {

            holder.textCategory.setText("Category");






            final LayoutInflater inflater  = LayoutInflater.from(mContext);

            holder.listLayout.removeAllViews();
            Random random = new Random();
            int randomGoals = random.nextInt(2)+1;
            for (int j = 0; j <= randomGoals; j++) {
                ConstraintLayout goalsView  = (ConstraintLayout) inflater.inflate(R.layout.card_list_item_main, null);
                ProgressBar progressBar = goalsView.findViewById(R.id.progressBarGoal);
                TextView textGoal = goalsView.findViewById(R.id.textGoalName);
                TextView textGoalProg = goalsView.findViewById(R.id.textGoalProg);
                textGoalProg.setText(randomGoals+j + "%");
                textGoal.setText("Gals Nr: " + j);
                progressBar.setProgress(randomGoals+j);

                holder.listLayout.addView(goalsView);
            }





        holder.textViewOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popup = new PopupMenu(mContext, v, Gravity.END);
                popup.inflate(R.menu.activity_main_options_menu);


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menuEditCat:
                                Intent intent = new Intent(mContext, Goals.class);
                                mContext.startActivity(intent);
                                return true;
                            case R.id.menuDelCat:


                                final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                                View aDialogView2 = inflater.inflate(R.layout.alert_dialog_layout_delete, null);
                                TextView msg = (TextView) aDialogView2.findViewById(R.id.textDeleteMsg);
                                Button btnYes = (Button) aDialogView2.findViewById(R.id.btnYes);
                                Button btnCloseDialog = (Button) aDialogView2.findViewById(R.id.btnCloseDialog);

                                String delText = mContext.getResources().getString(R.string.delCategory, "CategoryXX");
                                msg.setText(delText);

                                alertDialog.setView(aDialogView2);
                                alertDialog.show();

                                btnYes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        goals.remove(position);
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
        return goals.size();
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



