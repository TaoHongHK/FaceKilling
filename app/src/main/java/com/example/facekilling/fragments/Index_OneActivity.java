package com.example.facekilling.fragments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.customviews.TopBar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Index_OneActivity extends Fragment {

    private View mView;
    private TopBar topBar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private List<Integer> viewPagerTitles =
            new ArrayList<>(Arrays.asList(R.string.viewpager_one,R.string.viewpager_two));
    private TextView viewpagerOneTv;
    private TextView viewpagerTwoTv;
    private SparseArray<Fragment> mViewPagerFragments = new SparseArray<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_index__one,container,false);
        initView();
        return mView;
    }

    public void initView(){
        topBar = (TopBar) mView.findViewById(R.id.indexOneTopBar);
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawerlayout);
        viewPager = (ViewPager) mView.findViewById(R.id.indexOneViewPager);
        viewpagerOneTv = (TextView) mView.findViewById(R.id.viewpager_oneTv);
        viewpagerTwoTv = (TextView) mView.findViewById(R.id.viewpager_twoTv);
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
        viewPager.setOffscreenPageLimit(viewPagerTitles.size());
        viewPager.setCurrentItem(0);
        viewpagerOneTv.setBackgroundResource(R.drawable.blank);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                Fragment fragment = null;
               switch (i){
                   case 0:
                   fragment = new YanZhiCamera();
                   break;
                   case 1:
                       fragment = new BiaoQingBaoCamera();
                       break;
               }
               return fragment;
            }
            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                Fragment fragment = (Fragment) super.instantiateItem(container, position);
                mViewPagerFragments.put(position,fragment);
                return fragment;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                mViewPagerFragments.remove(position);
                super.destroyItem(container, position, object);
            }

            @Override
            public int getCount() {
                return viewPagerTitles.size();
            }
        });
        viewpagerOneTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        viewpagerTwoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              viewPager.setCurrentItem(1);
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }
            @Override
            public void onPageSelected(int i) {
               switch (i){
                   case 0:
                       viewpagerOneTv.setBackgroundResource(R.drawable.blank);
                       viewpagerTwoTv.setBackgroundResource(0);
                   break;
                   case 1:
                       viewpagerTwoTv.setBackgroundResource(R.drawable.blank);
                       viewpagerOneTv.setBackgroundResource(0);
                   break;
               }
            }
            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

}
