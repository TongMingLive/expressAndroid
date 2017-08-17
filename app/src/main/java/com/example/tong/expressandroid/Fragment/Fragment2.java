package com.example.tong.expressandroid.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tong.expressandroid.PlaceMeAll;
import com.example.tong.expressandroid.R;
import com.example.tong.expressandroid.SendAll;

/**
 * Created by Tong on 2017/3/17.
 */

public class Fragment2 extends Fragment {

    private android.widget.Button sendsend;
    private android.widget.Button sendplace;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.send,null);
        this.sendplace = (Button) view.findViewById(R.id.send_place);
        this.sendsend = (Button) view.findViewById(R.id.send_send);

        sendsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SendAll.class));
            }
        });

        sendplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PlaceMeAll.class));
            }
        });

        return view;
    }
}
