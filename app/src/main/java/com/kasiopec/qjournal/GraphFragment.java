package com.kasiopec.qjournal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


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
    int currentWeek, currentMonth, newWeek, newMonth, currentYear, newYear;
    private Calendar c;
    private TextView dates;
    private Date currentDate;


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
    public static Fragment newInstance(String param1) {
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

        currentDate = new Date();
        c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(currentDate);
        currentWeek = c.get(Calendar.WEEK_OF_YEAR);
        currentMonth = c.get(Calendar.MONTH);
        currentYear = c.get(Calendar.YEAR);
        newWeek = currentWeek;
        newMonth = currentMonth;
        newYear = currentYear;
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

        Button curButton = (Button) view.findViewById(R.id.curBtn);
        Button prevButton = (Button) view.findViewById(R.id.prevBtn);
        Button nextButton = (Button) view.findViewById(R.id.nextBtn);
        dates = (TextView) view.findViewById(R.id.textDates);

        if(tabName.equals("Weekly")){
            curButton.setText(R.string.btnCurrentWeek);
            prevButton.setText(R.string.btnPrevWeek);
            nextButton.setText(R.string.btnNextWeek);
            setCurWeek();
        }else{
            curButton.setText(R.string.btnCurrentMonth);
            prevButton.setText(R.string.btnPrevMonth);
            nextButton.setText(R.string.btnNextMonth);
            setCurMonth();
        }

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
                if(!spinnerInit){
                    spinnerInit = true;
                }else{
                    String category = catSpinner.getSelectedItem().toString();
                    ArrayList<Goal> filtredGoals = new ArrayList<>();
                    if(category.equals("All")){
                        loadGoals(goals);
                    }else{
                        for (int i = 0; i < goals.size(); i++) {
                            if(goals.get(i).getCategory().equals(category)){
                                filtredGoals.add(goals.get(i));
                            }
                        }
                        loadGoals(filtredGoals);

                    }
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        loadGoals(goals);

        curButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = catSpinner.getSelectedItem().toString();
                if(category.equals("All")){
                    goals = db.getAllGoals();
                }else{
                    goals =  db.getAllCategoryGoals(category, tabName);
                }

                loadGoals(goals);
                if(tabName.equals("Weekly")){
                    newWeek = currentWeek;
                    setCurWeek();
                }else{
                    newMonth= currentMonth;
                    setCurMonth();

                }
                newYear = currentYear;


            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = catSpinner.getSelectedItem().toString();
                if(tabName.equals("Weekly")){
                    newWeek = setNegWeek();
                    if(newWeek == currentWeek && category.equals("All") && newYear==currentYear){
                        goals = db.getAllGoals();
                    }else{
                        goals = db.getWeekGoals(newWeek, category, newYear);
                    }
                }else{
                    newMonth = setNegMonth();
                    if(newMonth == currentMonth && category.equals("All") && newYear == currentYear){
                        goals = db.getAllGoals();
                    }else{
                        goals = db.getMonthGoals(newMonth, category, newYear);
                    }
                }

                loadGoals(goals);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = catSpinner.getSelectedItem().toString();
                if(tabName.equals("Weekly")){
                    newWeek = setPosWeek();
                    if(newWeek == currentWeek && category.equals("All") && newYear == currentYear){
                        goals = db.getAllGoals();
                    }else{
                        goals = db.getWeekGoals(newWeek, category, newYear);
                    }
                }else{
                    newMonth = setPosMonth();
                    if(newMonth == currentMonth && category.equals("All")&& newYear == currentYear){
                        goals = db.getAllGoals();
                    }else{
                        goals = db.getMonthGoals(newMonth, category, newYear);
                    }
                }

                loadGoals(goals);
            }
        });


        return view;




    }
    private void setCurMonth(){
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
        c.setTime(currentDate);
        String month = sdf.format(c.getTime());
        dates.setText(getString(R.string.textViewMonth, month));


    }

    private int setPosMonth(){
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
        c.add(Calendar.MONTH, 1);
        String month = sdf.format(c.getTime());
        dates.setText(getString(R.string.textViewMonth, month));
        //dates.setText(sdf.format(c.getTime()));
        newYear = c.get(Calendar.YEAR);
        return c.get(Calendar.MONTH);
    }

    private int setNegMonth(){
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
        c.add(Calendar.MONTH, -1);
        String month = sdf.format(c.getTime());
        dates.setText(getString(R.string.textViewMonth, month));
        newYear = c.get(Calendar.YEAR);
        return c.get(Calendar.MONTH);
    }

    private int setPosWeek(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        c.add(Calendar.WEEK_OF_YEAR, 1);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String firstDay = sdf.format(c.getTime());
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String lastDay = sdf.format(c.getTime());
        dates.setText(getString(R.string.textViewWeek, firstDay, lastDay));
        //dates.setText("Week: " + firstDay + " - " + lastDay);
        newYear = c.get(Calendar.YEAR);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    private int setNegWeek(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        c.add(Calendar.WEEK_OF_YEAR, -1);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String firstDay = sdf.format(c.getTime());
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String lastDay = sdf.format(c.getTime());
        dates.setText(getString(R.string.textViewWeek, firstDay, lastDay));
        //dates.setText("Week: " + firstDay + " - " + lastDay);
        newYear = c.get(Calendar.YEAR);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    private void setCurWeek(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        c.setTime(currentDate);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String firstDay = sdf.format(c.getTime());
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String lastDay = sdf.format(c.getTime());
        dates.setText(getString(R.string.textViewWeek, firstDay, lastDay));
        //dates.setText("Week: " + firstDay + " - " + lastDay);

    }


    private void loadGoals(ArrayList<Goal> goals) {
        int i = 0;
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        BarData data;
        BarDataSet dataset;
        for (Goal goal: goals) {
            if(goal.getTimeFrame() == null && tabName.equals("Weekly")){
                barEntries.add(new BarEntry(goal.getCurrentTime(), i));
                labels.add(goal.getName());
                i++;
                LEGEND_LABEL = "week";
            }else if (goal.getTimeFrame() == null && tabName.equals("Monthly")){
                barEntries.add(new BarEntry(goal.getCurrentTime(), i));
                labels.add(goal.getName());
                i++;
                LEGEND_LABEL = "month";
            }else if(tabName.equals("Weekly") && goal.getTimeFrame().equals("Weekly")){
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

        dataset = new BarDataSet(barEntries, "Minutes spent this "+LEGEND_LABEL);
        dataset.setColor(getResources().getColor(R.color.colorPrimary));
        dataset.setHighLightAlpha(0);
        dataset.setLabel(labels.get(0));
        Description description = new Description();
        description.setText("");
        data = new BarData(dataset);
        chart.setData(data);
        chart.setDescription(description);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis xAxis = chart.getXAxis();
        chart.getAxisRight().setAxisMinimum(0f);
        chart.getAxisLeft().setAxisMinimum(0f);
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
