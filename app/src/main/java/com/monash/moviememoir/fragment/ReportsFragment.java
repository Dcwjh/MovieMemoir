package com.monash.moviememoir.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.monash.moviememoir.R;
import com.monash.moviememoir.networkconnect.ReportNetworkConnection;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReportsFragment extends Fragment {

    private int personId;

    private EditText etStartDate;
    private EditText etEndDate;
    private Button btnSearch;

    private PieChart pieChart;
    private BarChart barChart;

    private Spinner spYear;

    private int currentYear;
    private int currentMonth;
    private int currentDay;
    private  int userid;
    private Intent intent;

    private ReportNetworkConnection reportNetworkConnection;
    public static final int[] PIE_COLORS = {
            Color.rgb(181, 194, 202), Color.rgb(129, 216, 200), Color.rgb(241, 214, 145),
            Color.rgb(108, 176, 223), Color.rgb(195, 221, 155), Color.rgb(251, 215, 191),
            Color.rgb(237, 189, 189), Color.rgb(172, 217, 243)
    };


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_report, container, false);
        intent = getActivity().getIntent();
        userid = intent.getIntExtra("userid",5);
        initView(view);
        initAction();

        return view;
    }

    private void initView(View view) {
        etStartDate = view.findViewById(R.id.wlf_bengindate);
        etEndDate = view.findViewById(R.id.wlf_enddate);
        btnSearch = view.findViewById(R.id.wlf_btnshowpie);

        pieChart = view.findViewById(R.id.wlf_pie);
        barChart = view.findViewById(R.id.wlf_table);
        spYear = view.findViewById(R.id.wlf_year);

        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        reportNetworkConnection = new ReportNetworkConnection();

    }

    @SuppressLint("DefaultLocale")
    private void initAction() {
        etStartDate.setFocusable(false);
        etStartDate.setFocusableInTouchMode(false);
        etEndDate.setFocusable(false);
        etEndDate.setFocusableInTouchMode(false);

        String startDate = String.format("%d-%02d-%02d", currentYear - 1, currentMonth, currentDay);
        String endDate = String.format("%d-%02d-%02d", currentYear, currentMonth, currentDay);
//        etStartDate.setText(startDate);
//        etEndDate.setText(endDate);

        new LoadPieChartTask(startDate,endDate).execute();

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        currentYear = year;
                        currentMonth = month + 1;
                        currentDay = dayOfMonth;
                        etStartDate.setText(String.format("%d-%02d-%02d", year, month + 1, dayOfMonth));
                    }
                }, currentYear, currentMonth, currentDay).show();
            }
        });

        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        currentYear = year;
                        currentMonth = month + 1;
                        currentDay = dayOfMonth;
                        etEndDate.setText(String.format("%d-%02d-%02d", year, month + 1, dayOfMonth));
                    }
                }, currentYear, currentMonth, currentDay).show();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDate = etStartDate.getText().toString();
                String endDate = etEndDate.getText().toString();
                if (startDate.isEmpty() || endDate.isEmpty()) {
                    Toast.makeText(getContext(), "Please input the right date", Toast.LENGTH_SHORT).show();
                } else {
                    new LoadPieChartTask(startDate, endDate).execute();
                }
            }
        });

        final List<String> list = new ArrayList<>();
        list.add(String.valueOf(currentYear));
        list.add(String.valueOf(currentYear - 1));
        list.add(String.valueOf(currentYear - 2));
        list.add(String.valueOf(currentYear - 3));
        list.add(String.valueOf(currentYear - 4));
        list.add(String.valueOf(currentYear - 5));
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        spYear.setAdapter(yearAdapter);

        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String year = list.get(position);
                new LoadBarChartTask().execute(year);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private class LoadPieChartTask extends AsyncTask<Void, Void, List<Map<String, Object>>> {
        private String startdate;
        private String enddate;

        public LoadPieChartTask(String startdate, String enddate){
            this.startdate = startdate;
            this.enddate = enddate;
        }

        @Override
        protected List<Map<String, Object>> doInBackground(Void...voids) {
            return reportNetworkConnection.getPostcodeDataByUseridDate(userid, startdate, enddate);
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> response) {
            if (null != response) {
                Map<String, Double> pieValues = new HashMap<>();
                for (Map<String, Object> item : response) {
                    String postcode =  item.get("postcode").toString();
                    Double number = (Double) item.get("apperence number");
                    pieValues.put(postcode, number);
                }
                setPieChart(pieChart, pieValues, "", true);
            }
        }
    }

    private class LoadBarChartTask extends AsyncTask<String, Void, List<Map<String, Object>>> {
        @Override
        protected List<Map<String, Object>> doInBackground(String... params) {
            return reportNetworkConnection.getMonthCountByUseridYear(userid, params[0]);
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> response) {
            if (null != response) {
                List<Integer> barValues = new ArrayList<Integer>(Collections.nCopies(12, 0));
                for (Map<String, Object> item : response) {
                    int month = Integer.valueOf( item.get("month").toString());
                    int number = Integer.valueOf(item.get("watchedMovieNumber").toString());
                    barValues.set(month - 1, number);
                }
                setBarChart(barChart, barValues, true);
            }
        }
    }


    public void setPieChart(PieChart pieChart, Map<String, Double> pieValues, String title, boolean showLegend) {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(25, 10, 25, 25);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setCenterText(title);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.setCenterTextSize(22f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationAngle(120f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        Legend legend = pieChart.getLegend();
        if (showLegend) {
            legend.setEnabled(true);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            legend.setDrawInside(false);
            legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        } else {
            legend.setEnabled(false);
        }
        setPieChartData(pieChart, pieValues);
    }

    private void setPieChartData(PieChart pieChart, Map<String, Double> pieValues) {

        List<PieEntry> entries = new ArrayList<>();
        Set set = pieValues.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            entries.add(new PieEntry(Double.valueOf((Double) entry.getValue()).intValue(), entry.getKey().toString()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(PIE_COLORS);

        dataSet.setValueLinePart1OffsetPercentage(80f);
        dataSet.setValueLinePart1Length(0.3f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setValueLineColor(Color.YELLOW);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.DKGRAY);

        pieChart.setData(pieData);
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }

    public void setBarChart(BarChart barChart, List<Integer> barValues, boolean showLegend) {
        barChart.setBackgroundColor(Color.WHITE);
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);
        barChart.setDrawBorders(true);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new MyXAxisFormatter());
        xAxis.setGranularity(0.5f);
        xAxis.setLabelRotationAngle(-60);
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum(11.5f);
        xAxis.setLabelCount(12);
        xAxis.setTextSize(12f);
        xAxis.setTypeface(Typeface.DEFAULT_BOLD);

        Integer max = Collections.max(barValues);
        YAxis axisLeft = barChart.getAxisLeft();
        axisLeft.setAxisMinimum(0);
        axisLeft.setAxisMaximum(max + 1);
        axisLeft.setLabelCount(max);
        barChart.getAxisRight().setEnabled(false);


        Legend legend = barChart.getLegend();
        if (showLegend) {
            legend.setForm(Legend.LegendForm.LINE);
            legend.setTextSize(11f);
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        } else {
            legend.setDrawInside(false);
        }
        setBarChart(barChart, barValues);
    }

    public void setBarChart(BarChart barChart, List<Integer> barValues) {
        List<BarEntry> barEntryList = new ArrayList<>();
        for (int i = 0; i < barValues.size(); i++) {
            barEntryList.add(new BarEntry(i, barValues.get(i)));
        }

        BarDataSet dataSet = new BarDataSet(barEntryList, "");
        BarData data = new BarData(dataSet);
        data.setValueTextSize(10f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueFormatter(new MyValueFormatter());
        data.setBarWidth(0.9f);
        barChart.setData(data);
        barChart.invalidate();
    }

//    private void setPieChartData(PieChart pieChart, Map<String, Double> pieValues) {
//        List<PieEntry> entries = new ArrayList<>();
//        Set set = pieValues.entrySet();
//        Iterator it = set.iterator();
//        while (it.hasNext()) {
//            Map.Entry entry = (Map.Entry) it.next();
//            entries.add(new PieEntry(Double.valueOf((Double) entry.getValue()).intValue(), entry.getKey().toString()));
//        }
//
//        PieDataSet dataSet = new PieDataSet(entries, "");
//        dataSet.setSliceSpace(3f);
//        dataSet.setSelectionShift(5f);
//        dataSet.setColors(PIE_COLORS);
//
//        dataSet.setValueLinePart1OffsetPercentage(80f);
//        dataSet.setValueLinePart1Length(0.3f);
//        dataSet.setValueLinePart2Length(0.4f);
//        dataSet.setValueLineColor(Color.YELLOW);
//        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        PieData pieData = new PieData(dataSet);
//        pieData.setValueFormatter(new PercentFormatter());
//        pieData.setValueTextSize(11f);
//        pieData.setValueTextColor(Color.DKGRAY);
//
//        pieChart.setData(pieData);
//        pieChart.highlightValues(null);
//        pieChart.invalidate();
//    }



    private class MyXAxisFormatter extends ValueFormatter {

        private List<String> months = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec");

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            if (value >= 0 && value <= 11) {
                return months.get((int) value);
            } else {
                return "";
            }

        }
    }

    private class MyValueFormatter extends ValueFormatter {

        @Override
        public String getFormattedValue(float value) {
            DecimalFormat df = new DecimalFormat("#");
            return df.format(value);
        }
    }


}
