package com.example.facekilling.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.fragments.Index_OneFragment;
import com.example.facekilling.fragments.Index_ThreeFragment;
import com.example.facekilling.fragments.Index_TwoFragment;
import com.example.facekilling.javabean.Cof;
import com.example.facekilling.javabean.MainUser;


import de.hdodenhof.circleimageview.CircleImageView;

public class IndexActivity extends AppCompatActivity {

    private static final int TAB_COUNT = 2;

    private DrawerLayout drawerLayout;
    private FragmentTabHost mTabHost;

    private int lastChosenTab = -1;

    private int[] unSelectedTabIcon = {R.drawable.pkgrey,R.drawable.haoyougrey,R.drawable.frigrey};
    private int[] selectedTabIcon = {R.drawable.pkcolor,R.drawable.haoyoucolor,R.drawable.fricolor};

    private static Context context;
    private Cof cof;

    public static Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        context = getApplicationContext();

        //侧边栏监控
        monitorSidebar();



        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(getTabView(R.string.tab_first, unSelectedTabIcon[0]), Index_OneFragment.class,new Bundle());
        mTabHost.addTab(getTabView(R.string.tab_second, unSelectedTabIcon[1]), Index_TwoFragment.class,new Bundle());
        mTabHost.addTab(getTabView(R.string.tab_third,unSelectedTabIcon[2]), Index_ThreeFragment.class,new Bundle());
        //设置tabs之间的分隔线不显示
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                updateTab(mTabHost);
            }
        });
        initTab(mTabHost);

        //        //从发表按钮回来
        Intent intent = getIntent();
        int id = getIntent().getIntExtra("id", 0);
        cof = (Cof)intent.getSerializableExtra("cof");
        if (id != 0) {
            switchTab(id-1);
        }
    }

    private TabHost.TabSpec getTabView(int textId, int imgId) {
        String text = getResources().getString(textId);
        Drawable drawable = getResources().getDrawable(imgId);
        View tabbar_item = getLayoutInflater().inflate(R.layout.tabbar_item, null);
        TextView tab_title = (TextView) tabbar_item.findViewById(R.id.tab_title);
        ImageView tab_img = (ImageView) tabbar_item.findViewById(R.id.tab_img);
        tab_title.setText(text);
        tab_title.setTextColor(Color.LTGRAY);
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
            textView.setTextColor(Color.LTGRAY);
        }
        View currTabView = tabHost.getCurrentTabView();
        int currTabCount = tabHost.getCurrentTab();
        ImageView imageView = (ImageView) currTabView.findViewById(R.id.tab_img);
        TextView textView = (TextView) currTabView.findViewById(R.id.tab_title);
        imageView.setImageDrawable(getResources().getDrawable(selectedTabIcon[currTabCount]));
        textView.setTextColor(getResources().getColor(R.color.colorTheme));
        lastChosenTab = currTabCount;
    }

    public void monitorSidebar(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers();
                return true;
            }
        });
        //监控侧边栏头像
        View navHeaderView = navigationView.getHeaderView(0);
        CircleImageView cirIViewHead = (CircleImageView) navHeaderView.findViewById(R.id.avatar_img);
        cirIViewHead.setImageBitmap(MainUser.getInstance().getImageBitMap());
        TextView mail = (TextView) navHeaderView.findViewById(R.id.mail);
        mail.setText(MainUser.getInstance().getEmail());
        TextView user_name = (TextView) navHeaderView.findViewById(R.id.username);
        user_name.setText(MainUser.getInstance().getUser_name());

        cirIViewHead.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Toast.makeText(getContext(),"Head",Toast.LENGTH_SHORT).show();
            }
        });
        //监控侧边栏菜单
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.nav_profile:
                        drawerLayout.closeDrawers();
                        enterProfile();
                        break;
                    case R.id.nav_mail:
                        Toast.makeText(getContext(),"click the mail",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_settings:
                        Toast.makeText(getContext(),"click the setting",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_pictures:
                        drawerLayout.closeDrawers();
                        enterPictures();
                        break;
                    default:
                        drawerLayout.closeDrawers();
                }
                return true;
            }
        });
    }
    //宿主activity中的getTitles()方法
    public Cof getCof(){
        return cof;
    }

    //进入图库
    protected void enterPictures(){
        Intent intent = new Intent(getContext(), PicturesActivity.class);
        startActivity(intent);
    }

    public void enterProfile(){
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        startActivity(intent);
    }

    public void switchTab(int index){
        mTabHost.setCurrentTab(index);
        lastChosenTab = index;
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            View v=getCurrentFocus();
            boolean  hideInputResult =isShouldHideInput(v,ev);
            if(hideInputResult){
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) IndexActivity.this
                        .getSystemService(Activity.INPUT_METHOD_SERVICE);
                if(v != null){
                    if(imm.isActive()){
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            //之前一直不成功的原因是,getX获取的是相对父视图的坐标,getRawX获取的才是相对屏幕原点的坐标！！！
            if (event.getRawX() > left && event.getRawX() < right
                    && event.getRawY() > top && event.getRawY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}