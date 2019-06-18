package com.example.facekilling.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.customviews.TopBar;

public class Index_OneActivity extends Fragment {

    private Context mContext;
    private View mView;
    private TopBar topBar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_index__one,container,false);
        topBar = (TopBar) mView.findViewById(R.id.indexOneTopBar);
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawerlayout);
        viewPager = (ViewPager) mView.findViewById(R.id.indexOneViewPager);
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
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return new MyCamera();
            }

            @Override
            public int getCount() {
                return 1;
            }
        });
        return mView;
    }

}
