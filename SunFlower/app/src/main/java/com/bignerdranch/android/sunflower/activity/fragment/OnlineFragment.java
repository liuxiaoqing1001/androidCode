package com.bignerdranch.android.sunflower.activity.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.sunflower.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//在线音乐，未实现在线播放和下载，不在大作业范围内，待开发
public class OnlineFragment extends Fragment {

    private final String TAG = "NetFragment";
    //音乐搜索接口
    public static final String CLOUD_MUSIC_API_PREFIX = "http://s.music.163.com/search/get/?";
//    public static final String CLOUD_MUSIC_API_PREFIX = "https://y.qq.com/portal/search.html#page=1&searchid=1&remoteplace=txt.yqq.top&t=song&w=？";
    private Context mContext;
    //获取输入的搜索框
    private SearchView searchView;
    //根据关键字搜索到的音乐列表
    private ListView netMusicListView;
    //存放搜索到的音乐信息的list
    private List<Map<String, Object>> netMusicList = new ArrayList<>();
    //获取的json数据格式的网络响应赋值给searchResponse
    private String searchResponse = null;
    private LayoutInflater inflater;

//    private DBHelper dbHelper;

    //缓存专辑图片的下载链接，当下载完成并播放歌曲时，点击播放图标加载专辑图标时避免重新联网搜索，直接从缓存中获取
    private Map<String, SoftReference<String>> picUrlMap = new HashMap<String, SoftReference<String>>();

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();
        inflater = LayoutInflater.from(mContext);
        Log.d(TAG, "onCreate");
    }

    // NetFragment向外界展示的内容，返回值为view
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_online, container, false);
//        dbHelper=new DBHelper(getActivity());

        netMusicListView = (ListView) view.findViewById(R.id.netmusiclist);
        netMusicListView.setAdapter(netMusicListAdapter);
        netMusicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                new AlertDialog.Builder(mContext)
                        .setTitle("下载提示")
                        .setMessage("是否下载该歌曲？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        //点击搜索到的网络音乐列表的第position项即可建立下载任务下载对应歌曲
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                createDownloadTask(position);
                            }
                        }).show();

//                .setTitle("下载／试听")
//                        .setMessage("请选择下载或试听")
//                        .setNegativeButton("试听", new DialogInterface.OnClickListener() {
//                            @SuppressLint("WrongConstant")
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(mContext, "试听", 300).show();
//                                shiTing(pos);
//                            }
//                        })
//                        .setPositiveButton("下载",
//                                new DialogInterface.OnClickListener() {
//                                    @SuppressLint("WrongConstant")
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        downloadMusic(pos);
//                                        Toast.makeText(mContext, "下载", 300).show();
//                                    }
//                                }).show();

                //点击搜索到的网络音乐列表的第position项即可建立下载任务下载对应歌曲，下一篇介绍
//                createDownloadTask(position);
            }
        });
        searchView = (SearchView) view.findViewById(R.id.searchView);
        //搜索框监听，点击搜索建立异步任务执行搜索任务
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("WrongConstant")
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(mContext, "搜索内容为：" + query, 100).show();
                String musicUrl = getRealUrl(query);
                new SearchMusicTask(netMusicListView).execute(musicUrl);
                return false;
            }

            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {

            public boolean onClose() {
                netMusicListView.setAdapter(null);
//                 netMusicList.clear();adapter.notifyDataSetChanged();
                return false;
            }
        });
        return view;
    }

    // 网络音乐列表适配器
    private BaseAdapter netMusicListAdapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return netMusicList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            View view = convertView;
            Map<String, Object> item = netMusicList.get(position);
            if (convertView == null) {
                view = inflater.inflate(R.layout.music_item, null);
            }
            TextView musicTitle = (TextView) view.findViewById(R.id.musicitem_title_tv);
            musicTitle.setTag("title");
            TextView musicArtist = (TextView) view.findViewById(R.id.musicitem_artist_tv);
            musicTitle.setText((String) item.get("title"));
            musicArtist.setText((String) item.get("artist"));
            return view;
        }

    };

    //返回可以访问的网络资源
    private String getRealUrl(String query) {
        String key = null;
        try {
            key = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return CLOUD_MUSIC_API_PREFIX + "type=1&s='" + key
                + "'&limit=20&offset=0";
    }

    public void onDestroy() {
        super.onDestroy();
    }

    // 负责搜索音乐的异步任务，搜索完成后显示网络音乐列表
    private class SearchMusicTask extends AsyncTask<String, Void, Void> {
        private ListView musicList;

        public SearchMusicTask(ListView musicList) {
            this.musicList = musicList;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(String... params) {
            String url = params[0];
            try {
                HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
                conn.setConnectTimeout(5000);
                //使用缓存提高处理效率
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                //网络响应赋值给成员变量searchResponse
                searchResponse = sb.toString();
                parseResponse();
                Log.d(TAG, "searchResponse = " + searchResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //adapter数据更新后通知列表更新
            netMusicListAdapter.notifyDataSetChanged();
            //musicList.setAdapter(netMusicListAdapter);
        }

        //json解析网络响应
        private void parseResponse() {
            try {
                JSONObject response = new JSONObject(searchResponse);
                JSONObject result = response.getJSONObject("result");
                JSONArray songs = result.getJSONArray("songs");
                if (netMusicList.size() > 0) netMusicList.clear();
                for (int i = 0; i < songs.length(); i++) {
                    JSONObject song = songs.getJSONObject(i);
                    //获取歌曲名字
                    String title = song.getString("name");
                    //获取歌词演唱者
                    String artist = song.getJSONArray("artists")
                            .getJSONObject(0).getString("name");
                    //获取歌曲专辑图片的url
                    String albumPicUrl = song.getJSONObject("album").getString(
                            "picUrl");
                    //获取歌曲音频的url
                    String audioUrl = song.getString("audio");
                    Log.d(TAG, "doenloadUrl = " + audioUrl);
                    //保存音乐信息的Map
                    Map<String, Object> item = new HashMap<>();
                    item.put("title", title);
                    item.put("artist", artist);
                    item.put("picUrl", albumPicUrl);
                    picUrlMap.put(title + artist, new SoftReference<String>(
                            albumPicUrl));
                    item.put("audio", audioUrl);
                    //将一条歌曲信息存入list中
                    netMusicList.add(item);
                }
                Log.d(TAG, "搜到" + netMusicList.size() + "首歌");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
