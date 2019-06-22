package com.example.facekilling.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Dimension;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.facekilling.R;
import com.example.facekilling.util.DensityUtil;

public class TopBar extends LinearLayout {

    private TopbarClickListener clickListener;

    private Button leftButt,rightButt;
    private TextView leftTv,rightTv,tvTitle;
    private int backgroundColor;

    private int leftTextColor;
    private int leftButtIcon;
    private int leftTextBackground;
    private String leftText;
    private boolean leftTextVisible;
    private boolean leftButtVisible;
    private float leftButtWidth;
    private float leftButtHeight;


    private int rightTextColor;
    private int rightButtIcon;
    private String rightText;
    private boolean rightTextVisible;
    private int rightTextBackground;
    private boolean rightButtVisible;
    private float rightButtWidth;
    private float rightButtHeight;

    private float titleTextSize;
    private int titleTextColor;
    private String title;
    private boolean titleVisible;

    public interface TopbarClickListener{

        public void leftClicked();

        public void rightClicked();
    }

    public void setClickListener(TopbarClickListener clickListener){
        this.clickListener = clickListener;
    }


    public TopBar(Context context, AttributeSet attrs){
        super(context, (AttributeSet) attrs);
        initView(attrs);
    }

    public void initView(AttributeSet attrs){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,R.styleable.TopBar);

        backgroundColor = typedArray.getColor(R.styleable.TopBar_topBarBackgroundColor,0);

        //左边文本
        if (typedArray.hasValue(R.styleable.TopBar_leftText)){
            leftText = typedArray.getString(R.styleable.TopBar_leftText);
        }
        leftTextColor = typedArray.getColor(R.styleable.TopBar_leftTextColor,0);
        leftTextVisible = typedArray.getBoolean(R.styleable.TopBar_leftTextVisible,false);
        leftTextBackground = typedArray.getResourceId(R.styleable.TopBar_leftTextBackground,-1);

        //左边按钮
        leftButtIcon = typedArray.getResourceId(R.styleable.TopBar_leftButtIcon,-1);
        leftButtVisible = typedArray.getBoolean(R.styleable.TopBar_leftButtVisible,true);
        leftButtWidth = typedArray.getDimension(R.styleable.TopBar_leftButtWidth,R.dimen.topbar_butt_width);
        leftButtHeight = typedArray.getDimension(R.styleable.TopBar_leftButtHeight,R.dimen.topbar_butt_height);

        //右边文本
        if (typedArray.hasValue(R.styleable.TopBar_rightText)){
            rightText = typedArray.getString(R.styleable.TopBar_rightText);
        }
        rightTextColor = typedArray.getColor(R.styleable.TopBar_rightTextColor,0);
        rightTextVisible = typedArray.getBoolean(R.styleable.TopBar_rightTextVisible,false);
        rightTextBackground = typedArray.getResourceId(R.styleable.TopBar_rightTextBackground,-1);

        //右边按钮
        rightButtIcon = typedArray.getResourceId(R.styleable.TopBar_rightButtIcon,-1);
        rightButtVisible = typedArray.getBoolean(R.styleable.TopBar_rightButtVisible,false);
        rightButtWidth = typedArray.getDimension(R.styleable.TopBar_rightButtWidth,R.dimen.topbar_butt_width);
        rightButtHeight = typedArray.getDimension(R.styleable.TopBar_rightButtHeight,R.dimen.topbar_butt_height);

        //中间标题
        titleTextSize = typedArray.getDimension(R.styleable.TopBar_titleTextSize,0);
        titleTextColor = typedArray.getColor(R.styleable.TopBar_titleTextColor,0);
        title = typedArray.getString(R.styleable.TopBar_title);
        titleVisible = typedArray.getBoolean(R.styleable.TopBar_titleVisible,true);

        typedArray.recycle();

        View topBarLayout = LayoutInflater.from(getContext()).inflate(R.layout.topbar,null);

        leftButt = (Button) topBarLayout.findViewById(R.id.toolbar_left_btn);
        leftTv = (TextView) topBarLayout.findViewById(R.id.toolbar_left_tv);
        rightButt = (Button)topBarLayout.findViewById(R.id.toolbar_right_btn);
        rightTv = (TextView) topBarLayout.findViewById(R.id.toolbar_right_tv);
        tvTitle = (TextView) topBarLayout.findViewById(R.id.toolbar_title_tv);

        leftTv.setTextColor(leftTextColor);
        leftTv.setText(leftText);
        if(!leftTextVisible){
            leftTv.setVisibility(GONE);
        }
        else{
            leftTv.setVisibility(VISIBLE);
            if(leftTextBackground!=-1)
                leftTv.setBackground(getResources().getDrawable(leftTextBackground));
        }
        if(leftButtIcon != -1){
            leftButt.setBackgroundResource(leftButtIcon);
        }
        leftButt.setWidth(DensityUtil.dp2dx(getContext(),leftButtWidth));
        leftButt.setHeight(DensityUtil.dp2dx(getContext(),leftButtHeight));
        setButtVisible(leftButt,leftButtVisible);
        leftButt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.leftClicked();
            }
        });

        rightTv.setTextColor(rightTextColor);
        rightTv.setText(rightText);
        if(!rightTextVisible){
            rightTv.setVisibility(GONE);
        }
        else {
            rightTv.setVisibility(VISIBLE);
            if(rightTextBackground!=-1)
                rightTv.setBackground(getResources().getDrawable(rightTextBackground));
        }
        if(rightButtIcon != -1){
            rightButt.setBackgroundResource(rightButtIcon);
        }
        rightButt.setWidth(DensityUtil.dp2dx(getContext(),rightButtWidth));
        rightButt.setHeight(DensityUtil.dp2dx(getContext(),rightButtHeight));
        setButtVisible(rightButt,rightButtVisible);
        rightButt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.rightClicked();
            }
        });

        tvTitle.setText(title);
        if(0 != titleTextColor){
            tvTitle.setTextColor(titleTextColor);
        }
        if(0 != titleTextSize){
            tvTitle.setTextSize(titleTextSize);
        }
        tvTitle.setGravity(Gravity.CENTER);
        if(!titleVisible){
            tvTitle.setVisibility(GONE);
        }
        else {
            tvTitle.setVisibility(VISIBLE);
        }

        setBackgroundColor(backgroundColor);

        addView(topBarLayout);

        this.bringToFront();

    }

    public void setButtVisible(Button button,boolean flag){
        if (flag){
            button.setVisibility(View.VISIBLE);
        }
        else button.setVisibility(View.GONE);
    }

    public Button getLeftButt(){
        return leftButt;
    }

    public Button getRightButt(){
        return rightButt;
    }

    public TextView getLeftTextView(){
        return leftTv;
    }

    public TextView getRightTextView(){
        return rightButt;
    }
}
