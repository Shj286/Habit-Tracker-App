package com.example.habittracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Objects;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

public class ChartsFragment extends Fragment {

    private EditText editTextMotto;
    private LineChart chart;

    public ChartsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        editTextMotto = requireView().findViewById(R.id.editTextMotto);
        chart = requireView().findViewById(R.id.chart);

        // Set up chart
        setupChart();

        // Update chart with sample data
        updateChartData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_charts, container, false);
    }

    private void setupChart() {
        // Configure chart settings
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // Set chart description
        Description desc = new Description();
        desc.setText("Monthly and Yearly Completion");
        chart.setDescription(desc);
    }

    private void updateChartData() {
        ArrayList<Entry> entries = new ArrayList<>();
        // Sample data for monthly completion
        entries.add(new Entry(1, 20));
        entries.add(new Entry(2, 30));
        entries.add(new Entry(3, 40));
        entries.add(new Entry(4, 35));
        entries.add(new Entry(5, 50));
        entries.add(new Entry(6, 45));
        entries.add(new Entry(7, 55));
        entries.add(new Entry(8, 60));
        entries.add(new Entry(9, 70));
        entries.add(new Entry(10, 65));
        entries.add(new Entry(11, 75));
        entries.add(new Entry(12, 80));

        LineDataSet dataSet = new LineDataSet(entries, "Monthly Completion");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh chart
    }
}