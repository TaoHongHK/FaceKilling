package com.example.facekilling.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.adapter.ChatMessageAdapter;
import com.example.facekilling.util.ConnectToServer;

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


    public Chat_ViewPagerFragment newInstance(String friName){
        Bundle bundle = new Bundle();
        bundle.putString("FriendName",friName);
        Chat_ViewPagerFragment chat_viewPagerFragment = new Chat_ViewPagerFragment();
        chat_viewPagerFragment.setArguments(bundle);
        return chat_viewPagerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle!=null){
            friNameString = bundle.getString("FriendName");
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
    }

    public void initView(){
        friName = (TextView) mView.findViewById(R.id.fragment_chat_fri);
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
