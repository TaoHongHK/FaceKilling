package com.example.facekilling.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.activities.AddFriendsActivity;
import com.example.facekilling.customviews.TopBar;
import com.example.facekilling.util.GetBitmap;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class Index_TwoFragment extends Fragment {

    private Context mContext;
    private View mView;
    private TopBar topBar;
    private DrawerLayout drawerLayout;
    private ViewPager mViewPager;
    private TextView friTv;
    private TextView chatTv;
    private SparseArray<Fragment> fragments = new SparseArray<>();
    private int last_id = -1;

    private MyStatePageAdapter myStatePageAdapter;

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
        myStatePageAdapter = new MyStatePageAdapter(getChildFragmentManager());
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
                PopupMenu popupMenu = new PopupMenu(getContext(),topBar.getRightButt());
                popupMenu.getMenuInflater().inflate(R.menu.add_friend_menu,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        onPopItemSelected(item);
                        return false;
                    }
                });
            }
        });
        mViewPager = (ViewPager) mView.findViewById(R.id.index_viewPager);
        friTv = (TextView) mView.findViewById(R.id.fri_page_nav_tv);
        chatTv = (TextView) mView.findViewById(R.id.chat_page_nav_tv);
        Fri_ViewPagerFragment fri_viewPagerFragment = new Fri_ViewPagerFragment();
        fragments.put(0,fri_viewPagerFragment);
        fragments.put(1,new Chat_DefaultFragment());
        mViewPager.setCurrentItem(0);
        friTv.setBackgroundResource(R.drawable.blank);
        friTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swichViewPage(0);
            }
        });
        chatTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swichViewPage(1);
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

        fri_viewPagerFragment.setItemClickListener(new Fri_ViewPagerFragment.ItemClickListener() {
            @Override
            public void onItemClicked(String userName, int id) {
                if(last_id!=id){
                    fragments.get(1).onDestroy();
                    fragments.remove(1);
                    fragments.put(1,Chat_ViewPagerFragment.newInstance(userName,id));
                    myStatePageAdapter.notifyDataSetChanged();
                    swichViewPage(1);
                    last_id = id;
                }else swichViewPage(1);
            }
        });
        mViewPager.setAdapter(myStatePageAdapter);
    }

    public void onPopItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add_friend:
                //加好友
                Intent intent = new Intent(getContext(), AddFriendsActivity.class);
                startActivity(intent);
                break;
            case R.id.sao_yi_sao:
                //扫一扫
                IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
                // 开始扫描
                intentIntegrator.initiateScan();
                //扫描结果
                break;
            case R.id.produce_erweima:
                //生成我的二维码

                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragments.clear();
    }

    public void swichViewPage(int index){
        if(mViewPager!=null){
            if (index==0){
                mViewPager.setCurrentItem(index);
                friTv.setBackgroundResource(R.drawable.blank);
                chatTv.setBackgroundResource(0);
            }else {
                mViewPager.setCurrentItem(index);
                chatTv.setBackgroundResource(R.drawable.blank);
                friTv.setBackgroundResource(0);
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 获取解析结果
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Log.d("result", "onActivityResult: "+result);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "取消扫描", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "扫描内容:" + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    class MyStatePageAdapter extends FragmentStatePagerAdapter{

        public MyStatePageAdapter(FragmentManager fm) {
            super(fm);
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return PagerAdapter.POSITION_NONE;
        }

    }

}
