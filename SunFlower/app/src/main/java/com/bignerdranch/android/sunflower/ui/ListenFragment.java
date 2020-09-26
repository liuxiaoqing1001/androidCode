package com.bignerdranch.android.sunflower.ui;

import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.sunflower.R;
import com.bignerdranch.android.sunflower.activity.fragment.LogicFragment;
import com.bignerdranch.android.sunflower.activity.fragment.OnlineFragment;
import com.bignerdranch.android.sunflower.adapter.MusicPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListenFragment extends Fragment{

    //定义activity_main.xml的控件对象
    private TextView logicTv;
//    private TextView onlineTv;
    private ViewPager viewPager;
//    private ImageView syncImagv;
    //将Fragment放入List集合中，存放fragment对象
    private List<Fragment> fragmentList = new ArrayList<>();
    private MusicPagerAdapter musicPagerAdapter;

    public ListenFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_listen, container, false);
        Intent in = getActivity().getIntent();
        //绑定id
//        bangdingID();
        logicTv = view.findViewById(R.id.main_logic_tv);
//        onlineTv = view.findViewById(R.id.main_online_tv);
        viewPager = view.findViewById(R.id.main_vp);
//        syncImagv = view.findViewById(R.id.main_sync_imgv);

        //设置监听
        //创建fragment对象
        LogicFragment logicFragment = new LogicFragment();
//        OnlineFragment onlineFragment = new OnlineFragment();
        //将fragment对象添加到fragmentList中
        fragmentList.add(logicFragment);
//        fragmentList.add(onlineFragment);
        //通过MusicPagerAdapter类创建musicPagerAdapter的适配器，下面我将添加MusicPagerAdapter类的创建方法
        musicPagerAdapter = new MusicPagerAdapter(getFragmentManager(), fragmentList);
        //viewPager绑定适配器
        viewPager.setAdapter(musicPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                switch (position) {
//                    case 0:
//                        logicTv.setTextColor(getResources().getColor(R.color.colorWhite));
//                        onlineTv.setTextColor(getResources().getColor(R.color.colorWhite1));
//                        break;
//                    case 1:
//                        onlineTv.setTextColor(getResources().getColor(R.color.colorWhite));
//                        logicTv.setTextColor(getResources().getColor(R.color.colorWhite1));
//                        break;
//                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        logicTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewPager.setCurrentItem(0);
                viewPager.setAdapter(musicPagerAdapter);
            }
        });

//        onlineTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewPager.setCurrentItem(1);
//            }
//        });

//        syncImagv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewPager.setAdapter(musicPagerAdapter);
//            }
//        });

        return view;
    }

}
