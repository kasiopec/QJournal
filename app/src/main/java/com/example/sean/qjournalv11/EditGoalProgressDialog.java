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
import android.widget.Button;
import android.widget.EditText;

/**
 Fragment class which is called when user calls goal progress edit
 **/
public class EditGoalProgressDialog extends DialogFragment {
    private static final String ARG_PARAM1 = "Title";
    private static final String ARG_PARAM2 = "Goal";
    private static final String ARG_PARAM3 = "CurrentProg";

    private static OnDialogCloseListener mListener;
    private static DatabaseHelper db;

    EditText goalHours, goalMin;
    Button btnSave, btnCancel;

    private String fragTitle;
    private String goalID;
    private String currentProg;
    public EditGoalProgressDialog() {
    }

    public static EditGoalProgressDialog newInstance(Context mContext, String title, String goalID, int currentProg, OnDialogCloseListener listener){
        db = new DatabaseHelper(mContext);
        mListener = listener;
        EditGoalProgressDialog dialogFrag = new EditGoalProgressDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, goalID);
        args.putString(ARG_PARAM3, String.valueOf(currentProg));
        dialogFrag.setArguments(args);
        return dialogFrag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCancelable(false);
        if (getArguments() != null) {
            fragTitle = getArguments().getString(ARG_PARAM1);
            goalID = getArguments().getString(ARG_PARAM2);
            currentProg = getArguments().getString(ARG_PARAM3);
        }
        return inflater.inflate(R.layout.fragment_edit_progress_layout, container);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        goalHours = (EditText) view.findViewById(R.id.goalTextHours_dialogFragP);
        goalMin = (EditText) view.findViewById(R.id.goalTextMin_dialogFragP);
        btnCancel = (Button) view.findViewById(R.id.btnCloseDialog_dialogFragP);
        btnSave = (Button) view.findViewById(R.id.btnYes_dialogFragP);

        final TextInputLayout hourTil = (TextInputLayout) view.findViewById(R.id.textInputLayoutHours_dialogFragP);
        final TextInputLayout minTil = (TextInputLayout) view.findViewById(R.id.textInputLayoutMin_dialogFragP);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hours = 0;
                int min  = 0;

                if(goalMin.getText().toString().equals("") && goalHours.getText().length() > 0){
                    hours = Integer.parseInt(goalHours.getText().toString());
                }else if (goalHours.getText().toString().equals("") && goalMin.getText().length()>0) {
                    min = Integer.parseInt(goalMin.getText().toString());
                }else if(goalHours.getText().length() == 0 && goalMin.getText().length() == 0) {
                    hourTil.setError("Enter minutes or hours");
                    minTil.setError(" ");
                    return;
                }else{

                    min = Integer.parseInt(goalMin.getText().toString());
                    hours = Integer.parseInt(goalHours.getText().toString());
                }
                int progTimeInMin = hours*60 + min + Integer.parseInt(currentProg);
                db.updateGoalProgress(goalID, progTimeInMin);
                dismiss();
                mListener.onDialogClose();

            }
        });


        String title = getArguments().getString("Title", "ebala");
        getDialog().setTitle(title);

    }

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);

    }
}
