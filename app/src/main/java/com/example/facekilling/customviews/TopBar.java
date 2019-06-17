package com.example.facekilling.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.facekilling.R;

public class TopBar extends LinearLayout {

    private TopbarClickListener clickListener;

    private Button leftButt,rightButt;
    private TextView leftTv,rightTv,tvTitle;
    private int backgroundColor;

    private int leftTextColor;
    private int leftButtIcon;
    private String leftText;
    private boolean leftTextVisible;
    private boolean leftButtVisible;


    private int rightTextColor;
    private int rightButtIcon;
    private String rightText;
    private boolean rightTextVisible;
    private boolean rightButtVisible;

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
        this.bringToFront();
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

        //左边按钮
        leftButtIcon = typedArray.getResourceId(R.styleable.TopBar_leftButtIcon,-1);
        leftButtVisible = typedArray.getBoolean(R.styleable.TopBar_leftButtVisible,true);

        //右边文本
        if (typedArray.hasValue(R.styleable.TopBar_rightText)){
            rightText = typedArray.getString(R.styleable.TopBar_rightText);
        }
        rightTextColor = typedArray.getColor(R.styleable.TopBar_rightTextColor,0);
        rightTextVisible = typedArray.getBoolean(R.styleable.TopBar_rightTextVisible,false);

        //右边按钮
        rightButtIcon = typedArray.getResourceId(R.styleable.TopBar_leftButtIcon,-1);
        rightButtVisible = typedArray.getBoolean(R.styleable.TopBar_rightButtVisible,false);

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
        }
        if(leftButtIcon != -1){
            leftButt.setBackgroundResource(leftButtIcon);
        }
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
        }
        if(rightButtIcon != -1){
            rightButt.setBackgroundResource(rightButtIcon);
        }
        setButtVisible(rightButt,rightButtVisible);
        rightButt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.rightClicked();
            }
        });

        tvTitle.setText(title);
        tvTitle.setTextColor(titleTextColor);
        tvTitle.setTextSize(titleTextSize);
        tvTitle.setGravity(Gravity.CENTER);
        if(!titleVisible){
            tvTitle.setVisibility(GONE);
        }
        else {
            tvTitle.setVisibility(VISIBLE);
        }

        setBackgroundColor(backgroundColor);

        addView(topBarLayout,0);

    }

    public void setButtVisible(Button button,boolean flag){
        if (flag){
            button.setVisibility(View.VISIBLE);
        }
        else button.setVisibility(View.GONE);
    }
}
