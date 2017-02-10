package com.tianfb.text.carouselfigure;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.tianfb.text.carouselfigure.view.CarouselViewPager;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ListView mLv;
    private ArrayList<ArrayList<Integer>> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLv = (ListView) findViewById(R.id.lv);
        mList = new ArrayList<>();
        ArrayList<Integer> item1 = new ArrayList<>();
        item1.add(R.mipmap.apicture1);
        item1.add(R.mipmap.apicture2);
        item1.add(R.mipmap.apicture3);
        mList.add(item1);
        ArrayList<Integer> item2 = new ArrayList<>();
        item2.add(R.mipmap.c1);
        item2.add(R.mipmap.c2);
        item2.add(R.mipmap.c3);
        item2.add(R.mipmap.c4);
        mList.add(item2);
        ArrayList<Integer> item3 = new ArrayList<>();
        item3.add(R.mipmap.d1);
        item3.add(R.mipmap.d2);
        item3.add(R.mipmap.d3);
        item3.add(R.mipmap.d4);
        mList.add(item3);
        ArrayList<Integer> item4 = new ArrayList<>();
        item4.add(R.mipmap.c1);
        mList.add(item4);
        mList.add(item1);
        mList.add(item2);
        mList.add(item3);

        mLv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                View item = View.inflate(MainActivity.this, R.layout.item, null);
                CarouselViewPager viewpager = (CarouselViewPager) item.findViewById(R.id.viewpager);
                viewpager.setAdapter(MainActivity.this,mList.get(i));
                return item;
            }
        });
    }
}
