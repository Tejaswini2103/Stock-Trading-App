package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import android.text.SpannableString;
import org.json.JSONException;
import org.json.JSONObject;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import Details.Common.NewsItem;
import Details.Common.Info;
import Details.News.NewsRVInitializer;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager2.widget.ViewPager2;

import com.squareup.picasso.Picasso;

import Details.Common.Formatter;
import Common.StorageForAndroid;
import Common.ToastManager;

import Details.Portfolio.PortfolioController;
import Favorites.FavoritesItem;
import java.util.List;
import org.json.JSONArray;

public class AllStockDetailsActivity extends AppCompatActivity {

    private String ticker;
    Toolbar toolbar;
    private ConstraintLayout loadingView;
    private NestedScrollView successView;
    private ToastManager toastManager;
    String imageUrl = null;
    String changeAndPercentChange = null;
    private FetchStatus fetchStatus;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    ViewPagerFragmentSateAdpater viewPagerFragmentSateAdpater = new ViewPagerFragmentSateAdpater(this);
    private String[] titles = new String[]{"Tab1", "Tab2"};
    private Info info;
    private int[] icons = {
            R.mipmap.chartline_round,
            R.mipmap.clocktime
    };

    private int[] iconsSelected = {
            R.drawable.chartline,
            R.drawable.clockthree
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_StockTrading);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailspage_activity);
        initializeToolBar();
        Intent intent = getIntent();

        ticker = intent.getStringExtra("ticker");
        toolbar = (Toolbar) findViewById(R.id.toolbar_detailsPage);
        toolbar.setTitle(ticker);
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        System.out.println("ticker is ************************* "+ ticker);
        loadingView = findViewById(R.id.loadingIcon_detailsView);
        successView = findViewById(R.id.onSuccess_detailsView);
        toastManager = new ToastManager(this);

        StorageForAndroid.initialize(this);
        HandleBackendCalls.initialize(this);
        info = new Info();
        fetchStatus = new FetchStatus();
        fetchStatus.loading();
        DisplayLoadingView();
        startFetchingEverything();
    }

    private void initializeToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_detailsPage);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    public String getTicker() {
        return ticker;
    }
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    private void DisplayLoadingView() {
        loadingView.setVisibility(View.VISIBLE);
        successView.setVisibility(View.INVISIBLE);
    }

    private void startFetchingEverything() {
        HandleBackendCalls.fetchInfo(ticker, response -> {
            try {
                JSONObject jsonData = response.getJSONObject("info");
                System.out.println("Summary is ******************");
                System.out.println(jsonData);
                info.setDescription(jsonData.getString("name"));
                imageUrl = jsonData.getString("logo");
                info.setIndustry(jsonData.getString("finnhubIndustry"));
                info.setWeburl(jsonData.getString("weburl"));
                info.setIpostartDate(jsonData.getString("ipo"));
                info.setTicker(jsonData.getString("ticker"));
                System.out.println(info.getDescription());
                getPrices();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> { });

    }

    private void getPrices() {
        HandleBackendCalls.fetchCurrentPrices(ticker, response -> {
            try {
                JSONObject jsonData = response.getJSONObject("priceinfo");
                info.setHighPrice(jsonData.getDouble("h"));
                info.setLowPrice(jsonData.getDouble("l"));
                info.setOpenPrice(jsonData.getDouble("o"));
                info.setBidPrice(jsonData.getDouble("pc"));
                info.setLastPrice(jsonData.getDouble("c"));
                System.out.println("****************** ");
                System.out.println(info.getLastPrice());
                info.setChange(jsonData.getDouble("d"));


                tabLayout = findViewById(R.id.tab_layout);
                viewPager = findViewById(R.id.view_pager);
                viewPagerFragmentSateAdpater.setTicker(ticker);
                if(info.getChange()>0)
                    viewPagerFragmentSateAdpater.setColor("green");
                else
                    viewPagerFragmentSateAdpater.setColor("red");
                viewPager.setAdapter(viewPagerFragmentSateAdpater);
                new TabLayoutMediator(tabLayout, viewPager,
                        (tab, position) -> tab.setIcon(icons[position])
                ).attach();


                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if(tab.getPosition()==0) {
                            tab.setIcon(iconsSelected[0]);
                        }
                        if(tab.getPosition()==1) {
                            tab.setIcon(iconsSelected[1]);
                        }
                    }
                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        if(tab.getPosition()==0) {
                            tab.setIcon(icons[0]);
                        }
                        if(tab.getPosition()==1) {
                            tab.setIcon(icons[1]);
                        }
                    }
                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }
                });

                changeAndPercentChange = "$ "+String.format(Formatter.getPriceString(Math.round(jsonData.getDouble("d")*100.00)/100.00))+" ( "+String.format(Formatter.getPriceString(Math.round(jsonData.getDouble("dp")*100.00)/100.00))+" % )";
                onFetchSuccess();
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }, error -> { });
    }

    private void onFetchSuccess() {
        System.out.println("info is ***********************");
        System.out.println(info);
        initializeInfoView();
        initializePortfolioView();
        initializeStatsView();
        initializeAboutView();
        initializeNewsView();
        initalizeSocialSentimentsView();
        initializeChartView();
        initializeEarningsView();
        showSuccessLayout();
        fetchStatus.success();
    }

    private void initializeInfoView() {
        ImageView imageView = findViewById(R.id.icon_image);
        Picasso.get().load(imageUrl).into(imageView);
        TextView tickerView = findViewById(R.id.tickerDisplay_detailsview);
        tickerView.setText(ticker);
        TextView nameView = findViewById(R.id.stockName_detailsview);
        nameView.setText(info.getDescription());
        TextView lastPriceView = findViewById(R.id.lastPriceDisplay_detailsview);
        DecimalFormat formatter = new DecimalFormat("'$'#0.00;-'$'#0.00");
        lastPriceView.setText(formatter.format(info.getLastPrice()));
        TextView changeView = findViewById(R.id.priceChangeandPercent_detailsview);
        changeView.setText(changeAndPercentChange);
        changeView.setTextColor(getColor(info.getChangeColor()));
        ImageView trendingIcon = findViewById(R.id.upOrDownTrending);
        if(info.getChange()>=0) {
            trendingIcon.setImageResource(R.mipmap.greentrendingup);
        }
        else {
            trendingIcon.setImageResource(R.mipmap.trendingdownred);
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void initializeChartView() {

        System.out.println("inside recommendation chart view");
        WebView chartView23 = findViewById(R.id.trends_view);
        WebSettings settings23 = chartView23.getSettings();
        settings23.setJavaScriptEnabled(true);
        chartView23.loadUrl("file:///android_asset/trends.html?ticker="+ticker);

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initializeEarningsView() {

        System.out.println("inside earnings chart view");
        WebView chartView23 = findViewById(R.id.earnings_view);
        WebSettings settings23 = chartView23.getSettings();
        settings23.setJavaScriptEnabled(true);
        chartView23.loadUrl("file:///android_asset/earnings.html?ticker="+ticker);
    }

    @Override
    public void onResume() {
        System.out.println("inside onResume method");
        super.onResume();
        if(viewPager != null && viewPager.getAdapter() != null) {
            viewPager.getAdapter().notifyDataSetChanged();
        }
    }

    private void initializePortfolioView() {
        PortfolioController controller = new PortfolioController(this, toastManager, info);
        controller.show();
    }

    private void initializeStatsView() {
        TextView openPriceView = findViewById(R.id.actual_open_price);
        openPriceView.setText("Open Price : $"+Formatter.getPriceString(info.getOpenPrice()));
        TextView highPriceView = findViewById(R.id.actual_high_price);
        highPriceView.setText("High Price : $"+Formatter.getPriceString(info.getHighPrice()));
        TextView lowPriceView = findViewById(R.id.actual_low_price);
        lowPriceView.setText("Low Price : $"+Formatter.getPriceString(info.getLowPrice()));
        TextView prevClose = findViewById(R.id.actual_prev_close);
        prevClose.setText("Prev Close : $"+String.format(Formatter.getPriceString(info.getBidPrice())));
    }

    private void initializeAboutView() {

        TextView ipo = findViewById(R.id.actual_ipo_start_date);

        String startDate = info.getIpostartDate();
        String dateonly = startDate.substring(0,startDate.indexOf("T"));

        String datesplit[] = dateonly.split("-");
        String ipoDate = "";
        ipoDate = ipoDate.concat(datesplit[2]+"-");
        ipoDate = ipoDate.concat(datesplit[1]+"-");
        ipoDate = ipoDate.concat(datesplit[0]);
        ipo.setText(ipoDate);

        TextView industry = findViewById(R.id.actual_industry);
        industry.setText(info.getIndustry());
        TextView webpage = findViewById(R.id.actual_webpage);
        System.out.println("************************");
        System.out.println(info.getWeburl());
        System.out.println("************************");
        webpage.setText(info.getWeburl());
        TextView peers = findViewById(R.id.actual_peers);

        peers.setMovementMethod(new ScrollingMovementMethod());

        HandleBackendCalls.makePeersFetchRequest(ticker, response -> {
            try {
                JSONArray peersArray = response.getJSONArray("peers");
                String text = "";
                int i=0;
                for(i=0;i<peersArray.length()-1;i++) {
                    text = text.concat(peersArray.getString(i)+" ");
                }
                text = text.concat(peersArray.getString(i));

                text = text.concat(" XRX");
                text = text.concat(" SMCI");
                text = text.concat(" NTAP");
                SpannableString ss = new SpannableString(text);
                String splitText[] = text.split(" ");
                i = 0;
                for(i=0;i<splitText.length;i++) {
                    String s = splitText[i];
                    int start = text.indexOf(s);
                    int end = start+s.length();
                    ss.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(AllStockDetailsActivity.this, AllStockDetailsActivity.class);
                            intent.putExtra("ticker", s);
                            startActivity(intent);
                        }
                        @Override
                        public void updateDrawState(TextPaint ts) {
                            super.updateDrawState(ts);
                            ts.setColor(Color.BLUE);
                        }
                    },start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }


                peers.setText(ss);
                peers.setMovementMethod(LinkMovementMethod.getInstance());

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }, error -> {

        });

    }

    private void initializeNewsView() {

        List<NewsItem> news = new ArrayList<>();
        HandleBackendCalls.fetchNews(ticker, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("companynews");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    String title = jsonobject.getString("headline");
                    String publishedAt = "today";
                    String publisher = jsonobject.getString("source");
                    String urlToImage = jsonobject.getString("image");
                    String url = jsonobject.getString("url");
                    String summary = jsonobject.getString("summary");
                    if((url.length()>=10 && urlToImage.length()>=10)) {
                        NewsItem item = new NewsItem(publisher, publishedAt, title, url, urlToImage, summary);
                        news.add(item);
                        System.out.println(title + " " + publishedAt + " " + publisher + " " + urlToImage + " " + url);
                    }
                    if(news.size()==20)
                        break;
                }

                new NewsRVInitializer(this, this, news);

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }, error -> {

        });

    }

    public void initalizeSocialSentimentsView() {
        HandleBackendCalls.fetchMentions(ticker, response -> {
            try {
                JSONArray redditData = response.getJSONArray("reddit");
                JSONArray twitterData = response.getJSONArray("twitter");

                int twitterMentions = 0;
                int twitterPositiveMentions = 0;
                int twitterNegativeMentions = 0;
                int redditMentions = 0;
                int redditPositiveMentions = 0;
                int redditNegativeMentions = 0;

                for(int i =0;i< twitterData.length();i++) {
                    JSONObject jsonobjectTwitter = twitterData.getJSONObject(i);
                    twitterMentions = twitterMentions + jsonobjectTwitter.getInt("mention");
                    twitterPositiveMentions = twitterPositiveMentions + jsonobjectTwitter.getInt("positiveMention");
                    twitterNegativeMentions = twitterNegativeMentions + jsonobjectTwitter.getInt("negativeMention");
                }

                for(int i =0;i<redditData.length();i++) {
                    JSONObject jsonobjectReddit = redditData.getJSONObject(i);
                    redditMentions = redditMentions + jsonobjectReddit.getInt("mention");
                    redditPositiveMentions = redditPositiveMentions + jsonobjectReddit.getInt("positiveMention");
                    redditNegativeMentions = redditNegativeMentions + jsonobjectReddit.getInt("negativeMention");
                }

                TextView tickername = findViewById(R.id.ticker_name);
                tickername.setText(info.getDescription());
                TextView redditTM = findViewById(R.id.reddit_total_mentions);
                redditTM.setText(String.valueOf(redditMentions));
                TextView redditPM = findViewById(R.id.reddit_positive_mentions);
                redditPM.setText(String.valueOf(redditPositiveMentions));
                TextView redditNM = findViewById(R.id.reddit_negative_mentions);
                redditNM.setText(String.valueOf(redditNegativeMentions));
                TextView twitterTM = findViewById(R.id.twitter_total_mentions);
                twitterTM.setText(String.valueOf(twitterMentions));
                TextView twitterPM = findViewById(R.id.twitter_positive_mentions);
                twitterPM.setText(String.valueOf(twitterPositiveMentions));
                TextView twitterNM = findViewById(R.id.twitter_negative_mentions);
                twitterNM.setText(String.valueOf(twitterNegativeMentions));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            System.out.println("Mentions API call has failed");
        });
    }

    private void showSuccessLayout() {
        loadingView.setVisibility(View.INVISIBLE);
        successView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        HandleBackendCalls.cancelDetailFetchRequest();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorites, menu);
        MenuItem item = menu.findItem(R.id.favorite_ticker_detailsView);
        int icon = getFavoriteIcon(StorageForAndroid.isFavorite(ticker));
        item.setIcon(icon);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (itemId == R.id.favorite_ticker_detailsView) {
            onFavoriteClicked(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onFavoriteClicked(MenuItem item) {

        boolean isFavorite = StorageForAndroid.isFavorite(ticker);
        if (isFavorite) {
            StorageForAndroid.removeFromFavorites(ticker);
            toastManager.showToast(String.format("%s is removed from favorites", ticker));
        } else {
            FavoritesItem favoritesItem = FavoritesItem.newItem(ticker, info.getDescription(), info.getLastPrice());
            System.out.println(ticker+" "+ info.getName()+" "+ info.getLastPrice());
            StorageForAndroid.addToFavorites(favoritesItem);
            toastManager.showToast(String.format("%s is added to favorites", ticker));
        }
        int icon = getFavoriteIcon(!isFavorite);
        item.setIcon(icon);

    }

    private int getFavoriteIcon(boolean isFavorite) {
        return isFavorite ? R.mipmap.star_icon_red_full : R.mipmap.star_icon_red_empty;
    }
}

