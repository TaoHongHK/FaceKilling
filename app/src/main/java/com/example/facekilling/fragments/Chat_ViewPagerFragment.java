package com.example.facekilling.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.adapter.ChatMessageAdapter;
import com.example.facekilling.javabean.ChatMessage;
import com.example.facekilling.util.ConnectToServer;

import java.util.ArrayList;
import java.util.List;

public class Chat_ViewPagerFragment extends Fragment {

    private View mView;
    private EditText text;
    private Button bt;
    private RecyclerView recy;
    public List list;
    private ConnectToServer lj;
    private TextView friName;
    private String friNameString;
    public Handler hand = new MyHandle(this);


    public static Chat_ViewPagerFragment newInstance(String friName){
        Bundle bundle = new Bundle();
        bundle.putString("FriendName",friName);
        Chat_ViewPagerFragment chat_viewPagerFragment = new Chat_ViewPagerFragment();
        chat_viewPagerFragment.setArguments(bundle);
        Log.d("chatviewpager", "newInstance: "+friName);
        return chat_viewPagerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle!=null){
            friNameString = bundle.getString("FriendName");
            Log.d("chatviewpager", "onCreate: "+friNameString);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_chat_viewpager,container,false);
        initView();
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        friName.setText(friNameString);
        Log.d("chatviewpager", "onViewCreated: "+friNameString);
    }

    public void initView(){
        friName = (TextView) mView.findViewById(R.id.fragment_chat_fri);
        recy = (RecyclerView) mView.findViewById(R.id.fragment_chat_recy);
        text = (EditText) mView.findViewById(R.id.fragment_chat_text);
        bt = (Button) mView.findViewById(R.id.fragment_chat_bt);
        list = new ArrayList();
        LinearLayoutManager lin = new LinearLayoutManager(getContext());
        recy.setLayoutManager(lin);
        lj = new ConnectToServer(this);
        bt.setOnClickListener(new View.OnClickListener() {//给发送按钮设置监听事件
            @Override
            public void onClick(View v) {
                String s = text.getText().toString();
                if (s == null || s.equals("")) {
                    Toast.makeText(getContext(), "发送消息不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    list.add(new ChatMessage(s, R.drawable.sz, false));
                    //new一个xx类，第一个参数的信息的内容，第二个参数是头像的图片id，第三个参数表示左右
                    //true为左边，false为右
                    lj.fa(s);//发送消息
                    recy.setAdapter(new ChatMessageAdapter(list));//再把list添加到适配器
                    text.setText(null);
                    recy.scrollToPosition(list.size() - 1);//将屏幕移动到RecyclerView的底部
                }
            }
        });
    }


    class MyHandle extends Handler{
        private Chat_ViewPagerFragment chat_viewPagerFragment;

        public MyHandle(Chat_ViewPagerFragment chat_viewPagerFragment){
            this.chat_viewPagerFragment = chat_viewPagerFragment;
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(getContext(), "连接服务器成功", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    recy.setAdapter(new ChatMessageAdapter(list));//设置适配器
                    recy.scrollToPosition(list.size() - 1);//将屏幕移动到RecyclerView的底部
                    break;
            }
        }
    }
}
