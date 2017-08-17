package com.example.tong.expressandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.tong.expressandroid.bean.Find;

/**
 * Created by tong on 17-3-30.
 */

public class FindPage extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find);
        TextView title = (TextView) findViewById(R.id.friend_title);
        TextView page = (TextView) findViewById(R.id.friend_page);

        Find friend = (Find) getIntent().getSerializableExtra("friend");

        title.setText(friend.getFindtitle());
        page.setText(friend.getFindpage());


    }
}
