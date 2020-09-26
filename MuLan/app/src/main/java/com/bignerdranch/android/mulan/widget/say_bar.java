package com.bignerdranch.android.mulan.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bignerdranch.android.mulan.R;

public class say_bar  extends LinearLayout{
    private Context con;
    private int inputview_input_icon;
    private String inputview_input_hint;
    private boolean inputview_is_pass;

    private int right_icon;
    private View inflate;
    ImageView imageView;
    TextView editText;
    private View tv_search;

    public say_bar(@NonNull Context context) {
        super(context);
        init(context, null);
        this.con=context;
    }

    public say_bar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public say_bar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        if(attrs==null){
            return;
        }
        //获取属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.nav);
        inputview_input_icon = typedArray.getResourceId(R.styleable.nav_icon, R.mipmap.ic_launcher);
        right_icon = typedArray.getResourceId(R.styleable.nav_right_icon, R.mipmap.ic_launcher);
        inputview_input_hint = typedArray.getString(R.styleable.nav_hint);
        //释放
        typedArray.recycle();

        inflate = LayoutInflater.from(context).inflate(R.layout.say_bar, this, false);
        //imageView=  (ImageView)inflate.findViewById(R.id.tou);
        editText=  (TextView)inflate.findViewById(R.id.say_title);
        imageView=  (ImageView)inflate.findViewById(R.id.say_back);
        imageView.setImageResource(inputview_input_icon);
        editText.setText(inputview_input_hint);
        //editText.setInputType(inputview_is_pass?);
        addView(inflate);

    }
}
