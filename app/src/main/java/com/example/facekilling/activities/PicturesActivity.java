package com.example.facekilling.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.customviews.TopBar;
import com.example.facekilling.util.Picture;
import com.example.facekilling.util.PictureAdapater;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class PicturesActivity extends AppCompatActivity {

    private TopBar topBar;

    protected  DrawerLayout drawerLayout;

    private DrawerLayout mDrawerLayout;
    private static Context context;

    private Picture[] pictures = {
            new Picture(R.drawable.picture_01),
            new Picture(R.drawable.picture_02),
            new Picture(R.drawable.picture_03),
            new Picture(R.drawable.picture_04),
            new Picture(R.drawable.picture_05),
            new Picture(R.drawable.picture_06),
            new Picture(R.drawable.picture_07),
            new Picture(R.drawable.picture_08),
            new Picture(R.drawable.picture_09),
            new Picture(R.drawable.picture_10),
            new Picture(R.drawable.picture_11),
            new Picture(R.drawable.picture_12),
            new Picture(R.drawable.picture_13),
            new Picture(R.drawable.picture_14),
    };
    private List<Picture> picturesList = new ArrayList<>();

    private PictureAdapater adapter;

    public static Context getContext() {
        return context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pictures);
        context = getApplicationContext();

        topBar = (TopBar) findViewById(R.id.menuPicturesTopBar);
        topBar.setClickListener(new TopBar.TopbarClickListener() {
            @Override
            public void leftClicked() {
                Intent intent = new Intent(getContext(),IndexActivity.class);
                startActivity(intent);
            }

            @Override
            public void rightClicked() {

            }
        });

        initPictures();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.pictures_recycler_view);
        GridLayoutManager layoutManger = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManger);
        adapter = new PictureAdapater(picturesList);
        recyclerView.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.picture_menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.picture_profile:
                Toast.makeText(getContext(),"click the profile",Toast.LENGTH_SHORT).show();
                break;
            case R.id.picture_mail:
                Toast.makeText(getContext(),"click the mail",Toast.LENGTH_SHORT).show();
                break;
            case R.id.picture_settings:
                Toast.makeText(getContext(),"click the setting",Toast.LENGTH_SHORT).show();
                break;
            case R.id.picture_pictures:
                Toast.makeText(getContext(),"click the pictures",Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void initPictures(){
        picturesList.clear();
        for(int i=0;i<50;i++){
            Random random = new Random();
            int index = random.nextInt(pictures.length);
            picturesList.add(pictures[index]);
        }
    }

}
