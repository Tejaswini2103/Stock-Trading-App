package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class ViewPagerFragmentSateAdpater extends FragmentStateAdapter {

    String ticker;
    String color;
    tab1 firsttab = new tab1();
    tab2 secondtab = new tab2();

    public ViewPagerFragmentSateAdpater(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: {
                firsttab.setTicker(getTicker());
                firsttab.setColor(getColor());
                return firsttab;
            }
            case 1: {
                secondtab.setTicker(getTicker());
                return secondtab;
            }
        }
        return new tab1();
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getTicker() {
        return ticker;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
