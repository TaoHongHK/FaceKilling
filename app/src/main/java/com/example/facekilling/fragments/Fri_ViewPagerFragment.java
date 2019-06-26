package com.example.facekilling.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class Fri_ViewPagerFragment extends Fragment {

    private View mView;
    private Context context;
    private List<Friend> userList = new ArrayList<>();
    private ItemClickListener itemClickListener;

    public interface ItemClickListener{
        void onItemClicked(String userName);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        mView = inflater.inflate(R.layout.fragment_fri_viewpager,container,false);
        initUsers();
        initView();
        Log.d("friviewpager", "onCreateView: ");
        return mView;
    }

    public void initView(){
        FriendAdapter adapter = new FriendAdapter(getContext(), R.layout.friend_item, userList);
        final ListView listview = (ListView) mView.findViewById(R.id.list_view);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemClickListener.onItemClicked(userList.get(i).getUser_name());
            }
        });
    }

    public void  initUsers() {
        Friend user_1 = new Friend("张三", R.drawable.touxiang_1,"上海北京");
        userList.add(user_1);
        Friend user_2 = new Friend("李四", R.drawable.touxiang_2, "qinyuf");
        userList.add(user_2);
        Friend user_3 = new Friend("王五", R.drawable.touxiang_3, "qinyuf");
        userList.add(user_3);
        Friend user_4 = new Friend("王二麻子", R.drawable.touxiang_1,"");
        userList.add(user_4);
        Friend user_5 = new Friend("狗蛋", R.drawable.touxiang_2,"");
        userList.add(user_5);
        Friend user_6 = new Friend("小明", R.drawable.touxiang_3,"");
        userList.add(user_6);
        Friend user_7 = new Friend("小玉", R.drawable.touxiang_1,"");
        userList.add(user_7);
        Friend user_8 = new Friend("小微", R.drawable.touxiang_2,"");
        userList.add(user_8);
        Friend user_9 = new Friend("张三", R.drawable.touxiang_3,"");
        userList.add(user_9);
        Friend user_10 = new Friend("张三", R.drawable.touxiang_1,"");
        userList.add(user_10);
        Friend user_11 = new Friend("张三", R.drawable.touxiang_2,"");
        userList.add(user_11);
        Friend user_12 = new Friend("张三", R.drawable.touxiang_3,"");
        userList.add(user_12);
        Friend user_13 = new Friend("张三", R.drawable.touxiang_1,"");
        userList.add(user_13);
    }
}
