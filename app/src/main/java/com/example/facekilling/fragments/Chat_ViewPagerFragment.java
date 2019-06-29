package com.example.facekilling.fragments;

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
import com.example.facekilling.javabean.Friend;
import com.example.facekilling.javabean.MainUser;
import com.example.facekilling.util.OkHttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Chat_ViewPagerFragment extends Fragment {

    private View mView;
    private EditText text;
    private Button bt;
    private RecyclerView recy;
    public List<ChatMessage> list;
    private TextView friName;
    private String friNameString;
    private int friendId;


    public static Chat_ViewPagerFragment newInstance(String friName,int id){
        Bundle bundle = new Bundle();
        bundle.putString("FriendName",friName);
        bundle.putInt("FriendId",id);
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
            friendId = bundle.getInt("FriendId");
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
        recy.setAdapter(new ChatMessageAdapter(list));
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String s = text.getText().toString();
                        try {
                            OkHttpUtils.postSendInfoToFriend(MainUser.getInstance().getUser_id(),friendId,s);
                        }catch (IOException ie){
                            ie.printStackTrace();
                        }
                        list.add(new ChatMessage(s, MainUser.getInstance().getImageBitMap(), false));
                        //new一个xx类，第一个参数的信息的内容，第二个参数是头像的图片id，第三个参数表示左右
                        //true为左边，false为右
                        ChatMessageAdapter chatMessageAdapter = (ChatMessageAdapter) recy.getAdapter();
                        if (chatMessageAdapter != null) {
                            chatMessageAdapter.notifyDataSetChanged();
                        }
                        text.setText("");
                        recy.scrollToPosition(list.size() - 1);//将屏幕移动到RecyclerView的底部
                    }
                }).start();
            }
        });
    }
}
