package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class tab2 extends Fragment {

    String ticker;
    WebView chartView;

    public tab2() {	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("inside second tab");

        View view = inflater.inflate(R.layout.fragment_tab2, container, false);
        chartView = view.findViewById(R.id.detail_wv_chart22);
        System.out.println("chartView");
        System.out.println(chartView);
        WebSettings settings = chartView.getSettings();
        settings.setJavaScriptEnabled(true);
        chartView.loadUrl("file:///android_asset/tabViewCharts.html?ticker=" + ticker);
        chartView = null;
        return view;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

}
