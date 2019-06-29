package com.example.facekilling.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.facekilling.R;
import com.example.facekilling.adapter.FriendAdapter;
import com.example.facekilling.javabean.Friend;
import com.example.facekilling.javabean.MainUser;
import com.example.facekilling.javabean.User;
import com.example.facekilling.util.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

public class Fri_ViewPagerFragment extends Fragment {

    private View mView;
    private Context context;
    private List<Friend> userList = new ArrayList<>();
    private ItemClickListener itemClickListener;
    private ListView listview;
    private static FriendAdapter adapter;
    private FriendHandler friendHandler;

    private static int FRIENDLIST_WAHT = 0;

    public interface ItemClickListener{
        void onItemClicked(String userName,int id);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendHandler = new FriendHandler(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        mView = inflater.inflate(R.layout.fragment_fri_viewpager,container,false);
        initUsers();
        return mView;
    }


    public void initView(){
        listview = (ListView) mView.findViewById(R.id.list_view);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemClickListener.onItemClicked(userList.get(i).getUser_name(),userList.get(i).getUser_id());
            }
        });
    }

    public void  initUsers() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Integer> firendsIdList = MainUser.getInstance().getFriendIdList();
                List<Friend> friendsList = new ArrayList<>();
                for (int i : firendsIdList){
                    User user = OkHttpUtils.getUserInfo(i);
                    //String email, String user_name, Bitmap imageBitMap, String gender, String phone, String address
                    friendsList.add(new Friend(user.getEmail(),user.getUser_name(),user.getImageBitMap(),
                            user.getGender(),user.getPhone(),user.getAddress()));
                }
                friendHandler.obtainMessage(FRIENDLIST_WAHT,friendsList).sendToTarget();
            }
        }).start();
    }

    private class FriendHandler extends Handler{
        private Fri_ViewPagerFragment fri_viewPagerFragment;

        public FriendHandler(Fri_ViewPagerFragment fri_viewPagerFragment){
            this.fri_viewPagerFragment = fri_viewPagerFragment;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FRIENDLIST_WAHT){
                userList = (List<Friend>)msg.obj;
                adapter = new FriendAdapter(getContext(),userList);
                fri_viewPagerFragment.initView();
            }
        }
    }
}
