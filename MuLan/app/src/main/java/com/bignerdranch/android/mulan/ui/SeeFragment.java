package com.bignerdranch.android.mulan.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.mulan.R;
import com.bignerdranch.android.mulan.adapter.ListVideoAdapter;
import com.bignerdranch.android.mulan.entity.VideoBean;
import com.bignerdranch.android.mulan.helper.ScrollCalculatorHelper;
import com.bignerdranch.android.mulan.utils.DpTools;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SeeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SeeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //变量
    private RecyclerView recyclerView;
    private List<String> mp4Url;
    private List<VideoBean> list;
    //控制滚动播放
    ScrollCalculatorHelper scrollCalculatorHelper;

    public SeeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SeeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeeFragment newInstance(String param1, String param2) {
        SeeFragment fragment = new SeeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_see, container, false);
        Intent in = getActivity().getIntent();
        System.out.println("-------"+in.getStringExtra("Username"));

        initData();

        recyclerView = view.findViewById(R.id.rec_video);

        ListVideoAdapter list_video_adapter = new ListVideoAdapter(getActivity(), list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        //获取屏幕宽高
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        //自定播放帮助类 限定范围为屏幕一半的上下偏移180   括号里不用在意 因为是一个item一个屏幕
        scrollCalculatorHelper = new ScrollCalculatorHelper(R.id.video_view
                , dm.heightPixels / 2 - DpTools.dip2px(getActivity(), 180)
                , dm.heightPixels / 2 + DpTools.dip2px(getActivity(), 180));

        //让RecyclerView有ViewPager的翻页效果
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);
        //设置LayoutManager和Adapter
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(list_video_adapter);
        //设置滑动监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //第一个可见视图,最后一个可见视图
            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //如果newState的状态==RecyclerView.SCROLL_STATE_IDLE;
                //播放对应的视频
                scrollCalculatorHelper.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                Log.e("有几个item", firstVisibleItem + "    " + lastVisibleItem);
                //一屏幕显示一个item 所以固定1
                //实时获取设置 当前显示的GSYBaseVideoPlayer的下标
                scrollCalculatorHelper.onScroll(recyclerView, firstVisibleItem, lastVisibleItem, 1);

            }

        });


        return view;
    }

    public void initData() {
        makeData();
        //视频数据
        list = new ArrayList<>();
        for (int i = 0; i < mp4Url.size(); i++) {
            VideoBean video_bean = new VideoBean();
            video_bean.setUrl(mp4Url.get(i));
            video_bean.setBitmap(ContextCompat.getDrawable(getActivity(), R.drawable.ic_launcher_background));
            list.add(video_bean);
        }

    }

    private void makeData() {
        mp4Url=new ArrayList<>();
        mp4Url.add("android.resource://com.bignerdranch.android.mulan/" + R.raw.view1);
        mp4Url.add("android.resource://com.bignerdranch.android.mulan/" + R.raw.view2);
        mp4Url.add("android.resource://com.bignerdranch.android.mulan/" + R.raw.view3);
        mp4Url.add("android.resource://com.bignerdranch.android.mulan/" + R.raw.view4);
        mp4Url.add("android.resource://com.bignerdranch.android.mulan/" + R.raw.view5);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
