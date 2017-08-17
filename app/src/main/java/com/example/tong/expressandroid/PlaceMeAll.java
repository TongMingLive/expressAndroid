package com.example.tong.expressandroid;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tong.expressandroid.Util.App;
import com.example.tong.expressandroid.Util.HttpUtil;
import com.example.tong.expressandroid.bean.Place;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tong- on 2017/4/17.
 */

public class PlaceMeAll extends AppCompatActivity {
    private android.widget.ListView placelv;
    private String str,type;
    private List<Place> list = new ArrayList<>();
    private ListAdapter adapter = new myAdapte();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_me_all);
        this.placelv = (ListView) findViewById(R.id.place_lv);
    }
    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String,Object> map =new HashMap<>();
                map.put("userId", App.user.getUserid());
                str = HttpUtil.doPost(HttpUtil.path+"SelectPlaceUserServlet",map);
                if (str.equals("error")) {
                    handler.sendEmptyMessage(0x000);
                }else {
                    handler.sendEmptyMessage(0x123);
                }
            }
        }.start();
    }
    private class myAdapte extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
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
            LayoutInflater layoutInflater = LayoutInflater.from(PlaceMeAll.this);
            View v = layoutInflater.inflate(R.layout.find_item, null);
            LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.friend_item);
            TextView title = (TextView) v.findViewById(R.id.friend_item_title);
            TextView page = (TextView) v.findViewById(R.id.friend_item_page);

            title.setText("快递单号:"+list.get(position).getSendid());
            switch (list.get(position).getPlacetype()){
                case 0:
                    type = "审核中";
                    break;
                case 1:
                    type = "待配送";
                    break;
                case 2:
                    type = "已签收";
            }
            page.setText(type);

            linearLayout.setTag(position);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    Intent intent = new Intent();
                    intent.setClass(PlaceMeAll.this,PlaceMePage.class);
                    intent.putExtra("place",list.get(position));
                    intent.putExtra("type",type);
                    startActivity(intent);
                }
            });

            return v;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123){
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonArray jsonArray = parser.parse(str).getAsJsonArray();
                for (JsonElement json : jsonArray) {
                    list.add(gson.fromJson(json , Place.class));
                }
                placelv.setAdapter(adapter);
                if (list.size() == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(PlaceMeAll.this);
                    builder.setTitle("提示");
                    builder.setMessage("暂无代领订单");
                    builder.setNeutralButton("确定",null);
                    builder.show();
                }
            }else if (msg.what == 0x000){
                Snackbar.make(placelv, "网络连接失败，请检查您的网络", Snackbar.LENGTH_LONG).show();
            }
        }
    };
}
