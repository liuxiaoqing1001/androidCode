package com.bignerdranch.android.xiaoshutong.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bignerdranch.android.soulplanet.view.SoulPlanetsView;
import com.bignerdranch.android.xiaoshutong.R;
import com.bignerdranch.android.xiaoshutong.activity.SearchActivity;
import com.bignerdranch.android.xiaoshutong.adapter.TestAdapter;

public class SearchFragment extends Fragment {
    private View rootView;
    private TextView sv;
//    private View root;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_search,container,false);
        this.rootView=rootView;
        sv=rootView.findViewById(R.id.tv_sv);
        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        final SoulPlanetsView soulPlanet = rootView.findViewById(R.id.soulPlanetView);
//        soulPlanet.setAdapter(new TestAdapter());
//
//        rootView.findViewById(R.id.clBackground).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "数据更新中", Toast.LENGTH_SHORT).show();
//                soulPlanet.setAdapter(new TestAdapter());
//            }
//        });
//
//
//        soulPlanet.setOnTagClickListener(new SoulPlanetsView.OnTagClickListener() {
//            @Override
//            public void onItemClick(ViewGroup parent, View view, int position) {
//                Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}