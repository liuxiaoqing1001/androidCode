package com.bignerdranch.android.sunflower.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.sunflower.R;
import com.bignerdranch.android.sunflower.activity.RecLibActivity;
import com.bignerdranch.android.sunflower.activity.RecListenActivity;
import com.bignerdranch.android.sunflower.activity.RecRequreActivity;
import com.bignerdranch.android.sunflower.activity.Src2Activity;
import com.bignerdranch.android.sunflower.activity.Src3Activity;
import com.bignerdranch.android.sunflower.activity.Src4Activity;
import com.bignerdranch.android.sunflower.helper.GallerySnapHelper;
import com.bignerdranch.android.sunflower.adapter.SnapHelperAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class RecommendFragment extends Fragment {

    private ImageView rec_listen;
//    private ImageView rec_add;
    private ImageView rec_lib;
    private ImageView rec_requre;
    private TextView rec_listen1;
    //    private ImageView rec_add;
    private TextView rec_lib1;
    private TextView rec_requre1;
    private ImageButton src3;
    private ImageButton src4;
    private ImageButton src2;

    RecyclerView mRecyclerView;
    ArrayList<String> mData;
    LinearLayoutManager mLayoutManager;
    GallerySnapHelper mGallerySnapHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_recommend,container,false);

        Intent in = getActivity().getIntent();//in.getStringExtra("Username")
//        System.out.println("-------"+in.getStringExtra("Username"));

        rec_listen=view.findViewById(R.id.rec_listen);
//        rec_add=view.findViewById(R.id.rec_add);
        rec_lib=view.findViewById(R.id.rec_lib);
        rec_requre=view.findViewById(R.id.rec_requre);
        rec_listen1=view.findViewById(R.id.rec_listen1);
//        rec_add=view.findViewById(R.id.rec_add);
        rec_lib1=view.findViewById(R.id.rec_lib1);
        rec_requre1=view.findViewById(R.id.rec_requre1);

        mRecyclerView = view.findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        initData();
        mRecyclerView.setAdapter(new SnapHelperAdapter(getActivity(), mData));
        mGallerySnapHelper = new GallerySnapHelper();
        mGallerySnapHelper.attachToRecyclerView(mRecyclerView);

        src3=view.findViewById(R.id.bt_acr3);
        src4=view.findViewById(R.id.bt_acr4);
        src2=view.findViewById(R.id.bt_acr2);

        MultiTransformation mation_listen = new MultiTransformation(new CircleCrop());
        Glide.with(this).load(R.drawable.b)
                .apply(RequestOptions.bitmapTransform(mation_listen)).into(rec_listen);

        MultiTransformation mation_lib = new MultiTransformation(new CircleCrop());
        Glide.with(this).load(R.drawable.a)
                .apply(RequestOptions.bitmapTransform(mation_lib)).into(rec_lib);

        MultiTransformation mation_requre = new MultiTransformation(new CircleCrop());
        Glide.with(this).load(R.drawable.c)
                .apply(RequestOptions.bitmapTransform(mation_requre)).into(rec_requre);

        rec_listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), RecListenActivity.class);
                intent.putExtra("Username", in.getStringExtra("Username"));
                startActivity(intent);
            }
        });

//        rec_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                startActivity(new Intent(getActivity(), CourseActivity.class));
//            }
//        });

        rec_lib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), RecLibActivity.class);
                intent.putExtra("Username", in.getStringExtra("Username"));
                startActivity(intent);
            }
        });

        rec_requre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), RecRequreActivity.class);
                intent.putExtra("Username", in.getStringExtra("Username"));
                startActivity(intent);
            }
        });

        rec_listen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), RecListenActivity.class);
                intent.putExtra("Username", in.getStringExtra("Username"));
                startActivity(intent);
            }
        });

//        rec_add1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                startActivity(new Intent(getActivity(), CourseActivity.class));
//            }
//        });

        rec_lib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), RecLibActivity.class);
                intent.putExtra("Username", in.getStringExtra("Username"));
                startActivity(intent);
            }
        });

        rec_requre1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), RecRequreActivity.class);
                intent.putExtra("Username", in.getStringExtra("Username"));
                startActivity(intent);
            }
        });

        src3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), Src3Activity.class);
                intent.putExtra("Username", in.getStringExtra("Username"));
                startActivity(intent);
            }
        });

        src4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), Src4Activity.class);
                intent.putExtra("Username", in.getStringExtra("Username"));
                startActivity(intent);
            }
        });

        src2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), Src2Activity.class);
                intent.putExtra("Username", in.getStringExtra("Username"));
                startActivity(intent);
            }
        });

        return view;
    }

    private void initData() {
        mData = new ArrayList<>();
        for (int i= 0;i<50;i++){
            mData.add("i="+i);
        }
    }
}
