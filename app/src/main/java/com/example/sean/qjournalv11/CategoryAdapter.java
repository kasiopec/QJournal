package com.example.sean.qjournalv11;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context mContext;
    private List<String> goals;
    Goals goalsActivity;

    private int sSpinnerItem;




    public CategoryAdapter(Context mContext, List<String> goals, Goals goalsActivity) {
        this.mContext = mContext;
        this.goals = goals;
        this.goalsActivity = goalsActivity;
    }

    public interface ItemClickListener{
        void onItemLongClick(View v, int position);
        void onItemClick(View v, int position);
    }



    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{

        TextView goalName;
        ProgressBar goalProgressBar;
        TextView goalPercent;


        ItemClickListener clickListener;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            goalName = (TextView) itemView.findViewById(R.id.textGoalName_editCat);
            goalProgressBar = (ProgressBar) itemView.findViewById(R.id.progressBarGoal_editCat);
            goalPercent = (TextView) itemView.findViewById(R.id.textGoalProg_editCat);

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
        holder.goalName.setText(goals.get(i));
        holder.goalProgressBar.setProgress(20);
        holder.goalPercent.setText(holder.goalProgressBar.getProgress()+"%");

        holder.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemLongClick(View v, int position) {
                Toast.makeText(v.getContext(),"Long clicked on goal: " + goals.get(position),Toast.LENGTH_SHORT).show();



                PopupMenu popup = new PopupMenu(mContext, v, Gravity.END);
                popup.inflate(R.menu.activity_goals_option_menu);


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menuEditGoalName:
                                showCustomAlertDialog(item.getItemId());
                                Log.d("MENU", "Name id: " + Integer.toString(item.getItemId()));
                                return true;
                            case R.id.menuEditTimeSpent:
                                showCustomAlertDialog(item.getItemId());
                                Log.d("MENU", "Time id: " + Integer.toString(item.getItemId()));

                                return true;

                            case R.id.menuEditGoalWM:
                                showCustomAlertDialog(item.getItemId());
                                Log.d("MENU", "MW id: " + Integer.toString(item.getItemId()));

                                return true;
                            case R.id.menuDelGoal:
                                Log.d("MENU", "Del id: " + Integer.toString(item.getItemId()));

                                return true;


                            default:
                                return false;
                        }
                    }
                });
                popup.show();

            }

            @Override
            public void onItemClick(View v, int position) {
                goalsActivity.ll.setVisibility(View.VISIBLE);
                goalsActivity.bottomAppBar.getMenu().findItem(R.id.appBarTitle).setTitle("Goaldsfsdfsdfsdf " + goals.get(position));
             }
        });



    }

    @Override
    public int getItemCount() {
        return goals.size();
    }


    private void showCustomAlertDialog(int item){

        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View aDialogView;

        if(item == R.id.menuEditGoalWM){
            aDialogView = inflater.inflate(R.layout.alert_dialog_layout_spinner, null);
            Spinner spinner = (Spinner) aDialogView.findViewById(R.id.spinner);

            ArrayAdapter<CharSequence> adapterWM = ArrayAdapter.createFromResource(mContext,
                    R.array.period_arrays, android.R.layout.simple_spinner_dropdown_item);

            spinner.setDropDownVerticalOffset(97);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sSpinnerItem = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinner.setAdapter(adapterWM);

        }else{
            aDialogView = inflater.inflate(R.layout.alert_dialog_layout_edit, null);
            final TextInputLayout editDialogTil = (TextInputLayout) aDialogView.findViewById(R.id.textInputLayoutEdit);
            EditText editText = (EditText) aDialogView.findViewById(R.id.goalInputEdit);


            if(item == R.id.menuEditGoalName){
                Log.d("MENU", "Name id inside: " + Integer.toString(item));

                editDialogTil.setHint("Name");
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            }else{
                Log.d("MENU", "Time id inside: " + Integer.toString(item));
                editDialogTil.setHint("Minutes");
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }

        }

        Button btnYes = (Button) aDialogView.findViewById(R.id.btnYes);
        Button btnCloseDialog = (Button) aDialogView.findViewById(R.id.btnCloseDialog);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                alertDialog.dismiss();


            }
        });


        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog.setView(aDialogView);
        alertDialog.show();




    }


}
