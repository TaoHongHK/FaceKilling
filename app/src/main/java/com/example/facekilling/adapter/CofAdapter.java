package com.example.facekilling.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.facekilling.R;
import com.example.facekilling.adapter.PictureAdapater;
import com.example.facekilling.adapter.ReviewAdapter;
import com.example.facekilling.javabean.Cof;
import com.example.facekilling.javabean.MainUser;
import com.example.facekilling.javabean.Picture;
import com.example.facekilling.javabean.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CofAdapter extends RecyclerView.Adapter<CofAdapter.ViewHolder>{

    private Context mContext;
    private List<Cof> mCofList;
    private Context mcontext;

    private PopupWindow popupWindow;
    private View popupView = null;
    private EditText inputComment;
    private String nInputContentText;
    private TextView btn_submit;
    private RelativeLayout rl_input_container;
    private InputMethodManager mInputManager;
    private int commentIndex;
    private String comment = "";

    private boolean likeStatus = false;   //false表示没点赞，true表示点赞了


    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView headimage;
        TextView head_name;
        TextView content;
        LinearLayout pictureLinearLayout;
        RecyclerView pictureRecyclerView;
        ImageView images;
        Button like_button;
        Button review_button;
        TextView like_num;
        ListView listView;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            headimage = (ImageView) view.findViewById(R.id.cof_item_layout_headimage);
            head_name = (TextView) view.findViewById(R.id.cof_item_layout_head);
            content = (TextView) view.findViewById(R.id.cof_item_layout_content);
            pictureLinearLayout = (LinearLayout) view.findViewById(R.id.cof_item_layout_picture);
            pictureRecyclerView = (RecyclerView) view.findViewById(R.id.cof_item_layout_image) ;
            like_button = (Button) view.findViewById(R.id.cof_item_layout_like);
            review_button = (Button) view.findViewById(R.id.cof_item_layout_review);
            like_num = (TextView) view.findViewById(R.id.cof_item_layout_like_num);
            listView = (ListView) view.findViewById(R.id.review_recycler_view);
        }
    }

    public CofAdapter(Context context,List<Cof> cofList) {
        mCofList = cofList;
        mcontext = context;
    }


    public CofAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = (View) LayoutInflater.from(mContext).inflate(R.layout.cof_item,parent,false);
        return new CofAdapter.ViewHolder(view);
    }


    public void onBindViewHolder(@NonNull CofAdapter.ViewHolder holder, int position) {
        Cof cof = mCofList.get(position);
        //方便测试
        if(cof.getUser().getImageId() == -1){
            holder.headimage.setImageBitmap(cof.getUser().getImageBitMap());
        }
        else{
            Glide.with(mContext).load(cof.getUser().getImageId()).into(holder.headimage);
        }

        holder.head_name.setText(cof.getUser().getUser_name());
        holder.content.setText(cof.getContent());

        LinearLayout linearLayout = holder.pictureLinearLayout;
        RecyclerView recyclerView = holder.pictureRecyclerView;
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        //设置高度自适应
        recyclerView.setLayoutManager(layoutManager);
        List<Picture> images = new ArrayList<>();
        images.addAll(cof.getImagesList());
        PictureAdapater adapter = new PictureAdapater(cof.getImagesList());
        int num = adapter.getItemCount();
        int height;
        if (num==0){
            height=0;
        }
        else if(num > 9){
            height = 300 *3;
        }
        else{
            height = ((num-1)/3 + 1) * 300;
        }
        ViewGroup.LayoutParams lp=linearLayout.getLayoutParams();
        lp.height=height;

        linearLayout.setLayoutParams(lp);
        recyclerView.setAdapter(adapter);

        String string = cof.getPraise_num() + "  人点赞";
        holder.like_num.setText(string);

        List<Review> reviewList = new ArrayList<>();
        reviewList.addAll(cof.getReviewList());

        ReviewAdapter reviewAdapter = new ReviewAdapter(mContext,R.layout.review_item,reviewList);
        ListView listView = holder.listView;


        int totalHeight = 20;
        for (int i = 0, len = reviewAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = reviewAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (reviewAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
        listView.setAdapter(reviewAdapter);

        monitor(holder,position);

    }

    private void monitor(CofAdapter.ViewHolder holder,int position){

        final Button like_button = holder.like_button;
        Button review_button = holder.review_button;

        //点赞事件
        like_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(likeStatus){
                    like_button.setBackgroundResource(R.drawable.like);
                    likeStatus = false;
                }
                else{
                    like_button.setBackgroundResource(R.drawable.like_press);
                    likeStatus = true;
                }

            }
        });
        //评论事件
        final int index = position;
        review_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                showPopupcomment();
                commentIndex = index;
            }
        });
    }
    public int getItemCount() {
        return mCofList.size();
    }


    @SuppressLint("WrongConstant")
    private void showPopupcomment() {
        if (popupView == null){
            //加载评论框的资源文件
            popupView = LayoutInflater.from(mContext).inflate(R.layout.comment_popupwindow, null);
        }
        inputComment = (EditText) popupView.findViewById(R.id.et_discuss);
        inputComment.setText("");
        btn_submit = (Button) popupView.findViewById(R.id.btn_confirm);
        rl_input_container = (RelativeLayout)popupView.findViewById(R.id.rl_input_container);
        //利用Timer这个Api设置延迟显示软键盘，这里时间为200毫秒
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run()
            {
                mInputManager = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE); //有修改
                mInputManager.showSoftInput(inputComment, 0);
            }

        }, 200);
        if (popupWindow == null){
            popupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT, false);

        }
        //popupWindow的常规设置，设置点击外部事件，背景色
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE)
                    popupWindow.dismiss();
                return false;

            }
        });

        // 设置弹出窗体需要软键盘，放在setSoftInputMode之前
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        // 再设置模式，和Activity的一样，覆盖，调整大小。
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置popupwindow的显示位置，这里应该是显示在底部，即Bottom
        popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);

        popupWindow.update();

        //设置监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            public void onDismiss() {

                mInputManager.hideSoftInputFromWindow(inputComment.getWindowToken(), 0); //强制隐藏键盘


            }
        });
        //外部点击事件
        rl_input_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mInputManager.hideSoftInputFromWindow(inputComment.getWindowToken(), 0); //强制隐藏键盘
                popupWindow.dismiss();

            }
        });
        //评论框内的发送按钮设置点击事件
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                nInputContentText = inputComment.getText().toString().trim();

                if (nInputContentText == null || "".equals(nInputContentText)) {
                    Toast.makeText(mContext,"请输入评论内容",Toast.LENGTH_SHORT).show();
                    return;
                }
                mInputManager.hideSoftInputFromWindow(inputComment.getWindowToken(),0);
                popupWindow.dismiss();
                //
                comment += nInputContentText;
                Review review = new Review(MainUser.getInstance(),comment);
                Cof cof = mCofList.get(commentIndex);
                cof.getReviewList().add(review);
                notifyDataSetChanged();

            }
        });
    }
}
