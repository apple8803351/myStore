package com.example.apple.mystore;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class StoreFragment extends Fragment implements View.OnClickListener,View.OnLongClickListener,RadioGroup.OnCheckedChangeListener{

    private Button add,cut,add_shoppingcar;
    private RadioGroup produceGroup;
    private View touchView;
    private AlertDialog.Builder alertDialog;
    private TextView buy_number;

    private Timer timer = null;

    public int nowNumber = 1;
    private int nowProducePrice;
    private final static int BUY_LIMIT = 10;

    private String nowProduce;


    public StoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        add = (Button)view.findViewById(R.id.add_number);
        cut = (Button)view.findViewById(R.id.cut_number);
        add_shoppingcar = (Button)view.findViewById(R.id.add_shoppingcar);
        produceGroup = (RadioGroup)view.findViewById(R.id.produce);
        buy_number = (TextView)view.findViewById(R.id.buy_number);

        add.setOnClickListener(this);
        cut.setOnClickListener(this);
        add_shoppingcar.setOnClickListener(this);

        add.setOnLongClickListener(this);
        cut.setOnLongClickListener(this);

        produceGroup.setOnCheckedChangeListener(this);

        nowProduce = "全票";
        nowProducePrice = 300;

        return view;
    }

    @Override
    public void onClick(View v) {
        if(timer != null && v.equals(touchView))
        {
            timer.cancel();
            timer.purge();
            timer = null;
            MainActivity.store_layout.setEnabled(true);
            MainActivity.shoppingcar_layout.setEnabled(true);
            MainActivity.viewPager.setScrollEnable(true);
            MainActivity.enableBack = true;
        }
        else if(timer == null)
        {
            String title = "購物訊息";
            String message = "";

            switch (v.getId())
            {
                case R.id.add_shoppingcar :
                    message = "商品名稱：" + nowProduce + "\n\n" + "商品價格：" + nowProducePrice + "\n\n" + "購買數量：" + nowNumber + "\n\n" + "總額：" + (nowProducePrice* nowNumber);
                    showDialog(title, message);
                    break;
                default:
                    action(v);
            }
        }
    }

    private void showDialog(String title, String message)
    {
        alertDialog = new AlertDialog.Builder(StoreFragment.this.getContext());
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("送出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Shopping_carFragment.addProduce(nowProduce, String.valueOf(nowProducePrice), String.valueOf(nowNumber), String.valueOf(nowProducePrice * nowNumber));
                nowNumber = 1;
                buy_number.setText(String.valueOf(nowNumber));
            }
        });
        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    @Override
    public boolean onLongClick(View v) {
        if(timer == null)
        {
            MainActivity.store_layout.setEnabled(false);
            MainActivity.shoppingcar_layout.setEnabled(false);
            MainActivity.viewPager.setScrollEnable(false);
            MainActivity.enableBack = false;
            touchView = v;
            timer = new Timer(true);
            timer.schedule(new MyTask(), 0, 100);
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton radioButton = (RadioButton)this.getView().findViewById(checkedId);
        String[] information = radioButton.getTag().toString().split("_");
        nowProduce = information[0];
        nowProducePrice = Integer.valueOf(information[1]);
    }

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what) {
                case 1:
                    action(touchView);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void action(View view)
    {
        switch (view.getId())
        {
            case R.id.add_number:
                nowNumber = nowNumber + 1;
                if(nowNumber > BUY_LIMIT)
                {
                    nowNumber = BUY_LIMIT;
                    buy_number.setTextColor(Color.RED);
                }
                else
                {
                    buy_number.setTextColor(0xff4350fd);
                }
                break;
            case R.id.cut_number:
                nowNumber = nowNumber - 1;
                if(nowNumber < 1)
                {
                    nowNumber = 1;
                    buy_number.setTextColor(Color.RED);
                }
                else
                {
                    buy_number.setTextColor(0xff4350fd);
                }
                break;
        }
        buy_number.setText(String.valueOf(nowNumber));
    }

    //時間工作
    class MyTask extends TimerTask {
        public void run() {
            Message message = new Message();
            message.what = 1;
            mHandler.sendMessage(message);
        }
    }
}
