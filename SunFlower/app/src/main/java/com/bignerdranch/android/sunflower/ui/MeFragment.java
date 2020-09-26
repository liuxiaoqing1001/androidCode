package com.bignerdranch.android.sunflower.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.sunflower.R;
import com.bignerdranch.android.sunflower.activity.AboutActivity;
import com.bignerdranch.android.sunflower.activity.CourseActivity;
import com.bignerdranch.android.sunflower.widget.nav_bar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class MeFragment extends Fragment {

//    private View view;
    private TextView user_name;
    private ImageView mHBack;
    private ImageView mHHead;
    private nav_bar mCourse;
    private nav_bar mAbout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_me,container,false);

        user_name =view.findViewById(R.id.user_name);
        Intent in = getActivity().getIntent();
//        System.out.println("-------"+in.getStringExtra("Username"));
        user_name.setText(in.getStringExtra("Username"));

        mHBack=view.findViewById(R.id.h_back);
        mHHead=view.findViewById(R.id.h_head);
        //设置背景磨砂效果
        MultiTransformation mation1 = new MultiTransformation(new BlurTransformation(25));
        Glide.with(this).load(R.drawable.sunflower1)
                .apply(RequestOptions.bitmapTransform(mation1)).into(mHBack);
        //设置圆形图像
        MultiTransformation mation2 = new MultiTransformation(new CircleCrop());
        Glide.with(this).load(R.drawable.sunflower)
                .apply(RequestOptions.bitmapTransform(mation2)).into(mHHead);

        mCourse=view.findViewById(R.id.course);
        mCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), CourseActivity.class);
                intent.putExtra("Username", in.getStringExtra("Username"));
                startActivity(intent);
            }
        });

        mAbout=view.findViewById(R.id.about);
        mAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }

}
