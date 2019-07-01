package com.example.facekilling.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.activities.AddFriendsActivity;
import com.example.facekilling.adapter.FriendAdapter;
import com.example.facekilling.javabean.Friend;
import com.example.facekilling.javabean.MainUser;
import com.example.facekilling.javabean.User;
import com.example.facekilling.util.OkHttpUtils;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONException;

import java.io.IOException;
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
    private int longPos = -1;

    private static final int FRIENDLIST_WAHT = 3;
    private static final int DELETEFRIEND_WHAT = 2;

    public interface ItemClickListener{
        void onItemClicked(String userName, int id);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendHandler = new FriendHandler(this);
        adapter = new FriendAdapter(getContext(),userList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        mView = inflater.inflate(R.layout.fragment_fri_viewpager,container,false);
        initView();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initUsers();
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
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                if (position>userList.size()) return true;
                PopupMenu popupMenu = new PopupMenu(getContext(),view);
                popupMenu.getMenuInflater().inflate(R.menu.delete_friend_menu,popupMenu.getMenu());
                popupMenu.show();
                longPos = position;
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.delete_friend:
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            friendHandler.obtainMessage(DELETEFRIEND_WHAT,
                                                    OkHttpUtils.postDeleteFriend(MainUser.getInstance(),userList.get(position).getEmail())).sendToTarget();
                                        }catch (IOException ie){
                                            ie.printStackTrace();
                                        }catch (JSONException je){
                                            je.printStackTrace();
                                        }
                                    }
                                }).start();
                                break;
                        }
                        return false;
                    }
                });
                return true;
            }
        });
    }


    public void  initUsers() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MainUser.getInstance().setFriendIdList(OkHttpUtils.getFriendIdList(MainUser.getInstance().getUser_id()));
                List<Integer> firendsIdList = MainUser.getInstance().getFriendIdList();
                List<Friend> friendsList = new ArrayList<>();
                for (int i : firendsIdList){
                    User user = OkHttpUtils.getUserInfo(i);
                    //String email, String user_name, Bitmap imageBitMap, String gender, String phone, String address
                    friendsList.add(new Friend(i,user.getEmail(),user.getUser_name(),user.getImageBitMap(),
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
            switch (msg.what){
                case FRIENDLIST_WAHT: {
                    userList.clear();
                    userList.addAll((List<Friend>) msg.obj);
                    adapter.notifyDataSetChanged();
                }
                break;
                case DELETEFRIEND_WHAT:{
                    if ((boolean)msg.obj){
                        userList.remove(longPos);
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
            }
        }
    }
}
