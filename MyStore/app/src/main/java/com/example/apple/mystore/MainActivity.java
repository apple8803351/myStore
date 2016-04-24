package com.example.apple.mystore;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnTouchListener ,StoreFragment.OnFragmentInteractionListener,Shopping_carFragment.OnFragmentInteractionListener{

    private FragmentAdapter fragmentAdapter;
    public static MyViewPager viewPager;

    private StoreFragment storeFragment;
    private Shopping_carFragment shopping_carFragment;

    private ImageView tab_line;

    public static LinearLayout store_layout,shoppingcar_layout;

    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    private View touchView;

    private int currentIndex;
    private int screenWidth;

    private boolean touchOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        initial();
    }

    private void findView()
    {
        viewPager = (MyViewPager)findViewById(R.id.view);
        tab_line = (ImageView)findViewById(R.id.tab_line);
        store_layout = (LinearLayout)findViewById(R.id.store);
        shoppingcar_layout = (LinearLayout)findViewById(R.id.shopping_car);
    }

    private void initial()
    {
        store_layout.setOnTouchListener(this);
        shoppingcar_layout.setOnTouchListener(this);

        //這邊在處理tab_line的圖片
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tab_line.getLayoutParams();
        layoutParams.width = screenWidth / 2; //除於2是只有兩個fragment
        tab_line.setLayoutParams(layoutParams);

        storeFragment = new StoreFragment(); //把StoreFragment給實體化
        shopping_carFragment = new Shopping_carFragment(); //把Shopping_carFragment給實體化

        //加入list陣列
        fragmentList.add(storeFragment);
        fragmentList.add(shopping_carFragment);

        //實體化FragmentAdapter 把包含fragment型態的list丟到建構式中
        fragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), fragmentList);

        viewPager.setAdapter(fragmentAdapter);

        //viewPager 滑動監聽
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tab_line.getLayoutParams();

                if (currentIndex == 0 && position == 0) // 0 -> 1
                {
                    layoutParams.leftMargin = (int) (positionOffset * (screenWidth * 1.0 / 2) + currentIndex * (screenWidth / 2));
                }
                else if (currentIndex == 1 && position == 0) // 1 -> 0
                {
                    layoutParams.leftMargin = (int) (-(1 - positionOffset) * (screenWidth * 1.0 / 2) + currentIndex * (screenWidth / 2));
                }
                tab_line.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {
                currentIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //state為滑動中的狀態 有分三種 1:正在滑動 2:滑動完畢 0:什麼都沒有做。
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {

            // 創建退出對話框
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            // 設置對話框標題
            alertDialog.setTitle("系統提示");
            // 設置對話框消息
            alertDialog.setMessage("確定要退出嗎?");
            // 添加選擇按鈕並注冊監聽
            alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    storeFragment.getActivity().finish();
                    shopping_carFragment.getActivity().finish();
                    MainActivity.this.finish();
                }
            });
            alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            // 顯示對話框
            alertDialog.show();
        }

        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(touchView == null)
        {
            touchView = v;
            touchOut = false;
        }
        if(v.equals(touchView)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN :
                    v.setBackgroundColor(0xff5d8cc6);
                    break;
                case MotionEvent.ACTION_UP :
                    if(!touchOut)
                    {
                        v.setBackgroundColor(0xff20579c);
                        switch (touchView.getId())
                        {
                            case R.id.store :
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.shopping_car :
                                viewPager.setCurrentItem(1);
                                break;
                        }
                    }
                    touchView = null;
                    break;
                case MotionEvent.ACTION_MOVE :
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    if (nowX < v.getLeft() || nowX > v.getRight() || nowY < v.getTop() || nowY > v.getBottom())
                    {
                        if(!touchOut)
                        {
                            v.setBackgroundColor(0xff20579c);
                            touchOut = true;
                        }
                    }
                    break;
            }
        }
        return true;
    }
}
