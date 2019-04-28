package com.example.sean.qjournalv11;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class EditGoalDialog extends DialogFragment {
    EditText goalName, goalHours, goalMin;
    Spinner spinnerWM;
    Button btnSave, btnCancel;

    public EditGoalDialog() {
    }

    public static EditGoalDialog newInstance(String string){
        EditGoalDialog dialogFrag = new EditGoalDialog();
        Bundle args = new Bundle();
        args.putString("Title", string);
        dialogFrag.setArguments(args);
        return dialogFrag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCancelable(false);
        return inflater.inflate(R.layout.fragment_edit_goal_layout, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goalName = (EditText) view.findViewById(R.id.goalNameEdit_dialogFrag);
        goalHours = (EditText) view.findViewById(R.id.goalTextHours_dialogFrag);
        goalName = (EditText) view.findViewById(R.id.goalTextMin_dialogFrag);
        spinnerWM = (Spinner) view.findViewById(R.id.spinnerWM_dialogFrag);
        btnCancel = (Button) view.findViewById(R.id.btnCloseDialog_dialogFrag);
        btnSave = (Button) view.findViewById(R.id.btnYes_dialogFrag);


        ArrayAdapter<CharSequence> adapterWM = ArrayAdapter.createFromResource(view.getContext(),
                R.array.period_arrays, android.R.layout.simple_spinner_dropdown_item);

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
                dismiss();
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

        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

    }
}
