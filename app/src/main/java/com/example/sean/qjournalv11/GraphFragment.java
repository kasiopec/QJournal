package com.example.sean.qjournalv11;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GraphFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "fragName";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseHelper db;
    private String LEGEND_LABEL;
    private ArrayList<Goal> goals;



    private BarChart chart;
    private boolean spinnerInit = false;

    // TODO: Rename and change types of parameters
    private String tabName;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GraphFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment GraphFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphFragment newInstance(String param1) {
        GraphFragment fragment = new GraphFragment();
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

        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        // Inflate the layout for this fragment



        db = new DatabaseHelper(getContext());
        goals = db.getAllGoals();

        final ArrayList<String> categories = db.getAllCategories();
        categories.add("All");


        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<> (getContext(), R.layout.support_simple_spinner_dropdown_item, categories);

        chart = new BarChart(getContext());
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.chart);
        layout.addView(chart, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        final Spinner catSpinner = view.findViewById(R.id.catSpinner);
        catSpinner.setDropDownVerticalOffset(97);
        catSpinner.setAdapter(spinnerAdapter);
        catSpinner.setSelection(categories.indexOf("All"));

        catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //load goals with categoryselected
                Log.d("bars", "i ma triggered");
                if(!spinnerInit){
                    spinnerInit = true;
                }else{
                    String category = catSpinner.getSelectedItem().toString();
                    if(category.equals("All")){
                        goals = db.getAllGoals();
                        loadGoals(goals);
                        chart.invalidate();
                    }else{
                        Log.d("bars", "size of goals before load: " + goals.size());
                        goals =  db.getAllCategoryGoals(category, tabName);
                        Log.d("bars", "size of goals after load: " + goals.size());
                        loadGoals(goals);
                        chart.invalidate();

                    }
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loadGoals(goals);





        return view;
    }

    private void loadGoals(ArrayList<Goal> goals) {
        int i = 0;
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        BarData data;
        BarDataSet dataset;
        for (Goal goal: goals) {

            if(tabName.equals("Weekly") && goal.getTimeFrame().equals("Weekly")){
                barEntries.add(new BarEntry(goal.getCurrentTime(), i));
                labels.add(goal.getName());
                i++;
                LEGEND_LABEL = "week";
            }else if (tabName.equals("Monthly") && goal.getTimeFrame().equals("Monthly")){
                barEntries.add(new BarEntry(goal.getCurrentTime(), i));
                labels.add(goal.getName());
                i++;
                LEGEND_LABEL = "month";
            }

        }

        Log.d("bars", "bar entries: " + barEntries.size());
        dataset = new BarDataSet(barEntries, "Minutes spent this "+LEGEND_LABEL);
        dataset.setColor(getResources().getColor(R.color.colorPrimary));

        data = new BarData(labels, dataset);
        chart.setData(data);
        chart.setDescription("");
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis xAxis = chart.getXAxis();
        xAxis.setLabelsToSkip(0);
        chart.getAxisRight().setAxisMinValue(0f);
        chart.getAxisLeft().setAxisMinValue(0f);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setDrawGridLines(false);

        chart.getBarData().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                if (v != 0) {
                    DecimalFormat format = new DecimalFormat("#");
                    return format.format(v) + " min";
                }
                return "";
            }
        });



        chart.animateY(2000);
        chart.invalidate();

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
