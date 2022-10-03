package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.text.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import Common.SectionHandler;
import Common.StorageForAndroid;
import Common.ToastManager;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import android.os.Handler;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class HomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    //private SearchAdapter searchAdapterHome;
    private AdapterForSearch adapterForSearch;
    private SectionHandler sectionHandler;
    private Timer lastPricesFetchTimer;
    private static final int TIMER_IN_SECONDS = 15;
    private ConstraintLayout loadingScreen;
    private NestedScrollView successScreen;
    private ToastManager toastManager;
    private TextView dateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_main_page);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle("Stocks");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        HandleBackendCalls.initialize(this);
        loadingScreen = findViewById(R.id.showLoading);
        successScreen = findViewById(R.id.onSuccess);
        dateView = findViewById(R.id.dateDisplay);
        toastManager = new ToastManager(this);
        StorageForAndroid.initialize(this);
        sectionHandler = new SectionHandler(this, this);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        MenuItem menuItem  = menu.findItem(R.id.app_bar_menu_search);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setQueryHint("Search...");
        final SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setBackgroundColor(Color.parseColor("#FFF4F2F5"));
        searchAutoComplete.setTextColor(Color.BLACK);
        AtomicBoolean itemClicked = new AtomicBoolean(false);
        adapterForSearch = new AdapterForSearch(this, android.R.layout.simple_dropdown_item_1line);
        searchAutoComplete.setAdapter(adapterForSearch);
        searchAutoComplete.setThreshold(3);

        int START_AUTO_COMPLETE = 7;

        Handler handler = new Handler(message -> {
            if (message.what == START_AUTO_COMPLETE) {
                String query = searchAutoComplete.getText().toString();
                if (!TextUtils.isEmpty(query) && query.length()>=2) {
                    HandleBackendCalls.fetchSearchOptions(query, response -> {
                        try {
                            JSONArray jsonData = response.getJSONArray("result");
                            List<String> suggestions = new ArrayList<>();
                            for (int i = 0; i < jsonData.length(); i++) {
                                JSONObject jsonobject = jsonData.getJSONObject(i);
                                String description = jsonobject.getString("description");
                                String symbol = jsonobject.getString("symbol");
                                String type = jsonobject.getString("type");
                                if(type.equals("Common Stock") && !symbol.contains("."))
                                    suggestions.add(symbol+ " | "+description );
                            }
                            adapterForSearch.setDataAndNotify(suggestions);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> { });
                } else {
                    adapterForSearch.clearDataAndNotify();
                }
            }
            return false;
        });

        searchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (itemClicked.get()) {
                    itemClicked.set(false);
                } else {
                    HandleBackendCalls.cancelSearchOptionsReq();
                    handler.removeMessages(START_AUTO_COMPLETE);
                    handler.sendEmptyMessageDelayed(START_AUTO_COMPLETE, 300);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        searchAutoComplete.setOnItemClickListener((adapterView, view, itemIndex, id) -> {
            String formattedOption = (String) adapterView.getItemAtPosition(itemIndex);
            itemClicked.set(true);
            searchAutoComplete.setText(formattedOption);
            String ticker = formattedOption.split(" - ")[0];
            ticker = ticker.substring(0,ticker.indexOf("|")-1);
            System.out.println(ticker);
            showDetails(ticker);

        });

        return super.onCreateOptionsMenu(menu);
    }

    private void showDetails(String ticker) {
        Intent intent = new Intent(this, AllStockDetailsActivity.class);
        intent.putExtra("ticker", ticker);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sectionHandler.initializeAllSections();
        displayLoadingScreen();
        fetchLastPricesBlock();
    }

    private void displayLoadingScreen() {
        loadingScreen.setVisibility(View.VISIBLE);
        successScreen.setVisibility(View.INVISIBLE);
    }

    private void displaySuccessView() {
        loadingScreen.setVisibility(View.INVISIBLE);
        successScreen.setVisibility(View.VISIBLE);
    }

    private void fetchLastPricesBlock() {

        lastPricesFetchTimer = new Timer();
        lastPricesFetchTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                HandleBackendCalls.cancelLPRequest();
                List<String> tickers = sectionHandler.getAllTickers();
                System.out.println("tickers are" + tickers);
                //List<String> tickers = Arrays.asList("TSLA","AAPL");
                if (tickers.size() > 0) {
                    HandleBackendCalls.makeLPFetchRequest(tickers, response -> {
                        try {
                            System.out.println("response is ***************");
                            System.out.println(response);
                            Map<String, Double> lastPrices = new HashMap<>();
                            Map<String, Double> changeSinceLastClose = new HashMap<>();
                            Map<String, Double> percentChangeSinceLastClose = new HashMap<>();
                            for(int i=0;i<tickers.size();i++) {
                                JSONArray jsonData = response.getJSONArray(tickers.get(i));
                                JSONObject jsonobject = jsonData.getJSONObject(0);
                                //System.out.println(jsonData.get("o"));
                                //Converter.jsonToLastPrices(jsonData.toString());
                                lastPrices.put(tickers.get(i),jsonobject.getDouble("c"));
                                changeSinceLastClose.put(tickers.get(i),jsonobject.getDouble("d"));
                                percentChangeSinceLastClose.put(tickers.get(i),jsonobject.getDouble("dp"));
                                System.out.println(lastPrices);
                            }
                            onFetchSuccess(lastPrices,changeSinceLastClose,percentChangeSinceLastClose);
                        } catch (JSONException e) { }
                    }, error -> { });

                } else {
                    runOnUiThread(() -> onFetchSuccess(Collections.emptyMap(),Collections.emptyMap(),Collections.emptyMap()));
                }
            }
        }, 0, TimeUnit.SECONDS.toMillis(TIMER_IN_SECONDS));
    }

    private void onFetchSuccess(Map<String, Double> lastPrices,Map<String, Double> changeSinceLastClose,Map<String, Double> percentChangeSinceLastClose) {
        showDate();
        sectionHandler.updateAllSections(lastPrices,changeSinceLastClose,percentChangeSinceLastClose);
        displaySuccessView();
    }

    private void showDate() {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("America/Los_Angeles"));
        String dateString = zonedDateTime.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
        dateView.setText(dateString);
    }

    public void onFooterClick(View view) {
        Uri uri = Uri.parse("https://finnhub.io");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

}


