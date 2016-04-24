package com.example.apple.mystore;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class Shopping_carFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;

    private static Button buy,delete;
    private ListView shoppingcar_list;

    private  AlertDialog.Builder alertDialog;

    private static MyAdapter myAdapter;

    private static ArrayList<ArrayList<String>> information_list = new ArrayList<ArrayList<String>>();
    private static ArrayList<CheckBox> checkBox_list = new ArrayList<CheckBox>();

    private final static int NOT_SELECT = 1,BUY_OR_DELETE = 2;

    private int total_number = 0;

    public Shopping_carFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping_car, container, false);
        buy = (Button)view.findViewById(R.id.buy);
        delete = (Button)view.findViewById(R.id.delete);
        myAdapter = new MyAdapter(this.getContext());
        shoppingcar_list = (ListView)view.findViewById(R.id.listView);

        buy.setOnClickListener(this);
        delete.setOnClickListener(this);

        shoppingcar_list.setAdapter(myAdapter);

        shoppingcar_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!checkBox_list.get(position).isChecked()) {
                    total_number = total_number + Integer.valueOf(information_list.get(position).get(3));
                    checkBox_list.get(position).setChecked(true);
                } else {
                    total_number = total_number - Integer.valueOf(information_list.get(position).get(3));
                    checkBox_list.get(position).setChecked(false);
                }
            }
        });
        return view;
    }

    public static void addProduce(String name, String price, String number, String total)
    {
        ArrayList<String> list = new ArrayList<String>();
        list.add(name);
        list.add(price);
        list.add(number);
        list.add(total);

        information_list.add(list);
        checkBox_list.clear();

        myAdapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        String title = "購物訊息";
        String message = "";
        String positiveButtonText = "";

        if(!check_have_data())
        {
            message = "請選擇購物清單上的項目";
            positiveButtonText = "確定";
            showDialog(title, message, positiveButtonText, NOT_SELECT);
        }
        else
        {
            switch (v.getId())
            {
                case R.id.buy :
                    message = "天啊\n\n總價格竟然要 " + total_number + " 元\n\n是否真的要購買這些商品?";
                    positiveButtonText = "確定購買";
                    break;
                case R.id.delete :
                    message = "請問是否要刪除這些清單?";
                    positiveButtonText = "確定刪除";
                    break;
            }
            showDialog(title, message, positiveButtonText, BUY_OR_DELETE);
        }
    }

    private void showDialog(String title, String message, String positiveButtonText, int type){
        alertDialog = new AlertDialog.Builder(Shopping_carFragment.this.getContext());
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        switch(type)
        {
            case NOT_SELECT :
                alertDialog.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
            case BUY_OR_DELETE :
                alertDialog.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeData();
                        myAdapter.notifyDataSetChanged();
                        total_number = 0;
                    }
                });
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
        }
        alertDialog.show();
    }

    //檢查使用者有沒有選擇購物清單上的資料
    private boolean check_have_data(){
        boolean have_data = false;
        for(int i=0;i<checkBox_list.size();i++)
        {
            if(checkBox_list.get(i).isChecked()){
                have_data = true;
                break;
            }
        }
        return have_data;
    }

    //清除在清單中勾選的資料
    public void removeData(){
        int index = 0;
        while(index < information_list.size())
        {
            if(checkBox_list.get(0).isChecked())
            {
                information_list.remove(index);
                index = index - 1;
            }
            checkBox_list.remove(0);
            index = index + 1;
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //ListView客製化 套用layout裡面的mylistview.xml檔
    class MyAdapter extends BaseAdapter{
        Context myContext;
        LayoutInflater inflater;

        public MyAdapter(Context context){
            this.myContext = context;
            inflater = LayoutInflater.from(this.myContext);
        }

        @Override
        public int getCount() {
            return information_list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.my_listview, null);
            CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.checkBox);
            TextView produce_name = (TextView)convertView.findViewById(R.id.name);
            TextView produce_price = (TextView)convertView.findViewById(R.id.price);
            TextView produce_number = (TextView)convertView.findViewById(R.id.number);
            TextView produce_total = (TextView)convertView.findViewById(R.id.total);

            produce_name.setText(produce_name.getText() + information_list.get(position).get(0));
            produce_price.setText(produce_price.getText() + information_list.get(position).get(1));
            produce_number.setText(produce_number.getText() + information_list.get(position).get(2));
            produce_total.setText("NT$" + information_list.get(position).get(3));

            checkBox_list.add(checkBox);
            return convertView;
        }
    }
}
