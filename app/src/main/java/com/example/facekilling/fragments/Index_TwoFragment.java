package com.example.facekilling.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.customviews.TopBar;

public class Index_TwoFragment extends Fragment {

    private Context mContext;
    private View mView;
    private TopBar topBar;
    private DrawerLayout drawerLayout;
    private ViewPager mViewPager;
    private TextView friTv;
    private TextView chatTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_index__two,container,false);
        initView();

        return mView;
    }

    public void initView(){
        topBar = (TopBar) mView.findViewById(R.id.indexTwoTopBar);
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawerlayout);
        topBar.setClickListener(new TopBar.TopbarClickListener() {
            @Override
            public void leftClicked() {
                Toast.makeText(getContext(),"FaceKilling",Toast.LENGTH_SHORT).show();
                drawerLayout.openDrawer(Gravity.START);
            }

            @Override
            public void rightClicked() {
            }
        });
        mViewPager = (ViewPager) mView.findViewById(R.id.index_viewPager);
        friTv = (TextView) mView.findViewById(R.id.fri_page_nav_tv);
        chatTv = (TextView) mView.findViewById(R.id.chat_page_nav_tv);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                Fragment fragment = null;
                switch (i){
                    case 0:
                        fragment = new Fri_ViewPagerFragment();
                        break;
                    case 1:
                        fragment = new Chat_DefaultFragment();
                        break;
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        mViewPager.setCurrentItem(0);
        friTv.setBackgroundResource(R.drawable.blank);
        friTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
                friTv.setBackgroundResource(R.drawable.blank);
                chatTv.setBackgroundResource(0);
            }
        });
        chatTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
                chatTv.setBackgroundResource(R.drawable.blank);
                friTv.setBackgroundResource(0);
            }
        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i){
                    case 0:
                        friTv.setBackgroundResource(R.drawable.blank);
                        chatTv.setBackgroundResource(0);
                        break;
                    case 1:
                        chatTv.setBackgroundResource(R.drawable.blank);
                        friTv.setBackgroundResource(0);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
}
