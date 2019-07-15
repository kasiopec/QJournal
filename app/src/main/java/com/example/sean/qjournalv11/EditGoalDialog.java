package com.example.sean.qjournalv11;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 Fragment class which is called when user calls goal edit
 **/

public class EditGoalDialog extends DialogFragment {
    EditText goalName, goalHours, goalMin;
    Spinner spinnerWM;
    Button btnSave, btnCancel;
    TextInputLayout nameTil, hourTil, minTil;
    Goal passedGoal;
    private static DatabaseHelper db;
    private static OnDialogCloseListener mListener;

    public EditGoalDialog() {
    }

    public static EditGoalDialog newInstance(Context mContext, String string, OnDialogCloseListener listener){
        EditGoalDialog dialogFrag = new EditGoalDialog();
        db = new DatabaseHelper(mContext);
        mListener = listener;
        Bundle args = new Bundle();
        args.putString("Title", string);
        dialogFrag.setArguments(args);
        return dialogFrag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCancelable(false);

        Bundle bundle = getArguments();
        if(bundle != null){
            passedGoal = bundle.getParcelable("Goal");

        }
        return inflater.inflate(R.layout.fragment_edit_goal_layout, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goalName = (EditText) view.findViewById(R.id.goalNameEdit_dialogFrag);
        goalHours = (EditText) view.findViewById(R.id.goalTextHours_dialogFrag);
        goalMin = (EditText) view.findViewById(R.id.goalTextMin_dialogFrag);
        spinnerWM = (Spinner) view.findViewById(R.id.spinnerWM_dialogFrag);
        btnCancel = (Button) view.findViewById(R.id.btnCloseDialog_dialogFrag);
        btnSave = (Button) view.findViewById(R.id.btnYes_dialogFrag);
        nameTil = (TextInputLayout) view.findViewById(R.id.textInputLayoutGoalName_dialogFrag);
        hourTil = (TextInputLayout) view.findViewById(R.id.textInputLayoutHours_dialogFrag);
        minTil = (TextInputLayout) view.findViewById(R.id.textInputLayoutMin_dialogFrag);


        ArrayAdapter<CharSequence> adapterWM = ArrayAdapter.createFromResource(view.getContext(),
                R.array.period_arrays, android.R.layout.simple_spinner_dropdown_item);
        goalName.setText(passedGoal.getName());
        spinnerWM.setDropDownVerticalOffset(97);
        spinnerWM.setAdapter(adapterWM);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = goalName.getText().toString();
                String timeFrame = spinnerWM.getSelectedItem().toString();
                int hours = 0;
                int min  = 0;
                int newGoalTime = hours*60 + min;
                if(name.equals("")){
                    nameTil.setError("Name can not be empty");
                    return;
                }
                //check for edit box values. Hours are converted to minutes or does nothing if empty
                else if(goalMin.getText().toString().equals("") && goalHours.getText().length() > 0){
                    hours = Integer.parseInt(goalHours.getText().toString());
                    newGoalTime = hours*60 + min;
                    passedGoal.setGoalTime(newGoalTime);
                }else if (goalHours.getText().toString().equals("") && goalMin.getText().length()>0) {
                    min = Integer.parseInt(goalMin.getText().toString());
                    newGoalTime = hours * 60 + min;
                    passedGoal.setGoalTime(newGoalTime);
                }else if(goalHours.getText().length() > 0 && goalMin.getText().length() > 0) {

                    min = Integer.parseInt(goalMin.getText().toString());
                    hours = Integer.parseInt(goalHours.getText().toString());
                    newGoalTime = hours*60 + min;
                    passedGoal.setGoalTime(newGoalTime);

                }


                passedGoal.setTimeFrame(timeFrame);
                passedGoal.setName(name);

                db.updateGoal(passedGoal);
                mListener.onDialogClose();
                dismiss();
            }
        });


        String title = getArguments().getString("Title", "ebala");
        getDialog().setTitle(title);

    }

    @Override
    public void onResume() {
        super.onResume();
        //resize fragment, for some reason android wasn't able to use values from XML
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

    }
}
