package com.kasiopec.qjournal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ParentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ParentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "fragName";
    private static final String ARG_PARAM2 = "param2";

    private GoalsAdapter goalsAdapter;
    private DatabaseHelper db;
    ArrayList<Goal> finalData;
    LinearLayout nGoalLayout;
    RecyclerView rvGoals;

    // TODO: Rename and change types of parameters
    public String tabName;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ParentFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ParentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment newInstance(String param1) {
        ParentFragment fragment = new ParentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tabName = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent, container, false);

        nGoalLayout = (LinearLayout) view.findViewById(R.id.noGoalsLayout);
        nGoalLayout.setVisibility(View.INVISIBLE);

        rvGoals = (RecyclerView) view.findViewById(R.id.rw_goal_cards);





        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder grabbed, @NonNull RecyclerView.ViewHolder target) {

                int pos_grabbed = grabbed.getAdapterPosition();
                int pos_target = target.getAdapterPosition();
                Collections.swap(finalData, pos_grabbed, pos_target);

                goalsAdapter.notifyItemMoved(pos_grabbed, pos_target);
                rvGoals.scrollToPosition(pos_target);


                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            }
        });

        helper.attachToRecyclerView(rvGoals);




        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        db = new DatabaseHelper(getContext());

        ArrayList<Goal> rawdata  = db.getAllGoals();

        finalData = new ArrayList<>();

        if(tabName.equals("Weekly")){
            for (int i = 0; i < rawdata.size(); i++) {
                if(rawdata.get(i).getTimeFrame().equals("Weekly")){
                    finalData.add(rawdata.get(i));
                }
            }
        }else{
            for (int i = 0; i < rawdata.size(); i++) {
                if(rawdata.get(i).getTimeFrame().equals("Monthly")){
                    finalData.add(rawdata.get(i));
                }
            }
        }

        goalsAdapter = new GoalsAdapter(getContext(), finalData, tabName);



        rvGoals.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGoals.setNestedScrollingEnabled(false);
        rvGoals.setAdapter(goalsAdapter);



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
