package com.example.facekilling.fragments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.activities.BiaoQingBaoCameraActivity;
import com.example.facekilling.activities.YanZhiCameraActivity;
import com.example.facekilling.customviews.TopBar;

public class Index_OneFragment extends Fragment {

    private View mView;
    private TopBar topBar;
    private DrawerLayout drawerLayout;
    private ImageView clickImg;



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
        clickImg = (ImageView) mView.findViewById(R.id.click_img);
        topBar.setClickListener(new TopBar.TopbarClickListener() {
            @Override
            public void leftClicked() {
                drawerLayout.openDrawer(Gravity.START);
            }

            @Override
            public void rightClicked() {
            }
        });

        clickImg.setOnClickListener(new TouchListener());

    }

    class TouchListener implements View.OnClickListener {
        private int imageHeight;
        private int imageWidth;
        private int touchX;
        private int touchY;
        private boolean isTouch = false;



        @Override
        public void onClick(View v) {
            imageHeight = v.getHeight();
            imageWidth = v.getWidth();
            v.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    touchX = (int)event.getX();
                    touchY = (int) event.getY();
                    isTouch = true;
                    return false;
                }
            });
            if(isTouch){
                double k = -(0.261447 * imageHeight)/imageWidth;
                double b = 0.647623 * imageHeight;
                if( (touchY < k * touchX + b)){
                    Intent intent = new Intent(getContext(), YanZhiCameraActivity.class);
                    startActivity(intent);
                }
                else if(touchY > k * touchX + b){
                    Intent intent = new Intent(getContext(), BiaoQingBaoCameraActivity.class);
                    startActivity(intent);
                }
                isTouch = false;
            }

        }
    }

}
