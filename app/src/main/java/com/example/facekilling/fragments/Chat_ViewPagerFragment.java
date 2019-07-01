package com.example.facekilling.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.example.facekilling.R;
import com.example.facekilling.adapter.ChatMessageAdapter;
import com.example.facekilling.javabean.ChatMessage;
import com.example.facekilling.javabean.Friend;
import com.example.facekilling.javabean.Information;
import com.example.facekilling.javabean.MainUser;
import com.example.facekilling.javabean.User;
import com.example.facekilling.util.OkHttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Chat_ViewPagerFragment extends Fragment {

    private View mView;
    private EditText sendMsg;
    private Button sendButt;
    private RecyclerView recyclerView;
    private List<ChatMessage> list = new ArrayList<>();
    private TextView friName;
    private String friNameString;
    private int friendId;
    private User friend;
    private static final int CHAT_SEND_WHAT = 0;
    private static final int CHAT_RECEIVE_WHAT = 1;

    private ChatMessageAdapter chatMessageAdapter;

    private Thread mThread;

    private ChatHandler chatHandler;


    public static Chat_ViewPagerFragment newInstance(String friName,int id){
        Bundle bundle = new Bundle();
        bundle.putString("FriendName",friName);
        bundle.putInt("FriendId",id);
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
            friendId = bundle.getInt("FriendId");
        }
        chatHandler = new ChatHandler(this);
        Log.d("chatFragment", "onCreate");
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
        Log.d("chatFragment", "initView");
        friName = (TextView) mView.findViewById(R.id.fragment_chat_fri);
        recyclerView = (RecyclerView) mView.findViewById(R.id.fragment_chat_recy);
        sendMsg = (EditText) mView.findViewById(R.id.fragment_chat_text);
        sendButt = (Button) mView.findViewById(R.id.fragment_chat_bt);
        LinearLayoutManager lin = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lin);
        chatMessageAdapter = new ChatMessageAdapter(list);
        recyclerView.setAdapter(chatMessageAdapter);
        sendButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg();
            }
        });
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                friend = OkHttpUtils.getUserInfo(friendId);
                while (true){
                    try {
                        reveiveMsg();
                        Thread.sleep(500);
                    } catch (InterruptedException ine){
                        ine.printStackTrace();
                    }
                }
            }
        });
        mThread.start();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThread.interrupt();
    }

    public void reveiveMsg(){
        try {
            chatHandler.obtainMessage(CHAT_RECEIVE_WHAT,OkHttpUtils.postRecieveInfoFromFriend(
                    MainUser.getInstance())).sendToTarget();
        }catch (IOException ie){
            ie.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendMsg(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String s = sendMsg.getText().toString();
                try {
                    chatHandler.obtainMessage(CHAT_SEND_WHAT,OkHttpUtils.postSendInfoToFriend(
                            MainUser.getInstance().getUser_id(),friendId,s)).sendToTarget();
                }catch (IOException ie){
                    ie.printStackTrace();
                }

            }
        }).start();
    }


    private class ChatHandler extends Handler{
        private Chat_ViewPagerFragment chat_viewPagerFragment;

        public ChatHandler(Chat_ViewPagerFragment chat_viewPagerFragment){
            this.chat_viewPagerFragment = chat_viewPagerFragment;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHAT_SEND_WHAT: {
                    boolean result = (boolean) msg.obj;
                    if (result) {
                        list.add(new ChatMessage(sendMsg.getText().toString(), MainUser.getInstance().getImageBitMap(), ChatMessage.TYPE_SEND));
                        chatMessageAdapter.notifyItemInserted(list.size() - 1);
                        sendMsg.setText("");
                    }
                }
                break;
                case CHAT_RECEIVE_WHAT:{
                    List<Information> informations = (List<Information>) msg.obj;
                    for (Information friendInformation : getFriendInformation(informations)) {
                        if (friendInformation != null) {
                            list.add(new ChatMessage(friendInformation.getInformation(),friend.getImageBitMap(),
                                    ChatMessage.TYPE_RECEIVE));
                            chatMessageAdapter.notifyItemInserted(list.size() - 1);
                        }
                    }
                }
            }

        }

        public List<Information> getFriendInformation(List<Information> informations){
            List<Information> friendInformations = new ArrayList<>();
            for (Information information: informations){
                if (information.getPost_id() == friendId){
                    friendInformations.add(information);
                }
            }
            return friendInformations;
        }
    }
}
