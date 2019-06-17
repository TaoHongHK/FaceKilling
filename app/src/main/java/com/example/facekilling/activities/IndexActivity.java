package com.example.facekilling.activities;

import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.facekilling.R;

public class IndexActivity extends AppCompatActivity {

    private static final int TAB_COUNT = 4;

    private FragmentTabHost mTabHost;

    private int lastChosenTab = -1;

    private int[] selectedTabIcon = {R.drawable.little_fire,R.drawable.little_fire};
    private int[] unSelectedTabIcon = {R.drawable.little_fire,R.drawable.little_fire};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(getTabView(R.string.app_name, unSelectedTabIcon[0]), Index_OneActivity.class,new Bundle());
        mTabHost.addTab(getTabView(R.string.tab_second, unSelectedTabIcon[1]), Index_TwoActivity.class,new Bundle());
        //设置tabs之间的分隔线不显示
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                updateTab(mTabHost);
            }
        });
        initTab(mTabHost);
    }

    private TabHost.TabSpec getTabView(int textId, int imgId) {
        String text = getResources().getString(textId);
        Drawable drawable = getResources().getDrawable(imgId);
        View tabbar_item = getLayoutInflater().inflate(R.layout.tabbar_item, null);
        TextView tab_title = (TextView) tabbar_item.findViewById(R.id.tab_title);
        ImageView tab_img = (ImageView) tabbar_item.findViewById(R.id.tab_img);
        tab_title.setText(text);
        tab_title.setTextColor(getResources().getColor(R.color.colorTheme));
        tab_img.setImageDrawable(drawable);
        TabHost.TabSpec spec = mTabHost.newTabSpec(text).setIndicator(tabbar_item);
        return spec;
    }

    private void initTab(TabHost tabHost){
        tabHost.setCurrentTab(0);
        View currTabView = tabHost.getCurrentTabView();
        ImageView imageView = (ImageView) currTabView.findViewById(R.id.tab_img);
        TextView textView = (TextView) currTabView.findViewById(R.id.tab_title);
        imageView.setImageDrawable(getResources().getDrawable(selectedTabIcon[0]));
        textView.setTextColor(getResources().getColor(R.color.colorTheme));
        lastChosenTab = 0;
    }

    private void updateTab(TabHost tabHost){
        if(lastChosenTab!=-1){
            View lastTabView = tabHost.getTabWidget().getChildTabViewAt(lastChosenTab);
            ImageView imageView = (ImageView) lastTabView.findViewById(R.id.tab_img);
            TextView textView = (TextView) lastTabView.findViewById(R.id.tab_title);
            imageView.setImageDrawable(getResources().getDrawable(unSelectedTabIcon[lastChosenTab]));
            textView.setTextColor(getResources().getColor(R.color.colorTheme));
        }
        View currTabView = tabHost.getCurrentTabView();
        int currTabCount = tabHost.getCurrentTab();
        ImageView imageView = (ImageView) currTabView.findViewById(R.id.tab_img);
        TextView textView = (TextView) currTabView.findViewById(R.id.tab_title);
        imageView.setImageDrawable(getResources().getDrawable(selectedTabIcon[currTabCount]));
        textView.setTextColor(getResources().getColor(R.color.colorTheme));
        lastChosenTab = currTabCount;
    }
}