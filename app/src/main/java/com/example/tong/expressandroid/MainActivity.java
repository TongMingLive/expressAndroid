package com.example.tong.expressandroid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.example.tong.expressandroid.Fragment.Fragment1;
import com.example.tong.expressandroid.Fragment.Fragment2;
import com.example.tong.expressandroid.Fragment.Fragment3;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private long exitTime = 0;
    private BottomNavigationView navigation;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private List<Fragment> list = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.hide(list.get(0));
            fragmentTransaction.hide(list.get(1));
            fragmentTransaction.hide(list.get(2));
            fragmentTransaction.commit();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentTransaction.show(list.get(0));
                    return true;
                case R.id.navigation_send:
                    fragmentTransaction.show(list.get(1));
                    return true;
                case R.id.navigation_more:
                    fragmentTransaction.show(list.get(2));
                    return true;
            }
            fragmentTransaction.commit();
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //获取fragment管理者对象
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        list.add(new Fragment1());
        list.add(new Fragment2());
        list.add(new Fragment3());

        fragmentTransaction.add(R.id.content,list.get(0));
        fragmentTransaction.add(R.id.content,list.get(1));
        fragmentTransaction.hide(list.get(1));
        fragmentTransaction.add(R.id.content,list.get(2));
        fragmentTransaction.hide(list.get(2));

        fragmentTransaction.show(list.get(0));
        fragmentTransaction.commit();
    }

    //再次返回键退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Snackbar.make(navigation, "再按一次退出程序", Snackbar.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
