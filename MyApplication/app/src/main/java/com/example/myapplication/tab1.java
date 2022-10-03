package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

public class tab1 extends Fragment {

    String ticker;
    String color;
    WebView chartView;

    public tab1() {	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab2, container, false);
        chartView = view.findViewById(R.id.detail_wv_chart22);
        WebSettings settings = chartView.getSettings();
        settings.setJavaScriptEnabled(true);
        chartView.loadUrl("file:///android_asset/tabViewCharts.html?ticker=" + ticker+"hourly"+getColor());
        chartView = null;
        return view;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}